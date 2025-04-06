package com.zmx.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.zmx.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.zmx.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.zmx.weblog.admin.model.vo.category.FindCategoryPageListRspVO;
import com.zmx.weblog.admin.service.AdminCategoryService;
import com.zmx.weblog.common.domain.dos.CategoryDO;
import com.zmx.weblog.common.domain.mapper.CategoryMapper;
import com.zmx.weblog.common.enums.ResponseCodeEnum;
import com.zmx.weblog.common.exception.BizException;
import com.zmx.weblog.common.model.vo.SelectRspVO;
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
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加分类
     *
     * @param addCategoryReqVO
     * @return
     */
    @Override
    public Response addCategory(AddCategoryReqVO addCategoryReqVO) {
		String categoryName = addCategoryReqVO.getName();

        // 先判断该分类是否已经存在
        CategoryDO categoryDO = categoryMapper.selectByName(categoryName);

        if (Objects.nonNull(categoryDO)) {
            log.warn("分类名称： {}, 此分类已存在", categoryName);
            throw new BizException(ResponseCodeEnum.CATEGORY_NAME_IS_EXISTED);
        }

        // 构建 DO 类
        CategoryDO insertCategoryDO = CategoryDO.builder()
                .name(addCategoryReqVO.getName().trim())
                .build();

        // 执行 insert
        categoryMapper.insert(insertCategoryDO);

        return Response.success();
    }

    @Override
    public PageResponse findCategoryPageList(FindCategoryPageListReqVO req) {
        //获取当前页 每页显示的记录数
        long current = req.getCurrent();
        long size = req.getSize();

        // 构建分页对象
        Page<CategoryDO> page = new Page<>(current, size);

        // 构建查询条件
        LambdaQueryWrapper<CategoryDO> queryWrapper = new LambdaQueryWrapper<>();

        String name = req.getName();
        LocalDate startDate = req.getStartDate();
        LocalDate endDate = req.getEndDate();

        queryWrapper.like(StringUtils.isNotBlank(name), CategoryDO::getName, name.trim())
                .ge(Objects.nonNull(startDate), CategoryDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), CategoryDO::getCreateTime, endDate)
                .orderByDesc(CategoryDO::getCreateTime);

        // 执行分页查询
        Page<CategoryDO> pageResult = categoryMapper.selectPage(page, queryWrapper);

        List<CategoryDO> categoryDOList = pageResult.getRecords();
        
        //DO转为VO
        List<FindCategoryPageListRspVO> vos = null;
        if(CollectionUtils.isNotEmpty(categoryDOList)){
            vos = categoryDOList.stream()
                    .map(category->FindCategoryPageListRspVO.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .createTime(category.getCreateTime())
                            .build())
                    .collect(Collectors.toList());
        }


        return PageResponse.success(pageResult,vos);
    }

    @Override
    public Response deleteCategory(DeleteCategoryReqVO reqVO) {
        categoryMapper.deleteById(reqVO.getId());
        return Response.success();
    }

    @Override
    public Response findCategorySelectList() {
        //查询所有分类
        List<CategoryDO> categoryDOList = categoryMapper.selectList(new LambdaQueryWrapper<>());
        List<SelectRspVO> vos = null;
        //DO转为VO
        if(CollectionUtils.isNotEmpty(categoryDOList)){
            vos = categoryDOList.stream()
                    .map(category->SelectRspVO.builder()
                            .label(category.getName())
                            .value(category.getId()).build())
                            .collect(Collectors.toList());
        }
        return Response.success(vos);
    }
}
