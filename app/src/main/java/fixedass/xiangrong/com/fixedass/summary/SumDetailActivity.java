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
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

/**
 * @author Eileen
 * @create 2018-11-19
 * @Describe 单据详情
 */
public class SumDetailActivity extends Activity implements View.OnClickListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private User user;
    private TextView title;
    private int fragid;
    private String ipStr;
    private AssetStorage storage;                   //资产

    private ListViewEx lvx, lvxe;
    private static String[] row = {"groupInfo","companyInfo","deptInfo","className","state","extend1"};
    private static String[] rowName = {"所在集团","所在公司","所在部门","资产类别","资产状态","状态数量"};
    private List<Map<String, Object>> infoList = null;
    private static String[] assetRow = {"state","barCode","assName","className","assType","usePeople","useGroup","useCompany","useDept","storeAddress","careMan",
            "belongGroup","assPrice","assUnit","assNum","isOne","assSN","financialCode","oldName","simpName","assBrand","facName","proInfo","buyDate",
            "inDate","bookDate","useDate","brokenTime","depreciation","assSource","addInfo","createPeople","createTime","updatePeople","updateTime"};
    private static String[] assetRowName = {"资产状态","资产编码","资产名称","资产分类","规格型号","使用人","使用集团","使用公司","使用部门","存放地址","保管员",
            "所属公司","价格","单位","数量","一物一码","S/N码","资产凭证号","曾用名","简称","品牌","生产厂商","供应商","购入日期","登记日期",
            "入账日期","使用日期","报废年限","折旧方法","资产来源","备注","创建人","创建时间","更新人","更新时间"};
    private List<Map<String, Object>> detailList = null;

    // 返回主线程更新数据
    private static Handler handler = new Handler();
    private ProgressDialog dialog;  // 创建等待框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum_detail);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User) bundle.getSerializable("user");
        storage = (AssetStorage)bundle.getSerializable("sto");

        /*读取数据*/
        SharedPreferences sharedPreferences = SumDetailActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
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
        title.setText(R.string.sumDetail);

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

        initListViewHead(R.id.list_table_tvhead1, false, assetRowName[0]);
        initListViewHead(R.id.list_table_tvhead2, false, assetRowName[1]);
        initListViewHead(R.id.list_table_tvhead3, false, assetRowName[2]);
        initListViewHead(R.id.list_table_tvhead4, false, assetRowName[3]);
        initListViewHead(R.id.list_table_tvhead5, false, assetRowName[4]);
        initListViewHead(R.id.list_table_tvhead6, false, assetRowName[5]);
        initListViewHead(R.id.list_table_tvhead7, false, assetRowName[6]);
        initListViewHead(R.id.list_table_tvhead8, false, assetRowName[7]);
        initListViewHead(R.id.list_table_tvhead9, false, assetRowName[8]);
        initListViewHead(R.id.list_table_tvhead10, false, assetRowName[9]);
        initListViewHead(R.id.list_table_tvhead11, false, assetRowName[10]);
        initListViewHead(R.id.list_table_tvhead12, false, assetRowName[11]);
        initListViewHead(R.id.list_table_tvhead13, false, assetRowName[12]);
        initListViewHead(R.id.list_table_tvhead14, false, assetRowName[13]);
        initListViewHead(R.id.list_table_tvhead15, false, assetRowName[14]);
        initListViewHead(R.id.list_table_tvhead16, false, assetRowName[15]);
        initListViewHead(R.id.list_table_tvhead17, false, assetRowName[16]);
        initListViewHead(R.id.list_table_tvhead18, false, assetRowName[17]);
        initListViewHead(R.id.list_table_tvhead19, false, assetRowName[18]);
        initListViewHead(R.id.list_table_tvhead20, false, assetRowName[19]);
        initListViewHead(R.id.list_table_tvhead21, false, assetRowName[20]);
        initListViewHead(R.id.list_table_tvhead22, false, assetRowName[21]);
        initListViewHead(R.id.list_table_tvhead23, false, assetRowName[22]);
        initListViewHead(R.id.list_table_tvhead24, false, assetRowName[23]);
        initListViewHead(R.id.list_table_tvhead25, false, assetRowName[24]);
        initListViewHead(R.id.list_table_tvhead26, false, assetRowName[25]);
        initListViewHead(R.id.list_table_tvhead27, false, assetRowName[26]);
        initListViewHead(R.id.list_table_tvhead28, false, assetRowName[27]);
        initListViewHead(R.id.list_table_tvhead29, false, assetRowName[28]);
        initListViewHead(R.id.list_table_tvhead30, false, assetRowName[29]);
        initListViewHead(R.id.list_table_tvhead31, false, assetRowName[30]);
        initListViewHead(R.id.list_table_tvhead32, false, assetRowName[31]);
        initListViewHead(R.id.list_table_tvhead33, false, assetRowName[32]);
        initListViewHead(R.id.list_table_tvhead34, false, assetRowName[33]);
        initListViewHead(R.id.list_table_tvhead35, false, assetRowName[34]);
        lvxe = (ListViewEx)findViewById(R.id.table_lvLedgerList);
        lvxe.inital(R.layout.list_table_account, assetRow, new int[]{
                R.id.list_table_tvhead1,
                R.id.list_table_tvhead2,
                R.id.list_table_tvhead3,
                R.id.list_table_tvhead4,
                R.id.list_table_tvhead5,
                R.id.list_table_tvhead6,
                R.id.list_table_tvhead7,
                R.id.list_table_tvhead8,
                R.id.list_table_tvhead9,
                R.id.list_table_tvhead10,
                R.id.list_table_tvhead11,
                R.id.list_table_tvhead12,
                R.id.list_table_tvhead13,
                R.id.list_table_tvhead14,
                R.id.list_table_tvhead15,
                R.id.list_table_tvhead16,
                R.id.list_table_tvhead17,
                R.id.list_table_tvhead18,
                R.id.list_table_tvhead19,
                R.id.list_table_tvhead20,
                R.id.list_table_tvhead21,
                R.id.list_table_tvhead22,
                R.id.list_table_tvhead23,
                R.id.list_table_tvhead24,
                R.id.list_table_tvhead25,
                R.id.list_table_tvhead26,
                R.id.list_table_tvhead27,
                R.id.list_table_tvhead28,
                R.id.list_table_tvhead29,
                R.id.list_table_tvhead30,
                R.id.list_table_tvhead31,
                R.id.list_table_tvhead32,
                R.id.list_table_tvhead33,
                R.id.list_table_tvhead34,
                R.id.list_table_tvhead35
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
        switch (v.getId()){
            case R.id.btnBack:
                Redirect.redirect(SumDetailActivity.this, SumActivity.class,bundle);
                break;
        }
    }

    private void setDetail(){
        infoList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put(row[0], storage.getUseGroup());
        map.put(row[1], storage.getUseCompany());
        map.put(row[2], storage.getUseDept());
        map.put(row[3], storage.getClassName());
        map.put(row[4], storage.getState());
        map.put(row[5], storage.getExtend1());
        infoList.add(map);
        lvx.add(infoList);

        initDetail();
    }

    private void initDetail(){
        if (!Redirect.checkNetwork(SumDetailActivity.this)) {
            Toast toast = Toast.makeText(SumDetailActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(SumDetailActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MyDetailThread()).start();
        }
    }

    class MyDetailThread implements Runnable{
        String stateStr,useGroup,useCompany,useDept,className;

        @Override
        public void run() {
            try {
                useGroup = URLEncoder.encode(storage.getUseGroup(), "utf-8");
                useCompany = URLEncoder.encode(storage.getUseCompany(), "utf-8");
                useDept = URLEncoder.encode(storage.getUseDept(), "utf-8");
                className = URLEncoder.encode(storage.getClassName(), "utf-8");
                stateStr = URLEncoder.encode(storage.getState(),"utf-8");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                /*所有的数据*/
                String sql = "useGroup="+useGroup+"&useCompany="+useCompany
                        +"&useDept="+useDept+"&className="+className+"&state="+stateStr;
                String path = "http://" + ipStr + "/FixedAssService/sto/selSumDetail"
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
        detailList = new ArrayList<>();
        for (AssetStorage sto: stoList){
            Map<String, Object> map = new HashMap<>();
            map.put(assetRow[0],sto.getState());
            map.put(assetRow[1],sto.getBarCode());
            map.put(assetRow[2],sto.getAssName());
            map.put(assetRow[3],sto.getClassName());
            map.put(assetRow[4],sto.getAssType());
            map.put(assetRow[5],sto.getPeoInfo()==null?sto.getUsePeople():sto.getPeoInfo().getpName());
            map.put(assetRow[6],sto.getGroupInfo()==null?sto.getUseGroup():sto.getGroupInfo().getDeptName());
            map.put(assetRow[7],sto.getCompanyInfo()==null?sto.getUseCompany():sto.getCompanyInfo().getDeptName());
            map.put(assetRow[8],sto.getDeptInfo()==null?sto.getUseDept():sto.getDeptInfo().getDeptName());
            map.put(assetRow[9],sto.getAddressInfo()==null?sto.getStoreAddress():sto.getAddressInfo().getAddrName());
            map.put(assetRow[10],sto.getCareUser()==null?sto.getCareMan():sto.getCareUser().getpName());
            map.put(assetRow[11],sto.getbGroupInfo()==null?sto.getBelongGroup():sto.getbGroupInfo().getDeptName());
            map.put(assetRow[12],sto.getAssPrice());
            map.put(assetRow[13],sto.getAssUnit());
            map.put(assetRow[14],sto.getAssNum());
            map.put(assetRow[15],"是");
            map.put(assetRow[16],sto.getAssSN());
            map.put(assetRow[17],sto.getFinancialCode());
            map.put(assetRow[18],sto.getOldName());
            map.put(assetRow[19],sto.getSimpName());
            map.put(assetRow[20],sto.getAssBrand());
            map.put(assetRow[21],sto.getFacName());
            map.put(assetRow[22],sto.getProInfo()==null?sto.getProvider():sto.getProInfo().getProName());
            map.put(assetRow[23],sto.getBuyDate());
            map.put(assetRow[24],sto.getInDate());
            map.put(assetRow[25],sto.getBookDate());
            map.put(assetRow[26],sto.getUseDate());
            map.put(assetRow[27],sto.getBrokenTime());
            map.put(assetRow[28],sto.getDepreciation());
            map.put(assetRow[29],sto.getAssSource());
            map.put(assetRow[30],sto.getAddInfo());
            map.put(assetRow[31],sto.getCreateInfo()==null?sto.getCreatePeople():sto.getCreateInfo().getUserName());
            map.put(assetRow[32],sto.getCreateTime());
            map.put(assetRow[33],sto.getUpdateInfo()==null?sto.getUpdatePeople():sto.getUpdateInfo().getUserName());
            map.put(assetRow[34],sto.getUpdateTime());

            detailList.add(map);
        }
        lvxe.add(detailList);
    }
}
