package com.zmx.weblog.admin.model.vo.tag;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("删除标签请求VO")
public class DeleteTagReqVO {

    @NotNull(message = "标签ID不能为空")
    private Long id;
}
