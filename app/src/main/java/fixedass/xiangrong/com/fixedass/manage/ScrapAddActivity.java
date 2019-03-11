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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.asset.AssetAdapter_Scrap;
import fixedass.xiangrong.com.fixedass.asset.ScrapAssetActivity;
import fixedass.xiangrong.com.fixedass.bean.AssetScrap;
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

/*资产报废新增*/
/*添加资产资产选择显示在界面上，把每个值传给其他两个activity*/
public class ScrapAddActivity extends Activity implements View.OnClickListener {
    /*private Button btnQuit,btnBack;*/
    private ImageView btnQuit,btnBack;
    private TextView title;
    private int fragid;
    private String ipStr;
    private User user;

    //创建人、创建事件、备注信息
    private EditText createID,createDate,operNote;
    private String createIDUUID, createIDName;                                  //创建人的uuid、创建人的名称
    private String createDateStr,operNoteStr;                                   //对应每个文本的内容
    private Integer year,month,day;

    private ListView scrapAsset;                                                  //报废的资产
    private AssetAdapter_Scrap assetAdapter;                                          //适配器

    //添加资产、扫描资产、删除资产
    private TextView addAsset, scanAsset, delAsset;
    // 创建等待框
//    private ProgressDialog dialog;
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    /*报废的资产*/
    private DataBean<AssetStorage> stoData;
    private ArrayList<AssetStorage> stoList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_add);

        ExitApplication.getInstance().addActivity(this);

        /*读取数据*/
        SharedPreferences sharedPreferences = ScrapAddActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");

        createIDUUID = user.getUserUUID();
        createIDName = user.getUserName();
        createDateStr = bundle.getString("createDate");
        operNoteStr = bundle.getString("operNote");

        if (bundle.containsKey("stoData")){
            stoData = (DataBean<AssetStorage>) bundle.getSerializable("stoData");
            List<AssetStorage> asList = stoData.getData();
            stoList.addAll(asList);
        }

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
        title.setText("新建资产报废单");

        createID = (EditText) findViewById(R.id.createID);
        createID.setText(createIDName);

        createDate = (EditText)findViewById(R.id.createDate);
        if (createDateStr==""||createDateStr==null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());
            createDateStr = df.format(curDate);
        }
        createDate.setText(createDateStr);

        String[] split = createDateStr.split("-");
        year = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1])-1;
        day = Integer.parseInt(split[2]);

        operNote = (EditText)findViewById(R.id.operNote);
        operNote.setText(operNoteStr);

        scrapAsset = (ListView)findViewById(R.id.scrapAsset);
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

        createDate.setOnClickListener(this);

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
        createDateStr = createDate.getText().toString();
        operNoteStr = operNote.getText().toString();

        switch (v.getId()){
            case R.id.btnBack:          //返回
                Redirect.redirect(ScrapAddActivity.this, ScrapActivity.class, bundle);
                break;
            case R.id.btnQuit:          //保存
                if (stoList.size()<1){
                    Dialog dialog = new AlertDialog.Builder(ScrapAddActivity.this)
                            .setTitle("资产报废提示")
                            .setMessage("请选择要转报废的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }

                List<AssetScrap> scrapInfoList = AssetAdapter_Scrap.scrapList;
                System.out.println("========="+scrapInfoList.size());

                String barCode = "";
                for (AssetStorage sto: stoList){
                    barCode =  barCode+sto.getBarCode()+",";
                }
                barCode = barCode.substring(0,barCode.length()-1);

                /*把list转换成json*/
                String scrapListJson = new Gson().toJson(scrapInfoList);
                String conSql = "createDate="+createDateStr+"&operNote="+operNoteStr+"&barCode="+barCode+"&scrapList="+scrapListJson;
                Log.i("conSql",conSql);
                save(conSql);
                break;
            case R.id.addAsset:                 //添加资产
                setBundle(bundle);
                Redirect.redirect(ScrapAddActivity.this, ScrapAssetActivity.class, bundle);
                Redirect.hideIM(v, ScrapAddActivity.this);
                break;
            case R.id.scanAsset:                //扫描资产
//                Toast.makeText(ScrapAddActivity.this, "扫描资产", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(ScrapAddActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                Intent intent = new Intent(ScrapAddActivity.this, CaptureActivity.class);
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
                    Dialog dialog = new AlertDialog.Builder(ScrapAddActivity.this)
                            .setTitle("资产报废提示")
                            .setMessage("请选择要删除的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    Dialog dialog = new AlertDialog.Builder(ScrapAddActivity.this)
                            .setTitle("资产报废提示")
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
            case R.id.createDate:
                Redirect.hideIM(v, ScrapAddActivity.this);
                Dialog dialog = new DatePickerDialog(ScrapAddActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year+"-"+(month+1)+"-"+dayOfMonth;
                        createDate.setText(date);
                    }
                }, year,month,day);
                dialog.show();
                break;
        }
    }

    /*传递本方法的参数*/
    private void setBundle(Bundle bundle){
        bundle.putString("createDate", createDateStr);
        bundle.putString("operNote", operNoteStr);

        if (stoList.size()>0){
            bundle.putSerializable("stoData", stoData);
        }
    }

    //保存
    private void save(String conSql){
        if (!Redirect.checkNetwork(ScrapAddActivity.this)) {
            Toast toast = Toast.makeText(ScrapAddActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            /*dialog = new ProgressDialog(ScrapAddActivity.this);
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
                String path = "http://" + ipStr + "/FixedAssService/scrap/add";
                String content = "userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.add(path, content);
                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(ScrapAddActivity.this,"报废单创建成功",Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("fragid",fragid);
                        bundle.putSerializable("user",user);
                        Redirect.redirect(ScrapAddActivity.this, ScrapActivity.class, bundle);
                    }else {
                        Toast.makeText(ScrapAddActivity.this,"报废单创建失败，请重新创建报废单",Toast.LENGTH_LONG).show();
                    }
                }
            });
//            dialog.dismiss();
        }
    }

    //设置用户选择的资产信息
    private void setAsset(ArrayList<AssetStorage> stoList){
        //建立Adapter绑定数据源
        assetAdapter = new AssetAdapter_Scrap(stoList,this, fragid, user, this.stoList);
        //绑定Adapter
        scrapAsset.setAdapter(assetAdapter);
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
                        Dialog dialog = new AlertDialog.Builder(ScrapAddActivity.this)
                                .setTitle("资产报废提示")
                                .setMessage("您扫描的资产编码为："+sto.getBarCode()+"，该资产的资产状态为："+sto.getState())
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }
                }
            });
        }
    }
}
