package fixedass.xiangrong.com.fixedass.manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetFix;
import fixedass.xiangrong.com.fixedass.bean.AssetFixQuery;
import fixedass.xiangrong.com.fixedass.bean.AssetScrap;
import fixedass.xiangrong.com.fixedass.bean.AssetScrapQuery;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

/**
 * @author Eileen
 * @create 2018/10/9
 * @Describe 报废单详情
 */
public class ScrapListActivity extends Activity implements View.OnClickListener {
    /*private Button btnQuit,btnBack;*/
    private ImageView btnQuit,btnBack;
    private TextView title;
    private ListViewEx lvx;

    //资产编码，资产名称，资产状态，使用集团，使用公司，使用部门，使用人，保管员，地址，维修单位、维修人、维修电话、维修费用、维修日期、故障描述、备注信息
    /*private static String BARCODE = "barCode";                  //资产编码
    private static String ASSNAME = "assName";                  //资产名称
    private static String STATE = "state";                      //资产状态
    private static String USEGROUP = "useGroup";                //使用集团
    private static String USECOMPANY = "useCompany";            //使用公司
    private static String USEDEPT = "useDept";                  //使用部门
    private static String USEPEOPLE = "usePeople";              //使用人
    private static String CAREMAN = "careMan";                  //保管员
    private static String USEPLACE = "usePlace";                //地址
    private static String SCRAPDATE = "scrapDate";              //报废日期
    private static String UNUSEINFO = "unuseInfo";              //报废原因
    private static String UNUSELISTNOTE = "unuseListNote";      //备注信息*/

    private static String[] row = {"barCode","assName","state","useGroup","useCompany","useDept","usePeople","careMan","usePlace","scrapDate","unuseInfo","unuseListNote"};
    private static String[] rowName = {"资产编码","资产名称","资产状态","使用集团",
            "使用公司","使用部门","使用人","保管员","存放地址","报废日期","报废原因","备注信息"};

    private List<Map<String, Object>> infoList = null;
    private AssetScrapQuery scrapQuery;
    private int fragid;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_list);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        scrapQuery = (AssetScrapQuery)bundle.getSerializable("scrapQuery");
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");

        initView();
        initEvent();
        initContentDataList();
    }

    private void initView(){
        /*btnBack = (Button)findViewById(R.id.btnBack);
        btnQuit = (Button)findViewById(R.id.btnQuit);*/

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setVisibility(View.GONE);

        title = (TextView)findViewById(R.id.title);
        title.setText(R.string.assetScrapList);

        //初始化表格
        //资产编码，资产名称，资产状态，使用集团，使用公司，使用部门，使用人，保管员，地址，维修单位、维修人、维修电话、维修费用、维修日期、故障描述、备注信息
        initListViewHead(R.id.tv_list_table_scrap_tvhead1, false, rowName[0]);
        initListViewHead(R.id.tv_list_table_scrap_tvhead2, false, rowName[1]);
        initListViewHead(R.id.tv_list_table_scrap_tvhead3, false, rowName[2]);
        initListViewHead(R.id.tv_list_table_scrap_tvhead4, false, rowName[3]);
        initListViewHead(R.id.tv_list_table_scrap_tvhead5, false, rowName[4]);
        initListViewHead(R.id.tv_list_table_scrap_tvhead6, false, rowName[5]);
        initListViewHead(R.id.tv_list_table_scrap_tvhead7, false, rowName[6]);
        initListViewHead(R.id.tv_list_table_scrap_tvhead8, false, rowName[7]);
        initListViewHead(R.id.tv_list_table_scrap_tvhead9, false, rowName[8]);
        initListViewHead(R.id.tv_list_table_scrap_tvhead10, false, rowName[9]);
        initListViewHead(R.id.tv_list_table_scrap_tvhead11, false, rowName[10]);
        initListViewHead(R.id.tv_list_table_scrap_tvhead12, false, rowName[11]);

        lvx = (ListViewEx) this.findViewById(R.id.lv_table_lvScrapList);

        lvx.inital(R.layout.list_table_scrap, row,
            new int[] {
                R.id.tv_list_table_scrap_tvhead1,
                R.id.tv_list_table_scrap_tvhead2,
                R.id.tv_list_table_scrap_tvhead3,
                R.id.tv_list_table_scrap_tvhead4,
                R.id.tv_list_table_scrap_tvhead5,
                R.id.tv_list_table_scrap_tvhead6,
                R.id.tv_list_table_scrap_tvhead7,
                R.id.tv_list_table_scrap_tvhead8,
                R.id.tv_list_table_scrap_tvhead9,
                R.id.tv_list_table_scrap_tvhead10,
                R.id.tv_list_table_scrap_tvhead11
                ,R.id.tv_list_table_scrap_tvhead12
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

    private void initEvent() {
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);
        switch (v.getId()) {
            case R.id.btnBack:
                Redirect.redirect(ScrapListActivity.this, ScrapActivity.class, bundle);
                break;
            case R.id.btnQuit:
                Redirect.quit(ScrapListActivity.this);
                break;
        }
    }

    /**
     * 初始化content数据
     */
    private void initContentDataList() {
        infoList = new ArrayList<Map<String, Object>>();
        if(scrapQuery!=null){


            List<AssetScrap> scrapList = scrapQuery.getAssetOperate().getList();
            for (int i=0;i<scrapList.size();i++) {
                AssetScrap assetScrap = scrapList.get(i);
                //资产编码，资产名称，资产状态，使用集团，使用公司，使用部门，使用人，保管员，地址，维修单位、维修人、维修电话、维修费用、维修日期、故障描述、备注信息
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(row[0], assetScrap.getBarCode());
                AssetStorage storage = assetScrap.getStorage();
                map.put(row[1], storage.getAssName());

                String str="";
                switch (scrapQuery.getAssetOperate().getState()){
                    case -1: str="已删除";    break;
                    case 0: str="未报审";    break;
                    case 1: str="审核中";    break;
                    case 2: str="未通过";    break;
                    case 3: str="审核通过";    break;
                    case 7: str="已完成";    break;
                    case 8: str="导入待报审";    break;
                    case 9: str="导入待审核";    break;
                    case 10: str="导入未通过";    break;
                    case 11: str="导入新增";    break;
                }
                map.put(row[2], str);

                map.put(row[3], storage.getGroupInfo()==null?"":storage.getGroupInfo().getDeptName());
                map.put(row[4], storage.getCompanyInfo()==null?"":storage.getCompanyInfo().getDeptName());
                map.put(row[5], storage.getDeptInfo()==null?"":storage.getDeptInfo().getDeptName());
                map.put(row[6], storage.getUsePeopleEntity()==null?"":storage.getUsePeopleEntity().getpName());
                map.put(row[7], storage.getCareUser()==null?"":storage.getCareUser().getpName());
                map.put(row[8], storage.getAddress()==null?"":storage.getAddress().getAddrName());
                map.put(row[9], assetScrap.getScrapDate());
                map.put(row[10], assetScrap.getUnuseInfo());
                map.put(row[11], assetScrap.getUnuseListNote());


                infoList.add(map);
            }
            lvx.add(infoList);
        }
    }
}
