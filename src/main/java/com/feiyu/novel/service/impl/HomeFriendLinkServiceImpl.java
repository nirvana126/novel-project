package com.feiyu.novel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.dao.entity.HomeFriendLink;
import com.feiyu.novel.dto.resp.HomeFriendLinkRespDto;
import com.feiyu.novel.manager.cache.FriendLinkCacheManager;
import com.feiyu.novel.service.HomeFriendLinkService;
import com.feiyu.novel.dao.mapper.HomeFriendLinkMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author GSJ
* @description 针对表【home_friend_link(友情链接)】的数据库操作Service实现
* @createDate 2023-04-01 20:49:42
*/
@Service
public class HomeFriendLinkServiceImpl extends ServiceImpl<HomeFriendLinkMapper, HomeFriendLink>
    implements HomeFriendLinkService{

    @Resource
    private FriendLinkCacheManager friendLinkCacheManager;

    @Override
    public RestResp<List<HomeFriendLinkRespDto>> listFriendLink() {

        List<HomeFriendLinkRespDto> friendLinks = friendLinkCacheManager.listFriendLinks();
        return RestResp.ok(friendLinks);

    }
}




