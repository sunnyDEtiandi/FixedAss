package fixedass.xiangrong.com.fixedass.summary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fixedass.xiangrong.com.fixedass.MainActivity;
import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.asset.InquireAdapter;
import fixedass.xiangrong.com.fixedass.bean.AssetRecord;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

/**
  * @author Eileen
  * @create 2018-11-16
  * @Describe 资产查询--查询资产的基本数据以及资产的流程
  */
public class InquireActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private User user;
    private TextView title;
    private EditText barCode;
    private String barCodeStr;
    private int fragid;
    private String ipStr;

    private ListView loadAssetData;     //加载适配器
    private InquireAdapter adapter;

    private HorizontalScrollView hs_ledger_hslist;
    private ListViewEx lvx;
    private static String[] row = {"operbillCode","operType","createUser","createdate","note"};
    private static String[] rowName = {"单号","单据类型","创建人","创建日期","处理结果"};
    private List<Map<String, Object>> infoList = null;

     // 返回主线程更新数据
     private static Handler handler = new Handler();
     private ProgressDialog dialog;  // 创建等待框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User) bundle.getSerializable("user");
        if (bundle.containsKey("barCode")){
            barCodeStr = bundle.getString("barCode");
        }

        /*读取数据*/
        SharedPreferences sharedPreferences = InquireActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        initView();
        initEvent();
    }

    private void initView(){
        /*btnBack = (Button)this.findViewById(R.id.btnBack);
        btnQuit = (Button)this.findViewById(R.id.btnQuit);*/

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setVisibility(View.GONE);

        title = (TextView)this.findViewById(R.id.title);
        title.setText(R.string.assetInquire);
        barCode = (EditText)findViewById(R.id.barCode);

        loadAssetData = (ListView)findViewById(R.id.loadAssetData);

        hs_ledger_hslist = (HorizontalScrollView)findViewById(R.id.hs_ledger_hslist);
        initListViewHead(R.id.tv_list_table_tvhead1, false, rowName[0]);
        initListViewHead(R.id.tv_list_table_tvhead2, false, rowName[1]);
        initListViewHead(R.id.tv_list_table_tvhead3, false, rowName[2]);
        initListViewHead(R.id.tv_list_table_tvhead4, false, rowName[3]);
        initListViewHead(R.id.tv_list_table_tvhead5, false, rowName[4]);

        lvx = (ListViewEx)findViewById(R.id.lv_table_lvLedgerList);

        lvx.inital(R.layout.list_table_inquire, row, new int[]{
                R.id.tv_list_table_tvhead1,
                R.id.tv_list_table_tvhead2,
                R.id.tv_list_table_tvhead3,
                R.id.tv_list_table_tvhead4,
                R.id.tv_list_table_tvhead5
        });

        if (barCodeStr!=null&&!barCodeStr.trim().equals("")){
            barCode.setText(barCodeStr);
            selBarCode();
        }
    }

    private void initEvent(){
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        barCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER  && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Redirect.hideIM(v,InquireActivity.this);
                    /*barCode.setText(barCode.getText().toString());// 添加这句后实现效果*/
                    barCode.selectAll();
                    //根据编码查询资产信息
                    barCodeStr = barCode.getText().toString();
                    selBarCode();
                    return true;
                }
                return false;
            }
        });

        lvx.setOnItemClickListener(this);
    }

    int iListHead = 0;
    private void initListViewHead(int id, boolean isCheckBox, String text) {
        if (iListHead == 0) {
            iListHead = getResources().getColor(R.color.list_head);
        }

        if (!isCheckBox) {
            TextView view = (TextView) findViewById(id);
            view.setText(text);
            view.setBackgroundColor(iListHead);
            view.setTextAppearance(this, R.style.ListHeadText);
            view.setGravity(Gravity.CENTER);
        } else {
            CheckBox view = (CheckBox) findViewById(id);
            view.setText(text);
            view.setBackgroundColor(iListHead);
            view.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);
        switch (v.getId()){
            case R.id.btnBack:
                Redirect.redirect(InquireActivity.this, MainActivity.class,bundle);
                break;
            case R.id.btnQuit:
                Redirect.quit(InquireActivity.this);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAtPosition = parent.getItemAtPosition(position);
        Map<String, Object> map = (Map<String, Object>)itemAtPosition;
        AssetRecord record = new AssetRecord((String) map.get("operType"), (String) map.get("createUser"),(String)  map.get("createdate"),
                            (String) map.get("note"),(String) map.get("operbillCode"),barCodeStr);
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);
        bundle.putSerializable("record",record);

        Redirect.redirect(this,InquireDetailActivity.class, bundle);
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        Redirect.redirect(InquireActivity.this, MainActivity.class,bundle);
    }

//    根据资产编码查询资产履历
    private void selBarCode(){
        if (!Redirect.checkNetwork(InquireActivity.this)) {
            Toast toast = Toast.makeText(InquireActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(InquireActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MySelThread()).start();
        }
    }

    // 子线程接收数据，主线程修改数据
    public class MySelThread implements Runnable {
        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/sto/selBarCode"
                    + "?userUUID=" + user.getUserUUID()+"&barCode="+barCodeStr;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    System.out.println("========info======="+info);
                    try {
                        JSONArray jsonArray = new JSONArray(info);
                        JSONArray stoArray = jsonArray.getJSONArray(0);
                        ArrayList<AssetStorage> stoList = new ArrayList<>();
                        for (int i=0;i<stoArray.length();i++){
                            JSONObject stoObject = new JSONObject(stoArray.get(i).toString());
                            AssetStorage sto = new Gson().fromJson(stoObject.toString(), AssetStorage.class);
                            stoList.add(sto);
                        }

                        JSONArray recordArray = jsonArray.getJSONArray(1);
                        List<AssetRecord> recordList = new ArrayList<>();
                        for (int i=0; i<recordArray.length(); i++){
                            JSONObject recordObject = new JSONObject(recordArray.get(i).toString());
                            AssetRecord record = new Gson().fromJson(recordObject.toString(), AssetRecord.class);
                            recordList.add(record);
                        }
                        System.out.println("=============="+recordList+"========="+recordList);
                        initData(stoList,recordList);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }finally {
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    //讲履历展示在界面上
    private void initData(ArrayList<AssetStorage> stoList,List<AssetRecord> recordList){
        /*资产信息部分*/
        adapter = new InquireAdapter(stoList, this);
        loadAssetData.setAdapter(adapter);

        /*流水*/
        if (recordList.size()>0){
            hs_ledger_hslist.setVisibility(View.VISIBLE);
            infoList = new ArrayList<>();
            for (AssetRecord record:recordList){
                Map<String, Object> map = new HashMap<>();
                map.put(row[0], record.getOperbillCode());
                String operType = record.getOperType();
                switch(operType){
                    case "borrow":
                        operType = "借用单";
                        break;
                    case "change":
                        operType = "转移单";
                        break;
                    case "fix":
                        operType = "维修单";
                        break;
                    case "check":
                        operType = "检验单";
                        break;
                    case "scrap":
                        operType = "报废单";
                        break;
                }
                map.put(row[1],operType);
                map.put(row[2],record.getCreateUser());
                map.put(row[3],record.getCreatedate());
                map.put(row[4],record.getNote());
                infoList.add(map);
            }
            lvx.add(infoList);
        }else {
            hs_ledger_hslist.setVisibility(View.GONE);
        }
    }
}
