package com.feiyu.novel.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class BookChapterRespDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 章节id
     */
    private Long id;
    /**
     * 小说ID
     */
    private Long bookId;

    /**
     * 章节号
     */
    private Integer chapterNum;

    /**
     * 章节名
     */
    private String chapterName;

    /**
     * 章节字数
     */
    private Integer chapterWordCount;

    /**
     * 章节更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date chapterUpdateTime;

    /**
     * 是否收费;1-收费 0-免费
     */
    private Integer isVip;
}
