package com.zmx.weblog.web.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.zmx.weblog.common.domain.dos.ArticleDO;
import com.zmx.weblog.web.model.vo.article.FindIndexArticlePageListRspVO;

@Mapper
public interface ArticleConvert {
    ArticleConvert INSTANCE = Mappers.getMapper(ArticleConvert.class);

    FindIndexArticlePageListRspVO convertDO2VO(ArticleDO bean);

}
