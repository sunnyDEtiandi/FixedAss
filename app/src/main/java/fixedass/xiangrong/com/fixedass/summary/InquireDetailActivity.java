package fixedass.xiangrong.com.fixedass.summary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetRecord;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

/**
 * @author Eileen
 * @create 2018-11-19
 * @Describe 单据详情
 */
public class InquireDetailActivity extends Activity implements View.OnClickListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private User user;
    private TextView title;
    private int fragid;
    private String ipStr;
    private AssetRecord record;                   //资产
    private String barCodeStr;

    private ListViewEx lvx, lvxe;
    private static String[] row = {"operbillCode","operType","createUser","createdate","note"};
    private static String[] rowName = {"单号","单据类型","创建人","创建日期","处理结果"};
    private List<Map<String, Object>> infoList = null;
    private static String[] rowDetail = {"num","detailStr"};
    private static String[] rowDetailName = {"序号","内容"};
    private List<Map<String, Object>> detailList = null;

    // 返回主线程更新数据
    private static Handler handler = new Handler();
    private ProgressDialog dialog;  // 创建等待框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire_detail);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User) bundle.getSerializable("user");
        record = (AssetRecord) bundle.getSerializable("record");
        barCodeStr = record.getBarCode();

        /*读取数据*/
        SharedPreferences sharedPreferences = InquireDetailActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
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
        title.setText(R.string.inquireDetail);

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

        initListViewHead(R.id.tv_list_table_detail1, false, rowDetailName[0]);
        initListViewHead(R.id.tv_list_table_detail2, false, rowDetailName[1]);
        lvxe = (ListViewEx)findViewById(R.id.table_lvLedgerList);
        lvxe.inital(R.layout.list_table_inquire_detail, rowDetail, new int[]{
                R.id.tv_list_table_detail1,
                R.id.tv_list_table_detail2
        });

        setDetail();
    }

    private void initEvent(){
        btnBack.setOnClickListener(this);
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
        bundle.putString("barCode", barCodeStr);
        switch (v.getId()){
            case R.id.btnBack:
                Redirect.redirect(InquireDetailActivity.this, InquireActivity.class,bundle);
                break;
        }
    }

    private void setDetail(){
        infoList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put(row[0], record.getOperbillCode());
        map.put(row[1], record.getOperType());
        map.put(row[2], record.getCreateUser());
        map.put(row[3], record.getCreatedate());
        map.put(row[4], record.getNote());
        infoList.add(map);
        lvx.add(infoList);

        initDetail();
    }

    private void initDetail(){
        if (!Redirect.checkNetwork(InquireDetailActivity.this)) {
            Toast toast = Toast.makeText(InquireDetailActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(InquireDetailActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MyDetailThread()).start();
        }
    }
    class MyDetailThread implements Runnable{
        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/sto/selDetail"
                    + "?userUUID=" + user.getUserUUID()+"&barCode="+barCodeStr+"&operbillCode="+record.getOperbillCode();
                String info = WebService.executeHttpGet(path);

                //ArrayList<AssetStorage> stoList = StrConvertObject.strConvertSto(info);
                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(info);
                        ArrayList<String> stoList = new ArrayList<>();
                        for (int i=0;i<jsonArray.length();i++){
                            String str = jsonArray.get(i).toString();
                            stoList.add(str);
                        }

                        detailList = new ArrayList<>();
                        for (int i=0; i<stoList.size(); i++){
                            Map<String, Object> map = new HashMap();
                            map.put("num", i+1);
                            map.put("detailStr", stoList.get(i));

                            detailList.add(map);
                        }
                        lvxe.add(detailList);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }finally {
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}
