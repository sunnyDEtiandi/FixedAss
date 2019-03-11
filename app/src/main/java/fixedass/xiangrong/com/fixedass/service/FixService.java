package fixedass.xiangrong.com.fixedass.service;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Convert;

/**
 * @author Eileen
 * @create 2018/9/27
 * @Describe 资产转移与数据库的链接
 */
public class FixService {
    /*获得借还的所有数据--筛选*/
    public static String getFixSel(String ip, User user, String conSql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/fix/getSel";
            path = path + "?deptUUID=" + user.getDeptUUID()+"&userUUID=" + user.getUserUUID()+"&"+conSql;
            Log.i("fix-ip",path);

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

    /*获得借还的所有数据--排序*/
    public static String getOrderBy(String ip, User user, String conSql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/fix/getOrder";
            path = path + "?deptUUID=" + user.getDeptUUID()+"&userUUID=" + user.getUserUUID()+"&conSql="+conSql;
            Log.i("odrerby-ip",path);

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

    /*获取报审信息*/
    public static String examineInfo(String ip,User user,String operbillCodes){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/fix/examineInfo";
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
            String path = "http://" + ip + "/FixedAssService/fix/examine";
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
            String path = "http://" + ip + "/FixedAssService/fix/lookThroughInfo";
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
            String path = "http://" + ip + "/FixedAssService/fix/lookThrough";
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
    public static String confirmInfo(String ip,User user,String operbillCodes){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/fix/confirmInfo";
            path = path + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
            Log.i("confirmInfo-ip",path);

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
    public static String confirm(String ip,User user,String conSql){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/fix/confirm";
            path = path + "?userUUID=" + user.getUserUUID()+"&"+conSql;
            Log.i("confirm-ip",path);

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

    /*删除维修单据*/
    public static String delete(String ip,User user,String operbillCodes){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/fix/delete";
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

    /*获取保管员*/
    public static String getCareMan(String ip,User user,String pUUID){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/fix/getCareMan";
            path = path + "?sysUUID=" + user.getSysUUID()+"&pUUID="+pUUID;
            Log.i("getCareMan-ip",path);

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

    /*获取存放地址*/
    public static String getPlace(String ip,User user,String pUUID){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/fix/getPlace";
            path = path + "?sysUUID=" + user.getSysUUID()+"&pUUID="+pUUID;
            Log.i("getPlace-ip",path);

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

    /*添加维修*/
    public static String add(String ip, User user, String conSql){
        // URL 地址
        String path = "http://" + ip + "/FixedAssService/fix/add";
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
            String content = "userUUID=" + user.getUserUUID()+"&"+conSql;
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


        /*HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            path = path + "?userUUID=" + user.getUserUUID()+"&"+conSql;
            Log.i("fixAdd-ip",path);

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
        }*/
        return null;
    }

    /*修改项目*/
    public static String updateInfo(String ip,User user,String operbillCodes){
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + ip + "/FixedAssService/fix/updateInfo";
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
            String path = "http://" + ip + "/FixedAssService/fix/update";
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
