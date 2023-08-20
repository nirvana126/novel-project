package com.feiyu.novel.service;

import com.feiyu.novel.core.common.req.PageReqDto;
import com.feiyu.novel.core.common.resp.PageRespDto;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.dao.entity.BookInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feiyu.novel.dto.req.BookAddReqDto;
import com.feiyu.novel.dto.req.ChapterAddReqDto;
import com.feiyu.novel.dto.req.UserCommentReqDto;
import com.feiyu.novel.dto.resp.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
* @author GSJ
* @description 针对表【book_info(小说信息)】的数据库操作Service
* @createDate 2023-04-01 20:50:25
*/
public interface BookInfoService extends IService<BookInfo> {

    RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId) throws NoSuchAlgorithmException;

    RestResp<List<BookRankRespDto>> listVisitRankBooks();

    RestResp<List<BookRankRespDto>> listNewestRankBooks();

    RestResp<List<BookRankRespDto>> listUpdateRankBooks();

    RestResp<BookInfoRespDto> getBookById(Long bookId);

    RestResp<BookChapterAboutRespDto> getNewestChapter(Long bookId);

    RestResp<List<BookChapterRespDto>> listChapters(Long bookId);

    RestResp<BookContentAboutRespDto> getBookContentAbout(Long chapterId);

    RestResp<Long> getPreChapterId(Long chapterId);

    RestResp<Long> getNextChapterId(Long chapterId);

    RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection);

    RestResp<Void> saveComment(UserCommentReqDto dto);

    RestResp<Void> updateComment(Long userId, Long id, String content);

    RestResp<Void> deleteComment(Long userId, Long id);

    RestResp<BookCommentRespDto> listNewestComments(Long bookId);

    RestResp<Void> addVisitCount(Long bookId);

    RestResp<Void> saveBook(BookAddReqDto dto);

    RestResp<PageRespDto<BookInfoRespDto>> listAuthorBooks(PageReqDto dto);

    RestResp<Void> saveBookChapter(ChapterAddReqDto dto);

    RestResp<PageRespDto<BookChapterRespDto>> listBookChapters(Long bookId, PageReqDto dto);
}
