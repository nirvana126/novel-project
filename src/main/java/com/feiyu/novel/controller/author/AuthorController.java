package com.feiyu.novel.controller.author;


import com.feiyu.novel.core.auth.UserHolder;
import com.feiyu.novel.core.common.req.PageReqDto;
import com.feiyu.novel.core.common.resp.PageRespDto;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.core.constant.ApiRouterConsts;
import com.feiyu.novel.dto.req.AuthorRegisterReqDto;
import com.feiyu.novel.dto.req.BookAddReqDto;
import com.feiyu.novel.dto.req.ChapterAddReqDto;
import com.feiyu.novel.dto.resp.BookChapterRespDto;
import com.feiyu.novel.dto.resp.BookInfoRespDto;
import com.feiyu.novel.service.AuthorService;
import com.feiyu.novel.service.BookInfoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(ApiRouterConsts.API_AUTHOR_URL_PREFIX)
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final BookInfoService bookInfoService;

    /**
     * 作家注册接口
     */
    @Operation(summary = "作家注册接口")
    @PostMapping("register")
    public RestResp<Void> register(@Valid @RequestBody AuthorRegisterReqDto dto) {
        dto.setUserId(UserHolder.getUserId());
        return authorService.register(dto);
    }

    /**
     * 查询作家状态接口
     */
    @Operation(summary = "作家状态查询接口")
    @GetMapping("status")
    public RestResp<Integer> getStatus() {
        return authorService.getStatus(UserHolder.getUserId());
    }

    /**
     * 小说发布接口
     */
    @Operation(summary = "小说发布接口")
    @PostMapping("book")
    public RestResp<Void> publishBook(@Valid @RequestBody BookAddReqDto dto) {
        return bookInfoService.saveBook(dto);
    }

    /**
     * 小说发布列表查询接口
     */
    @Operation(summary = "小说发布列表查询接口")
    @GetMapping("books")
    public RestResp<PageRespDto<BookInfoRespDto>> listBooks( PageReqDto dto) {
        return bookInfoService.listAuthorBooks(dto);
    }

    /**
     * 小说章节发布接口
     */
    @Operation(summary = "小说章节发布接口")
    @PostMapping("book/chapter/{bookId}")
    public RestResp<Void> publishBookChapter(
            @PathVariable("bookId") Long bookId,
            @Valid @RequestBody ChapterAddReqDto dto) {
        dto.setBookId(bookId);
        return bookInfoService.saveBookChapter(dto);
    }

    /**
     * 小说章节发布列表查询接口
     */
    @Operation(summary = "小说章节发布列表查询接口")
    @GetMapping("book/chapters/{bookId}")
    public RestResp<PageRespDto<BookChapterRespDto>> listBookChapters(
            @PathVariable("bookId") Long bookId,
            PageReqDto dto) {
        return bookInfoService.listBookChapters(bookId, dto);
    }
}
