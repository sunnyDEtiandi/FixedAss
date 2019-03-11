package fixedass.xiangrong.com.fixedass.summary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import fixedass.xiangrong.com.fixedass.tree.DeptActivity;
import fixedass.xiangrong.com.fixedass.tree.DeptFragment;
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

/**
 * @author Eileen
 * @create 2018-11-12
 * @Describe   资产台账
 */
public class LedgerActivity extends Activity implements View.OnClickListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private TextView title/*,setSort,setEdit*/;
    private ImageView setSort,setEdit;
    private EditText barCode;
    private String barCodeStr;

    private String orderBy = "";

    private String[] popContents = new String[]{"编码","编码逆序","创建日期","创建日期逆序","入账日期","入账日期逆序"};
    private EditPopWin editPopWin;
    private String useDeptStr,useDeptUUIDStr, classNameStr,classCodeStr, bookDateStr, bookDate2Str;

    private int fragid;
    private User user;
    private String ipStr;

    // 返回主线程更新数据
    private static Handler handler = new Handler();

    private ListViewEx lvx;
    private static String[] assetRow = {"state","barCode","assName","className","assType","usePeople","useGroup","useCompany","useDept","storeAddress","careMan",
            "belongGroup","assPrice","assUnit","assNum","isOne","assSN","financialCode","oldName","simpName","assBrand","facName","proInfo","buyDate",
            "inDate","bookDate","useDate","brokenTime","depreciation","assSource","addInfo","createPeople","createTime","updatePeople","updateTime"};
    private static String[] assetRowName = {"资产状态","资产编码","资产名称","资产分类","规格型号","使用人","使用集团","使用公司","使用部门","存放地址","保管员",
            "所属公司","价格","单位","数量","一物一码","S/N码","资产凭证号","曾用名","简称","品牌","生产厂商","供应商","购入日期","登记日期",
            "入账日期","使用日期","报废年限","折旧方法","资产来源","备注","创建人","创建时间","更新人","更新时间"};
    private List<Map<String, Object>> infoList = null;

    private ProgressDialog dialog;  // 创建等待框

    private Integer year, month, day, year2, month2, day2;
    private View view;

    @Override
    protected void onStart() {
        super.onStart();

        //获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //设置popupWindow的宽度，这几个数字是根据布局中的textView权重得出的4.2表示这一行的总权重，3.1表示textView的权重
        int with = (int) (((metrics.widthPixels-50) / 2) * 1);
        // 找到需要填充到pop的布局
        view = LayoutInflater.from(this).inflate(R.layout.list_pop, null);
        // 根据此布局建立pop
        final PopupWindow popupWindow = new PopupWindow(view);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setWidth(with);

        //这样设置pop才可以缩回去
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // 填充此布局上的列表
        ListView listView = (ListView) view.findViewById(R.id.pop_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.list_content, popContents);
        listView.setAdapter(adapter);

        // 当listView受到点击时替换mTextView当前显示文本
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                switch (arg2){
                    case 0:
                        orderBy = "barCode";
                        break;
                    case 1:
                        orderBy = "barCode1";
                        break;
                    case 2:
                        orderBy = "createTime";
                        break;
                    case 3:
                        orderBy = "createTime1";
                        break;
                    case 4:
                        orderBy = "bookDate";
                        break;
                    case 5:
                        orderBy = "bookDate1";
                        break;
                    default:
                        orderBy="";
                }
                SharedPreferences ledgerConfig = LedgerActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = ledgerConfig.edit();
                edit.putString("orderBy",orderBy);
                edit.commit();

                String data = "";
                if (bookDateStr!=null&&!bookDateStr.trim().equals("")){
                    data = bookDateStr;
                    if ((bookDate2Str!=null&&!bookDate2Str.trim().equals(""))){
                        data += ","+bookDate2Str;
                    }
                }

                //String sql = "useDept="+useDeptUUIDStr+"&className='"+classNameStr+"'&classCode="+classCodeStr+"&bookDate="+data+"&barCode="+barCodeStr+"&orderBy="+orderBy;
                String sql = "useDept="+useDeptUUIDStr+"&className="+classCodeStr+"&bookDate="+data+"&barCode="+barCodeStr+"&orderBy="+orderBy;
                sel(sql);
                popupWindow.dismiss();
            }
        });
        // 当mTextView受到点击时显示pop
        setSort.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindow.showAsDropDown(v);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger);

        ExitApplication.getInstance().addActivity(this);	//记录所有的Activity

        //editPopWin = new EditPopWin(this,onClickListener);

        /*读取数据*/
        SharedPreferences sharedPreferences = LedgerActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");
        SharedPreferences ledgerConfig = LedgerActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
        if (useDeptStr==null||useDeptStr.trim().equals("")){
            useDeptStr = ledgerConfig.getString("useDept", "");
            useDeptUUIDStr = ledgerConfig.getString("useDeptUUID", "");
        }
        classNameStr = ledgerConfig.getString("className","");
        classCodeStr = ledgerConfig.getString("classCode","");
        bookDateStr = ledgerConfig.getString("bookDate","");
        bookDate2Str = ledgerConfig.getString("bookDate2","");
        orderBy = ledgerConfig.getString("orderBy","createTime1");
        barCodeStr = "";

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User) bundle.getSerializable("user");

        initView();
        initEvent();

        /*if (bundle.containsKey("deptName")){
            useDeptStr = bundle.getString("deptName");
            useDeptUUIDStr = bundle.getString("deptUUID");
            setEdit.performClick();
        }*/
    }

    private void initView(){
        /*btnBack = (Button)this.findViewById(R.id.btnBack);
        btnQuit = (Button)this.findViewById(R.id.btnQuit);*/
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setVisibility(View.GONE);

        title = (TextView)this.findViewById(R.id.title);
        title.setText(R.string.assetLedger);
        /*setSort = (TextView)findViewById(R.id.setSort);
        setEdit = (TextView)findViewById(R.id.setEdit);*/
        setSort = (ImageView)findViewById(R.id.setSort);
        setEdit = (ImageView)findViewById(R.id.setEdit);

        barCode = (EditText) findViewById(R.id.barCode);

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

        lvx = (ListViewEx) findViewById(R.id.lv_table_lvLedgerList);

        lvx.inital(R.layout.list_table_account, assetRow, new int[]{
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

        String data = "";
        if (bookDateStr!=null&&!bookDateStr.trim().equals("")){
            data = bookDateStr;
            if ((bookDate2Str!=null&&!bookDate2Str.trim().equals(""))){
                data += ","+bookDate2Str;
            }
        }
        String sql = "useDept="+useDeptUUIDStr+"&className="+classCodeStr+"&bookDate="+data+"&barCode="+barCodeStr+"&orderBy="+orderBy;
        sel(sql);
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt("fragid",fragid);
            bundle.putSerializable("user", user);
            switch (v.getId()) {
                case R.id.btn_save_pop:
                    useDeptStr = editPopWin.useDept.getText().toString().trim();
                    useDeptUUIDStr = editPopWin.useDeptUUID.getText().toString().trim();
                    classNameStr = editPopWin.className.getText().toString().trim();
                    classCodeStr = editPopWin.classCode.getText().toString().trim();
                    bookDateStr = editPopWin.bookDate.getText().toString().trim();
                    bookDate2Str = editPopWin.bookDate2.getText().toString().trim();

                    System.out.println(useDeptStr+"——"+classNameStr+"——"+bookDateStr+"----"+bookDate2Str);

                    SharedPreferences ledgerConfig = LedgerActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit = ledgerConfig.edit();
                    edit.putString("useDept",useDeptStr);
                    edit.putString("useDeptUUID", useDeptUUIDStr);
                    edit.putString("className",classNameStr);
                    edit.putString("classCode",classCodeStr);
                    edit.putString("bookDate",bookDateStr);
                    edit.putString("bookDate2",bookDate2Str);
                    edit.commit();

                    String data="";
                    if (bookDateStr!=null&&!bookDateStr.trim().equals("")){
                        data = bookDateStr;
                        if ((bookDate2Str!=null&&!bookDate2Str.trim().equals(""))){
                            data += ","+bookDate2Str;
                        }
                    }
                    String sql = "useDept="+useDeptUUIDStr+"&className="+classCodeStr+"&bookDate="+data+"&barCode="+barCodeStr+"&orderBy="+orderBy;
                    sel(sql);

                    editPopWin.dismiss();
                    break;
                case R.id.btn_cancel_pop:
                    editPopWin.useDept.setText("");
                    editPopWin.useDeptUUID.setText("");
                    editPopWin.className.setText("");
                    editPopWin.classCode.setText("");
                    editPopWin.bookDate.setText("");
                    editPopWin.bookDate2.setText("");

                    SharedPreferences ledgerConfig1 = LedgerActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = ledgerConfig1.edit();
                    edit1.putString("useDept","");
                    edit1.putString("useDeptUUID", "");
                    edit1.putString("className","");
                    edit1.putString("classCode","");
                    edit1.putString("bookDate","");
                    edit1.putString("bookDate2","");
                    edit1.commit();

                    String data1="";
                    bookDateStr = editPopWin.bookDate.getText().toString();
                    bookDate2Str = editPopWin.bookDate2.getText().toString();
                    if (bookDateStr!=null&&!bookDateStr.trim().equals("")){
                        data1 = bookDateStr;
                        if ((bookDate2Str!=null&&!bookDate2Str.trim().equals(""))){
                            data1 += ","+bookDate2Str;
                        }
                    }
                    String sql1 = "useDept="+useDeptUUIDStr+"&className="+classNameStr+"&classCode="+classCodeStr+"&bookDate="+data1+"&barCode="+barCodeStr+"&orderBy="+orderBy;
                    sel(sql1);

                    editPopWin.dismiss();
                    break;
                case R.id.bookDate:
                    if (bookDateStr==null||bookDateStr.trim().equals("")){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        bookDateStr = format.format(new Date());
                    }
                    String[] split = bookDateStr.split("-");
                    year = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[1])-1;
                    day = Integer.parseInt(split[2]);

                    Redirect.hideIM(v, LedgerActivity.this);
                    Dialog dialog = new DatePickerDialog(LedgerActivity.this, new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            bookDateStr = year+"-"+(month+1)+"-"+dayOfMonth;
                            editPopWin.bookDate.setText(bookDateStr);
                        }
                    }, year,month,day);
                    dialog.show();

                    SharedPreferences ledgerConfig2 = LedgerActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit2 = ledgerConfig2.edit();
                    edit2.putString("bookDate",bookDateStr);
                    edit2.commit();

                    break;
                case R.id.bookDate2:
                    if (bookDate2Str==null||bookDate2Str.trim().equals("")){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        bookDate2Str = format.format(new Date());
                    }
                    String[] split1 = bookDate2Str.split("-");
                    year2 = Integer.parseInt(split1[0]);
                    month2 = Integer.parseInt(split1[1])-1;
                    day2 = Integer.parseInt(split1[2]);

                    Redirect.hideIM(v, LedgerActivity.this);
                    Dialog dialog1 = new DatePickerDialog(LedgerActivity.this, new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            bookDate2Str = year+"-"+(month+1)+"-"+dayOfMonth;
                            editPopWin.bookDate2.setText(bookDate2Str);
                        }
                    }, year2,month2,day2);
                    dialog1.show();

                    SharedPreferences ledgerConfig3 = LedgerActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit3 = ledgerConfig3.edit();
                    edit3.putString("bookDate2",bookDate2Str);
                    edit3.commit();
                    break;
                case R.id.useDept:
                    Class<?> clazz = DeptFragment.class;
                    Intent i = new Intent(LedgerActivity.this, DeptActivity.class);
                    i.putExtra(DeptActivity.FRAGMENT_PARAM, clazz);
                    i.putExtras(bundle);
                    LedgerActivity.this.startActivity(i);
                    break;
                case R.id.className:
                    break;
            }
        }
    };

    private void initEvent(){
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        setSort.setOnClickListener(this);
        setSort.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    Redirect.hideIM(v,LedgerActivity.this);
                }
                return false;
            }
        });
        setSort.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus==true) {
                    Redirect.hideIM(v,LedgerActivity.this);
                }
            }
        });
        setEdit.setOnClickListener(this);
        setEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus==true) {
                    Redirect.hideIM(v,LedgerActivity.this);
                }
            }
        });
        setEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    Redirect.hideIM(v,LedgerActivity.this);
                }
                return false;
            }
        });
        barCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER  && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Redirect.hideIM(v,LedgerActivity.this);
                    /*barCode.setText(barCode.getText().toString());// 添加这句后实现效果*/
                    barCodeStr = barCode.getText().toString();
                    barCode.selectAll();
                    //根据编码查询资产信息

                    String data = "";
                    if (bookDateStr!=null&&!bookDateStr.trim().equals("")){
                        data = bookDateStr;
                        if ((bookDate2Str!=null&&!bookDate2Str.trim().equals(""))){
                            data += ","+bookDate2Str;
                        }
                    }
                    String sql = "useDept="+useDeptUUIDStr+"&className="+classCodeStr+"&bookDate="+data+"&barCode="+barCodeStr+"&orderBy="+orderBy;
                    sel(sql);

                    return true;
                }
                return false;
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
                cleanConfig();
                Redirect.redirect(LedgerActivity.this, MainActivity.class,bundle);
                break;
            case R.id.btnQuit:
                cleanConfig();
                Redirect.quit(this);
                break;
            case R.id.setEdit:
                //openSelCon(v);
                Redirect.redirect(LedgerActivity.this, LedgerSelActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        cleanConfig();
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        Redirect.redirect(LedgerActivity.this, MainActivity.class,bundle);
    }

    private void openSelCon(View v){
        int[] location = new int[2];
        setEdit.getLocationOnScreen(location);
        int x = location[0];            //540
        int y = location[1];            //433

        editPopWin.useDept.setText(useDeptStr);
        editPopWin.useDeptUUID.setText(useDeptUUIDStr);
        editPopWin.className.setText(classNameStr);
        editPopWin.classCode.setText(classCodeStr);
        editPopWin.bookDate.setText(bookDateStr);
        editPopWin.bookDate2.setText(bookDate2Str);

        editPopWin.showAtLocation(v, Gravity.RIGHT, 0, -80);
    }

    private void sel(String conSql){
        if (!Redirect.checkNetwork(LedgerActivity.this)) {
            Toast toast = Toast.makeText(LedgerActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(LedgerActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MySelThread(conSql)).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class MySelThread implements Runnable {
        private String conSql;

        public MySelThread(String conSql) {
            this.conSql = conSql;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/sto/getSel"
                    + "?userUUID=" + user.getUserUUID()+"&"+conSql;
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
            Map<String, Object> map = new HashMap<>();
            /*"state","barCode","assName","className","assType","usePeople","useGroup","useCompany","useDept","storeAddress","careMan",
"belongGroup","assPrice","assUnit","assNum","isOne","assImg","assSN","financialCode","oldName","simpName","assBrand","facName","proInfo","buyDate",
"inDate","bookDate","useDate","brokenTime","depreciation","assSource","addInfo","createPeople","createTime","updatePeople","updateTime"*/
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

            infoList.add(map);
        }
        lvx.add(infoList);
    }

    /*清除数据*/
    private void cleanConfig(){
        //指定操作的文件名称
        SharedPreferences sh = LedgerActivity.super.getSharedPreferences("ledgerConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();                      //编辑文件
        edit.clear();
        edit.commit();
    }
}
