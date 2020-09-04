package com.rf.cms.v3.Utils;

import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * com.rf.cms.v3.Utils 于2020/8/20 由Administrator 创建 .
 */
public class DownLoadFile {
    public static void downloadFileFromUrl(String fileUrl, String fileName, String savePath) throws Exception {
        java.net.URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(3 * 1000);
        connection.setRequestProperty(
                "Accept",
                "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                        + "application/x-shockwave-flash, application/xaml+xml, "
                        + "application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, "
                        + "application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        connection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
        connection.setRequestProperty("Referer", fileUrl);
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
        connection.setRequestProperty("Connection", "Keep-Alive");
//        connection.setRequestProperty("x-tif-paasid", "itdccs");
//        connection.setRequestProperty("x-tif-signature", "E92103109F70F9A3B1A88DD65FC3F0AFD133AF79AC544CD0F7D1E3402C13247F");
//        connection.setRequestProperty("x-tif-timestamp", "1526628523");
//        connection.setRequestProperty("x-tif-nonce", "9744980052016466");
        InputStream in = connection.getInputStream();
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        File file = new File(savePath + fileName);

        OutputStream out = new FileOutputStream(file);

        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = in.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }

//        StringBuffer buffer = new StringBuffer();
//        InputStreamReader inr = new InputStreamReader(in, "utf-8");
//        BufferedReader rd = new BufferedReader(inr);
//        int ch;
//        for (int length = 0; (ch = rd.read()) > -1; length++)
//            buffer.append((char) ch);
//        String s = buffer.toString();
//        s.replaceAll("//&[a-zA-Z]{1,10};", "").replace("<[^>]>", "");
//        System.out.println(s);

        out.close();
        in.close();
    }

    public static InputStream downloadFileFromUrl(String fileUrl) throws Exception {
        java.net.URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(3 * 1000000);
        connection.setRequestProperty(
                "Accept",
                "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                        + "application/x-shockwave-flash, application/xaml+xml, "
                        + "application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, "
                        + "application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        connection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
        connection.setRequestProperty("Referer", fileUrl);
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
        connection.setRequestProperty("Connection", "Keep-Alive");
//        connection.setRequestProperty("x-tif-paasid", "itdccs");
//        connection.setRequestProperty("x-tif-signature", "E92103109F70F9A3B1A88DD65FC3F0AFD133AF79AC544CD0F7D1E3402C13247F");
//        connection.setRequestProperty("x-tif-timestamp", "1526628523");
//        connection.setRequestProperty("x-tif-nonce", "9744980052016466");
        InputStream in = connection.getInputStream();

        return in;
    }
//保存图片代码
    //            String filePath=AposeUtils.class.getClassLoader().getResource("").getPath();
//            File targetFile = new File(filePath);
//            if (!targetFile.exists()) {
//                targetFile.mkdirs();
//
//            }
//            FileOutputStream out = new FileOutputStream(filePath + "4.jpg");
//
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//
//            byte[] buffer = new byte[1024];
//            int length;
//
//            while ((length = imgs.read(buffer)) > 0) {
//                output.write(buffer, 0, length);
//            }
//            byte[] context=output.toByteArray();
//            out.write(output.toByteArray());
//            imgs.close();
//            out.close();

    public static String downloadFileFromUrlByname(String fileUrl,String filePath,String filename) throws Exception {
        java.net.URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5 * 10000000);
        connection.setRequestProperty(
                "Accept",
                "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                        + "application/x-shockwave-flash, application/xaml+xml, "
                        + "application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, "
                        + "application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        connection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
        connection.setRequestProperty("Referer", fileUrl);
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
        connection.setRequestProperty("Connection", "Keep-Alive");
//        connection.setRequestProperty("x-tif-paasid", "itdccs");
//        connection.setRequestProperty("x-tif-signature", "E92103109F70F9A3B1A88DD65FC3F0AFD133AF79AC544CD0F7D1E3402C13247F");
//        connection.setRequestProperty("x-tif-timestamp", "1526628523");
//        connection.setRequestProperty("x-tif-nonce", "9744980052016466");
        InputStream in = connection.getInputStream();
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();

        }
        File file = new File(filePath+"/"+filename);
        if(!file.exists()){
            FileOutputStream out = new FileOutputStream(filePath+"/"+filename);

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = in.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context=output.toByteArray();
            out.write(output.toByteArray());
            out.close();
            output.close();
        }else {

        }
        in.close();
        connection.disconnect();
        return filePath+"/"+filename;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    public static InputStream getInputStream(String imgUrl) {
        InputStream inputStream = null;
        try{
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(imgUrl).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
            httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
            httpURLConnection.setRequestProperty("Referer","no-referrer");
            httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(20000);
            inputStream = httpURLConnection.getInputStream();
        }catch (IOException e){
            e.printStackTrace();
        }
        return inputStream;
    }


    public static void downfile(HttpServletResponse response,byte[] data){
        ServletOutputStream outputStream = null;
        try {
            // inline在浏览器中直接显示，不提示用户下载
            // attachment弹出对话框，提示用户进行下载保存本地
            // 默认为inline方式
            response.setHeader("Content-disposition", "attachment;filename=" +
                    new String( "123456.doc".getBytes("UTF-8"), "UTF-8"));
            // 写出
            outputStream = response.getOutputStream();
            IOUtils.write(data, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (outputStream!=null){
                    outputStream.close();
                    outputStream=null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
