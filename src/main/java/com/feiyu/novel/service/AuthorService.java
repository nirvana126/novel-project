package com.feiyu.novel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.dao.entity.AuthorInfo;
import com.feiyu.novel.dto.req.AuthorRegisterReqDto;

public interface AuthorService {
    RestResp<Void> register(AuthorRegisterReqDto dto);

    RestResp<Integer> getStatus(Long userId);
}
