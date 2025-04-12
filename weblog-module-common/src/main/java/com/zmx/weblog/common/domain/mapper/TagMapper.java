package com.zmx.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.common.domain.dos.TagDO;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public interface TagMapper extends BaseMapper<TagDO> {
    /**
     * 根据标签名查询
     * 
     * @param name
     * @return
     */
    default TagDO selectByName(String name) {
        // 构建查询条件
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TagDO::getName, name);

        // 执行查询
        return selectOne(wrapper);
    }

    default List<TagDO> searchByKey(String keyword) {

        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(TagDO::getName, keyword).orderByDesc(TagDO::getCreateTime);
        return selectList(wrapper);
    }

    default Page<TagDO> findTagPageList(String name, LocalDate startDate, LocalDate endDate, long current,
            long pageSize) {

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

        // 执行分页查询
        Page<TagDO> pageResult = selectPage(new Page<>(current, pageSize), queryWrapper);
        return pageResult;
    }

}
