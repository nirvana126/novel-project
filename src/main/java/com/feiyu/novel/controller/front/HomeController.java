package com.feiyu.novel.controller.front;

import com.feiyu.novel.core.constant.ApiRouterConsts;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.core.rateLimiter.Limit;
import com.feiyu.novel.core.rateLimiter.LimitType;
import com.feiyu.novel.dto.resp.HomeBookRespDto;
import com.feiyu.novel.dto.resp.HomeFriendLinkRespDto;
import com.feiyu.novel.service.HomeBookService;
import com.feiyu.novel.service.HomeFriendLinkService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_HOME_URL_PREFIX)
@Slf4j
public class HomeController {

    @Resource
    private HomeBookService homeBookService;

    @Resource
    private HomeFriendLinkService homeFriendLinkService;

    @Operation(summary = "首页小说展示")
    @GetMapping("books")
//    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<List<HomeBookRespDto>> listHomeBooks(){
        return homeBookService.listHomeBooks();
    }

    @Operation(summary = "友情链接")
    @GetMapping("friend_Link/list")
    @Limit(period = 1,count = 50,limitType = LimitType.IP)
    public RestResp<List<HomeFriendLinkRespDto>> listFriendLinks(){
        return homeFriendLinkService.listFriendLink();
    }


}
