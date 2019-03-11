package fixedass.xiangrong.com.fixedass.count;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetCountBill;
import fixedass.xiangrong.com.fixedass.bean.AssetCountDetail;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

/**
 * @author Eileen
 * @create 2018-11-19
 * @Describe 单据详情
 */
public class ResultDetailActivity extends Activity implements View.OnClickListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private User user;
    private TextView title;
    private int fragid;
    private String ipStr;
    private AssetCountBill bill;                   //盘点单

    private ListViewEx lvx, lvxe;
    private static String[] row = {"countBillCode","cPeoInfo","createDate","countNote","countResult"};
    private static String[] rowName = {"盘点单号","创建人","创建时间","盘点备注","盘点结果"};
    private List<Map<String, Object>> infoList = null;
    private static String[] rowDetail = {"barCode","assName","className","companyInfo","departmentInfo","placeInfo","countState"};
    private static String[] rowDetailName = {"资产编码","资产名称","资产分类","盘点公司","盘点部门","盘点地址","盘点状态"};
    private List<Map<String, Object>> detailList = null;

    private Spinner countState;
    private Integer[] stateInt = {4,1,0,2};
    private static String[] states = {"所有","已盘点","未盘点","盘错"};
    private int countStateInt = 4;

    // 返回主线程更新数据
    private static Handler handler = new Handler();
    private ProgressDialog dialog;  // 创建等待框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User) bundle.getSerializable("user");
        bill = (AssetCountBill) bundle.getSerializable("bill");

        /*读取数据*/
        SharedPreferences sharedPreferences = ResultDetailActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
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
        title.setText(R.string.countResultDetail);

        lvx = (ListViewEx)findViewById(R.id.lv_table_lvLedgerList);
        initListViewHead(R.id.list_table_tvhead1, false, rowName[0]);
        initListViewHead(R.id.list_table_tvhead2, false, rowName[1]);
        initListViewHead(R.id.list_table_tvhead3, false, rowName[2]);
        initListViewHead(R.id.list_table_tvhead4, false, rowName[3]);
        initListViewHead(R.id.list_table_tvhead5, false, rowName[4]);
        lvx.inital(R.layout.list_table_result, row, new int[]{
                R.id.list_table_tvhead1,
                R.id.list_table_tvhead2,
                R.id.list_table_tvhead3,
                R.id.list_table_tvhead4,
                R.id.list_table_tvhead5
        });

        initListViewHead(R.id.tv_list_table_tvhead1, false, rowDetailName[0]);
        initListViewHead(R.id.tv_list_table_tvhead2, false, rowDetailName[1]);
        initListViewHead(R.id.tv_list_table_tvhead3, false, rowDetailName[2]);
        initListViewHead(R.id.tv_list_table_tvhead4, false, rowDetailName[3]);
        initListViewHead(R.id.tv_list_table_tvhead5, false, rowDetailName[4]);
        initListViewHead(R.id.tv_list_table_tvhead6, false, rowDetailName[5]);
        initListViewHead(R.id.tv_list_table_tvhead7, false, rowDetailName[6]);

        lvxe = (ListViewEx)findViewById(R.id.table_lvLedgerList);
        lvxe.inital(R.layout.list_table_count_detail, rowDetail, new int[]{
                R.id.tv_list_table_tvhead1,
                R.id.tv_list_table_tvhead2,
                R.id.tv_list_table_tvhead3,
                R.id.tv_list_table_tvhead4,
                R.id.tv_list_table_tvhead5,
                R.id.tv_list_table_tvhead6,
                R.id.tv_list_table_tvhead7
        });

        countState = (Spinner)findViewById(R.id.countState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countState.setAdapter(adapter);
    }

    private void initEvent(){
        btnBack.setOnClickListener(this);

        countState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countStateInt = stateInt[position];
                setDetail();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                Redirect.redirect(ResultDetailActivity.this, ResultActivity.class,bundle);
                break;
        }
    }

    private void setDetail(){
        infoList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put(row[0], bill.getCountBillCode());
        map.put(row[1], bill.getCreatePeople());
        map.put(row[2], bill.getCreateDate());
        map.put(row[3], bill.getCountNote());
        map.put(row[4], bill.getSysUUID());
        infoList.add(map);
        lvx.add(infoList);

        initDetail();
    }

    private void initDetail(){
        if (!Redirect.checkNetwork(ResultDetailActivity.this)) {
            Toast toast = Toast.makeText(ResultDetailActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(ResultDetailActivity.this);
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
                String path = "http://" + ipStr + "/FixedAssService/count/selDetailResult"
                    + "?userUUID=" + user.getUserUUID()+"&countBillCode="+bill.getCountBillCode()+"&countState="+countStateInt;

                String info = WebService.executeHttpGet(path);
                ArrayList<AssetCountDetail> detailLists = StrConvertObject.strConvertCountDetail(info);
                @Override
                public void run() {
                    detailList = new ArrayList<>();
                    for (AssetCountDetail detail: detailLists){
                        Map<String, Object> map = new HashMap();
                        map.put(rowDetail[0],detail.getBarCode() );
                        map.put(rowDetail[1],detail.getStorage().getAssName());
                        map.put(rowDetail[2], detail.getStorage().getClassName());
                        map.put(rowDetail[3],detail.getCompanyInfo()==null?"":detail.getCompanyInfo().getDeptName());
                        map.put(rowDetail[4], detail.getDepartmentInfo()==null?"":detail.getDepartmentInfo().getDeptName());
                        map.put(rowDetail[5], detail.getPlaceInfo()==null?"":detail.getPlaceInfo().getAddrName());
                        int countState = detail.getCountState();
                        String stateStr = "";
                        switch (countState){
                            case 0:
                                stateStr = "未盘点";
                                break;
                            case 1:
                                stateStr = "已盘点";
                                break;
                            case 2:
                                stateStr = "修改盘点";
                                break;
                            case 3:
                                stateStr = "新增盘点";
                                break;
                        }
                        map.put(rowDetail[6],stateStr);
                        detailList.add(map);
                    }
                    lvxe.add(detailList);
                    dialog.dismiss();
                }
            });
        }
    }
}
