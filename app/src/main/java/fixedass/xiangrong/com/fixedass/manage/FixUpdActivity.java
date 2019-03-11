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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.asset.AssetAdapter_Fix;
import fixedass.xiangrong.com.fixedass.asset.FixUpdAssetActivity;
import fixedass.xiangrong.com.fixedass.bean.AssetFix;
import fixedass.xiangrong.com.fixedass.bean.AssetOperate;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.DataBean;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.Convert;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.zxing.android.CaptureActivity;
import fixedass.xiangrong.com.fixedass.zxing.common.Constant;

/*添加资产资产选择显示在界面上，把每个值传给其他两个activity*/
public class FixUpdActivity extends Activity implements View.OnClickListener {
    private ImageView btnQuit,btnBack;
    private TextView title;
    private int fragid, state;
    private String ipStr, operbillCode;
    private User user;

    //维修单号、创建人、创建事件、备注信息
    private EditText fixCode,createID,createDate,operNote;
    private String createIDStr,createDateStr,operNoteStr;                                   //对应每个文本的内容

    private ListView fixAsset;                                                  //维修的资产
    private AssetAdapter_Fix assetAdapter;                                          //适配器

    //添加资产、扫描资产、删除资产
    private TextView addAsset, scanAsset, delAsset;
    // 创建等待框
    private ProgressDialog dialog;
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    /*维修的资产*/
    private DataBean<AssetStorage> stoData;
    private ArrayList<AssetStorage> stoList = new ArrayList<>();
    /*资产的维修信息*/
    private DataBean<AssetFix> fixData;
    private List<AssetFix> fixList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_upd);

        ExitApplication.getInstance().addActivity(this);

        /*读取数据*/
        SharedPreferences sharedPreferences = FixUpdActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");
        operbillCode = bundle.getString("operbillCode");
        state = bundle.getInt("state");

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
        if (bundle.containsKey("fixData")){
            fixData = (DataBean<AssetFix>) bundle.getSerializable("fixData");
            List<AssetFix> fixs = fixData.getData();
            if (fixList==null){
                fixList = new ArrayList<>();
                fixList.addAll(fixs);
                for (AssetFix fix: fixList){
                    stoList.add(fix.getStorage());
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
        title.setText("编辑资产维修单据");

        fixCode = (EditText) findViewById(R.id.fixCode);
        createID = (EditText) findViewById(R.id.createID);
        createDate = (EditText)findViewById(R.id.createDate);
        operNote = (EditText)findViewById(R.id.operNote);

        fixAsset = (ListView)findViewById(R.id.fixAsset);

        addAsset = (TextView) findViewById(R.id.addAsset);
        scanAsset = (TextView) findViewById(R.id.scanAsset);
        delAsset = (TextView) findViewById(R.id.delAsset);
        if (createIDStr==null){
            initData();
        }else {
            fixCode.setText(operbillCode);
            createID.setText(createIDStr);
            createDate.setText(createDateStr);
            operNote.setText(operNoteStr);

            if (stoList.size()>0){
                if (fixList!=null&&!fixList.isEmpty()){
                    setAsset(stoList,fixList);
                }else {
                    setAsset(stoList);
                }
            }
        }

        View includeViewFixUpdate = (View) findViewById(R.id.menu);
        if (state==8){
            includeViewFixUpdate.setVisibility(View.GONE);
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
                Redirect.redirect(FixUpdActivity.this, FixActivity.class, bundle);
                break;
            case R.id.btnQuit:          //保存
                if (stoList.isEmpty()){
                    Dialog dialog = new AlertDialog.Builder(FixUpdActivity.this)
                            .setTitle("资产维修提示")
                            .setMessage("请选择要转维修的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }

                List<AssetFix> fixInfoList = AssetAdapter_Fix.fixList;
                String barCode = "";
                for (AssetStorage sto: stoList){
                    barCode =  barCode+sto.getBarCode()+",";
                }
                barCode = barCode.substring(0,barCode.length()-1);

                /*把list转换成json*/
                String fixListJson = new Gson().toJson(fixInfoList);
                String conSql = "operbillCode="+operbillCode+"&operNote="+operNoteStr+"&barCode="+barCode+"&fixList="+fixListJson;
                update(conSql);
                break;
            case R.id.addAsset:                 //添加资产
                setBundle(bundle);
                Redirect.redirect(FixUpdActivity.this, FixUpdAssetActivity.class, bundle);
                break;
            case R.id.scanAsset:                //扫描资产
//                Toast.makeText(FixAddActivity.this, "扫描资产", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(FixUpdActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                Intent intent = new Intent(FixUpdActivity.this, CaptureActivity.class);
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
                    Dialog dialog = new AlertDialog.Builder(FixUpdActivity.this)
                            .setTitle("资产维修提示")
                            .setMessage("请选择要删除的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    Dialog dialog = new AlertDialog.Builder(FixUpdActivity.this)
                            .setTitle("资产维修提示")
                            .setMessage("是否要删除选择的资产！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for(int i=0;i<stoList.size();i++){
                                        if (stoLists.contains(stoList.get(i))){
                                            if (fixList!=null&&fixList.size()>0){
                                                for (int j=0; j<fixList.size(); j++){
                                                    if (fixList.get(j).getBarCode().equals(stoList.get(i).getBarCode())){
                                                        fixList.remove(j);
                                                        j--;
                                                    }
                                                }
                                            }
                                            stoList.remove(i);
                                            i--;
                                        }
                                    }
                                    setAsset(stoList,fixList);
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
                String path = "http://" + ipStr + "/FixedAssService/fix/updateInfo"
                    + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCode;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    AssetOperate operate = null;
                    try {
                        JSONObject jsonObject = new JSONObject(info);
                        operate = StrConvertObject.setConvertAssetOperateFix(jsonObject);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if (operate!=null){
                        fixCode.setText(operate.getOperbillCode());
                        fixList = operate.getList();

                        createIDStr = operate.getUser().getUserName();
                        createDateStr = operate.getCreatedate();
                        operNoteStr = operate.getOperNote();

                        createID.setText(createIDStr);
                        createDate.setText(createDateStr);
                        operNote.setText(operNoteStr);

                        for (AssetFix fix: fixList){
                            stoList.add(fix.getStorage());
                        }
                        setAsset(stoList, fixList);
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
        if (fixList.size()>0){
            fixData = new DataBean<>();
            fixData.setData(fixList);
            bundle.putSerializable("fixData", fixData);
        }
    }

    //保存
    private void update(String conSql){
        if (!Redirect.checkNetwork(FixUpdActivity.this)) {
            Toast toast = Toast.makeText(FixUpdActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            /*dialog = new ProgressDialog(this);
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
                String path = "http://" + ipStr + "/FixedAssService/fix/update";
                String content = "userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.add(path, content);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(FixUpdActivity.this,"维修单修改成功",Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("fragid",fragid);
                        bundle.putSerializable("user",user);
                        Redirect.redirect(FixUpdActivity.this, FixActivity.class, bundle);
                    }else {
                        Toast.makeText(FixUpdActivity.this,"维修单修改失败，请重新修改维修单",Toast.LENGTH_LONG).show();
                    }
                }
            });
//            dialog.dismiss();
        }
    }

    //设置用户选择的资产信息
    private void setAsset(ArrayList<AssetStorage> stoList){
        //建立Adapter绑定数据源
        assetAdapter = new AssetAdapter_Fix(stoList,this, fragid, user, this.stoList);
        //绑定Adapter
        fixAsset.setAdapter(assetAdapter);
    }
    private void setAsset(ArrayList<AssetStorage> stoList,List<AssetFix> fixList){
        //建立Adapter绑定数据源
        assetAdapter = new AssetAdapter_Fix(stoList,this, fragid, user, this.stoList, fixList);
        //绑定Adapter
        fixAsset.setAdapter(assetAdapter);
    }

    String barCode = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == Convert.REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                barCode = data.getStringExtra(Constant.CODED_CONTENT);
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
                            if (fixList!=null&&!fixList.isEmpty()){
                                setAsset(stoList,fixList);
                            }else {
                                setAsset(stoList);
                            }
                        }
                    }else {
                        Dialog dialog = new AlertDialog.Builder(FixUpdActivity.this)
                                .setTitle("资产维修提示")
                                .setMessage("您扫描的资产编码为："+sto.getBarCode()+"，该资产的资产状态为："+sto.getState())
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }
                }
            });
        }
    }
}
