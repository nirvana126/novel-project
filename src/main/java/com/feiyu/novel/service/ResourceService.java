package com.feiyu.novel.service;

import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.dto.resp.ImgVerifyCodeRespDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 资源（图片/视频/文档）相关服务类
 *
 * @author xiongxiaoyang
 * @date 2022/5/17
 */
public interface ResourceService {

    /**
     * 获取图片验证码
     *
     * @throws IOException 验证码图片生成失败
     * @return Base64编码的图片
     */
    RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException;

    /**
     * 图片上传
     * @param file 需要上传的图片
     * @return 图片访问路径
     * */
    RestResp<String> uploadImage(MultipartFile file);
}

