package com.feiyu.novel.controller.front;

import com.feiyu.novel.core.auth.UserHolder;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.core.constant.ApiRouterConsts;
import com.feiyu.novel.dto.req.UserCommentReqDto;
import com.feiyu.novel.dto.req.UserInfoUptReqDto;
import com.feiyu.novel.dto.req.UserLoginReqDto;
import com.feiyu.novel.dto.req.UserRegisterReqDto;
import com.feiyu.novel.dto.resp.UserInfoRespDto;
import com.feiyu.novel.dto.resp.UserLoginRespDto;
import com.feiyu.novel.dto.resp.UserRegisterRespDto;
import com.feiyu.novel.service.BookInfoService;
import com.feiyu.novel.service.UserInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_USER_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserInfoService userInfoService;

    private final BookInfoService bookInfoService;

    /**
     * 用户注册接口
     * @param dto
     * @return
     */
    @PostMapping("register")
    public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto dto){
        return userInfoService.register(dto);
    }


    /**
     * 用户登录接口
     * @param dto
     * @return
     */
    @PostMapping("login")
    public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto dto){
        return userInfoService.login(dto);
    }

    /**
     * 用户信息查询接口
     * @return
     */
    @GetMapping
    public RestResp<UserInfoRespDto> getUserInfo(){
        return userInfoService.getUserInfo(UserHolder.getUserId());
    }

    /**
     * 用户信息修改接口
     * @param dto
     * @return
     */
    @PutMapping
    public RestResp<Void> updateUserInfo(@Valid @RequestBody UserInfoUptReqDto dto){
        dto.setUserId(UserHolder.getUserId());
        return userInfoService.updateUserInfo(dto);
    }

    /**
     * 用户反馈提交
     * @param content
     * @return
     */
    @PostMapping("feedback")
    public RestResp<Void> submitFeedback(@RequestBody String content){
        return userInfoService.saveFeedback(UserHolder.getUserId(),content);
    }

    /**
     * 用户删除反馈
     * @param id
     * @return
     */
    @DeleteMapping("feedback/{id}")
    public RestResp<Void> deleteFeedback(@PathVariable Long id){
        return userInfoService.deleteFeedback(UserHolder.getUserId(),id);
    }

    /**
     * 发表评论接口
     * @param dto
     * @return
     */
    @PostMapping("comment")
    public RestResp<Void> comment(@Valid @RequestBody UserCommentReqDto dto){
        dto.setUserId(UserHolder.getUserId());
        return bookInfoService.saveComment(dto);
    }

    /**
     * 修改评论接口
     */
    @PutMapping("comment/{id}")
    public RestResp<Void> updateComment(@PathVariable Long id, String content) {
        return bookInfoService.updateComment(UserHolder.getUserId(), id, content);
    }
    /**
     * 删除评论接口
     */
    @DeleteMapping("comment/{id}")
    public RestResp<Void> deleteComment(@PathVariable Long id) {
        return bookInfoService.deleteComment(UserHolder.getUserId(), id);
    }


}
