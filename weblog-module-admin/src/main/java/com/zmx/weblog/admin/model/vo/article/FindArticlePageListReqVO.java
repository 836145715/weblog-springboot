package com.zmx.weblog.admin.model.vo.article;

import java.time.LocalDate;
import com.zmx.weblog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("查询文章分页数据入参 VO")
public class FindArticlePageListReqVO extends BasePageQuery {
    /**
     * 文章标题
     */
    private String title;

    /**
     * 创建的起始日期
     */
    private LocalDate startDate;

    /**
     * 创建的结束日期
     */
    private LocalDate endDate;
}