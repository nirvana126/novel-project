package com.feiyu.novel.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.feiyu.novel.core.common.constant.CacheConsts;
import com.feiyu.novel.core.constant.DatabaseConsts;
import com.feiyu.novel.dao.entity.AuthorInfo;
import com.feiyu.novel.dao.mapper.AuthorInfoMapper;
import com.feiyu.novel.dto.AuthorInfoDto;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 作家信息 缓存管理类
 *
 * @author xiongxiaoyang
 * @date 2022/5/12
 */
@Component
//@RequiredArgsConstructor
public class AuthorInfoCacheManager {

    @Resource
    private AuthorInfoMapper authorInfoMapper;

    @Resource
    private RedisTemplate<String,AuthorInfoDto> redisTemplate;

    /**
     * 查询作家信息，并放入缓存中
     */
//    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
//        value = CacheConsts.AUTHOR_INFO_CACHE_NAME, unless = "#result == null")
    public AuthorInfoDto getAuthor(Long userId) {
        QueryWrapper<AuthorInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .eq(DatabaseConsts.AuthorInfoTable.COLUMN_USER_ID, userId)
            .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        AuthorInfo authorInfo = authorInfoMapper.selectOne(queryWrapper);
        if (Objects.isNull(authorInfo)) {
            return null;
        }
        AuthorInfoDto authorInfoDto = AuthorInfoDto.builder()
                .id(authorInfo.getId())
                .penName(authorInfo.getPenName())
                .status(authorInfo.getStatus()).build();
        String key = "Cache:Novel:AuthorInfoCache:" + authorInfo.getId();
        redisTemplate.opsForValue().set(key,authorInfoDto,86400, TimeUnit.SECONDS);
        return authorInfoDto;
    }

//    @CacheEvict(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
//        value = CacheConsts.AUTHOR_INFO_CACHE_NAME)
//    public void evictAuthorCache() {
//        // 调用此方法自动清除作家信息的缓存
//    }

}
