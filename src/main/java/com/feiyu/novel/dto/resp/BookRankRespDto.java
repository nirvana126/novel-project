package com.feiyu.novel.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 小说排行榜响应信息
 */
@Data
public class BookRankRespDto implements Serializable {

    private Long id;

    /**
     * 小说封面地址
     */
    private String picUrl;

    /**
     * 小说名
     */
    private String bookName;

    /**
     * 书籍描述
     */
    private String bookDesc;

    /**
     * 类别ID
     */
    private Long categoryId;

    /**
     * 类别名
     */
    private String categoryName;

    /**
     * 作家名
     */
    private String authorName;

    /**
     * 总字数
     */
    private Integer wordCount;

    /**
     * 最新章节名
     */
    private String lastChapterName;

    /**
     * 最新章节更新时间
     */
    @JsonFormat(pattern = "MM/dd HH:mm")
    private Date lastChapterUpdateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}
