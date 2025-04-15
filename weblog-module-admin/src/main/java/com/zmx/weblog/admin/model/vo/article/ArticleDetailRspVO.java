package com.zmx.weblog.admin.model.vo.article;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("文章详情响应VO")
public class ArticleDetailRspVO {
    private Long id;
    private String title;
    private String cover;
    private String summary;
    private String content;
    private Long categoryId;
    private List<Long> tagIds;
}