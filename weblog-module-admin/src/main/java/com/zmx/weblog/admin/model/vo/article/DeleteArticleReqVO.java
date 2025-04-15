package com.zmx.weblog.admin.model.vo.article;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("删除文章请求VO")
public class DeleteArticleReqVO {
    @ApiModelProperty("文章ID")
    @NotNull(message = "文章ID不能为空")
    private Long articleId;
}