package com.zmx.weblog.admin.model.vo.tag;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@ApiModel("查询标签分页数据出参 VO")
public class FindTagPageListRspVO {
    /**
     * 标签 ID
     */
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}