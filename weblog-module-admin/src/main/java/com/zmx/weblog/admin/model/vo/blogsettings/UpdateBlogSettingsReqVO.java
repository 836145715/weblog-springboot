package com.zmx.weblog.admin.model.vo.blogsettings;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("博客设置请求VO")
public class UpdateBlogSettingsReqVO {
    @NotBlank(message = "博客Logo不能为空")
    private String logo;

    @NotBlank(message = "博客名称不能为空")
    private String name;

    @NotBlank(message = "作者名不能为空")
    private String author;

    @NotBlank(message = "介绍语不能为空")
    private String introduction;

    @NotBlank(message = "作者头像不能为空")
    private String avatar;

    private String githubHomepage;

    private String csdnHomepage;

    private String giteeHomepage;

    private String zhihuHomepage;
}