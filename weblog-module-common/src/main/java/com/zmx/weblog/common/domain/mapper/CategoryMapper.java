package com.zmx.weblog.common.domain.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.common.domain.dos.CategoryDO;

public interface CategoryMapper extends BaseMapper<CategoryDO> {

    /**
     * 根据用户名查询
     * 
     * @param categoryName
     * @return
     */
    default CategoryDO selectByName(String categoryName) {
        // 构建查询条件
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getName, categoryName);

        // 执行查询
        return selectOne(wrapper);
    }

    default List<CategoryDO> searchByKey(String keyword) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(CategoryDO::getName, keyword).orderByDesc(CategoryDO::getCreateTime);
        return selectList(wrapper);
    }

    default List<CategoryDO> selectList() {
        return selectList(new QueryWrapper<>());
    }

    default Page<CategoryDO> findCategoryPageList(String name, LocalDate startDate, LocalDate endDate, long current,
            long pageSize) {

        // 构建查询条件
        LambdaQueryWrapper<CategoryDO> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotBlank(name), CategoryDO::getName, name.trim())
                .ge(Objects.nonNull(startDate), CategoryDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), CategoryDO::getCreateTime, endDate)
                .orderByDesc(CategoryDO::getCreateTime);

        // 执行分页查询
        Page<CategoryDO> pageResult = selectPage(new Page<>(current, pageSize), queryWrapper);
        return pageResult;
    }
}
