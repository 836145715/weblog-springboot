package com.zmx.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zmx.weblog.common.domain.dos.TagDO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public interface TagMapper extends BaseMapper<TagDO> {

    /**
     * 根据标签名列表查询
     * 
     * @param names
     * @return
     */
    default List<TagDO> selectByNames(List<String> names) {
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TagDO::getName, names);
        return selectList(wrapper);
    }

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

    default List<TagDO> selectList() {
        return selectList(new QueryWrapper<>());
    }

    /**
     * 批量插入标签（已存在的不插入），通过一条SQL实现
     * 注意：需要在TagDO.name上有唯一约束
     */
    int insertBatch(@Param("names") List<String> names);


    /**
     * 根据标签 ID 批量查询
     * @param tagIds
     * @return
     */
    default List<TagDO> selectByIds(List<Long> tagIds){
        if (CollectionUtils.isEmpty(tagIds)){
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TagDO::getId, tagIds);
        return selectList(wrapper);
    }

}
