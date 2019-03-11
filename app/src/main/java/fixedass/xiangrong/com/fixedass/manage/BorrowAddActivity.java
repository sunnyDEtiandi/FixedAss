package fixedass.xiangrong.com.fixedass.manage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.asset.AssetAdapter;
import fixedass.xiangrong.com.fixedass.asset.BorrowAssetActivity;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.DataBean;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.Convert;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.tree.BorrowUserActivity;
import fixedass.xiangrong.com.fixedass.tree.BorrowUserFragment;
import fixedass.xiangrong.com.fixedass.zxing.android.CaptureActivity;
import fixedass.xiangrong.com.fixedass.zxing.common.Constant;

/**
 * @author Eileen
 * @create 2018/9/26
 * @Describe 添加资产资产选择显示在界面上，把每个值传给其他两个activity
 */
public class BorrowAddActivity extends Activity implements View.OnClickListener {
    /*private Button btnQuit,btnBack;*/
    private ImageView btnQuit,btnBack;
    private TextView title;
    private int fragid;
    private String ipStr;
    private User user;
    private String pName,pUUID;        //借用人名称，编码
    private String borrowDateStr,borrowDay,borrowInf, operNot;
    //借用人、借出日期、借用时长（天）、借用原因、备注
    private EditText borrowPeople, borrowDate, borrowDays, borrowInfo, operNote;
    private ListView borrowAsset;               //借用资产
    private AssetAdapter assetAdapter;         //适配器
    //添加资产、扫描资产、删除资产
    private TextView addAsset, scanAsset, delAsset;
    // 创建等待框
//    private ProgressDialog dialog;
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    /*借用的资产*/
    private DataBean<AssetStorage> stoData;
    private ArrayList<AssetStorage> stoList = new ArrayList<>();

