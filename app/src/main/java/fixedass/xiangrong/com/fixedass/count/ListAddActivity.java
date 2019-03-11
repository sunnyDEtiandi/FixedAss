package fixedass.xiangrong.com.fixedass.count;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.tree.ClassActivity;
import fixedass.xiangrong.com.fixedass.tree.ClassListActivity;
import fixedass.xiangrong.com.fixedass.tree.ClassListFragment;
import fixedass.xiangrong.com.fixedass.tree.DeptListActivity;
import fixedass.xiangrong.com.fixedass.tree.DeptListFragment;
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

public class ListAddActivity extends Activity implements View.OnClickListener {
    private int fragid;
    private User user;
    private String ipStr;

    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack, btnQuit;
    private TextView title;

    private EditText dept,deptUUID,className,classCode,countNote,createPeople;
    private String deptStr,deptUUIDStr,classNameStr,classCodeStr,countNoteStr;

    private ListViewEx lvx;
    private List<Map<String, Object>> infoList = null;
    /*private static String[] row = {"barCode","assName","className","groupInfo","companyInfo","deptInfo","peoInfo","careUser","addressInfo"};
    private static String[] rowName = {"资产编码","资产名称","资产分类","使用集团","使用公司","使用部门","使用人","保管员","地址"};*/
    private static String[] row = {"barCode","assName","className","companyInfo","deptInfo","peoInfo","careUser","addressInfo"};
    private static String[] rowName = {"资产编码","资产名称","资产分类","使用公司","使用部门","使用人","保管员","地址"};

    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");

