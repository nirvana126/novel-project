package com.feiyu.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BookContentAboutRespDto  {

    private BookInfoRespDto bookInfo;

    private BookChapterRespDto chapterInfo;

    private String bookContent;
}
