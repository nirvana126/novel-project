package com.feiyu.novel.service;

import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.dao.entity.HomeBook;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feiyu.novel.dto.resp.HomeBookRespDto;

import java.util.List;

/**
* @author GSJ
* @description 针对表【home_book(小说推荐)】的数据库操作Service
* @createDate 2023-04-01 20:42:16
*/
public interface HomeBookService extends IService<HomeBook> {

    RestResp<List<HomeBookRespDto>> listHomeBooks();
}
