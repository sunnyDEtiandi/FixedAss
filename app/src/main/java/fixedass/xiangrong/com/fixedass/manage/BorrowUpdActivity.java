package fixedass.xiangrong.com.fixedass.manage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.asset.AssetAdapter;
import fixedass.xiangrong.com.fixedass.asset.BorrowUpdAssetActivity;
import fixedass.xiangrong.com.fixedass.bean.AssetBorrow;
import fixedass.xiangrong.com.fixedass.bean.AssetOperate;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.DataBean;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.Convert;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.tree.BorrowUpdUserActivity;
import fixedass.xiangrong.com.fixedass.tree.BorrowUpdUserFragment;
import fixedass.xiangrong.com.fixedass.zxing.android.CaptureActivity;
import fixedass.xiangrong.com.fixedass.zxing.common.Constant;

/**
 * @author Eileen
 * @create 2018/9/26
 * @Describe 添加资产资产选择显示在界面上，把每个值传给其他两个activity
 */
public class BorrowUpdActivity extends Activity implements View.OnClickListener {
    /*private Button btnQuit,btnBack;*/
    private ImageView btnQuit,btnBack;
    private TextView title;
    private int fragid, state;
    private String ipStr, operbillCode;
    private User user;

    //借用单号、借用人、借出日期、借用时长（天）、借用原因、备注
    private EditText borrowCode, borrowPeople, borrowDate, borrowDays, borrowInfo, operNote;
    private String borrowPeopleStr, borrowDateStr, borrowDaysStr,borrowInfoStr, operNoteStr;

    private String pName, pUUID;

