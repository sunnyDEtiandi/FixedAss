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
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.asset.AssetAdapter_Test;
import fixedass.xiangrong.com.fixedass.asset.TestUpdAssetActivity;
import fixedass.xiangrong.com.fixedass.bean.AssetFix;
import fixedass.xiangrong.com.fixedass.bean.AssetOperate;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.AssetTest;
import fixedass.xiangrong.com.fixedass.bean.DataBean;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.Convert;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.zxing.android.CaptureActivity;
import fixedass.xiangrong.com.fixedass.zxing.common.Constant;

/*资产检验新增*/
/*添加资产资产选择显示在界面上，把每个值传给其他两个activity*/
public class TestUpdActivity extends Activity implements View.OnClickListener {
    private ImageView btnQuit, btnBack;
    private TextView title;
    private int fragid;
    private String ipStr, operbillCode;
    private User user;

    //创建人、创建事件、备注信息
    private EditText testCode,createID,createDate,operNote;
    private String createIDStr, createDateStr,operNoteStr;                                 //创建人的uuid、创建人的名称

    private ListView testAsset;                                                  //检验的资产
    private AssetAdapter_Test assetAdapter;                                          //适配器

    //添加资产、扫描资产、删除资产
    private TextView addAsset, scanAsset, delAsset;
    // 创建等待框
    private ProgressDialog dialog;
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    /*检验的资产*/
    private DataBean<AssetStorage> stoData;
    private ArrayList<AssetStorage> stoList = new ArrayList<>();
    /*资产的维修信息*/
    private DataBean<AssetTest> testData;
    private List<AssetTest> testList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_upd);

        ExitApplication.getInstance().addActivity(this);

        /*读取数据*/
        SharedPreferences sharedPreferences = TestUpdActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");
        operbillCode = bundle.getString("operbillCode");

        if (bundle.containsKey("createID")){
            createIDStr = bundle.getString("createID");
            createDateStr = bundle.getString("createDate");
            operNoteStr = bundle.getString("operNote");
        }

        if (bundle.containsKey("stoData")){
            stoData = (DataBean<AssetStorage>) bundle.getSerializable("stoData");
            List<AssetStorage> asList = stoData.getData();
            stoList.addAll(asList);
        }
        if (bundle.containsKey("testData")){
            testData = (DataBean<AssetTest>) bundle.getSerializable("testData");
            List<AssetTest> tests = testData.getData();
            if (testList==null){
                testList = new ArrayList<>();
                testList.addAll(tests);
                for (AssetTest test: testList){
                    stoList.add(test.getStorage());
                }
            }
        }

        initView();
        initEvent();
    }

    private void initView(){
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setImageResource(R.drawable.save);

        title = (TextView)findViewById(R.id.title);
        title.setText("编辑资产检验单据");

        testCode = (EditText) findViewById(R.id.testCode);
        createID = (EditText) findViewById(R.id.createID);
        createDate = (EditText) findViewById(R.id.createDate);
        operNote = (EditText) findViewById(R.id.operNote);

        testAsset = (ListView)findViewById(R.id.testAsset);

        addAsset = (TextView) findViewById(R.id.addAsset);
        scanAsset = (TextView) findViewById(R.id.scanAsset);
        delAsset = (TextView) findViewById(R.id.delAsset);

        if (createIDStr==null){
            initData();
        }else {
            testCode.setText(operbillCode);
            createID.setText(createIDStr);
            createDate.setText(createDateStr);
            operNote.setText(operNoteStr);

            if (stoList.size()>0){
                if (testList!=null&&!testList.isEmpty()){
                    setAsset(stoList, testList);
                }else {
                    setAsset(stoList);
                }
            }
        }
    }

    private void initEvent(){
        btnQuit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        addAsset.setOnClickListener(this);
        scanAsset.setOnClickListener(this);
        delAsset.setOnClickListener(this);
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
        operNoteStr = operNote.getText().toString();

        switch (v.getId()){
            case R.id.btnBack:          //返回
                Redirect.redirect(TestUpdActivity.this, TestActivity.class, bundle);
                break;
            case R.id.btnQuit:          //保存
                if (stoList.isEmpty()){
                    Dialog dialog = new AlertDialog.Builder(TestUpdActivity.this)
                            .setTitle("资产检验提示")
                            .setMessage("请选择要转检验的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }

                List<AssetTest> testInfoList = AssetAdapter_Test.testList;
                String barCode = "";
                for (AssetStorage sto: stoList){
                    barCode =  barCode+sto.getBarCode()+",";
                }
                barCode = barCode.substring(0,barCode.length()-1);

                /*把list转换成json*/
                String testListJson = new Gson().toJson(testInfoList);
                String conSql = "operbillCode="+operbillCode+"&operNote="+operNoteStr+"&barCode="+barCode+"&testList="+testListJson;
                update(conSql);
                break;
            case R.id.addAsset:                 //添加资产
                setBundle(bundle);
                Redirect.redirect(TestUpdActivity.this, TestUpdAssetActivity.class, bundle);
                break;
            case R.id.scanAsset:                //扫描资产
//                Toast.makeText(TestAddActivity.this, "扫描资产", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(TestUpdActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                Intent intent = new Intent(TestUpdActivity.this, CaptureActivity.class);
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
                    Dialog dialog = new AlertDialog.Builder(TestUpdActivity.this)
                            .setTitle("资产检验提示")
                            .setMessage("请选择要删除的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    Dialog dialog = new AlertDialog.Builder(TestUpdActivity.this)
                            .setTitle("资产检验提示")
                            .setMessage("是否要删除选择的资产！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for(int i=0;i<stoList.size();i++){
                                        if (stoLists.contains(stoList.get(i))){
                                            if (testList!=null&&testList.size()>0){
                                                for (int j=0; j<testList.size(); j++){
                                                    if (testList.get(j).getBarCode().equals(stoList.get(i).getBarCode())){
                                                        testList.remove(j);
                                                        j--;
                                                    }
                                                }
                                            }
                                            stoList.remove(i);
                                            i--;
                                        }
                                    }
                                    setAsset(stoList, testList);
                                }
                            })
                            .setNegativeButton("取消", null).create();
                    dialog.show();
                }
                break;
        }
    }

    public class IntiDataThread implements Runnable {
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/test/updateInfo"
                        + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCode;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    AssetOperate operate = null;
                    try {
                        JSONObject jsonObject = new JSONObject(info);
                        operate = StrConvertObject.setConvertAssetOperateTest(jsonObject);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if (operate!=null){
                        testCode.setText(operate.getOperbillCode());
                        testList = operate.getList();

                        createIDStr = operate.getUser().getUserName();
                        createDateStr = operate.getCreatedate();
                        operNoteStr = operate.getOperNote();

                        createID.setText(createIDStr);
                        createDate.setText(createDateStr);
                        operNote.setText(operNoteStr);

                        for (AssetTest test: testList){
                            stoList.add(test.getStorage());
                        }
                        setAsset(stoList, testList);
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    /*传递本方法的参数*/
    private void setBundle(Bundle bundle){
        bundle.putString("operbillCode", operbillCode);
        bundle.putString("createID",createIDStr);
        bundle.putString("createDate", createDateStr);
        bundle.putString("operNote", operNoteStr);

        if (stoList.size()>0){
            stoData = new DataBean<>();
            stoData.setData(stoList);
            bundle.putSerializable("stoData", stoData);
        }
        if (testList.size()>0){
            testData = new DataBean<>();
            testData.setData(testList);
            bundle.putSerializable("testData", testData);
        }
    }

    //保存
    private void update(String conSql){
        if (!Redirect.checkNetwork(TestUpdActivity.this)) {
            Toast toast = Toast.makeText(TestUpdActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            /*dialog = new ProgressDialog(TestAddActivity.this);
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
                String path = "http://" + ipStr + "/FixedAssService/test/update";
                String content = "userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.add(path, content);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(TestUpdActivity.this,"检验单修改成功",Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("fragid",fragid);
                        bundle.putSerializable("user",user);
                        Redirect.redirect(TestUpdActivity.this, TestActivity.class, bundle);
                    }else {
                        Toast.makeText(TestUpdActivity.this,"检验单修改失败，请重新修改检验单",Toast.LENGTH_LONG).show();
                    }
                }
            });
//            dialog.dismiss();
        }
    }

    //设置用户选择的资产信息
    private void setAsset(ArrayList<AssetStorage> stoList){
        //建立Adapter绑定数据源
        assetAdapter = new AssetAdapter_Test(stoList,this, fragid, user, this.stoList);
        //绑定Adapter
        testAsset.setAdapter(assetAdapter);
    }
    private void setAsset(ArrayList<AssetStorage> stoList,List<AssetTest> testList){
        //建立Adapter绑定数据源
        assetAdapter = new AssetAdapter_Test(stoList,this, fragid, user, this.stoList, testList);
        //绑定Adapter
        testAsset.setAdapter(assetAdapter);
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
                        if (stoList.size()>0){
                            if (testList!=null&&!testList.isEmpty()){
                                setAsset(stoList,testList);
                            }else {
                                setAsset(stoList);
                            }
                        }
                    }else {
                        Dialog dialog = new AlertDialog.Builder(TestUpdActivity.this)
                                .setTitle("资产检验提示")
                                .setMessage("您扫描的资产编码为："+sto.getBarCode()+"，该资产的资产状态为："+sto.getState())
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }
                }
            });
        }
    }
}
