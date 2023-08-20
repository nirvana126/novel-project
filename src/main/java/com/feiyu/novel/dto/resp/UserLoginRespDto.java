package com.feiyu.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginRespDto {

    private Long uid;

    private String nickName;

    private String token;
}
