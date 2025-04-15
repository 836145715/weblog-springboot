package com.zmx.weblog.admin.model.vo.article;

import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("查询文章分页数据出参 VO")
public class FindArticlePageListRspVO {
    /**
     * 文章ID
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}