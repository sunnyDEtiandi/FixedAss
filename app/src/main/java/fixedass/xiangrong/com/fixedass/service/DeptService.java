package fixedass.xiangrong.com.fixedass.service;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Convert;

/*部门相关与数据库的交互*/
public class DeptService {

    /*获得根目录也就是集团*/
    public static String getGroup(String ip,User user){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/dept/getGroup";
            path = path + "?deptUUID=" + user.getDeptUUID();
            Log.i("getGroup-ip",path);

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3*1000); // 设置超时时间
            conn.setReadTimeout(3*1000);
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

    /*获得二级目录也就是集团*/
    public static String getSecondary(String ip,User user){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/dept/getSecondary";
            path = path + "?deptUUID=" + user.getDeptUUID();
            Log.i("getSecondary-ip",path);

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3*1000); // 设置超时时间
            conn.setReadTimeout(3*1000);
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

    /*获得三级目录也就是集团*/
    public static String getDeptJstree(String ip,User user){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/dept/getDeptJstree";
            path = path + "?deptUUID=" + user.getDeptUUID();
            Log.i("getDeptJstree-ip",path);

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3*1000); // 设置超时时间
            conn.setReadTimeout(3*1000);
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
