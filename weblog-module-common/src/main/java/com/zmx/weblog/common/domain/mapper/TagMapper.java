package com.zmx.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.weblog.common.domain.dos.TagDO;

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

}
