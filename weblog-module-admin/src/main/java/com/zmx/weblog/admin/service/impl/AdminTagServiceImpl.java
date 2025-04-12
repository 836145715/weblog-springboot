package com.zmx.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.admin.model.vo.tag.AddTagReqVO;
import com.zmx.weblog.admin.model.vo.tag.DeleteTagReqVO;
import com.zmx.weblog.admin.model.vo.tag.FindTagPageListReqVO;
import com.zmx.weblog.admin.model.vo.tag.FindTagPageListRspVO;
import com.zmx.weblog.admin.service.AdminTagService;
import com.zmx.weblog.common.domain.dos.TagDO;
import com.zmx.weblog.common.domain.mapper.TagMapper;
import com.zmx.weblog.common.enums.ResponseCodeEnum;
import com.zmx.weblog.common.exception.BizException;
import com.zmx.weblog.common.utils.PageResponse;
import com.zmx.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminTagServiceImpl implements AdminTagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public Response addTag(AddTagReqVO addTagReqVO) {
        String tagName = addTagReqVO.getName();

        // 先判断该标签是否已经存在
        TagDO tagDO = tagMapper.selectByName(tagName);

        if (Objects.nonNull(tagDO)) {
            log.warn("标签名称： {}, 此标签已存在", tagName);
            throw new BizException(ResponseCodeEnum.TAG_NAME_IS_EXISTED);
        }

        // 构建 DO 类
        TagDO insertTagDO = TagDO.builder()
                .name(addTagReqVO.getName().trim())
                .build();

        // 执行 insert
        tagMapper.insert(insertTagDO);

        return Response.success();
    }

    @Override
    public Response deleteTag(DeleteTagReqVO deleteTagReqVO) {
        tagMapper.deleteById(deleteTagReqVO.getId());
        return Response.success();
    }

    @Override
    public PageResponse findTagPageList(FindTagPageListReqVO findTagPageListReqVO) {
        // 从请求参数中获取查询条件
        String name = findTagPageListReqVO.getName(); // 标签名称
        LocalDate startDate = findTagPageListReqVO.getStartDate(); // 开始时间
        LocalDate endDate = findTagPageListReqVO.getEndDate(); // 结束时间

        // 构建 MyBatis Plus 查询条件
        LambdaQueryWrapper<TagDO> queryWrapper = new LambdaQueryWrapper<>();

        // 构建查询条件:
        // 1. 标签名称模糊查询
        // 2. 创建时间在 startDate 和 endDate 之间
        // 3. 按创建时间倒序排序
        queryWrapper.like(StringUtils.isNotBlank(name), TagDO::getName, name.trim())
                .ge(Objects.nonNull(startDate), TagDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), TagDO::getCreateTime, endDate)
                .orderByDesc(TagDO::getCreateTime);

        // 构建分页对象,设置当前页和每页显示的记录数
        Page<TagDO> page = new Page<>(findTagPageListReqVO.getCurrent(), findTagPageListReqVO.getSize());
        // 执行分页查询
        Page<TagDO> pageResult = tagMapper.selectPage(page, queryWrapper);

        // 将 DO 转换为 VO 对象
        List<FindTagPageListRspVO> vos = null;
        // 如果查询结果不为空,则转换数据
        if (CollectionUtils.isNotEmpty(pageResult.getRecords())) {
            vos = pageResult.getRecords().stream()
                    .map(tag -> FindTagPageListRspVO.builder()
                            .id(tag.getId()) // 设置标签ID
                            .name(tag.getName()) // 设置标签名称
                            .createTime(tag.getCreateTime()) // 设置创建时间
                            .build())
                    .collect(Collectors.toList());
        }

        System.out.println("vos: " + vos);

        // 返回分页数据
        return PageResponse.success(pageResult, vos);
    }

}
