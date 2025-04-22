package com.zmx.weblog.web.model.vo.article;

import com.zmx.weblog.common.model.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(description = "查询首页文章分页列表请求参数")
public class FindIndexArticlePageListReqVO extends BasePageQuery {

}