        /*读取数据*/
        SharedPreferences sharedPreferences = ListAddActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        if (bundle.containsKey("deptName")){
            deptStr = bundle.getString("deptName");
            deptUUIDStr = bundle.getString("deptUUID");

            SharedPreferences listConfig = ListAddActivity.super.getSharedPreferences("listConfig", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = listConfig.edit();
            edit.putString("useDept",deptStr);
            edit.putString("useDeptUUID", deptUUIDStr);
            edit.commit();
        }
        if (bundle.containsKey("className")){
            classNameStr = bundle.getString("className");
            classCodeStr = bundle.getString("classCode");
            SharedPreferences listConfig = ListAddActivity.super.getSharedPreferences("listConfig", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = listConfig.edit();
            edit.putString("className",classNameStr);
            edit.putString("classCode",classCodeStr);
            edit.commit();
        }

        SharedPreferences listConfig = ListAddActivity.super.getSharedPreferences("listConfig", Activity.MODE_PRIVATE);
        if (deptStr==null||deptStr.trim().equals("")){
            deptStr = listConfig.getString("useDept", "");
            deptUUIDStr = listConfig.getString("useDeptUUID", "");
        }
        if (classCodeStr==null||classCodeStr.trim().equals("")){
            classNameStr = listConfig.getString("className","");
            classCodeStr = listConfig.getString("classCode","");
        }
        countNoteStr = listConfig.getString("countNote", "");

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
        title.setText("新建盘点单");

        dept = (EditText)findViewById(R.id.dept);
        dept.setText(deptStr);
        deptUUID = (EditText)findViewById(R.id.deptUUID);
        deptUUID.setText(deptUUIDStr);
        className = (EditText)findViewById(R.id.className);
        className.setText(classNameStr);
        classCode = (EditText)findViewById(R.id.classCode);
        classCode.setText(classCodeStr);
        countNote = (EditText)findViewById(R.id.countNote);
        countNote.setText(countNoteStr);
        createPeople = (EditText)findViewById(R.id.createPeople);
        createPeople.setText(user.getUserName());

        initListViewHead(R.id.tv_list_table_test_tvhead1, false, rowName[0]);
        initListViewHead(R.id.tv_list_table_test_tvhead2, false, rowName[1]);
        initListViewHead(R.id.tv_list_table_test_tvhead3, false, rowName[2]);
        initListViewHead(R.id.tv_list_table_test_tvhead4, false, rowName[3]);
        initListViewHead(R.id.tv_list_table_test_tvhead5, false, rowName[4]);
        initListViewHead(R.id.tv_list_table_test_tvhead6, false, rowName[5]);
        initListViewHead(R.id.tv_list_table_test_tvhead7, false, rowName[6]);
        initListViewHead(R.id.tv_list_table_test_tvhead8, false, rowName[7]);
        //initListViewHead(R.id.tv_list_table_test_tvhead9, false, rowName[8]);

        lvx = (ListViewEx)findViewById(R.id.table_lvLedgerList);
        lvx.inital(R.layout.list_table_addcount, row, new int[]{
                R.id.tv_list_table_test_tvhead1,
                R.id.tv_list_table_test_tvhead2,
                R.id.tv_list_table_test_tvhead3,
                R.id.tv_list_table_test_tvhead4,
                R.id.tv_list_table_test_tvhead5,
                R.id.tv_list_table_test_tvhead6,
                R.id.tv_list_table_test_tvhead7,
                R.id.tv_list_table_test_tvhead8
                //,R.id.tv_list_table_test_tvhead9
        });

        initData();
    }

    private void initEvent(){
        btnQuit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        dept.setOnClickListener(this);
        className.setOnClickListener(this);
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
                SharedPreferences listConfig = ListAddActivity.super.getSharedPreferences("listConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = listConfig.edit();
                edit.putString("useDept","");
                edit.putString("useDeptUUID", "");
                edit.putString("className","");
                edit.putString("classCode","");
                edit.putString("countNote","");
                edit.commit();

                Redirect.redirect(this, ListActivity.class, bundle);
                break;
            case R.id.btnQuit:                                                                      //保存
                save();
                break;
            case R.id.dept:                                                                         //部门名称
                countNoteStr = countNote.getText().toString();
                SharedPreferences listConfig1 = ListAddActivity.super.getSharedPreferences("listConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit1 = listConfig1.edit();
                edit1.putString("countNote",countNoteStr);
                edit1.commit();

                Class<?> clazz = DeptListFragment.class;
                Intent i = new Intent(ListAddActivity.this, DeptListActivity.class);
                i.putExtra(DeptListActivity.FRAGMENT_PARAM, clazz);
                i.putExtras(bundle);
                ListAddActivity.this.startActivity(i);
                break;
            case R.id.className:                                                                    //资产分类
                countNoteStr = countNote.getText().toString();
                SharedPreferences listConfig2 = ListAddActivity.super.getSharedPreferences("listConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit2 = listConfig2.edit();
                edit2.putString("countNote",countNoteStr);
                edit2.commit();

                Class<?> clazz1 = ClassListFragment.class;
                Intent intent = new Intent(ListAddActivity.this, ClassListActivity.class);
                intent.putExtra(ClassActivity.FRAGMENT_PARAM,clazz1);
                intent.putExtras(bundle);
                ListAddActivity.this.startActivity(intent);
                break;
        }
    }

    private void initData(){
        if (!Redirect.checkNetwork(ListAddActivity.this)) {
            Toast toast = Toast.makeText(ListAddActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            String sql = "deptUUID="+deptUUIDStr+"&classCode="+classCodeStr;
            // 创建子线程，分别进行Get和Post传输
            new Thread(new InitDataThread(sql)).start();
        }
    }
    class InitDataThread implements Runnable{
        String sql;

        public InitDataThread(String sql) {
            this.sql = sql;
        }

        @Override
        public void run() {
            String path = "http://" + ipStr + "/FixedAssService/count/countSto";
            path = path + "?userUUID=" + user.getUserUUID()+"&"+sql;

            String info = WebService.executeHttpGet(path);
            final ArrayList<AssetStorage> stoList = StrConvertObject.strConvertSto(info);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    infoList = new ArrayList<>();
                    for (AssetStorage sto: stoList){
                        Map<String, Object> map = new HashMap<>();
                        map.put(row[0], sto.getBarCode());
                        map.put(row[1], sto.getAssName());
                        map.put(row[2], sto.getClassName());
                        /*map.put(row[3], sto.getGroupInfo()==null?"":sto.getGroupInfo().getDeptName());
                        map.put(row[4], sto.getCompanyInfo()==null?"":sto.getCompanyInfo().getDeptName());
                        map.put(row[5], sto.getDeptInfo()==null?"":sto.getDeptInfo().getDeptName());
                        map.put(row[6], sto.getPeoInfo()==null?"":sto.getPeoInfo().getpName());
                        map.put(row[7], sto.getCareUser()==null?"":sto.getCareUser().getpName());
                        map.put(row[8], sto.getAddressInfo()==null?"":sto.getAddressInfo().getAddrName());*/

                        map.put(row[3], sto.getCompanyInfo()==null?"":sto.getCompanyInfo().getDeptName());
                        map.put(row[4], sto.getDeptInfo()==null?"":sto.getDeptInfo().getDeptName());
                        map.put(row[5], sto.getPeoInfo()==null?"":sto.getPeoInfo().getpName());
                        map.put(row[6], sto.getCareUser()==null?"":sto.getCareUser().getpName());
                        map.put(row[7], sto.getAddressInfo()==null?"":sto.getAddressInfo().getAddrName());

                        infoList.add(map);
                    }
                    lvx.add(infoList);
                }
            });
        }
    }

//    新增
    private void save(){
        if (!Redirect.checkNetwork(ListAddActivity.this)) {
            Toast toast = Toast.makeText(ListAddActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            countNoteStr = countNote.getText().toString();
            try {
                countNoteStr = URLEncoder.encode(countNoteStr,"utf-8");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            String sql = "deptUUID="+deptUUIDStr+"&classCode="+classCodeStr+"&countNote="+countNoteStr;

            // 创建子线程，分别进行Get和Post传输
            new Thread(new MySaveThread(sql)).start();
        }
    }
    class MySaveThread implements Runnable {
        String sql;

        public MySaveThread(String sql) {
            this.sql = sql;
        }

        @Override
        public void run() {
            String path = "http://" + ipStr + "/FixedAssService/count/add";
            path = path + "?userUUID=" + user.getUserUUID()+"&"+sql;

            final String info = WebService.executeHttpGet(path);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(ListAddActivity.this,"盘点单创建成功",Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("fragid",fragid);
                        bundle.putSerializable("user",user);
                        Redirect.redirect(ListAddActivity.this, ListActivity.class, bundle);
                    }else {
                        Toast.makeText(ListAddActivity.this,"盘点单创建失败，请重新创建盘点单",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
