package com.zmx.weblog.admin.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.zmx.weblog.admin.model.vo.article.ArticleDetailRspVO;
import com.zmx.weblog.common.domain.dos.ArticleDO;

@Mapper
public interface ArticleDetailConvert {
    ArticleDetailConvert INSTANCE = Mappers.getMapper(ArticleDetailConvert.class);

    ArticleDetailRspVO convertDO2VO(ArticleDO bean);

}
