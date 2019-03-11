package fixedass.xiangrong.com.fixedass.service;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Convert;

/**
 * Created by Administrator on 2018/4/27.
 */

public class WebService{
    // 通过Get方式获取HTTP服务器数据--判断用户是否可以登录
    public static String executeHttpGet(String path) {
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            Log.e("path", path);

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(10*1000); // 设置超时时间
            conn.setReadTimeout(10*1000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return Convert.parseInfo(is);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /*添加维修*/
    public static String add(String path, String content){
        Log.e("add: content", content);

        // URL 地址
        HttpURLConnection urlConn = null;
        InputStream is = null;
        try {
            URL url = new URL(path);
            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setDoOutput(true);      // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            urlConn.setDoInput(true);       // 设置连接输入流为true
            urlConn.setRequestMethod("POST");       // 设置请求方式为post
            urlConn.setUseCaches(false);            // post请求缓存设为false
            urlConn.setRequestProperty("Charset", "UTF-8");
            //urlConn.setRequestProperty("Content-type", "application/x-java-serialized-object");
            urlConn.setRequestProperty("Content-Type","application/x-javascript; charset=UTF-8");
            urlConn.setRequestProperty("accept","application/json");
            /*建立连接*/
            urlConn.connect();
            urlConn.getOutputStream().write(content.toString().trim().getBytes());
            if (urlConn.getResponseCode() == 200) {
                is = urlConn.getInputStream();
                return Convert.parseInfo(is);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 意外退出时进行连接关闭保护
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return null;
    }

    /*获得用户的权限*/
    public static String getUserAuthority(String ip,User user){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/authority/userFunc";
            path = path + "?userUUID=" + user.getUserUUID();
            Log.i("ip",path);

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(10*1000); // 设置超时时间
            conn.setReadTimeout(10*1000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return Convert.parseInfo(is);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
