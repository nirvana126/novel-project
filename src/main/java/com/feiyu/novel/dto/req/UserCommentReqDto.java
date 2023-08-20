package com.feiyu.novel.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserCommentReqDto {

    private Long userId;

    @NotNull(message = "小说id不能为空！")
    private Long bookId;

    @NotBlank(message = "评论内容不能为空！")
    @Length(min = 10,max = 512)
    private String commentContent;
}
