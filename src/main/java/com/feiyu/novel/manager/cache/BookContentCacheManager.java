package com.feiyu.novel.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.feiyu.novel.core.common.constant.CacheConsts;
import com.feiyu.novel.core.constant.DatabaseConsts;
import com.feiyu.novel.dao.entity.BookContent;
import com.feiyu.novel.dao.mapper.BookContentMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 小说内容 缓存管理类
 *
 * @author xiongxiaoyang
 * @date 2022/5/12
 */
@Component
@RequiredArgsConstructor
public class BookContentCacheManager {

    private final BookContentMapper bookContentMapper;

    private static final String BOOK_CONTENT_KEY_PREFIX="book:content:chapterId:";

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 查询小说内容，并放入Redis中,别忘记设置过期时间
     */
    public String getBookContent(Long chapterId) {
        String key = BOOK_CONTENT_KEY_PREFIX+chapterId;
        String res = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(res)){
            QueryWrapper<BookContent> contentQueryWrapper = new QueryWrapper<>();
            contentQueryWrapper.eq(DatabaseConsts.BookContentTable.COLUMN_CHAPTER_ID, chapterId)
                    .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
            BookContent bookContent = bookContentMapper.selectOne(contentQueryWrapper);
            String content = bookContent.getContent();
            stringRedisTemplate.opsForValue().set(key,content,60*60, TimeUnit.SECONDS);
            return content;
        }else {
            return res;
        }
    }
}
