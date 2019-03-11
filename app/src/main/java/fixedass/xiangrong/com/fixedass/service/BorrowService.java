package fixedass.xiangrong.com.fixedass.service;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Convert;

/**
 * Created by Administrator on 2018/4/28.
 */

public class BorrowService {
    /*获得借还的所有数据--排序*/
    public static String getBorrowOrder(String ip, User user, String conSql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/borrow/getOrder";
            path = path + "?deptUUID=" + user.getDeptUUID()+"&userUUID=" + user.getUserUUID()+"&conSql="+conSql;
            Log.i("borrow-ip",path);

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

    /*获得借还的所有数据--筛选*/
    public static String getBorrowSel(String ip, User user, String conSql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/borrow/getSel";
            path = path + "?deptUUID=" + user.getDeptUUID()+"&userUUID=" + user.getUserUUID()+"&"+conSql;
            Log.i("borrow-ip",path);

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

    /*获取报审信息*/
    public static String examineInfo(String ip,User user,String operbillCodes){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/borrow/examineInfo";
            path = path + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
            Log.i("examineInfo-ip",path);

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3*1000); // 设置超时时间
            conn.setReadTimeout(3*1000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return Convert.parseInfoMax(is);
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
    /*获取报审信息*/
    public static String exam(String ip,User user,String conSql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/borrow/examine";
            path = path + "?userUUID=" + user.getUserUUID()+"&"+conSql;
            Log.i("exam-ip",path);

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

    /*获取审核信息*/
    public static String lookThroughInfo(String ip,User user,String operbillCodes){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/borrow/lookThroughInfo";
            path = path + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
            Log.i("lookThroughInfo-ip",path);

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3*1000); // 设置超时时间
            conn.setReadTimeout(3*1000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return Convert.parseInfoMax(is);
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
    /*获取报审信息*/
    public static String lookThrough(String ip,User user,String conSql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/borrow/lookThrough";
            path = path + "?userUUID=" + user.getUserUUID()+"&"+conSql;
            Log.i("exam-ip",path);

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

    /*获取归还信息*/
    public static String returnInfo(String ip,User user,String operbillCodes){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/borrow/returnInfo";
            path = path + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
            Log.i("returnInfo-ip",path);

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3*1000); // 设置超时时间
            conn.setReadTimeout(3*1000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return Convert.parseInfoMax(is);
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
    /*归还资产*/
    public static String assetReturn(String ip,User user,String conSql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/borrow/return";
            path = path + "?userUUID=" + user.getUserUUID()+"&"+conSql;
            Log.i("re-ip",path);

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

    /*删除借还单据*/
    public static String delete(String ip,User user,String operbillCodes){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/borrow/delete";
            path = path + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
            Log.i("delete-ip",path);

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

    /*添加借用*/
    public static String add(String ip, User user, String conSql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/borrow/add";
            path = path + "?userUUID=" + user.getUserUUID()+"&"+conSql;
            Log.i("borrowAdd-ip",path);

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

    /*获得借还单据*/
    public static String updateInfo(String ip,User user,String operbillCodes){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/borrow/updateInfo";
            path = path + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
            Log.i("updateInfo-ip",path);

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3*1000); // 设置超时时间
            conn.setReadTimeout(3*1000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return Convert.parseInfoMax(is);
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

    /*修改借还单据*/
    public static String update(String ip, User user, String conSql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            String path = "http://" + ip + "/FixedAssService/borrow/update";
            path = path + "?userUUID=" + user.getUserUUID()+"&"+conSql;
            Log.i("update-ip",path);

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
