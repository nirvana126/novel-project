package com.feiyu.novel.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.feiyu.novel.core.json.UsernameSerializer;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 小说评论 响应DTO
 * @author xiongxiaoyang
 * @date 2022/5/17
 */
@Data
@Builder
public class BookCommentRespDto {

    private Long commentTotal;

    private List<CommentInfo> comments;

    @Data
    @Builder
    public static class CommentInfo {

        private Long id;

        private String commentContent;

        @JsonSerialize(using = UsernameSerializer.class)
        private String commentUser;

        private Long commentUserId;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date commentTime;

    }

}
