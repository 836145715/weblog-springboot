package com.zmx.weblog.admin.model.vo.category;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("搜索分类请求VO")
public class SearchCateReqVO {
    /**
     * 分类名称
     */
    private String keyword;

}
