package com.feiyu.novel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.dao.entity.HomeBook;
import com.feiyu.novel.dto.resp.HomeBookRespDto;
import com.feiyu.novel.manager.cache.HomeBookCacheManager;
import com.feiyu.novel.service.HomeBookService;
import com.feiyu.novel.dao.mapper.HomeBookMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author GSJ
* @description 针对表【home_book(小说推荐)】的数据库操作Service实现
* @createDate 2023-04-01 20:42:16
*/
@Service
public class HomeBookServiceImpl extends ServiceImpl<HomeBookMapper, HomeBook>
    implements HomeBookService{

    @Resource
    private HomeBookCacheManager homeBookCacheManager;

    @Override
    public RestResp<List<HomeBookRespDto>> listHomeBooks() {

        return RestResp.ok(homeBookCacheManager.listHomeBooks());
    }
}