    //日期
    private Integer year,month,day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_add);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");
        if(bundle.containsKey("pName")){
            pName = bundle.getString("pName");
            pUUID = bundle.getString("pUUID");
            borrowDateStr = bundle.getString("borrowDate");
            borrowDay = bundle.getString("borrowDays");
            borrowInf = bundle.getString("borrowInfo");
            operNot = bundle.getString("operNote");
        }
        if (bundle.containsKey("stoData")){
            stoData = (DataBean<AssetStorage>) bundle.getSerializable("stoData");
            List<AssetStorage> asList = stoData.getData();
            stoList.addAll(asList);
        }

        /*读取数据*/
        SharedPreferences sharedPreferences = BorrowAddActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        initView();
        initEvent();
    }

    private void initView(){
        /*btnQuit = (Button)findViewById(R.id.btnQuit);
        btnQuit.setText("保存");
        btnBack = (Button)findViewById(R.id.btnBack);*/
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setImageResource(R.drawable.save);

        title = (TextView)findViewById(R.id.title);
        title.setText("新建资产借还单据");

        borrowPeople = (EditText)findViewById(R.id.borrowPeople);
        borrowPeople.setText(pName);
        borrowDate = (EditText)findViewById(R.id.borrowDate);
        if (borrowDateStr==""||borrowDateStr==null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());
            borrowDateStr = df.format(curDate);
        }
        borrowDate.setText(borrowDateStr);

        String[] split = borrowDateStr.split("-");
        year = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1])-1;
        day = Integer.parseInt(split[2]);

        borrowDays = (EditText)findViewById(R.id.borrowDays);
        borrowDays.setText(borrowDay);
        borrowInfo = (EditText)findViewById(R.id.borrowInfo);
        borrowInfo.setText(borrowInf);
        operNote = (EditText)findViewById(R.id.operNote);
        operNote.setText(operNot);

        borrowAsset = (ListView)findViewById(R.id.borrowAsset);
        if (stoList.size()>0){
            setAsset(stoList);
        }

        addAsset = (TextView) findViewById(R.id.addAsset);
        scanAsset = (TextView) findViewById(R.id.scanAsset);
        delAsset = (TextView) findViewById(R.id.delAsset);
    }

    private void initEvent(){
        btnQuit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        borrowDate.setOnClickListener(this);
        borrowPeople.setOnClickListener(this);
        addAsset.setOnClickListener(this);
        scanAsset.setOnClickListener(this);
        delAsset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);
        //获取文本框数据
        String borrowPeo = borrowPeople.getText().toString();
        borrowDateStr = borrowDate.getText().toString();
        borrowDay = borrowDays.getText().toString();
        borrowInf = borrowInfo.getText().toString();
        operNot = operNote.getText().toString();
        switch (v.getId()) {
            case R.id.btnBack:          //返回
                Redirect.redirect(BorrowAddActivity.this, BorrowActivity.class, bundle);
                break;
            case R.id.btnQuit:          //保存
                try {
                    borrowInf = URLEncoder.encode(borrowInf,"utf-8");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                try {
                    operNot = URLEncoder.encode(operNot,"utf-8");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                if (borrowPeo.trim().equals("")){
                    /*Toast.makeText(BorrowAddActivity.this, "借用人不能为空！", Toast.LENGTH_LONG).show();*/
                    Dialog dialog = new AlertDialog.Builder(BorrowAddActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("借用人不能为空！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }
                if (borrowDateStr.trim().equals("")){
                    /*Toast.makeText(BorrowAddActivity.this, "借出日期不能为空！", Toast.LENGTH_LONG).show();*/
                    Dialog dialog = new AlertDialog.Builder(BorrowAddActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("借出日期不能为空！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }
                if (borrowDay.trim().equals("")){
                    /*Toast.makeText(BorrowAddActivity.this, "借用时长不能为空！", Toast.LENGTH_LONG).show();*/
                    Dialog dialog = new AlertDialog.Builder(BorrowAddActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("借用时长不能为空！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }
                if (stoList.isEmpty()){
                    Dialog dialog = new AlertDialog.Builder(BorrowAddActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("要借用的资产不能为空！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }
                //borrowPeople,         borrowDays,operNote,borrowDate,borrowInfo,assetUUID
                String assetUUID = "";
                for (AssetStorage sto: stoList){
                    assetUUID =  assetUUID+sto.getAssetUUID()+",";
                }
                assetUUID = assetUUID.substring(0,assetUUID.length()-1);

                String conSql = "borrowPeople="+pUUID+"&borrowDate="+borrowDateStr+"&borrowDays="+borrowDay+"&borrowInfo="
                                +borrowInf+"&operNote="+operNot+"&assetUUID="+assetUUID;
                save(conSql);
                break;
            case R.id.addAsset:                 //添加资产
                setBundle(bundle);
                /*Toast.makeText(BorrowAddActivity.this, "添加资产", Toast.LENGTH_SHORT).show();*/
                Redirect.redirect(BorrowAddActivity.this, BorrowAssetActivity.class, bundle);
                break;
            case R.id.scanAsset:                //扫描资产
//                Toast.makeText(BorrowAddActivity.this, "扫描资产", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(BorrowAddActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                Intent intent = new Intent(BorrowAddActivity.this, CaptureActivity.class);
                startActivityForResult(intent, Convert.REQUEST_CODE_SCAN);
                break;
            case  R.id.delAsset:                //删除资产
                /*Toast.makeText(BorrowAddActivity.this, "删除资产", Toast.LENGTH_SHORT).show();*/
                HashMap<Integer, Boolean> selCheck = assetAdapter.selCheck;
                final List<AssetStorage> stoLists = new ArrayList<>();
                for(int j = 0; j< assetAdapter.getCount(); j++) {
                    if (selCheck.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetStorage sto = (AssetStorage) assetAdapter.getItem(j);
                        stoLists.add(sto);
                    }
                }
                if(stoLists.size()==0){
                    Dialog dialog = new AlertDialog.Builder(BorrowAddActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("请选择要删除的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    /*stoData = new DataBean<>();
                    stoData.setData(stoLists);
                    bundle.putSerializable("stoData", stoData);
                    Redirect.redirect(AssetActivity.this, BorrowAddActivity.class,bundle);*/
                    Dialog dialog = new AlertDialog.Builder(BorrowAddActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("是否要删除选择的资产！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for(int i=0;i<stoList.size();i++){
                                        if (stoLists.contains(stoList.get(i))){
                                            stoList.remove(i);
                                        }
                                    }
                                    setAsset(stoList);
                                }
                            })
                            .setNegativeButton("取消", null).create();
                    dialog.show();
                }
                break;
            case R.id.borrowDate:
                Redirect.hideIM(v, BorrowAddActivity.this);
                Dialog dialog = new DatePickerDialog(BorrowAddActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year+"-"+(month+1)+"-"+dayOfMonth;
                        borrowDate.setText(date);
                    }
                }, year,month,day);
                dialog.show();
                break;
            case R.id.borrowPeople:
                setBundle(bundle);
                Class<?> clazz = BorrowUserFragment.class;
                Intent i = new Intent(BorrowAddActivity.this, BorrowUserActivity.class);
                i.putExtra(BorrowUserActivity.FRAGMENT_PARAM, clazz);
                i.putExtras(bundle);
                BorrowAddActivity.this.startActivity(i);
                break;
        }
    }

    /*传递本方法的参数*/
    private void setBundle(Bundle bundle){
        bundle.putString("pName", pName);
        bundle.putString("pUUID", pUUID);
        bundle.putString("borrowDate", borrowDateStr);
        bundle.putString("borrowDays", borrowDay);
        bundle.putString("borrowInfo", borrowInf);
        bundle.putString("operNote", operNot);
        if (stoList.size()>0){
            /*stoData.setData(stoList);*/
            bundle.putSerializable("stoData", stoData);
        }
    }

    //保存
    private void save(String conSql){
        if (!Redirect.checkNetwork(BorrowAddActivity.this)) {
            Toast toast = Toast.makeText(BorrowAddActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            /*dialog = new ProgressDialog(BorrowAddActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();*/
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MySaveThread(conSql)).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class MySaveThread implements Runnable {
        private String conSql;

        public MySaveThread(String conSql) {
            this.conSql = conSql;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/borrow/add"
                    + "?userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(BorrowAddActivity.this,"借用单创建成功",Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("fragid",fragid);
                        bundle.putSerializable("user",user);
                        Redirect.redirect(BorrowAddActivity.this, BorrowActivity.class, bundle);
                    }else {
                        Toast.makeText(BorrowAddActivity.this,"借用单创建失败，请重新创建借用单",Toast.LENGTH_LONG).show();
                    }
                }
            });
//            dialog.dismiss();
        }
    }

    //设置用户选择的资产信息
    private void setAsset(ArrayList<AssetStorage> stoList){
        //建立Adapter绑定数据源
        assetAdapter = new AssetAdapter(stoList,this, fragid, user, this.stoList);
        //绑定Adapter
        borrowAsset.setAdapter(assetAdapter);
    }

    //扫描资产
    private void zxingScan(){
        Intent intent = new Intent(BorrowAddActivity.this, CaptureActivity.class);
        /*ZxingConfig是配置类
         *可以设置是否显示底部布局，闪光灯，相册，
         * 是否播放提示音  震动
         * 设置扫描框颜色等
         * 也可以不传这个参数
         * */
        /*ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(false);//是否播放扫描声音 默认为true
        config.setShake(false);//是否震动  默认为true
        config.setDecodeBarCode(false);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);*/
        //startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    String barCode = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == Convert.REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                barCode = data.getStringExtra(Constant.CODED_CONTENT);
                System.out.println("=====content===="+barCode);
                for (int i=0; i<stoList.size(); i++){
                    if (stoList.get(i).getBarCode().equals(barCode)){
                        break;
                    }
                    if (i==stoList.size()-1){
                        new Thread(new BarCodeThread()).start();
                    }
                }
                if (stoList.size()<1){
                    new Thread(new BarCodeThread()).start();
                }
            }
        }
    }
    class BarCodeThread implements Runnable{
        @Override
        public void run() {
            handler.post(new Runnable() {
                //查询数据
                String path = "http://" + ipStr + "/FixedAssService/sto/selBarCodes"
                    + "?userUUID=" + user.getUserUUID()+"&barCode="+barCode;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    AssetStorage sto = StrConvertObject.convertSto(info);
                    if (sto.getState().equals("领用")){
                        stoList.add(sto);
                        setAsset(stoList);
                        stoData = new DataBean<>();
                        stoData.setData(stoList);
                    }else {
                        Dialog dialog = new AlertDialog.Builder(BorrowAddActivity.this)
                                .setTitle("资产借还提示")
                                .setMessage("您扫描的资产编码为："+sto.getBarCode()+"，该资产的资产状态为："+sto.getState())
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }
                }
            });
        }
    }
}
