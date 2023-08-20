package com.feiyu.novel.controller.front;

import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.core.constant.ApiRouterConsts;
import com.feiyu.novel.core.rateLimiter.Limit;
import com.feiyu.novel.core.rateLimiter.LimitType;
import com.feiyu.novel.dto.resp.NewsInfoRespDto;
import com.feiyu.novel.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台门户-新闻模块 API 控制器
 *
 * @author xiongxiaoyang
 * @date 2022/5/12
 */
@Tag(name = "NewsController", description = "前台门户-新闻模块")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_NEWS_URL_PREFIX)
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /**
     * 最新新闻列表查询接口(轮播图右下侧）
     */
    @Operation(summary = "最新新闻列表查询接口")
    @GetMapping("latest_list")
    @Limit(key = "latestNews_limit",period = 60,count = 1000,limitType = LimitType.CUSTOMER)
    public RestResp<List<NewsInfoRespDto>> listLatestNews() {
        return newsService.listLatestNews();
    }

    /**
     * 新闻信息查询接口
     */
    @Operation(summary = "新闻信息查询接口")
    @GetMapping("{id}")
    @Limit(key = "news_info_limit",period = 60,count = 1000,limitType = LimitType.CUSTOMER)
    public RestResp<NewsInfoRespDto> getNews(
        @Parameter(description = "新闻ID") @PathVariable Long id) {
        return newsService.getNews(id);
    }
}