    private ListView borrowAsset;               //借用资产
    private AssetAdapter assetAdapter;         //适配器
    //添加资产、扫描资产、删除资产
    private TextView addAsset, scanAsset, delAsset;
    // 创建等待框
    private ProgressDialog dialog;
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    /*借用的资产*/
    private DataBean<AssetStorage> stoData;
    private ArrayList<AssetStorage> stoList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_upd);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");
        operbillCode = bundle.getString("operbillCode");
        state = bundle.getInt("state");

        if (bundle.containsKey("borrowDate")){
            borrowPeopleStr = bundle.getString("borrowPeople");
            borrowDateStr = bundle.getString("borrowDate");
            borrowDaysStr = bundle.getString("borrowDays");
            borrowInfoStr = bundle.getString("borrowInfo");
            operNoteStr = bundle.getString("operNote");
        }

        if (bundle.containsKey("pName")){
            pName = bundle.getString("pName");
            pUUID = bundle.getString("pUUID");
        }

        if (bundle.containsKey("stoData")){
            stoData = (DataBean<AssetStorage>) bundle.getSerializable("stoData");
            List<AssetStorage> asList = stoData.getData();
            stoList.addAll(asList);
        }

        /*读取数据*/
        SharedPreferences sharedPreferences = BorrowUpdActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        initView();
        initEvent();
    }

    private void initView(){
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setImageResource(R.drawable.save);

        title = (TextView)findViewById(R.id.title);
        title.setText("编辑资产借还单据");

        borrowCode = (EditText)findViewById(R.id.borrowCode);
        borrowPeople = (EditText)findViewById(R.id.borrowPeople);
        borrowDate = (EditText)findViewById(R.id.borrowDate);
        borrowDays = (EditText)findViewById(R.id.borrowDays);
        borrowInfo = (EditText)findViewById(R.id.borrowInfo);
        operNote = (EditText)findViewById(R.id.operNote);

        borrowAsset = (ListView)findViewById(R.id.borrowAsset);

        addAsset = (TextView) findViewById(R.id.addAsset);
        scanAsset = (TextView) findViewById(R.id.scanAsset);
        delAsset = (TextView) findViewById(R.id.delAsset);
        if (borrowDaysStr==null){
            initData();
        }else {
            borrowCode.setText(operbillCode);

            if (state==11){
                borrowPeople.setText(pName);
            }else {
                borrowPeople.setText(borrowPeopleStr);
            }
            borrowDate.setText(borrowDateStr);
            borrowDays.setText(borrowDaysStr);
            borrowInfo.setText(borrowInfoStr);
            operNote.setText(operNoteStr);

            if (stoList.size()>0){
                setAsset(stoList);
            }
        }
    }

    private void initEvent(){
        btnQuit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        addAsset.setOnClickListener(this);
        scanAsset.setOnClickListener(this);
        delAsset.setOnClickListener(this);
        if (state==11){
            borrowPeople.setOnClickListener(this);
        }
    }

    private void initData(){
        if (!Redirect.checkNetwork(this)) {
            Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
//            初始化借还单信息
            new Thread(new IntiDataThread()).start();
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);
        //获取文本框数据
        borrowDaysStr = borrowDays.getText().toString();
        borrowInfoStr = borrowInfo.getText().toString();
        operNoteStr = operNote.getText().toString();
        switch (v.getId()) {
            case R.id.btnBack:          //返回
                Redirect.redirect(BorrowUpdActivity.this, BorrowActivity.class, bundle);
                break;
            case R.id.btnQuit:          //保存
                try {
                    borrowInfoStr = URLEncoder.encode(borrowInfoStr,"utf-8");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                try {
                    operNoteStr = URLEncoder.encode(operNoteStr,"utf-8");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                if (borrowDaysStr.trim().equals("")){
                    /*Toast.makeText(BorrowAddActivity.this, "借用时长不能为空！", Toast.LENGTH_LONG).show();*/
                    Dialog dialog = new AlertDialog.Builder(BorrowUpdActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("借用时长不能为空！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }
                if (stoList.isEmpty()){
                    Dialog dialog = new AlertDialog.Builder(BorrowUpdActivity.this)
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

                String conSql = "";
                if (state==11){
                    conSql = "operbillCode="+operbillCode+"&state="+state+"&borrowPeople="+pUUID
                            +"&borrowDays="+borrowDaysStr+"&borrowInfo="+borrowInfoStr+"&operNote="+operNoteStr+"&assetUUID="+assetUUID;
                }else if (state==0){
                    conSql = "operbillCode="+operbillCode+"&state="+state
                            +"&borrowDays="+borrowDaysStr+"&borrowInfo="+borrowInfoStr+"&operNote="+operNoteStr+"&assetUUID="+assetUUID;
                }
                update(conSql);
                break;
            case R.id.addAsset:                 //添加资产
                setBundle(bundle);
                Redirect.redirect(BorrowUpdActivity.this, BorrowUpdAssetActivity.class, bundle);
                break;
            case R.id.scanAsset:                //扫描资产
                ActivityCompat.requestPermissions(BorrowUpdActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                Intent intent = new Intent(BorrowUpdActivity.this, CaptureActivity.class);
                startActivityForResult(intent, Convert.REQUEST_CODE_SCAN);
                break;
            case  R.id.delAsset:                //删除资产
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
                    Dialog dialog = new AlertDialog.Builder(BorrowUpdActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("请选择要删除的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    Dialog dialog = new AlertDialog.Builder(BorrowUpdActivity.this)
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
                                    Log.e("删除===========修改", "stoList.size()="+stoList.size());
                                    setAsset(stoList);
                                }
                            })
                            .setNegativeButton("取消", null).create();
                    dialog.show();
                }
                break;
            case R.id.borrowPeople:                         //借用人
                setBundle(bundle);
                Class<?> clazz = BorrowUpdUserFragment.class;
                Intent i = new Intent(this, BorrowUpdUserActivity.class);
                i.putExtra(BorrowUpdUserActivity.FRAGMENT_PARAM, clazz);
                i.putExtras(bundle);
                BorrowUpdActivity.this.startActivity(i);
                break;
        }
    }

    //初始化数据
    public class IntiDataThread implements Runnable{
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/borrow/updateInfo"
                    + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCode;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    AssetOperate operate = null;
                    try {
                        JSONObject jsonObject = new JSONObject(info);
                        operate = StrConvertObject.setConvertAssetOperateBorrow(jsonObject);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if (operate!=null){
                        borrowCode.setText(operate.getOperbillCode());
                        List<AssetBorrow> list = operate.getList();
                        AssetBorrow borrow = list.get(0);

                        borrowPeopleStr = borrow.getBorrowPeople();
                        borrowPeople.setText(borrowPeopleStr);
                        borrowDateStr = borrow.getBorrowDate();
                        borrowDate.setText(borrowDateStr);
                        borrowDaysStr = borrow.getBorrowDays()+"";
                        borrowDays.setText(borrowDaysStr);
                        borrowInfoStr = borrow.getBorrowInfo();
                        borrowInfo.setText(borrowInfoStr);
                        operNoteStr = operate.getOperNote();
                        operNote.setText(operNoteStr);

                        for (AssetBorrow borrow1: list){
                            stoList.add(borrow1.getStorage());
                        }
                        setAsset(stoList);
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    /*传递本方法的参数*/
    private void setBundle(Bundle bundle){
        bundle.putString("operbillCode", operbillCode);
        bundle.putString("borrowPeople", borrowPeopleStr);

        bundle.putInt("state", state);

        bundle.putString("borrowDate", borrowDateStr);
        bundle.putString("borrowDays", borrowDaysStr);
        bundle.putString("borrowInfo", borrowInfoStr);
        bundle.putString("operNote", operNoteStr);

        if (stoList.size()>0){
            stoData = new DataBean<>();
            stoData.setData(stoList);
            bundle.putSerializable("stoData", stoData);
        }

    }

    //修改
    private void update(String conSql){
        if (!Redirect.checkNetwork(BorrowUpdActivity.this)) {
            Toast toast = Toast.makeText(BorrowUpdActivity.this,"网络未连接", Toast.LENGTH_SHORT);
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
            new Thread(new MyUpdThread(conSql)).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class MyUpdThread implements Runnable {
        private String conSql;

        public MyUpdThread(String conSql) {
            this.conSql = conSql;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/borrow/update"
                    + "?userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    Log.e("修改", "info="+info +"======num======"+num);

                    if (num>0){
                        Toast.makeText(BorrowUpdActivity.this,"借用单修改成功",Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("fragid",fragid);
                        bundle.putSerializable("user",user);
                        Redirect.redirect(BorrowUpdActivity.this, BorrowActivity.class, bundle);
                    }else {
                        Toast.makeText(BorrowUpdActivity.this,"借用单修改失败，请重新修改借用单",Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(BorrowUpdActivity.this, CaptureActivity.class);
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
                        Dialog dialog = new AlertDialog.Builder(BorrowUpdActivity.this)
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
