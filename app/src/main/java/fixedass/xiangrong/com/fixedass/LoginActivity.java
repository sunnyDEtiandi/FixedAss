package fixedass.xiangrong.com.fixedass;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

public class LoginActivity extends Activity implements View.OnClickListener {
    // 登陆按钮
    /*private Button loginBtn,cancelBtn;*/
    private ImageView loginBtn/*, cancelBtn*/;
    // 用户名、密码
    private EditText userName,passWord;
    // 错误提示、地址设计
    private TextView point,ipSet,cancelBtn;
    /*private ImageView ipSet;*/
    //服务器地址
    private String ipStr;
    private EditText inputServer;

    // 返回的数据
    private String info;
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    // 创建等待框
    private ProgressDialog dialog;

    //jsonObject转object
    private Gson gson;
    private GsonBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ExitApplication.getInstance().addActivity(this);	//记录所有的Activity

        initView();
        initEvent();
    }

    /*初始化控件*/
    private void initView(){
        userName = (EditText) findViewById(R.id.userName);
        passWord = (EditText) findViewById(R.id.passWord);
        /*loginBtn = (Button) findViewById(R.id.loginBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);*/
        loginBtn = (ImageView)findViewById(R.id.loginBtn);
        /*cancelBtn = (ImageView)findViewById(R.id.cancelBtn);*/
        cancelBtn = (TextView)findViewById(R.id.cancelBtn);

        point = (TextView) findViewById(R.id.point);
        /*ipSet = (TextView) findViewById(R.id.ipSet);*/
        /*ipSet = (ImageView)findViewById(R.id.ipSet);*/
        ipSet = (TextView)findViewById(R.id.ipSet);

        inputServer = new EditText(LoginActivity.this);
        /*读取数据*/
        SharedPreferences sharedPreferences = LoginActivity.super.getSharedPreferences("ipConfig",Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");
        inputServer.setText(ipStr);
        /*清除数据*/
        clean();

        //这两句代码必须的，为的是初始化出来gson这个对象，才能拿来用
        builder = new GsonBuilder();
        gson = builder.create();
    }

    /*给控件设置监听*/
    private void initEvent(){
        /*回车事件*/
        passWord.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER  && event.getAction() == KeyEvent.ACTION_UP) {
                    Redirect.hideIM(v,LoginActivity.this);
                    login();
                    return true;
                }
                return false;
            }
        });

        // 设置按钮监听器
        loginBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        ipSet.setOnClickListener(this);
    }

    /*按钮监听事件*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                // 检测网络，无法检测wifi
                login();
                break;
            case R.id.cancelBtn:
                AlertDialog.Builder build = new AlertDialog.Builder(LoginActivity.this);
                build.setTitle("注意").setMessage("确定要退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null).show();
                break;
            case R.id.ipSet:
                ipSetting();
                break;
        };
    }

    private void ipSetting(){
        inputServer.setHintTextColor(getResources().getColor(R.color.gray));
        /*inputServer.setText("192.168.1.181:8080");*/
        inputServer.selectAll();
        inputServer.setHint("192.168.1.1:8080");

        ViewGroup parent = (ViewGroup) inputServer.getParent();
        if (parent!=null){
            parent.removeAllViews();
        }

        Dialog dialog = new AlertDialog.Builder(LoginActivity.this)
                .setIcon(R.drawable.tool)
                .setTitle("系统设置")
                .setMessage("服务器地址：")
                .setView(inputServer)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //指定操作的文件名称
                        SharedPreferences sh = LoginActivity.super.getSharedPreferences("ipConfig",Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sh.edit();                      //编辑文件
                        String ipStr1 = inputServer.getText().toString();               //获取用户输入的服务器地址
                        if(ipStr1.trim().equals("")){
                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("注意").setMessage("请先填写系统设置中的服务器地址！")
                                    .setPositiveButton("确定", null).create();
                            alertDialog.show();
                            return;
                        }
                        if(!ipStr.equals(ipStr1)){
                            ipStr = ipStr1;
                            edit.putString("ipStr",ipStr);                                 //保存
                        }
                        edit.commit();                                                  //提交跟新
                        Redirect.hideIM(inputServer,LoginActivity.this);
                    }
                })
                .setNegativeButton("取消", null).create();
        dialog.show();

    }

    /*登录*/
    private void login(){
        if (!Redirect.checkNetwork(LoginActivity.this)) {
            Toast toast = Toast.makeText(LoginActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            if(userName.getText().toString().equals("")){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       point.setText("用户名不能为空");
                    }
                });
            }else if(passWord.getText().toString().equals("")){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        point.setText("密码不能为空");
                    }
                });
            }else if(ipStr.equals("")){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        point.setText("请输入服务器的地址！");
                    }
                });
            }else{
                // 提示框
                dialog = new ProgressDialog(this);
                dialog.setTitle("提示");
                dialog.setMessage("正在登陆，请稍后...");
                dialog.setCancelable(false);
                dialog.show();

                // 创建子线程，分别进行Get和Post传输
                new Thread(new MyThread()).start();
            }
        }
    }

    // 子线程接收数据，主线程修改数据
    public class MyThread implements Runnable {
        @Override
        public void run() {
            String name = userName.getText().toString();
            String pwd =  new SimpleHash("MD5", passWord.getText().toString(), ByteSource.Util.bytes(name), 5).toString();

            String path = "http://" + ipStr + "/FixedAssService/login/login" + "?username=" + name + "&password=" + pwd;

            info = WebService.executeHttpGet(path);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("=======info======"+info);
                        JSONArray jsonArray = new JSONArray(info);
                        String msg = jsonArray.getJSONObject(0).getString("msg");
                        if(msg.equals("")){
                            JSONObject jsonObject = jsonArray.getJSONObject(1);
                            User user = gson.fromJson(jsonObject.toString(),User.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user",user);
                            bundle.putInt("fragid",0);
                            Redirect.redirect(LoginActivity.this,MainActivity.class,bundle);
                        }else{
                            point.setText(msg);
                            dialog.dismiss();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /*清除数据*/
    private void clean(){
        /*SharedPreferences sharedPreferences = LoginActivity.super.getSharedPreferences("ipConfig",Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit1 = sharedPreferences.edit();
        edit1.clear();
        edit1.commit();*/

        SharedPreferences sh = LoginActivity.super.getSharedPreferences("borrowConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();                      //编辑文件
        edit.clear();
        edit.commit();
        SharedPreferences shs = LoginActivity.super.getSharedPreferences("transferConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor shEdit = shs.edit();                      //编辑文件
        shEdit.clear();
        shEdit.commit();
    }

    //退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setTitle("注意").setMessage("确定要退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null).show();
                break;
            default:
                break;
        }
        return false;
    }
}
