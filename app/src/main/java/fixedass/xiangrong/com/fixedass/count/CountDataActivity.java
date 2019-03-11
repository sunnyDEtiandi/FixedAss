package fixedass.xiangrong.com.fixedass.count;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import fixedass.xiangrong.com.fixedass.MainActivity;
import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.Address;
import fixedass.xiangrong.com.fixedass.bean.AssetCountDetail;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.Dept;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

public class CountDataActivity extends Activity implements View.OnClickListener,View.OnKeyListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private TextView title;

    private User user;
    private int fragid;
    private Dept dept;
    private Address address;
    private String ipStr, countBillCode;

    private EditText barCode, barCodeValue, assName, className, assPrice, assType, bookDate, useCompany, useDept, usePeople, storeAddress;
    private RelativeLayout relative;
    private String barCodeStr;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_data);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");
        countBillCode = bundle.getString("countBillCode");
        dept = (Dept) bundle.getSerializable("dept");
        address = (Address) bundle.getSerializable("address");

        /*读取数据*/
        SharedPreferences sharedPreferences = CountDataActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initView();
        initEvent();
    }

    private void initView(){
        /*btnBack = (Button)findViewById(R.id.btnBack);
        btnQuit = (Button)findViewById(R.id.btnQuit);
        btnQuit.setText("结束");*/


        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setImageResource(R.drawable.end);


        title = (TextView)findViewById(R.id.title);
        title.setText("资产盘点");

        barCode = (EditText)findViewById(R.id.barCode);
        barCodeValue = (EditText)findViewById(R.id.barCodeValue);
        assName = (EditText)findViewById(R.id.assName);
        className = (EditText)findViewById(R.id.className);
        assType = (EditText)findViewById(R.id.assType);
        assPrice = (EditText)findViewById(R.id.assPrice);
        bookDate = (EditText)findViewById(R.id.bookDate);
        useCompany = (EditText)findViewById(R.id.useCompany);
        useDept = (EditText)findViewById(R.id.useDept);
        usePeople = (EditText)findViewById(R.id.usePeople);
        storeAddress = (EditText)findViewById(R.id.storeAddress);

        relative = (RelativeLayout)findViewById(R.id.relative);
    }

    private void initEvent(){
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);

        barCode.setOnKeyListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid", fragid);
        bundle.putSerializable("user", user);
        switch (v.getId()){
            case R.id.btnBack:
                bundle.putString("countBillCode", countBillCode);
                bundle.putSerializable("dept", dept);
                bundle.putSerializable("address", address);
                Redirect.redirect(this, CounttingActivity.class, bundle);
                break;
            case R.id.btnQuit:
                Redirect.redirect(this, MainActivity.class, bundle);
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(relative.getWindowToken(), 0);
            barCode.setText(barCode.getText().toString());// 添加这句后实现效果
            barCode.selectAll();
            selectByBarCode();
            return true;
        }
        return false;
    }

    private void selectByBarCode(){
        if (!Redirect.checkNetwork(CountDataActivity.this)) {
            Toast toast = Toast.makeText(CountDataActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MyCodeThread()).start();
        }
    }
    class MyCodeThread implements Runnable{
        AssetStorage storage;

        @Override
        public void run() {
            selSto();
        }

        //获取资产信息
        void selSto(){
            barCodeStr = barCode.getText().toString().toUpperCase();
            if (barCodeStr==null || barCodeStr.equals("")){             //没有输入资产编码
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CountDataActivity.this, "请输入或扫描要查询的资产编码", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {                                                         //输入资产编码
                String path = "http://" + ipStr + "/FixedAssService/sto/selSto"
                    + "?userUUID="+user.getUserUUID()+"&barCode="+barCodeStr;
                final String info = WebService.executeHttpGet(path);                     //获得该编码的资产信息

                if (info.equals("null")){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setString();
                            Toast.makeText(CountDataActivity.this, "不存在该编码的资产！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(info);
                        storage = new Gson().fromJson(jsonObject.toString(), AssetStorage.class);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    selDetail();
                }
            }
        }
//        获得盘点
        void selDetail(){
            barCodeStr = barCode.getText().toString().toUpperCase();

            String path = "http://" + ipStr + "/FixedAssService/count/selCountDetail"
                 + "?userUUID=" + user.getUserUUID()+"&countbillCode="+countBillCode+"&barCode="+barCodeStr;
            String info = WebService.executeHttpGet(path);

            if (info.equals("null")) {                                                                                      //盘点单中不存在这个资产
                //盘点单中不存在这个资产
                String deptStr = storage.getDeptInfo()==null?"":storage.getDeptInfo().getDeptName();
                String addStr = storage.getAddress()==null?"":storage.getAddress().getAddrName();

                String str = "";
                //判断部门或者地址是否相同
                if (!dept.getDeptName().equals(deptStr)){
                    if (!address.getAddrName().equals(addStr)) {
                        str = "本盘点单不存在该资产,并且该资产不属于本部门以及本地址，是否添加？";
                    }else {
                        str = "本盘点单不存在该资产,并且该资产不属于本部门，是否添加？";
                    }
                }else if (!address.getAddrName().equals(addStr)){
                    str = "本该盘点单不存在该资产,并且该资产不属于本地址，是否添加？";
                }else {
                    str = "该盘点单不存在该资产，是否添加？";
                }
                final String strMsg = str;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder build = new AlertDialog.Builder(CountDataActivity.this);
                        build.setTitle("注意").setMessage(strMsg)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String msg = add();
                                        setStoStr(storage);
                                        Toast.makeText(CountDataActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setString();
                                    }
                                }).show();
                    }
                });
            }else {
                //盘点单中存在这个资产
                AssetCountDetail detail = null;
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    detail = new Gson().fromJson(jsonObject.toString(), AssetCountDetail.class);           //获得盘点详情
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                //判断盘点状态
                if (detail.getCountState() != 0) {                                                                         //已盘点--已处理
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setString();
                            Toast.makeText(CountDataActivity.this, "该物品已盘点！", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {                                                                            //未盘点
                    String deptStr = storage.getDeptInfo()==null?"":storage.getDeptInfo().getDeptName();
                    String addStr = storage.getAddress()==null?"":storage.getAddress().getAddrName();

                    String str = "";
                    //判断部门或者地址是否相同
                    if (!dept.getDeptName().equals(deptStr)){
                        if (!address.getAddrName().equals(addStr)) {
                            str = "该资产不属于本部门以及本地址，是否变更？";
                        }else {
                            str = "该资产不属于本部门，是否变更？";
                        }
                    }else if (!address.getAddrName().equals(addStr)){
                        str = "该资产不属于本地址，是否变更？";
                    }else {
                        str = null;
                    }

                    if (str==null) {                                //是本部门本地址的盘点清单
                        final String msg = upd();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setStoStr(storage);
                                Toast.makeText(CountDataActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {                                                     //不是本部门或本地址  的盘点清单
                        final String msg = str;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder build = new AlertDialog.Builder(CountDataActivity.this);
                                build.setTitle("注意").setMessage(msg)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String msg = updDeptPlace();
                                                setStoStr(storage);
                                                Toast.makeText(CountDataActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("取消",
                                                    new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    setString();
                                                }
                                            }
                                        ).show();
                            }
                        });
                    }

                }
            }
        }

        //修改资产的盘点状态
        String upd(){
            String path = "http://" + ipStr + "/FixedAssService/count/updDetail"
                 + "?userUUID=" + user.getUserUUID()+"&countbillCode="+countBillCode+"&barCode="+barCodeStr;
            String info = WebService.executeHttpGet(path);

            int count = Integer.parseInt(info);
            String msg = "";
            if (count>0){
                msg = "记录成功";
            }else {
                msg = "请输入或扫描要查询的资产编码";
            }
            return msg;
        }

        //修改资产的部门以及地址
        String updDeptPlace(){
            String path = "http://" + ipStr + "/FixedAssService/count/updDeptPlace"
                 + "?userUUID=" + user.getUserUUID()+"&countbillCode="+countBillCode+"&barCode="+barCodeStr+"&deptUUID="+dept.getDeptUUID()+"&addrUUID="+address.getAddrUUID();
            String info = WebService.executeHttpGet(path);

            int count = Integer.parseInt(info);
            String msg = "";
            if (count>0){
                msg = "记录成功";
            }else {
                msg = "请输入或扫描要查询的资产编码";
            }
            return msg;
        }

        String add(){
            String path = "http://" + ipStr + "/FixedAssService/count/addDetail"
                    + "?userUUID=" + user.getUserUUID()+"&countbillCode="+countBillCode+"&barCode="+barCodeStr
                    +"&deptUUID="+dept.getDeptUUID()+"&addrUUID="+address.getAddrUUID();
            String info = WebService.executeHttpGet(path);
            int count = Integer.parseInt(info);
            String msg = "";
            if (count>0){
                msg = "记录成功";
            }else {
                msg = "请输入或扫描要查询的资产编码";
            }
            return msg;
        }
    }

    private void setString(){
        barCodeValue.setText("");
        assName.setText("");
        className.setText("");
        assPrice.setText("");
        assType.setText("");
        bookDate.setText("");
        useCompany.setText("");
        useDept.setText("");
        usePeople.setText("");
        storeAddress.setText("");
    }

    private void setStoStr(AssetStorage storage){
        barCodeValue.setText(storage.getBarCode());
        assName.setText(storage.getAssName());
        className.setText(storage.getClassName());
        assPrice.setText(storage.getAssPrice()+"");
        assType.setText(storage.getAssType());
        bookDate.setText(storage.getBookDate());
        useCompany.setText(storage.getCompanyInfo()==null?"":storage.getCompanyInfo().getDeptName());
        String deptStr = storage.getDeptInfo()==null?"":storage.getDeptInfo().getDeptName();
        useDept.setText(deptStr);
        usePeople.setText(storage.getUsePeopleEntity()==null?"":storage.getUsePeopleEntity().getpName());
        String addStr = storage.getAddress()==null?"":storage.getAddress().getAddrName();
        storeAddress.setText(addStr);
    }
}
