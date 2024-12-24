package com.indigo.framework.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 史偕成
 * @title LocaleConfigBaseVO
 * @description 图片操作工具类
 * @create 2023/11/28 9:23
 */
@Slf4j
public class PicUtils {

    // 魔术数字定义
    private static final byte[] JPEG_MAGIC_NUMBER = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final byte[] PNG_MAGIC_NUMBER = new byte[]{(byte) 0x89, 'P', 'N', 'G', (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A};
    private static final byte[] GIF_MAGIC_NUMBER = new byte[]{'G', 'I', 'F', '8'};
    private static final byte[] BMP_MAGIC_NUMBER = new byte[]{'B', 'M'};


    private static final Integer ZERO = 0;
    private static final Integer ONE_ZERO_TWO_FOUR = 1024;
    private static final Integer NINE_ZERO_ZERO = 900;
    private static final Integer THREE_TWO_SEVEN_FIVE = 3275;
    private static final Integer TWO_ZERO_FOUR_SEVEN = 2047;
    private static final Double ZERO_EIGHT_FIVE = 0.85;
    private static final Double ZERO_SIX = 0.6;
    private static final Double ZERO_FOUR_FOUR = 0.44;
    private static final Double ZERO_FOUR = 0.4;

    /**
     * 图片压缩
     * @param file 文件
     * @return
     * @throws IOException
     */
    public  static InputStream imageCompression(MultipartFile file) {
        //获取文件输入流
        try {
            InputStream inputStream = file.getInputStream();
            return imageCompression(inputStream);
        }catch (IOException e) {
            log.error("pic compress error",e);
        }
       return null;
    }

    /**
     * 图片压缩
     * @param inputStream 文件流
     * @return
     * @throws IOException
     */
    public static InputStream imageCompression(InputStream inputStream) {
        //获取文件输入流
        try {
            // 把图片读入到内存中
            BufferedImage bufImg = ImageIO.read(inputStream);
            // 压缩代码,存储图片文件byte数组
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //防止图片变红,这一步非常重要
            BufferedImage bufferedImage = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics().drawImage(bufImg,0,0, Color.WHITE,null);
            //先转成jpg格式来压缩,然后在通过OSS来修改成源文件本来的后缀格式
            ImageIO.write(bufferedImage,"jpg",bos);
            //获取输出流
            inputStream = new ByteArrayInputStream(bos.toByteArray());
            return inputStream;
        } catch (IOException e) {
            log.error("pic compress error",e);
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }catch (IOException e) {
                log.error("pic compress error, inputStream close error",e);
            }
        }
        return null;
    }


    /**
     * 图片识别
     * @param inputStream 输入流
     * @return true/false
     */
    public static Boolean imageCheck(InputStream inputStream) {
        try {
            // 读取文件的前几个字节
            inputStream.mark(8); // 标记流的当前位置，以便稍后重置
            byte[] bytes = new byte[8];
            inputStream.read(bytes);
            inputStream.reset(); // 重置流到标记的位置
            // JPEG文件的魔术数字是0xFFD8开头
            if (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8) {
                return true;
            }
            // PNG文件的魔术数字是0x89504E470D0A1A0A
            if (bytes[1] == (byte) 0x50 && bytes[2] == (byte) 0x4E && bytes[3] == (byte) 0x47 &&
                    bytes[4] == (byte) 0x0D && bytes[5] == (byte) 0x0A && bytes[6] == (byte) 0x1A &&
                    bytes[7] == (byte) 0x0A) {
                return true;
            }
            // GIF87a和GIF89a的魔术数字分别是"GIF87a"和"GIF89a"
            if (bytes[0] == 'G' && bytes[1] == 'I' && bytes[2] == 'F' &&
                    (bytes[3] == '8' && (bytes[4] == '7' || bytes[4] == '9') && bytes[5] == 'a')) {
                return true;
            }
            // BMP文件的魔术数字可能是"BM"（Windows位图）或者"BA"（OS/2位图）
            if (bytes[0] == 'B' && (bytes[1] == 'M' || bytes[1] == 'A')) {
                return true;
            }
        }catch (IOException e) {
            log.error("pic recognize error,", e);
        }
        return false;
    }

    /**
     * 根据指定大小压缩图片
     * 利用Google  Thumbnails 工具类进行压缩
     * @param imageBytes  源图片字节数组
     * @return 压缩质量后的图片字节数组
     */
    public static InputStream compressPicForScale(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length <= ZERO ) {
            return null;
        }
        long srcSize = imageBytes.length;
        // 自动调节
        double accuracy = getAccuracy(srcSize / ONE_ZERO_TWO_FOUR);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
            Thumbnails.of(inputStream)
                    .scale(accuracy)
                    .outputQuality(accuracy)
                    .toOutputStream(outputStream);
            imageBytes = outputStream.toByteArray();
            log.info("图片原大小={}kb | 压缩后大小={}kb",
                    srcSize / ONE_ZERO_TWO_FOUR, imageBytes.length / ONE_ZERO_TWO_FOUR);
        } catch (Exception e) {
            log.error("【图片压缩】msg=图片压缩失败!", e);
            return null;
        }
        return new ByteArrayInputStream(imageBytes);
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < NINE_ZERO_ZERO) {
            accuracy = ZERO_EIGHT_FIVE;
        } else if (size < TWO_ZERO_FOUR_SEVEN) {
            accuracy = ZERO_SIX;
        } else if (size < THREE_TWO_SEVEN_FIVE) {
            accuracy = ZERO_FOUR_FOUR;
        } else {
            accuracy = ZERO_FOUR;
        }
        return accuracy;
    }
}
