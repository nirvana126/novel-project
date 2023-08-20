package com.feiyu.novel.dto.resp;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoRespDto {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String userPhoto;

    /**
     * 用户性别;0-男 1-女
     */
    private Integer userSex;
}
