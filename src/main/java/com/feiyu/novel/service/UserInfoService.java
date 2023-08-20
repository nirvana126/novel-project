package com.feiyu.novel.service;

import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.dao.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feiyu.novel.dto.req.UserInfoUptReqDto;
import com.feiyu.novel.dto.req.UserLoginReqDto;
import com.feiyu.novel.dto.req.UserRegisterReqDto;
import com.feiyu.novel.dto.resp.UserInfoRespDto;
import com.feiyu.novel.dto.resp.UserLoginRespDto;
import com.feiyu.novel.dto.resp.UserRegisterRespDto;

/**
* @author GSJ
* @description 针对表【user_info(用户信息)】的数据库操作Service
* @createDate 2023-04-04 19:01:10
*/
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户注册
     *
     * @param dto 注册参数
     * @return JWT
     */
    RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto);

    RestResp<UserLoginRespDto> login(UserLoginReqDto dto);

    RestResp<UserInfoRespDto> getUserInfo(Long userId);

    RestResp<Void> updateUserInfo(UserInfoUptReqDto dto);

    RestResp<Void> saveFeedback(Long userId, String content);

    RestResp<Void> deleteFeedback(Long userId, Long id);
}
