package com.feiyu.novel.controller.front;

import com.feiyu.novel.core.common.resp.PageRespDto;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.core.constant.ApiRouterConsts;
import com.feiyu.novel.dto.req.BookSearchReqDto;
import com.feiyu.novel.dto.resp.BookInfoRespDto;
import com.feiyu.novel.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_FRONT_SEARCH_URL_PREFIX)
public class SearchController {

    private final SearchService searchService;

    /**
     * 小说搜索接口
     */
    @GetMapping("books")
    public RestResp<PageRespDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition) {
        return searchService.searchBooks(condition);
    }
}
