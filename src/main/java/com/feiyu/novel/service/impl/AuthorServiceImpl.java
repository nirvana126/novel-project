package com.feiyu.novel.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.feiyu.novel.core.auth.UserHolder;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.dao.entity.AuthorInfo;
import com.feiyu.novel.dao.mapper.AuthorInfoMapper;
import com.feiyu.novel.dto.req.AuthorRegisterReqDto;
import com.feiyu.novel.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorInfoMapper authorInfoMapper;

    @Override
    public RestResp<Void> register(AuthorRegisterReqDto dto) {

        //检验该用户是否为已注册的作家
        Long userId = UserHolder.getUserId();
        QueryWrapper<AuthorInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        AuthorInfo authorInfo = authorInfoMapper.selectOne(queryWrapper);
        if(authorInfo!=null){
            return RestResp.ok();
        }
        AuthorInfo newAuthor = new AuthorInfo();
        newAuthor.setUserId(userId);
        newAuthor.setInviteCode("0");
        newAuthor.setPenName(dto.getPenName());
        newAuthor.setTelPhone(dto.getTelPhone());
        newAuthor.setChatAccount(dto.getChatAccount());
        newAuthor.setEmail(dto.getEmail());
        newAuthor.setWorkDirection(dto.getWorkDirection());
        newAuthor.setCreateTime(new Date());
        newAuthor.setUpdateTime(new Date());
        authorInfoMapper.insert(newAuthor);
        return RestResp.ok();
    }

    @Override
    public RestResp<Integer> getStatus(Long userId) {
        QueryWrapper<AuthorInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        AuthorInfo authorInfo = authorInfoMapper.selectOne(queryWrapper);
        return authorInfo == null ? RestResp.ok(null) : RestResp.ok(authorInfo.getStatus());

    }
}
