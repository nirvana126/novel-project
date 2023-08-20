package com.feiyu.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feiyu.novel.core.common.constant.ErrorCodeEnum;
import com.feiyu.novel.core.common.exception.BusinessException;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.core.common.util.JwtUtils;
import com.feiyu.novel.core.constant.DatabaseConsts;
import com.feiyu.novel.core.constant.SystemConfigConsts;
import com.feiyu.novel.dao.entity.UserFeedback;
import com.feiyu.novel.dao.entity.UserInfo;
import com.feiyu.novel.dao.mapper.UserFeedbackMapper;
import com.feiyu.novel.dto.req.UserInfoUptReqDto;
import com.feiyu.novel.dto.req.UserLoginReqDto;
import com.feiyu.novel.dto.req.UserRegisterReqDto;
import com.feiyu.novel.dto.resp.UserInfoRespDto;
import com.feiyu.novel.dto.resp.UserLoginRespDto;
import com.feiyu.novel.dto.resp.UserRegisterRespDto;
import com.feiyu.novel.manager.redis.VerifyCodeManager;
import com.feiyu.novel.service.UserInfoService;
import com.feiyu.novel.dao.mapper.UserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
* @author GSJ
* @description 针对表【user_info(用户信息)】的数据库操作Service实现
* @createDate 2023-04-04 19:01:10
*/
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    private final VerifyCodeManager verifyCodeManager;

    private final UserInfoMapper userInfoMapper;

    private final UserFeedbackMapper userFeedbackMapper;

    private final JwtUtils jwtUtils;


    @Override
    public RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto) {
        // 校验图形验证码是否正确
        if (!verifyCodeManager.imgVerifyCodeOk(dto.getSessionId(), dto.getVelCode())) {
            // 图形验证码校验失败
            throw new BusinessException(ErrorCodeEnum.USER_VERIFY_CODE_ERROR);
        }

        // 校验手机号是否已注册
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME, dto.getUsername())
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        if (userInfoMapper.selectCount(queryWrapper) > 0) {
            // 手机号已注册
            throw new BusinessException(ErrorCodeEnum.USER_NAME_EXIST);
        }

        // 注册成功，保存用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)));
        userInfo.setUsername(dto.getUsername());
        userInfo.setNickName(dto.getUsername());
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfo.setSalt("0");
        userInfoMapper.insert(userInfo);

        // 删除验证码
        verifyCodeManager.removeImgVerifyCode(dto.getSessionId());

        // 生成JWT 并返回
        return RestResp.ok(
                UserRegisterRespDto.builder()
                        .token(jwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_FRONT_KEY))
                        .uid(userInfo.getId())
                        .build()
        );

    }

    @Override
    public RestResp<UserLoginRespDto> login(UserLoginReqDto dto) {
        //查询用户
        QueryWrapper<UserInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",dto.getUsername());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        //判断用户是否存在
        if (userInfo==null){
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }
        //校验密码
        if (!DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)).equals(userInfo.getPassword())){
            throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR);
        }
        //生成jwt并返回
        UserLoginRespDto userLoginRespDto = UserLoginRespDto.builder().uid(userInfo.getId())
                .nickName(userInfo.getNickName())
                .token(jwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_FRONT_KEY))
                .build();
        return RestResp.ok(userLoginRespDto);

    }

    @Override
    public RestResp<UserInfoRespDto> getUserInfo(Long userId) {

        UserInfo userInfo = userInfoMapper.selectById(userId);
        UserInfoRespDto userInfoRespDto = UserInfoRespDto.builder().userSex(userInfo.getUserSex()).userPhoto(userInfo.getUserPhoto())
                .nickName(userInfo.getNickName()).build();
        return RestResp.ok(userInfoRespDto);
    }

    @Override
    public RestResp<Void> updateUserInfo(UserInfoUptReqDto dto) {

        UserInfo userInfo = new UserInfo();
        userInfo.setId(dto.getUserId());
        userInfo.setUserSex(dto.getUserSex());
        userInfo.setNickName(dto.getNickName());
        userInfo.setUserPhoto(dto.getUserPhoto());
        userInfoMapper.updateById(userInfo);
        return RestResp.ok();

    }

    @Override
    public RestResp<Void> saveFeedback(Long userId, String content) {

        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setUserId(userId);
        userFeedback.setContent(content);
        userFeedback.setCreateTime(new Date());
        userFeedback.setUpdateTime(new Date());
        userFeedbackMapper.insert(userFeedback);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> deleteFeedback(Long userId, Long id) {

        QueryWrapper<UserFeedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id).eq("user_id",userId);
        userFeedbackMapper.delete(queryWrapper);
        return RestResp.ok();
    }

}




