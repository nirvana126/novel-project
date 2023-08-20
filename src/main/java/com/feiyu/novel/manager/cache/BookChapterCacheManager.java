package com.feiyu.novel.manager.cache;

import com.feiyu.novel.core.common.constant.CacheConsts;
import com.feiyu.novel.dao.entity.BookChapter;
import com.feiyu.novel.dao.mapper.BookChapterMapper;
import com.feiyu.novel.dto.resp.BookChapterRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 小说章节 缓存管理类
 *
 * @author xiongxiaoyang
 * @date 2022/5/12
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookChapterCacheManager {

    private final BookChapterMapper bookChapterMapper;

    /**
     * 查询小说章节信息，并放入本地缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.BOOK_CHAPTER_CACHE_NAME)
    public BookChapterRespDto getChapter(Long chapterId) {
        log.info("当前章节id:"+chapterId);
        BookChapter bookChapter = bookChapterMapper.selectById(chapterId);
        return BookChapterRespDto.builder()
            .id(chapterId)
            .bookId(bookChapter.getBookId())
            .chapterNum(bookChapter.getChapterNum())
            .chapterName(bookChapter.getChapterName())
            .chapterWordCount(bookChapter.getWordCount())
            .chapterUpdateTime(bookChapter.getUpdateTime())
            .build();
    }


}
