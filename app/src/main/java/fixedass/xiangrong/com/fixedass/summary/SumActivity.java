package fixedass.xiangrong.com.fixedass.summary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fixedass.xiangrong.com.fixedass.MainActivity;
import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

/**
 * @author Eileen
 * @create 2018-11-29
 * @Describe 资产统计
 */
public class SumActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private TextView title;
    private User user;

    private Spinner state;
    private String stateStr;

    //查询条件
    private String useDeptStr,useDeptUUIDStr, classNameStr,classCodeStr, useDateStr, useDate2Str;

    private Button selBtn;

    private int fragid;
    private String ipStr;

    private ListViewEx lvx;
    private static String[] row = {"groupInfo","companyInfo","deptInfo","className","state","extend1"};
    private static String[] rowName = {"所在集团","所在公司","所在部门","资产类别","资产状态","状态数量"};
    private List<Map<String, Object>> infoList = null;

    // 返回主线程更新数据
    private static Handler handler = new Handler();
    private ProgressDialog dialog;  // 创建等待框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");

        /*读取数据*/
        SharedPreferences sharedPreferences = SumActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");
        SharedPreferences sumConfig = SumActivity.super.getSharedPreferences("sumConfig", Activity.MODE_PRIVATE);
        if (useDeptStr==null||useDeptStr.trim().equals("")){
            useDeptStr = sumConfig.getString("useDept", "");
            useDeptUUIDStr = sumConfig.getString("useDeptUUID", "");
        }
        classNameStr = sumConfig.getString("className","");
        classCodeStr = sumConfig.getString("classCode","");
        useDateStr = sumConfig.getString("useDate","");
        useDate2Str = sumConfig.getString("useDate2","");

        initView();
        initEvent();
        setState();
    }

    private void initView(){
        /*btnBack = (Button)this.findViewById(R.id.btnBack);
        btnQuit = (Button)this.findViewById(R.id.btnQuit);*/
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setVisibility(View.GONE);

        title = (TextView)this.findViewById(R.id.title);
        title.setText(R.string.assetSum);

        state = (Spinner)findViewById(R.id.state);

        initListViewHead(R.id.tv_list_table_tvhead1, false, rowName[0]);
        initListViewHead(R.id.tv_list_table_tvhead2, false, rowName[1]);
        initListViewHead(R.id.tv_list_table_tvhead3, false, rowName[2]);
        initListViewHead(R.id.tv_list_table_tvhead4, false, rowName[3]);
        initListViewHead(R.id.tv_list_table_tvhead5, false, rowName[4]);
        initListViewHead(R.id.tv_list_table_tvhead6, false, rowName[5]);

        lvx = (ListViewEx)findViewById(R.id.lv_table_lvLedgerList);

        lvx.inital(R.layout.list_table_sum, row, new int[]{
                R.id.tv_list_table_tvhead1,
                R.id.tv_list_table_tvhead2,
                R.id.tv_list_table_tvhead3,
                R.id.tv_list_table_tvhead4,
                R.id.tv_list_table_tvhead5,
                R.id.tv_list_table_tvhead6
        });

        selBtn = (Button)findViewById(R.id.sel);
    }

    private void initEvent(){
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        selBtn.setOnClickListener(this);
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

    private void setState(){
        // 第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
        List<String> stateList = new ArrayList<>();
        stateList.add("所有");
        stateList.add("领用");
        stateList.add("借用");
        stateList.add("转移");
        stateList.add("维修");
        stateList.add("检验");
        stateList.add("报废");
        stateList.add("损坏");

        //  建立Adapter绑定数据源
        // 第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stateList);
        // 第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 第四步：将适配器添加到下拉列表上
        state.setAdapter(adapter);
        // 第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateStr = adapter.getItem(position);
                String stateStrCode = "";
                //将utf-8格式的中文转换成URL编码
                try {
                    stateStrCode = URLEncoder.encode(stateStr,"utf-8");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                String data = "";
                if (useDateStr!=null&&!useDateStr.trim().equals("")){
                    data = useDateStr;
                    if ((useDate2Str!=null&&!useDate2Str.trim().equals(""))){
                        data += ","+useDate2Str;
                    }
                }
                String sql = "useDept="+useDeptUUIDStr+"&classCode="+classCodeStr+"&useDate="+data+"&state="+stateStrCode;
                setSumState(sql);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        switch (v.getId()){
            case R.id.btnBack:
                Redirect.redirect(SumActivity.this, MainActivity.class,bundle);
                break;
            case R.id.btnQuit:
                Redirect.quit(SumActivity.this);
                break;
            case R.id.sel:
                Redirect.redirect(SumActivity.this,SumSelActivity.class,bundle);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAtPosition = parent.getItemAtPosition(position);
        Map<String, Object> map = (Map<String, Object>)itemAtPosition;
        AssetStorage sto = new AssetStorage((String)map.get("className"),(String)map.get("state"),(String)map.get("groupInfo"),
                (String)map.get("companyInfo"),(String)map.get("deptInfo"),(String)map.get("extend1"));

        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);
        bundle.putSerializable("sto",sto);

        Redirect.redirect(this, SumDetailActivity.class, bundle);
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        Redirect.redirect(SumActivity.this, MainActivity.class,bundle);
    }

    private void setSumState(String sql){
        if (!Redirect.checkNetwork(SumActivity.this)) {
            Toast toast = Toast.makeText(SumActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(SumActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MySumThread(sql)).start();
        }
    }
    class MySumThread implements Runnable{
        String sql;

        public MySumThread(String sql) {
            this.sql = sql;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/sto/getSumCount"
                    + "?userUUID=" + user.getUserUUID()+"&"+sql;
                String info = WebService.executeHttpGet(path);
                ArrayList<AssetStorage> stoList = StrConvertObject.strConvertSto(info);

                @Override
                public void run() {
                    initData(stoList);
                    dialog.dismiss();
                }
            });
        }
    }
    private void initData(List<AssetStorage> stoList){
        infoList = new ArrayList<>();
        for (AssetStorage sto: stoList){
            System.out.println("===========sto.getExtend1()==="+sto.getExtend1());
            Map<String, Object> map = new HashMap<>();
            map.put(row[0],sto.getGroupInfo()==null?"":sto.getGroupInfo().getDeptName());
            map.put(row[1],sto.getCompanyInfo()==null?"":sto.getCompanyInfo().getDeptName());
            map.put(row[2],sto.getDeptInfo()==null?"":sto.getDeptInfo().getDeptName());
            map.put(row[3],sto.getClassName());
            map.put(row[4],sto.getState());
            map.put(row[5],sto.getExtend1());
            infoList.add(map);
        }
        lvx.add(infoList);
    }
}
