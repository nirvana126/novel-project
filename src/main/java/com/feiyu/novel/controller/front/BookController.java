package com.feiyu.novel.controller.front;

import com.feiyu.novel.core.annotation.IPLimit;
import com.feiyu.novel.core.common.resp.PageRespDto;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.core.constant.ApiRouterConsts;
import com.feiyu.novel.core.rateLimiter.Limit;
import com.feiyu.novel.core.rateLimiter.LimitType;
import com.feiyu.novel.dto.req.BookSearchReqDto;
import com.feiyu.novel.dto.resp.*;
import com.feiyu.novel.service.BookInfoService;
import com.feiyu.novel.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_FRONT_BOOK_URL_PREFIX)
@Slf4j
public class BookController {


    private final BookInfoService bookInfoService;


    /**
     * 小说分类查询页面
     */
    @GetMapping("category/list")
    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<List<BookCategoryRespDto>> listCategory(@Parameter Integer workDirection){
        return bookInfoService.listCategory(workDirection);
    }

    /**
     * 小说点击榜查询接口
     */
    @Operation(summary = "小说点击榜查询接口")
    @GetMapping("visit_rank")
//    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<List<BookRankRespDto>> listVisitRankBooks() {
        return bookInfoService.listVisitRankBooks();
    }

    /**
     * 小说新书榜查询接口
     */
    @Operation(summary = "小说新书榜查询接口")
    @GetMapping("newest_rank")
    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<List<BookRankRespDto>> listNewestRankBooks() {
        return bookInfoService.listNewestRankBooks();
    }

    /**
     * 小说更新榜查询接口
     */
    @Operation(summary = "小说更新榜查询接口")
    @GetMapping("update_rank")
    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<List<BookRankRespDto>> listUpdateRankBooks() {
        return bookInfoService.listUpdateRankBooks();
    }

    /**
     * 根据id查询小说简介信息
     */
    @Operation(summary = "根据id查询小说简介信息")
    @GetMapping("{bookId}")
    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<BookInfoRespDto> getBookById(@PathVariable Long bookId) {

        return bookInfoService.getBookById(bookId);
    }

    /**
     * 增加小说点击量接口
     */
    @Operation(summary = "增加小说点击量接口")
    @PostMapping("visit")
    public RestResp<Void> addVisitCount(@Parameter(description = "小说ID") Long bookId) {
        return bookInfoService.addVisitCount(bookId);
    }

    /**
     * 根据id查询小说最新章节
     */
    @GetMapping("last_chapter/about")
    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<BookChapterAboutRespDto> getNewestChapter(@Parameter Long bookId){
        return bookInfoService.getNewestChapter(bookId);
    }

    /**
     * 小说章节列表查询接口
     */
    @Operation(summary = "小说章节列表查询接口")
    @GetMapping("chapter/list")
    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<List<BookChapterRespDto>> listChapters(
            @Parameter(description = "小说ID") Long bookId) {
        return bookInfoService.listChapters(bookId);
    }

    /**
     * 小说内容（正文）相关信息查询
     */
    @Operation(summary = "小说内容相关信息查询接口")
    @GetMapping("content/{chapterId}")
    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<BookContentAboutRespDto> getBookContentAbout(
            @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId) {
        return bookInfoService.getBookContentAbout(chapterId);
    }

    /**
     * 获取上一章节ID接口
     */
    @Operation(summary = "获取上一章节ID接口")
    @GetMapping("pre_chapter_id/{chapterId}")
    public RestResp<Long> getPreChapterId(
            @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId) {
        return bookInfoService.getPreChapterId(chapterId);
    }

    /**
     * 获取下一章节ID接口
     */
    @Operation(summary = "获取下一章节ID接口")
    @GetMapping("next_chapter_id/{chapterId}")
    public RestResp<Long> getNextChapterId(
            @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId) {
        return bookInfoService.getNextChapterId(chapterId);
    }

    /**
     * 同类推荐列表查询接口
     */
    @Operation(summary = "同类推荐列表查询接口")
    @GetMapping("rec_list")
    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<List<BookInfoRespDto>> listRecBooks(
            @Parameter(description = "小说ID") Long bookId) throws NoSuchAlgorithmException {
        return bookInfoService.listRecBooks(bookId);
    }

    /**
     * 小说最新评论查询接口
     */
    @GetMapping("comment/newest_list")
    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<BookCommentRespDto> listNewestComments(Long bookId) {
        return bookInfoService.listNewestComments(bookId);
    }



    @GetMapping("/test")
    @Limit(key = "ip_limit_test",period = 10,count = 3,limitType = LimitType.IP)
    public RestResp<String> test2(){
        return RestResp.ok("正在进行测试......");
    }



}
