package com.feiyu.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterRespDto {

    private Long uid;

    private String token;
}
