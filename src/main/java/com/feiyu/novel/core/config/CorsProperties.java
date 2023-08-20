package com.feiyu.novel.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 跨域配置属性
 *
 * @author xiongxiaoyang
 * @date 2023/4/2
 */
@ConfigurationProperties(prefix = "novel.cors")
@Data
public class CorsProperties {

    /**
     * 允许跨域的域名
     * */
    private List<String> allowOrigins;
}
