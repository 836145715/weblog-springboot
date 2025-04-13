package com.zmx.weblog.admin.model.vo.tag;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

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
@ApiModel("批量添加标签请求VO")
public class AddBatchTagReqVO {

    @NotEmpty(message = "标签名称数组不能为空")
    @ApiModelProperty(value = "标签名称数组", required = true)
    private List<String> names;

}
