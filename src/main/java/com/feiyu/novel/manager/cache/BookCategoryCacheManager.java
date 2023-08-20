package com.feiyu.novel.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.feiyu.novel.core.common.constant.CacheConsts;
import com.feiyu.novel.dao.entity.BookCategory;
import com.feiyu.novel.dao.mapper.BookCategoryMapper;
import com.feiyu.novel.dto.resp.BookCategoryRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookCategoryCacheManager {

    private final BookCategoryMapper bookCategoryMapper;

    /**
     * 根据作品方向查询小说分类列表，并放入本地缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,value = CacheConsts.BOOK_CATEGORY_LIST_CACHE_NAME)
    public List<BookCategoryRespDto> listCategory(Integer workDirection){
        QueryWrapper<BookCategory> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("work_direction",workDirection);
        List<BookCategory> bookCategories = bookCategoryMapper.selectList(queryWrapper);
        return bookCategories.stream().map(v ->
                BookCategoryRespDto.builder()
                        .id(v.getId())
                        .name(v.getName()).build()).toList();
    }
}
