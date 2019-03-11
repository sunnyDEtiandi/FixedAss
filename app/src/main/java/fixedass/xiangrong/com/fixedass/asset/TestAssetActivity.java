package fixedass.xiangrong.com.fixedass.asset;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.DataBean;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.manage.TestAddActivity;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/*添加资产的资产详情*/
public class TestAssetActivity extends Activity implements View.OnClickListener{
    /*标题栏*/
    /*private Button btnQuit, btnBack;*/
    private ImageView btnBack,btnQuit;
    private TextView title;
    /*资产编码*/
    private EditText barCode;
    private String barCodeValue;

    private int fragid;
    private String ipStr;
    private User user;

    /*资产加载的地方*/
    private ListView loadAssetData;     //加载适配器
    private AssetAdapter assetAdapter;  //适配器
    /*选中的资产*/
    private DataBean<AssetStorage> stoData;
    private ArrayList<AssetStorage> selStoList = new ArrayList<>();

    /*和数据库进行交互*/
    private ProgressDialog dialog;  // 创建等待框
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    //jsonObject转object
    private Gson gson;
    private GsonBuilder builder;

    /*activity数据交互*/
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        ExitApplication.getInstance().addActivity(this);

        /*读取数据*/
        SharedPreferences sharedPreferences = TestAssetActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        Intent intent = getIntent();
        bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");

        if (bundle.containsKey("stoData")){
            stoData = (DataBean<AssetStorage>)bundle.getSerializable("stoData");
            List<AssetStorage> storages = stoData.getData();
            selStoList.addAll(storages);
        }

        initView();
        initEvent();
        getAddAss();
    }

    private void initView(){
        /*btnBack = (Button)findViewById(R.id.btnBack);
        btnQuit = (Button)findViewById(R.id.btnQuit);
        btnQuit.setText("保存");*/

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setImageResource(R.drawable.save);

        title = (TextView)findViewById(R.id.title);
        title.setText("资产选择");
        barCode = (EditText)findViewById(R.id.barCode);
        barCodeValue = barCode.getText().toString();
        loadAssetData = (ListView)findViewById(R.id.loadAssetData);

        //这两句代码必须的，为的是初始化出来gson这个对象，才能拿来用
        builder = new GsonBuilder();
        gson = builder.create();
    }

    private void initEvent(){
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        barCode.setOnKeyListener(returnKey);
    }

    /*和数据库进行交互*/
    private void getAddAss(){
        if (!Redirect.checkNetwork(TestAssetActivity.this)) {
            Toast toast = Toast.makeText(TestAssetActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(TestAssetActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MyGetAddAssThread()).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class MyGetAddAssThread implements Runnable {
        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/sto/getAddSto"
                    + "?deptUUID=" + user.getDeptUUID()+"&barCode="+barCodeValue;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(info);
                        ArrayList<AssetStorage> stoList = new ArrayList<>();
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject object = new JSONObject(jsonArray.get(i).toString());
                            AssetStorage sto = gson.fromJson(object.toString(), AssetStorage.class);
                            stoList.add(sto);
                        }

                        setStoList(stoList);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    /*设置资产信息*/
    private void setStoList(ArrayList<AssetStorage> stoList){
        //建立Adapter绑定数据源
        assetAdapter = new AssetAdapter(stoList, this, fragid, user, this.selStoList);
        //绑定Adapter
        loadAssetData.setAdapter(assetAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                Redirect.redirect(TestAssetActivity.this, TestAddActivity.class, bundle);
                break;
            case R.id.btnQuit:      //保存
                HashMap<Integer, Boolean> selCheck = assetAdapter.selCheck;
                List<AssetStorage> stoLists = new ArrayList<>();
                for(int j = 0; j< assetAdapter.getCount(); j++) {
                    if (selCheck.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetStorage sto = (AssetStorage) assetAdapter.getItem(j);
                        stoLists.add(sto);
                    }
                }
                if(stoLists.size()==0){
                    Dialog dialog = new AlertDialog.Builder(TestAssetActivity.this)
                            .setTitle("资产维修提示")
                            .setMessage("请选择要维修的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    stoData = new DataBean<>();
                    stoData.setData(stoLists);
                    bundle.putSerializable("stoData", stoData);

                    System.out.println("=========stoDate==="+stoData);

                    Redirect.redirect(TestAssetActivity.this, TestAddActivity.class,bundle);
                }
                break;
        }
    }
    View.OnKeyListener returnKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                Redirect.hideIM(v,TestAssetActivity.this);
                /*barCode.setText(barCode.getText().toString());// 添加这句后实现效果*/
                barCode.selectAll();
                barCodeValue = barCode.getText().toString();

                /*查询*/
                getAddAss();

                //根据编码查询资产信息
                return true;
            }
            return false;
        }
    };
}
