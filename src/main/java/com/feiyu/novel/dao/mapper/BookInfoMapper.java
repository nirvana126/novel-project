package com.feiyu.novel.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feiyu.novel.dao.entity.BookInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feiyu.novel.dto.req.BookSearchReqDto;
import com.feiyu.novel.dto.resp.BookInfoRespDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author GSJ
* @description 针对表【book_info(小说信息)】的数据库操作Mapper
* @createDate 2023-04-01 20:50:25
* @Entity com.feiyu.novel.dao.entity.BookInfo
*/
public interface BookInfoMapper extends BaseMapper<BookInfo> {

    /**
     * 小说搜索
     * @param page mybatis-plus 分页对象
     * @param condition 搜索条件
     * @return 返回结果
     * */
    List<BookInfo> searchBooks(Page<BookInfoRespDto> page, BookSearchReqDto condition);

    /**
     * 增加小说点击量
     * @param bookId
     */
    void addVisitCount(@Param("bookId") Long bookId);
}




