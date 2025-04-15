package com.zmx.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.weblog.common.domain.dos.ArticleTagRelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleTagRelMapper extends BaseMapper<ArticleTagRelDO> {


    int insertBatch(List<ArticleTagRelDO> relDOS);
}