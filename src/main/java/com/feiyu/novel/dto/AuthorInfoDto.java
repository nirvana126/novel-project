package com.feiyu.novel.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class AuthorInfoDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 5042142555246454323L;
    private Long id;

    private Integer status;

    private String penName;
}
