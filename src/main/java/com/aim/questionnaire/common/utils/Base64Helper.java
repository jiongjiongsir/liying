package com.aim.questionnaire.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import org.apache.commons.codec.binary.Base64;



public class Base64Helper {
    /**
     * 编码
     * @param byteArray
     * @return
     */
    public static String encode(byte[] byteArray) {
        return new String(new Base64().encode(byteArray));
    }

    /**
     * 解码
     * @param base64EncodedString
     * @return ss
     */
    public static byte[] decode(String base64EncodedString) {
        return new Base64().decode(base64EncodedString);
    }


    /**
     * 本地图片转换成base64字符串
     * @param imgFile	图片对象
     * @return
     * @author huangshikai
     */
//    public static String ImageToBase64ByLocal(File imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
//        byte[] data = null;
//        // 读取图片字节数组
//        try {
//            InputStream in = new FileInputStream(imgPath);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        // 返回Base64编码过的字节数组字符串
//        System.out.println("本地图片转换Base64:" + encoder.encode(Objects.requireNonNull(data)));
//        return encoder.encode(Objects.requireNonNull(data));// 返回Base64编码过的字节数组字符串
//    }

    /**
     * 本地图片转换成base64字符串
     * @param imgFilePath 本地图片存放路径
     * @return
     * @author huangshikai
     */
    public static String ImageToBase64ByLocal(String imgFilePath){
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
//        System.out.println("本地图片转换Base64:" + encoder.encode(Objects.requireNonNull(data)));
        return Base64.encodeBase64String(data);// 返回Base64编码过的字节数组字符串
    }



}

