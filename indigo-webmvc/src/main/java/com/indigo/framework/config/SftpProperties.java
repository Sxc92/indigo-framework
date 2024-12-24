package com.indigo.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author 史偕成
 * @title LocaleConfigBaseVO
 * @description
 * @create 2023/11/28 9:23
 */
@Data
@Configuration
@ConfigurationProperties("mom.sftp")
public class SftpProperties {

    /**
     * sftp 用户名
     */
    private String username;

    /**
     * sftp ip地址
     */
    private String host;

    /**
     * sftp 端口
     */
    private int port;

    /**
     * 协议方式 sftp
     */
    private String protocol;

    /**
     * sftp 密码
     */
    private String password;

    /**
     * sftp 根目录
     */
    public  String basePath;

    /**
     * session 配置
     */
    public SessionProperties session;

    /**
     * 图片压缩配置
     */
    public ImageCompressProperties image;

    @Data
    public static class SessionProperties {
        /**
         * session 配置
         */
        private Map<String, String> config;

        private int timeout;
    }

    /**
     * 图片压缩配置
     */
    @Data
    public static class ImageCompressProperties {


    }
}
