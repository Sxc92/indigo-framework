package com.indigo.framework.utils;

import cn.hutool.core.util.StrUtil;
import com.indigo.framework.config.SftpProperties;
import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.indigo.framework.utils.PicUtils.imageCheck;

/**
 * @author 史偕成
 * @title LocaleConfigBaseVO
 * @description
 * @create 2023/11/28 9:23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SftpUtils {

    private final SftpProperties sftpProperties;


    private ChannelSftp createSftp()  {
        JSch jsch = new JSch();
        Channel channel;
        try {
            Session session = jsch.getSession(sftpProperties.getUsername(), sftpProperties.getHost(), sftpProperties.getPort());
            if (session == null) {
                throw new RuntimeException(sftpProperties.getHost() + "session is null");
            }
            session.setPassword(sftpProperties.getPassword());
            session.setTimeout(sftpProperties.getSession().getTimeout());
            // 让ssh客户端自动接受新主机的 hostKey // 注意：在实际应用中，你应该验证主机密钥
            for (String key : sftpProperties.getSession().getConfig().keySet()) {
                session.setConfig(key, sftpProperties.getSession().getConfig().get(key));
            }
            session.connect();
            // 打开sftp渠道，除sftp外还有shell、X11等类型
            channel = session.openChannel(sftpProperties.getProtocol());
            channel.connect();
        }catch (JSchException e) {
            log.error("session error");
            return null;
        }
        return (ChannelSftp) channel;
    }

    /**
     * 关闭连接
     */
    private void disconnect(ChannelSftp sftp) {
        try {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                } else if (sftp.isClosed()) {
                    log.error("sftp 连接已关闭");
                }
                if (sftp.getSession() != null) {
                    sftp.getSession().disconnect();
                }
            }
        } catch (JSchException e) {
            log.error("sftp 断开连接失败，原因：{}", e.getMessage(), e);
        }
    }

    /**
     * 上传文件
     * @param multipartFile 文件
     * @param path 文件子路径
     * @return 文件存放路径
     */
    public String uploadFile(MultipartFile multipartFile, String path) {
        try {
            return uploadInputStream(new ByteArrayInputStream(multipartFile.getBytes()), path, multipartFile.getOriginalFilename());
        }catch (IOException e) {
            log.error("upload file error", e);
        }
        return null;
    }

    /**
     * 上传文件
     * @param inputStream 文件输入流
     * @param path 文件子路径
     * @param fileName 文件名称
     * @return 文件存放路径
     */
    public String uploadInputStream(InputStream inputStream, String path, String fileName) {
        ChannelSftp sftp = null;
        try {
            // 1. 判断文件类型
            // 如果是图片进行压缩，并返回压缩输出流
            if (imageCheck(inputStream)) {

                inputStream = PicUtils.compressPicForScale(inputStream.readAllBytes());
            }
            if (inputStream == null) {
                throw new RuntimeException("inputStream is null");
            }
            sftp = createSftp();
            if (sftp == null) {
                // 如果创建sftp 通道失败 抛出异常
                throw new RuntimeException("sftp channel create error");
            }
            // 日期目录
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            // 2. 组装出目录路径， 判断路径是否存在，若不存在则创建
            StringBuffer currentPath = new StringBuffer();
            String targetCatalogPath = sftpProperties.getBasePath() + path + "/" +dateDir;
            for (String dir : targetCatalogPath.split("/")) {
                if (!dir.isEmpty()) {
                    currentPath.append("/").append(dir);
                    if (!isDirExist(sftp, currentPath.toString())) {
                        // 创建目录
                        sftp.mkdir(currentPath.toString());
                    }
                }
            }

//            // 3.获取业务相对路径
//            String filePath = path +"/" + fileName;
            // 4. 执行上传文件
            sftp.put(inputStream, currentPath.append("/").append(fileName).toString());
            // 5.返回文件相对路径
            return path +dateDir+"/" + fileName;
        }catch (SftpException | IOException e) {
            log.error("upload failed", e);
            return "";
        } finally {
            // final: Close the SFTP channel and session
            disconnect(sftp);
        }
    }


    /**
     * 判断文件路径在sftp服务器是否存在，若不存在则直接生成
     *
     * @param sftp          sftp
     * @param directoryPath 文件夹路径 如: roster/test.html
     * @return
     */
    public boolean isDirExist(ChannelSftp sftp, String directoryPath) {
        try {
            // 判断文件在sftp服务器是否存在
            sftp.lstat(directoryPath);
            return true;
        } catch (SftpException e) {
            return false;
        }
    }


    /**
     * 根据地址获取目标地址下的文件流
     *
     * @param filePath
     * @return
     */
    public InputStream getFileInputStreamByPath(String filePath) {
        InputStream inputStream = null;
        // 参数校验
        if (StrUtil.isEmpty(filePath)) {
            log.error("uploadFile data is empty. filePath:{}", filePath);
            return null;
        }
        String path = sftpProperties.getBasePath() + filePath;
        System.out.println("读取文件===>" + path);
        try {
            ChannelSftp sftpChannel = createSftp();
            if (sftpChannel == null) {
                return null;
            }
            inputStream = sftpChannel.get(path);
        } catch (Exception exception) {
            log.error("", exception);
            return null;
        }
        return inputStream;
    }

    /**
     * 删除文件
     * @param path
     * @return
     */
    public boolean delFile(String path) {
        ChannelSftp sftpChannel=null;
        try {
             sftpChannel = createSftp();
            if (sftpChannel == null) {
                return false;
            }
            sftpChannel.rm(sftpProperties.getBasePath()+path);
        } catch (Exception exception) {
            log.error("rm file error", exception);
            return false;
        } finally {
            disconnect(sftpChannel);
        }
        return true;
    }
}
