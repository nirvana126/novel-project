package com.feiyu.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookCategoryRespDto {

    /**
     * 类别id
     */
    private Long id;

    /**
     * 类别名
     */
    private String name;
}
