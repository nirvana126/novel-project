package com.feiyu.novel.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.feiyu.novel.core.common.constant.ErrorCodeEnum;
import com.feiyu.novel.core.common.exception.BusinessException;
import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.core.constant.SystemConfigConsts;
import com.feiyu.novel.dto.resp.ImgVerifyCodeRespDto;
import com.feiyu.novel.manager.redis.VerifyCodeManager;
import com.feiyu.novel.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    @Value("${novel.file.upload.path}")
    private String fileUploadPath;

    private final VerifyCodeManager verifyCodeManager;

    @Override
    public RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException {

        String sessionId = IdWorker.get32UUID();
        return RestResp.ok(ImgVerifyCodeRespDto.builder()
                .sessionId(sessionId)
                .img(verifyCodeManager.genImgVerifyCode(sessionId))
                .build());
    }

    @SneakyThrows
    @Override
    public RestResp<String> uploadImage(MultipartFile file) {

        LocalDateTime now = LocalDateTime.now();
        String savePath =
                SystemConfigConsts.IMAGE_UPLOAD_DIRECTORY
                        + now.format(DateTimeFormatter.ofPattern("yyyy")) + File.separator
                        + now.format(DateTimeFormatter.ofPattern("MM")) + File.separator
                        + now.format(DateTimeFormatter.ofPattern("dd"));
        String oriName = file.getOriginalFilename();
        assert oriName != null;
        String saveFileName = IdWorker.get32UUID() + oriName.substring(oriName.lastIndexOf("."));
        File saveFile = new File( savePath, saveFileName);
        if (!saveFile.getParentFile().exists()) {
            boolean isSuccess = saveFile.getParentFile().mkdirs();
            if (!isSuccess) {
                throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
            }
        }
        file.transferTo(saveFile);
        if (Objects.isNull(ImageIO.read(saveFile))) {
            // 上传的文件不是图片
            Files.delete(saveFile.toPath());
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_TYPE_NOT_MATCH);
        }
        return RestResp.ok(savePath + File.separator + saveFileName);
    }
}
