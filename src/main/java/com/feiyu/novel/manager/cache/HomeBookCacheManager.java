package com.feiyu.novel.manager.cache;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.feiyu.novel.core.common.constant.CacheConsts;
import com.feiyu.novel.dao.entity.BookInfo;
import com.feiyu.novel.dao.entity.HomeBook;
import com.feiyu.novel.dao.mapper.BookInfoMapper;
import com.feiyu.novel.dao.mapper.HomeBookMapper;
import com.feiyu.novel.dto.resp.HomeBookRespDto;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HomeBookCacheManager {

    @Resource
    private HomeBookMapper homeBookMapper;

    @Resource
    private BookInfoMapper bookInfoMapper;

    //这里分别指定了缓存管理器和缓存组件的名字
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER
            , cacheNames = CacheConsts.HOME_BOOK_CACHE_NAME)
    public List<HomeBookRespDto> listHomeBooks(){

        //先从数据库查出所有首页推荐小说列表
        List<HomeBook> homeBooks = homeBookMapper.selectList(null);
        //若为空则返回空集合
        if (CollectionUtils.isEmpty(homeBooks)){
            return Collections.emptyList();
        }
        //取出其中id列表
        List<Long> bookIds = homeBooks.stream().map(HomeBook::getBookId).toList();

        //根据小说id列表查询相关小说信息
        QueryWrapper<BookInfo> bookInfoQueryWrapper=new QueryWrapper<>();
        bookInfoQueryWrapper.in("id",bookIds);
        List<BookInfo> bookInfos = bookInfoMapper.selectList(bookInfoQueryWrapper);

        //组装所需的HomeBookRespDto数据
        if (CollectionUtils.isEmpty(bookInfos)){
            return Collections.emptyList();
        }
        //先将bookInfo以id为键组成键值对形式
        Map<Long, BookInfo> bookInfoMap = bookInfos.stream().collect(Collectors.toMap(BookInfo::getId, p-> p));
        return homeBooks.stream().map(homeBook -> {
            Long bookId = homeBook.getBookId();
            Integer type = homeBook.getType();
            BookInfo bookInfo = bookInfoMap.get(bookId);
            HomeBookRespDto homeBookRespDto = new HomeBookRespDto();
            homeBookRespDto.setType(type);
            homeBookRespDto.setBookId(bookId);
            homeBookRespDto.setBookName(bookInfo.getBookName());
            homeBookRespDto.setAuthorName(bookInfo.getAuthorName());
            homeBookRespDto.setBookDesc(bookInfo.getBookDesc());
            homeBookRespDto.setPicUrl(bookInfo.getPicUrl());
            return homeBookRespDto;
        }).toList();
    }
}
