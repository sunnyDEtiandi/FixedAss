package fixedass.xiangrong.com.fixedass.service;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Convert;

public class StoService {

    /*获得所在集团或者公司的资产信息*/
    public static String getAddSto(String ip,User user,String barCode){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/sto/getAddSto";
            path = path + "?deptUUID=" + user.getDeptUUID()+"&barCode="+barCode;
            Log.i("getAddSto-ip",path);

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

    /*履历查询*/
    public static String selBarCode(String ip,User user,String barCode){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/sto/selBarCode";
            path = path + "?userUUID=" + user.getUserUUID()+"&barCode="+barCode;
            Log.i("selBarCode-ip",path);

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
    /*履历详情查询*/
    public static String selDetail(String ip,User user,String barCode,String operbillCode){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/sto/selDetail";
            path = path + "?userUUID=" + user.getUserUUID()+"&barCode="+barCode+"&operbillCode="+operbillCode;
            Log.i("selDetail-ip",path);

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

    //资产统计
    public static String getSumCount(String ip,User user,String sql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/sto/getSumCount";
            path = path + "?userUUID=" + user.getUserUUID()+"&"+sql;
            Log.i("getSumCount-ip",path);

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

    //资产统计详情
    public static String selSumDetail(String ip,User user,String sql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/sto/selSumDetail";
            path = path + "?userUUID=" + user.getUserUUID()+"&"+sql;
            Log.i("selSumDetail-ip",path);

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

    //类型统计
    public static String countState(String ip){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/sto/countState";
            Log.i("countState-ip",path);

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

    //根据资产编码查询资产信息
    public static String selSto(String ip,User user,String barCode){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/sto/selSto";
            path = path + "?userUUID=" + user.getUserUUID()+"&barCode="+barCode;
            Log.i("selSto-ip",path);

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

    /*查询数据*/
    public static String selBarCodes(String ip, User user, String barCode){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/sto/selBarCodes";
            path = path + "?userUUID=" + user.getUserUUID()+"&barCode="+barCode;
            Log.i("selBarCodes-ip",path);

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
