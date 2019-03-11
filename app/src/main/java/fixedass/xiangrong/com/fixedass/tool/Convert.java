package fixedass.xiangrong.com.fixedass.tool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/4/28.
 * 流的转换
 */

public class Convert {
    public static final int REQUEST_CODE_SCAN = 111;

    // 将输入流转化为 String 型
    public static String parseInfo(InputStream inStream) throws Exception {
        byte[] data = read(inStream);
        // 转化为字符串
        return new String(data, "UTF-8");
    }

    // 将输入流转化为byte型
    private static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }

    // 将输入流转化为 String 型
    public static String parseInfoMax(InputStream inStream) throws Exception {
        byte[] data = readMax(inStream);
        // 转化为字符串
        return new String(data, "UTF-8");
    }

    // 将输入流转化为byte型
    private static byte[] readMax(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        /*Log.i("len",inStream.read(buffer)+"");*/
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);

        }
        inStream.close();
        return outputStream.toByteArray();
    }

}
