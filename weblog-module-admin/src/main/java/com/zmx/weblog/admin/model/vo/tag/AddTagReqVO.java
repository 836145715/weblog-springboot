package com.zmx.weblog.admin.model.vo.tag;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

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
@ApiModel("新增标签请求VO")
public class AddTagReqVO {

    @NotBlank(message = "标签名称不能为空")
    @Length(min = 1, max = 10, message = "标签名称字数限制 1 ~ 10 之间")
    private String name;
}
