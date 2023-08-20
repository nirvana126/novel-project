package com.feiyu.novel.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feiyu.novel.core.annotation.Key;
import com.feiyu.novel.core.annotation.Lock;
import com.feiyu.novel.core.auth.UserHolder;
import com.feiyu.novel.core.common.constant.ErrorCodeEnum;
import com.feiyu.novel.core.common.exception.BusinessException;
import com.feiyu.novel.core.common.req.PageReqDto;
import com.feiyu.novel.core.common.resp.PageRespDto;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.core.constant.DatabaseConsts;
import com.feiyu.novel.dao.entity.*;
import com.feiyu.novel.dao.mapper.*;
import com.feiyu.novel.dto.req.BookAddReqDto;
import com.feiyu.novel.dto.req.ChapterAddReqDto;
import com.feiyu.novel.dto.req.UserCommentReqDto;
import com.feiyu.novel.dto.resp.*;
import com.feiyu.novel.manager.cache.*;
//import com.feiyu.novel.manager.mq.AmqpMsgManager;
import com.feiyu.novel.service.BookInfoService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author GSJ
* @description 针对表【book_info(小说信息)】的数据库操作Service实现
* @createDate 2023-04-01 20:50:25
*/
@Service
//@RequiredArgsConstructor
@Slf4j
public class BookInfoServiceImpl extends ServiceImpl<BookInfoMapper, BookInfo>
    implements BookInfoService{

    @Resource
    private BookInfoCacheManager bookInfoCacheManager;
    @Resource
    private BookRankCacheManager bookRankCacheManager;
    @Resource
    private BookChapterCacheManager bookChapterCacheManager;
    @Resource
    private BookContentCacheManager bookContentCacheManager;
    @Resource
    private BookChapterMapper bookChapterMapper;
    @Resource
    private BookCategoryCacheManager bookCategoryCacheManager;
    @Resource
    private BookCommentMapper bookCommentMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private BookInfoMapper bookInfoMapper;
    @Resource
    private AuthorInfoMapper authorInfoMapper;
    @Resource
    private BookContentMapper bookContentMapper;
//    @Resource
//    private AmqpMsgManager amqpMsgManager;

    public static final String PIC_URL="https://ts3.cn.mm.bing.net/th?id=OIP-C.uW9CroBrl4vP-Dvi5V9ckwHaLG&w=204&h=306&c=8&rs=1&qlt=90&o=6&dpr=1.3&pid=3.1&rm=2";

    @Override
    public RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId)
            throws NoSuchAlgorithmException {
        Long categoryId = bookInfoCacheManager.getBookInfo(bookId).getCategoryId();
        List<Long> lastUpdateIdList = bookInfoCacheManager.getLastUpdateIdList(categoryId);
        List<BookInfoRespDto> respDtoList = new ArrayList<>();
        List<Integer> recIdIndexList = new ArrayList<>();
        int count = 0;
        Random rand = SecureRandom.getInstanceStrong();
        while (count < 10) {
            int recIdIndex = rand.nextInt(lastUpdateIdList.size());
            if (!recIdIndexList.contains(recIdIndex)) {
                recIdIndexList.add(recIdIndex);
                bookId = lastUpdateIdList.get(recIdIndex);
                BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(bookId);
                respDtoList.add(bookInfo);
                count++;
            }
        }
        return RestResp.ok(respDtoList);
    }

    @Override
    public RestResp<List<BookRankRespDto>> listVisitRankBooks() {

        return RestResp.ok(bookRankCacheManager.listVisitRankBooks());
    }

    @Override
    public RestResp<List<BookRankRespDto>> listNewestRankBooks() {

        return RestResp.ok(bookRankCacheManager.listNewestRankBooks());
    }

    @Override
    public RestResp<List<BookRankRespDto>> listUpdateRankBooks() {

        return RestResp.ok(bookRankCacheManager.listUpdateRankBooks());
    }

    @Override
    public RestResp<BookInfoRespDto> getBookById(Long bookId) {

        return RestResp.ok(bookInfoCacheManager.getBookInfo(bookId));
    }

    @Override
    public RestResp<BookChapterAboutRespDto> getNewestChapter(Long bookId) {
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(bookId);
        Long lastChapterId = bookInfo.getLastChapterId();
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(lastChapterId);
        String bookContent = bookContentCacheManager.getBookContent(lastChapterId);
        //查询章节总数
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",bookId);
        Long chapterTotal = bookChapterMapper.selectCount(queryWrapper);
        //组装数据并返回
        BookChapterAboutRespDto bookChapterAboutRespDto = BookChapterAboutRespDto.builder()
                .chapterInfo(chapter)
                .chapterTotal(chapterTotal)
                .contentSummary(bookContent.substring(0, 30))
                .build();
        return RestResp.ok(bookChapterAboutRespDto);

    }

    @Override
    public RestResp<List<BookChapterRespDto>> listChapters(Long bookId) {

        QueryWrapper<BookChapter> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("book_id",bookId).orderByAsc("chapter_num");
        List<BookChapter> bookChapters = bookChapterMapper.selectList(queryWrapper);

        //注意BookChapterRespDto.builder()自带返回值，故此处lambda表达式无需写return
        List<BookChapterRespDto> res = bookChapters.stream().map(v ->
                BookChapterRespDto.builder().id(v.getId())
                .chapterName(v.getChapterName())
                .isVip(v.getIsVip())
                .build()).toList();
        return RestResp.ok(res);
    }

    @Override
    public RestResp<BookContentAboutRespDto> getBookContentAbout(Long chapterId) {

        // 查询章节信息
        BookChapterRespDto bookChapter = bookChapterCacheManager.getChapter(chapterId);

        // 查询章节内容
        String content = bookContentCacheManager.getBookContent(chapterId);

        // 查询小说信息
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(bookChapter.getBookId());

        // 组装数据并返回
        return RestResp.ok(BookContentAboutRespDto.builder()
                .bookInfo(bookInfo)
                .chapterInfo(bookChapter)
                .bookContent(content)
                .build());
    }

    @Override
    public RestResp<Long> getPreChapterId(Long chapterId) {
        // 查询小说ID 和 章节号
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        Long bookId = chapter.getBookId();
        Integer chapterNum = chapter.getChapterNum();

        // 查询上一章信息并返回章节ID
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .lt(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM, chapterNum)
                .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        return RestResp.ok(
                Optional.ofNullable(bookChapterMapper.selectOne(queryWrapper))
                        .map(BookChapter::getId)
                        .orElse(null)
        );
    }

    @Override
    public RestResp<Long> getNextChapterId(Long chapterId) {

        // 查询小说ID 和 章节号
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        Long bookId = chapter.getBookId();
        Integer chapterNum = chapter.getChapterNum();

        // 查询下一章信息并返回章节ID
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .gt(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM, chapterNum)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        return RestResp.ok(
                Optional.ofNullable(bookChapterMapper.selectOne(queryWrapper))
                        .map(BookChapter::getId)
                        .orElse(null)
        );
    }

    @Override
    public RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection) {

        List<BookCategoryRespDto> bookCategoryRespDtos = bookCategoryCacheManager.listCategory(workDirection);
        return RestResp.ok(bookCategoryRespDtos);
    }

    @Lock(prefix = "userComment")
    @Override
    public RestResp<Void> saveComment(@Key(expr = "#{userId + '::' + bookId}")UserCommentReqDto dto) {
        // 利用分布式锁防止用户重复发表评论
        QueryWrapper<BookComment> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",dto.getUserId()).eq("book_id",dto.getBookId());
        Long count = bookCommentMapper.selectCount(queryWrapper);
        if (count > 0){
            return RestResp.fail(ErrorCodeEnum.USER_COMMENTED);
        }
        BookComment bookComment = new BookComment();
        bookComment.setBookId(dto.getBookId());
        bookComment.setUserId(dto.getUserId());
        bookComment.setCommentContent(dto.getCommentContent());
        bookComment.setCreateTime(new Date());
        bookComment.setUpdateTime(new Date());
        bookCommentMapper.insert(bookComment);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> updateComment(Long userId, Long id, String content) {

        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id).eq("user_id",userId);
        BookComment bookComment = new BookComment();
        bookComment.setCommentContent(content);
        bookCommentMapper.update(bookComment,queryWrapper);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> deleteComment(Long userId, Long id) {

        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id).eq("user_id",userId);
        bookCommentMapper.delete(queryWrapper);
        return RestResp.ok();
    }

    @Override
    public RestResp<BookCommentRespDto> listNewestComments(Long bookId) {
        //先根据bookId查book_comment表，得出当前小说的评论数量
        QueryWrapper<BookComment> bookCommentQueryWrapper=new QueryWrapper<>();
        bookCommentQueryWrapper.eq("book_id",bookId);
        Long count = bookCommentMapper.selectCount(bookCommentQueryWrapper);
        BookCommentRespDto respDto;
        if (count == 0){
             respDto = BookCommentRespDto.builder().commentTotal(count).comments(Collections.emptyList()).build();
            return RestResp.ok(respDto);
        }
        bookCommentQueryWrapper.eq("book_id",bookId).orderByDesc("create_time").last("limit 5");
        List<BookComment> bookComments = bookCommentMapper.selectList(bookCommentQueryWrapper);
        List<Long> userIdList = bookComments.stream().map(BookComment::getUserId).toList();
        // 根据评论用户id查询相应用户信息
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.in("id",userIdList);
        List<UserInfo> userInfos = userInfoMapper.selectList(userInfoQueryWrapper);
        Map<Long, String> userInfoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getId, UserInfo::getUsername));
        //开始组装响应dto
        List<BookCommentRespDto.CommentInfo> commentInfos = bookComments.stream().map(v ->
                BookCommentRespDto.CommentInfo.builder()
                        .id(v.getId()).commentContent(v.getCommentContent())
                        .commentTime(v.getCreateTime()).commentUserId(v.getUserId())
                        .commentUser(userInfoMap.get(v.getUserId())).build()).toList();
        respDto = BookCommentRespDto.builder().commentTotal(count).comments(commentInfos).build();
        return RestResp.ok(respDto);
    }

    @Override
    public RestResp<Void> addVisitCount(Long bookId) {

        bookInfoMapper.addVisitCount(bookId);
        return RestResp.ok();
    }

    /**
     * 作家发布小说
     * @param dto
     * @return
     */
    @Override
    public RestResp<Void> saveBook(BookAddReqDto dto) {
        String bookName = dto.getBookName();
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_name",bookName);
        Long count = bookInfoMapper.selectCount(queryWrapper);
        if (count > 0){
            return RestResp.fail(ErrorCodeEnum.AUTHOR_BOOK_NAME_EXIST);
        }
        QueryWrapper<AuthorInfo> authorInfoQueryWrapper = new QueryWrapper<>();
        authorInfoQueryWrapper.eq("user_id",UserHolder.getUserId());
        AuthorInfo authorInfo = authorInfoMapper.selectOne(authorInfoQueryWrapper);
        BookInfo bookInfo = new BookInfo();
        //设置作家信息
        bookInfo.setAuthorId(authorInfo.getId());
        bookInfo.setAuthorName(authorInfo.getPenName());
        //设置其他信息
        bookInfo.setWorkDirection(dto.getWorkDirection());
        bookInfo.setCategoryId(dto.getCategoryId());
        bookInfo.setCategoryName(dto.getCategoryName());
        bookInfo.setPicUrl(Optional.ofNullable(dto.getPicUrl()).orElse(PIC_URL));
        bookInfo.setBookName(dto.getBookName());
        bookInfo.setBookDesc(dto.getBookDesc());
        bookInfo.setScore(0);
        bookInfo.setIsVip(0);
        bookInfo.setCreateTime(new Date());
        bookInfo.setUpdateTime(new Date());
        bookInfoMapper.insert(bookInfo);
        return RestResp.ok();

    }

    @Override
    public RestResp<PageRespDto<BookInfoRespDto>> listAuthorBooks(PageReqDto dto) {

        IPage<BookInfo> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookTable.AUTHOR_ID, UserHolder.getAuthorId())
                .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName());
        IPage<BookInfo> bookInfoPage = bookInfoMapper.selectPage(page, queryWrapper);
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(),
                bookInfoPage.getRecords().stream().map(v -> BookInfoRespDto.builder()
                        .id(v.getId())
                        .bookName(v.getBookName())
                        .picUrl(v.getPicUrl())
                        .categoryName(v.getCategoryName())
                        .wordCount(v.getWordCount())
                        .visitCount(v.getVisitCount())
                        .updateTime(v.getUpdateTime())
                        .build()).toList()));
    }

    /**
     * 小说章节发布和更新
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResp<Void> saveBookChapter(ChapterAddReqDto dto) {

        Long bookId = dto.getBookId();
        BookInfo bookInfo = bookInfoMapper.selectById(bookId);
        if(!Objects.equals(bookInfo.getAuthorId(),UserHolder.getAuthorId())){
            return RestResp.fail(ErrorCodeEnum.USER_UN_AUTH);
        }
//        System.out.println("当前线程作家ID为："+UserHolder.getAuthorId());
//        System.out.println("当前发布小说的作家ID："+bookInfo.getAuthorId());
        //查询最新章节序号，便于更新
        QueryWrapper<BookChapter> bookChapterQueryWrapper = new QueryWrapper<>();
        bookChapterQueryWrapper.eq("book_id",bookId).orderByDesc("create_time").last("limit 1");
        BookChapter bookChapter = bookChapterMapper.selectOne(bookChapterQueryWrapper);
        int chapterNum=0;
        if(bookChapter!=null){
            chapterNum=bookChapter.getChapterNum()+1;
        }
        BookChapter newChapter = new BookChapter();
        newChapter.setBookId(bookId);
        newChapter.setChapterNum(chapterNum);
        newChapter.setChapterName(dto.getChapterName());
        newChapter.setWordCount(dto.getChapterContent().length());
        newChapter.setIsVip(dto.getIsVip());
        newChapter.setCreateTime(new Date());
        newChapter.setUpdateTime(new Date());
        bookChapterMapper.insert(newChapter);
        //同时保存最新章节到小说内容表
        BookContent bookContent = new BookContent();
        bookContent.setChapterId(newChapter.getId());
        bookContent.setContent(dto.getChapterContent());
        bookContent.setCreateTime(new Date());
        bookContent.setUpdateTime(new Date());
        bookContentMapper.insert(bookContent);
        //更新小说信息表
        BookInfo info = new BookInfo();
        Integer wordCount = info.getWordCount();
        wordCount = Optional.ofNullable(wordCount).orElse(0);
        info.setId(dto.getBookId());
        info.setWordCount(wordCount+newChapter.getWordCount());
        info.setLastChapterId(newChapter.getId());
        info.setLastChapterName(newChapter.getChapterName());
        info.setLastChapterUpdateTime(new Date());
        info.setUpdateTime(new Date());
        bookInfoMapper.updateById(info);

        //清除小说信息缓存
        bookInfoCacheManager.evictBookInfoCache(dto.getBookId());
        //然后发送小说信息更新的MQ消息
//        amqpMsgManager.sendBookChangeMsg(dto.getBookId());
        return RestResp.ok();
    }

    @Override
    public RestResp<PageRespDto<BookChapterRespDto>> listBookChapters(Long bookId, PageReqDto dto) {

        IPage<BookChapter> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM);
        IPage<BookChapter> bookChapterPage = bookChapterMapper.selectPage(page, queryWrapper);
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(),
                bookChapterPage.getRecords().stream().map(v -> BookChapterRespDto.builder()
                        .id(v.getId())
                        .chapterName(v.getChapterName())
                        .chapterUpdateTime(v.getUpdateTime())
                        .isVip(v.getIsVip())
                        .build()).toList()));
    }

}




