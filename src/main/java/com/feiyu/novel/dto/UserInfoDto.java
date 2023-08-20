package com.feiyu.novel.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class UserInfoDto implements Serializable {


    @Serial
    private static final long serialVersionUID = -5263127318766676255L;
    private Long id;

    private Integer status;
}
