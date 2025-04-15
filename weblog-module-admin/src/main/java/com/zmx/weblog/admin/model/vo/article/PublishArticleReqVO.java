package com.zmx.weblog.admin.model.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发布文章请求VO")
public class PublishArticleReqVO {

    @NotBlank(message = "文章标题不能为空")
    @ApiModelProperty("文章标题")
    @Length(min = 1, max = 40, message = "文章标题字数需大于 1 小于 40")
    private String title;

    @NotBlank(message = "文章封面不能为空")
    @ApiModelProperty("文章封面")
    private String cover;

    @ApiModelProperty("文章摘要")
    private String summary;

    @NotBlank(message = "文章内容不能为空")
    @ApiModelProperty("文章内容")
    private String content;

    @NotNull(message = "文章分类不能为空")
    @ApiModelProperty("分类ID")
    private Long categoryId;

    @NotEmpty(message = "文章标签不能为空")
    @ApiModelProperty("标签列表")
    private List<String> tags;
}