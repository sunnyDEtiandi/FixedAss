package fixedass.xiangrong.com.fixedass.manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.UserManagerCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetBorrow;
import fixedass.xiangrong.com.fixedass.bean.AssetBorrowQuery;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

/**
 * @author Eileen
 * @create 2018/6/12
 * @Describe 借用单详情
 */
public class BorrowListActivity extends Activity implements View.OnClickListener {
    /*private Button btnQuit,btnBack;*/
    private ImageView btnQuit,btnBack;
    private TextView title;
    private ListViewEx lvx;

    /*private static String BARCODE = "barCode";                  //资产编码
    private static String ASSNAME = "assName";                  //资产名称
    private static String STATE = "state";                      //资产状态
    private static String BORROWPEOPLE = "borrowPeople";        //借用人
    private static String BORROWDATE = "borrowDate";            //借用日期
    private static String BORROWDAYS = "borrowDays";            //借用时长
    private static String BORROWINFO = "borrowInfo";            //借用原因
    private static String RETURNDATE = "returnDate";            //归还日期
    //private static String USEGROUP = "useGroup";                //使用集团
    private static String USECOMPANY = "useCompany";           //使用公司
    private static String USEDEPT = "useDept";                  //使用部门
    private static String USEPEOPLE = "usePeople";              //使用人
    private static String CAREMAN = "careMan";                  //保管员
    private static String STOREADDRESS = "storeAddress";        //地址*/

    private static String[] row = {"barCode","assName","state","borrowPeople","borrowDate","borrowDays",
                                    "borrowInfo","returnDate","useGroup","useCompany","useDept","usePeople","careMan","storeAddress"};
    private static String[] rowName = {"资产编码","资产名称","资产状态","借用人","借用日期","借用时长","借用原因","归还日期","使用集团",
                                    "使用公司","使用部门","使用人","保管员","存放地址"};



    private List<Map<String, Object>> infoList = null;
    private AssetBorrowQuery assetBorrowQuery;
    private int fragid;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_list);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assetBorrowQuery = (AssetBorrowQuery)bundle.getSerializable("assetBorrowQuery");

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
        title.setText(R.string.assetBorrowList);

        //初始化表格
        initListViewHead(R.id.tv_list_table_borrow_tvhead1, false, rowName[0]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead2, false, rowName[1]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead3, false, rowName[2]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead4, false, rowName[3]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead5, false, rowName[4]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead6, false, rowName[5]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead7, false, rowName[6]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead8, false, rowName[7]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead9, false, rowName[8]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead10, false, rowName[9]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead11, false, rowName[10]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead12, false, rowName[11]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead13, false, rowName[12]);
        initListViewHead(R.id.tv_list_table_borrow_tvhead14, false, rowName[13]);

        lvx = (ListViewEx) this.findViewById(R.id.lv_table_lvBorrowList);

        lvx.inital(R.layout.list_table_borrow, row, new int[] {
                R.id.tv_list_table_borrow_tvhead1,
                R.id.tv_list_table_borrow_tvhead2,
                R.id.tv_list_table_borrow_tvhead3,
                R.id.tv_list_table_borrow_tvhead4,
                R.id.tv_list_table_borrow_tvhead5,
                R.id.tv_list_table_borrow_tvhead6,
                R.id.tv_list_table_borrow_tvhead7,
                R.id.tv_list_table_borrow_tvhead8,
                R.id.tv_list_table_borrow_tvhead9,
                R.id.tv_list_table_borrow_tvhead10,
                R.id.tv_list_table_borrow_tvhead11,
                R.id.tv_list_table_borrow_tvhead12,
                R.id.tv_list_table_borrow_tvhead13
                ,R.id.tv_list_table_borrow_tvhead14
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
                Redirect.redirect(BorrowListActivity.this, BorrowActivity.class, bundle);
                break;
            case R.id.btnQuit:
                Redirect.quit(BorrowListActivity.this);
                break;
        }
    }

    /**
     * 初始化content数据
     */
    private void initContentDataList() {
        infoList = new ArrayList<Map<String, Object>>();
        if(assetBorrowQuery!=null){
            List<AssetBorrow> borrowList = assetBorrowQuery.getAssetOperate().getList();
            for (int i=0;i<borrowList.size();i++) {
                AssetBorrow assetBorrow = borrowList.get(i);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put(row[0], assetBorrow.getBarCode());
                AssetStorage storage = assetBorrow.getStorage();
                map.put(row[1], storage.getAssName());
                Integer isReturn = assetBorrow.getIsReturn();
                String str = "";
                switch (isReturn){
                    case -1:
                        str = "已删除";
                        break;
                    case 0:
                        str = "未报审";
                        break;
                    case 1:
                        str = "审核中";
                        break;
                    case 2:
                        str = "未通过";
                        break;
                    case 3:
                        str = "审核通过";
                        break;
                    case 4:
                        str = "借用中";
                        break;
                    case 5:
                        str = "已归还";
                        break;
                }
                map.put(row[2], str);
                map.put(row[3], assetBorrow.getBorrowPeople());
                map.put(row[4], assetBorrow.getBorrowDate());
                map.put(row[5], assetBorrow.getBorrowDays());
                map.put(row[6], assetBorrow.getBorrowInfo());
                map.put(row[7], assetBorrow.getReturnDate());
                map.put(row[8], storage.getUseGroup());
                map.put(row[9], storage.getUseCompany());
                map.put(row[10], storage.getUseDept());
                map.put(row[11], storage.getUsePeople());
                map.put(row[12], storage.getCareMan());
                map.put(row[13], storage.getStoreAddress());


                infoList.add(map);
            }
            lvx.add(infoList);
        }
    }
}
