package com.zmx.weblog.common.aspect;

import com.zmx.weblog.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile; // <-- Import MultipartFile

import javax.servlet.http.HttpServletRequest;   // <-- Import HttpServletRequest (optional, but good practice)
import javax.servlet.http.HttpServletResponse;  // <-- Import HttpServletResponse (optional, but good practice)
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
// removed unused import: import java.util.function.Function;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class ApiOperationLogAspect {

    /** 以自定义 @ApiOperationLog 注解为切点，凡是添加 @ApiOperationLog 的方法，都会执行环绕中的代码 */
    @Pointcut("@annotation(com.zmx.weblog.common.aspect.ApiOperationLog)")
    public void apiOperationLog() {}

    /**
     * 环绕
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("apiOperationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 请求开始时间
            long startTime = System.currentTimeMillis();

            // MDC
            MDC.put("traceId", UUID.randomUUID().toString());

            // 获取被请求的类和方法
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            // 请求入参
            Object[] args = joinPoint.getArgs();
            // === MODIFICATION START ===
            // 入参转字符串，对特殊类型进行处理
            String argsJsonStr = Arrays.stream(args)
                    .map(this::formatArgument) // 使用新的格式化方法
                    .collect(Collectors.joining(", "));
            // === MODIFICATION END ===


            // 功能描述信息
            String description = getApiOperationLogDescription(joinPoint);

            // 打印请求相关参数
            log.info("====== 请求开始: [{}], 入参: {}, 请求类: {}, 请求方法: {} =================================== ",
                    description, argsJsonStr, className, methodName);

            // 执行切点方法
            Object result = joinPoint.proceed();

            // 执行耗时
            long executionTime = System.currentTimeMillis() - startTime;

            // 打印出参等相关信息 (注意: 如果返回值也可能包含不可序列化对象，这里也可能需要特殊处理)
            String resultJsonStr;
            try {
                resultJsonStr = JsonUtil.toJsonString(result);
            } catch (Exception e) {
                log.warn("序列化返回值出错: {}", e.getMessage());
                resultJsonStr = "[Serialization Error]"; // Or handle differently
            }

            log.info("====== 请求结束: [{}], 耗时: {}ms, 出参: {} =================================== ",
                    description, executionTime, resultJsonStr);


            return result;
        } finally {
            MDC.clear();
        }
    }

    /**
     * 格式化方法参数，特殊处理不可序列化的类型
     * @param arg 方法参数
     * @return 参数的字符串表示
     */
    private String formatArgument(Object arg) {
        if (arg == null) {
            return "null";
        }
        // 处理 MultipartFile
        if (arg instanceof MultipartFile) {
            MultipartFile file = (MultipartFile) arg;
            // 只记录元数据，避免序列化文件流
            return String.format("{\"_type\":\"MultipartFile\", \"originalFilename\":\"%s\", \"size\":%d, \"contentType\":\"%s\"}",
                    file.getOriginalFilename(), file.getSize(), file.getContentType());
        }
        // 处理 HttpServletRequest/Response (可选, 但推荐)
        else if (arg instanceof HttpServletRequest) {
            return "<HttpServletRequest>";
        } else if (arg instanceof HttpServletResponse) {
            return "<HttpServletResponse>";
        }
        // 其他类型尝试用 JsonUtil 序列化
        else {
            try {
                return JsonUtil.toJsonString(arg);
            } catch (Exception e) {
                log.warn("参数序列化出错: {}, 参数类型: {}", e.getMessage(), arg.getClass().getName());
                // 序列化失败时返回类型和对象的 toString() 或其他标记
                return String.format("[\"Serialization Error for %s: %s\"]", arg.getClass().getSimpleName(), e.getMessage());
            }
        }
    }


    /**
     * 获取注解的描述信息
     * @param joinPoint
     * @return
     */
    private String getApiOperationLogDescription(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ApiOperationLog apiOperationLog = method.getAnnotation(ApiOperationLog.class);
        return apiOperationLog.description();
    }

    /*
    // 不再需要这个方法
    private Function<Object, String> toJsonStr() {
        return arg -> JsonUtil.toJsonString(arg);
    }
    */

}