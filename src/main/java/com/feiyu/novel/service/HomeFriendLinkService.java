package com.feiyu.novel.service;

import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.dao.entity.HomeFriendLink;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feiyu.novel.dto.resp.HomeFriendLinkRespDto;
import org.apache.catalina.LifecycleState;

import java.util.List;

/**
* @author GSJ
* @description 针对表【home_friend_link(友情链接)】的数据库操作Service
* @createDate 2023-04-01 20:49:42
*/
public interface HomeFriendLinkService extends IService<HomeFriendLink> {

    RestResp<List<HomeFriendLinkRespDto>> listFriendLink();
}
