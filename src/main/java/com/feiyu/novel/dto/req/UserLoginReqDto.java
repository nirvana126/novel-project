package com.feiyu.novel.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserLoginReqDto {

    @NotBlank(message = "手机号不能为空！")
    @Pattern(regexp = "^1[3|4|5|6|7|8|9][0-9]{9}$",message = "手机号格式不正确！")
    private String username;

    @NotBlank(message = "密码不能为空！")
    private String password;
}
