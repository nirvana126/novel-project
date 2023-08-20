package com.feiyu.novel.core.auth;

import com.feiyu.novel.core.common.constant.ErrorCodeEnum;
import com.feiyu.novel.core.common.exception.BusinessException;
import com.feiyu.novel.core.common.util.JwtUtils;
import com.feiyu.novel.core.constant.SystemConfigConsts;
import com.feiyu.novel.dto.UserInfoDto;
import com.feiyu.novel.manager.cache.UserInfoCacheManager;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 策略模式实现用户认证授权
 */
public interface AuthStrategy {

    /**
     * 独立的用户认证逻辑
     * @param token
     * @param requestUri
     * @throws BusinessException
     */
    void auth(String token,String requestUri)throws BusinessException;

    /**
     * 默认的通用单点登录认证授权（适用于前台门户、作家系统等多个系统）
     * @param jwtUtils
     * @param token
     * @param userInfoCacheManager
     * @return
     */
    default Long authSSO(JwtUtils jwtUtils,String token,UserInfoCacheManager userInfoCacheManager){
        //token为空
        if(!StringUtils.hasText(token)){
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        //token存在，则解析出用户ID
        Long userId = jwtUtils.parseToken(token, SystemConfigConsts.NOVEL_FRONT_KEY);
        if(Objects.isNull(userId)){
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        UserInfoDto user = userInfoCacheManager.getUser(userId);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }
        //用户存在，则设置userid到前线程
        UserHolder.setUserId(userId);
        //返回userId
        return userId;
    }
}
