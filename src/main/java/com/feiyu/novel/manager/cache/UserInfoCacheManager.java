package com.feiyu.novel.manager.cache;

import com.feiyu.novel.core.common.constant.CacheConsts;
import com.feiyu.novel.dao.entity.UserInfo;
import com.feiyu.novel.dao.mapper.UserInfoMapper;
import com.feiyu.novel.dto.UserInfoDto;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
//@RequiredArgsConstructor
public class UserInfoCacheManager {

    @Resource
    private  UserInfoMapper userInfoMapper;

    @Resource
    private  RedisTemplate<String,UserInfoDto> redisTemplate;

//    public UserInfoCacheManager(UserInfoMapper userInfoMapper, RedisTemplate<String, UserInfoDto> redisTemplate) {
//        this.userInfoMapper = userInfoMapper;
//        this.redisTemplate = redisTemplate;
//    }

    public UserInfoDto getUser(Long userId){
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if(userInfo == null){
            return null;
        }
        //若不为空，则存入Redis中
        String key = "Cache:Novel:UserInfoCache:" + userId;
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .id(userInfo.getId())
                .status(userInfo.getStatus()).build();
        redisTemplate.opsForValue().set(key,userInfoDto,86400, TimeUnit.SECONDS);
        return userInfoDto;
    }
}
