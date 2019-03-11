package fixedass.xiangrong.com.fixedass.count;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fixedass.xiangrong.com.fixedass.MainActivity;
import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetCountBill;
import fixedass.xiangrong.com.fixedass.bean.CountSel;
import fixedass.xiangrong.com.fixedass.bean.Function;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.manage.BorrowEditPopWin;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.CheckboxAdapter;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

/**
 * @author Eileen
 * @create 2018-11-20
 * @Describe 盘点单
 */
public class ListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private TextView title/*,setSort,setEdit*/;
    private ImageView setSort,setEdit;

    private int fragid;
    private User user;

    private String ipStr;

    private static Handler handler = new Handler();

//    查询条件
    private String[] popContents = new String[]{"创建时间","创建时间逆序"};       //排序
    private BorrowEditPopWin editPopWin;
    private String countBillCode, createDate, createDate1, orderBy="",createPeople,countNote;                                                //筛选

    private int year,month,day,year1,month1,day1;

    private List<CountSel> infoList;
    private ListViewEx lvx;
    private static String[] row = {"isCheck","countBillCode","cPeoInfo","createDate","countNote"};
    private static String[] rowName = {"选择","盘点单号","创建人","创建时间","盘点备注"};
    private CheckboxAdapter checkboxAdapter;                //适配器

    //悬浮菜单
    private FloatingActionButton listMenu;    //悬浮菜单
    private Boolean isOpen = false;
    private RelativeLayout rlAddBill;
    private int[] llId = new int[]{R.id.ll01,R.id.ll02};
    private LinearLayout[] ll = new LinearLayout[llId.length];
    private int[] fabId = new int[]{R.id.miniFab01,R.id.miniFab02};
    private FloatingActionButton[] fab = new FloatingActionButton[fabId.length];

    private Button selAll, /*selNoAll,*/ invSel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User) bundle.getSerializable("user");

        /*读取数据*/
        SharedPreferences sharedPreferences = ListActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");
        SharedPreferences listConfig = ListActivity.super.getSharedPreferences("listConfig", Activity.MODE_PRIVATE);
        countBillCode = listConfig.getString("countBillCode", "");
        createDate = listConfig.getString("createDate", "");
        createDate1 = listConfig.getString("createDate1", "");
        createPeople = listConfig.getString("createPeople", "");
        countNote = listConfig.getString("countNote","");

        initView();
        initEvent();
        bindEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //设置popupWindow的宽度，这几个数字是根据布局中的textView权重得出的4.2表示这一行的总权重，3.1表示textView的权重
        int with = (int) (((metrics.widthPixels-50) / 2) * 1);
        // 找到需要填充到pop的布局
        View view = LayoutInflater.from(this).inflate(R.layout.list_pop, null);
        // 根据此布局建立pop
        final PopupWindow popupWindow = new PopupWindow(view);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(with);

        //这样设置pop才可以缩回去
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // 填充此布局上的列表
        ListView listView = (ListView) view.findViewById(R.id.pop_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_content, popContents);
        listView.setAdapter(adapter);

        // 当listView受到点击时替换mTextView当前显示文本
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch (arg2){
                    case 0:
                        orderBy = "createDate";
                        break;
                    case 1:
                        orderBy = "createDate1";
                        break;
                }
                selCon();
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

    private void initView(){
        /*btnBack = (Button)findViewById(R.id.btnBack);
        btnQuit = (Button)findViewById(R.id.btnQuit);*/
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setVisibility(View.GONE);

        title = (TextView)findViewById(R.id.title);
        title.setText(R.string.countList);

        /*setSort = (TextView)findViewById(R.id.setSort);
        setEdit = (TextView)findViewById(R.id.setEdit);*/
        setSort = (ImageView)findViewById(R.id.setSort);
        setEdit = (ImageView)findViewById(R.id.setEdit);
        editPopWin = new BorrowEditPopWin(this,onClickListener);

        listMenu = (FloatingActionButton)findViewById(R.id.listMenu);
        rlAddBill = (RelativeLayout)findViewById(R.id.rlAddBill);
        for (int i = 0; i < llId.length;i++){
            ll[i] = (LinearLayout)findViewById(llId[i]);
        }
        for (int i = 0;i < fabId.length; i++){
            fab[i] = (FloatingActionButton)findViewById(fabId[i]);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String curDateStr = df.format(curDate);
        String[] split = curDateStr.split("-");
        if (createDate!=null&&!createDate.trim().equals("")){
            split = createDate.split("-");
        }
        year = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1])-1;
        day = Integer.parseInt(split[2]);
        String[] split1 = curDateStr.split("-");
        if (createDate1!=null&&!createDate1.trim().equals("")){
            split1 = createDate1.split("-");
        }
        year1 = Integer.parseInt(split1[0]);
        month1 = Integer.parseInt(split1[1]);
        day1 = Integer.parseInt(split1[2]);

        lvx = (ListViewEx)findViewById(R.id.lv_table_lvLedgerList);
        initListViewHead(R.id.list_table_tvhead1, true, rowName[0]);
        initListViewHead(R.id.list_table_tvhead2, false, rowName[1]);
        initListViewHead(R.id.list_table_tvhead3, false, rowName[2]);
        initListViewHead(R.id.list_table_tvhead4, false, rowName[3]);
        initListViewHead(R.id.list_table_tvhead5, false, rowName[4]);
        lvx.inital(R.layout.list_table_lists, row, new int[]{
                R.id.list_table_tvhead1,
                R.id.list_table_tvhead2,
                R.id.list_table_tvhead3,
                R.id.list_table_tvhead4,
                R.id.list_table_tvhead5
        });

        selCon();

        selAll = (Button)findViewById(R.id.selAll);
        /*selNoAll = (Button)findViewById(R.id.selNoAll);*/
        invSel = (Button)findViewById(R.id.invSel);
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
            switch (v.getId()) {
                case R.id.btn_save_pop:
                    countBillCode = editPopWin.borrowDepatment.getText().toString().trim();
                    createDate = editPopWin.borrowDate.getText().toString().trim();
                    createDate1 = editPopWin.borrowDate1.getText().toString().trim();

                    //指定操作的文件名称
                    SharedPreferences sh = ListActivity.super.getSharedPreferences("listConfig", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sh.edit();                      //编辑文件
                    edit.putString("countBillCode", countBillCode);          //保存
                    edit.putString("createDate", createDate);
                    edit.putString("createDate1", createDate1);
                    edit.commit();

                    selCon();
                    editPopWin.dismiss();
                    break;
                case R.id.btn_cancel_pop:
                    editPopWin.borrowDepatment.setText("");
                    editPopWin.borrowDate.setText("");
                    editPopWin.borrowDate1.setText("");
                    cleanConfig();

                    countBillCode = editPopWin.borrowDepatment.getText().toString().trim();
                    createDate = editPopWin.borrowDate.getText().toString().trim();
                    createDate1 = editPopWin.borrowDate1.getText().toString().trim();
                    editPopWin.dismiss();
                    selCon();
                    break;
                case R.id.borrowDate:
                    Redirect.hideIM(v, ListActivity.this);
                    Dialog dialog = new DatePickerDialog(ListActivity.this, new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String date = year+"-"+(month+1)+"-"+dayOfMonth;
                            editPopWin.borrowDate.setText(date);
                        }
                    }, year,month,day);
                    dialog.show();
                    break;
                case R.id.borrowDate1:
                    Redirect.hideIM(v, ListActivity.this);
                    Dialog dialog1 = new DatePickerDialog(ListActivity.this, new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String date = year+"-"+(month+1)+"-"+dayOfMonth;
                            editPopWin.borrowDate1.setText(date);
                        }
                    }, year1,month1,day1);
                    dialog1.show();
                    break;
            }
        }
    };

    private void initEvent(){
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        setSort.setOnClickListener(this);
        setEdit.setOnClickListener(this);

        lvx.setOnItemClickListener(this);

        selAll.setOnClickListener(this);
        /*selNoAll.setOnClickListener(this);*/
        invSel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        switch (v.getId()){
            case R.id.btnBack:
                cleanConfig();
                Redirect.redirect(ListActivity.this, MainActivity.class,bundle);
                break;
            case R.id.btnQuit:
                cleanConfig();
                Redirect.quit(ListActivity.this);
                break;
            case R.id.setEdit:
                int[] location = new int[2];
                setEdit.getLocationOnScreen(location);
                int x = location[0];            //540
                int y = location[1];            //433

                editPopWin.query1.setText("盘点单号：");
                editPopWin.query2.setText("创建时间：");
                editPopWin.borrowDepatment.setText(countBillCode);
                editPopWin.borrowDate.setText(createDate);
                editPopWin.borrowDate1.setText(createDate1);
                editPopWin.showAtLocation(v, Gravity.LEFT, x, -(y%100+117));
                break;
            case R.id.listMenu:
                int icon = isOpen? android.R.drawable.ic_dialog_dialer:android.R.drawable.ic_menu_close_clear_cancel;
                listMenu.setImageResource(icon);
                isOpen = !isOpen;
                rlAddBill.setVisibility(isOpen ? View.VISIBLE : View.GONE);
                break;
            case R.id.miniFab01:                                            //新增
                Redirect.redirect(this, ListAddActivity.class, bundle);
                hideFABMenu();
                break;
            case R.id.miniFab02:
                hideFABMenu();//删除
                delete();
                break;
            case R.id.selAll:
                selAll(v);
                break;
            /*case R.id.selNoAll:
                selNoAll(v);
                break;*/
            case R.id.invSel:
                invSel(v);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        cleanConfig();
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        Redirect.redirect(ListActivity.this, MainActivity.class,bundle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
        AssetCountBill bill = new AssetCountBill((String)map.get("countBillCode"),(String)map.get("cPeoInfo"),(String)map.get("createDate"),(String) map.get("countNote"));

        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);
        bundle.putSerializable("bill", bill);

        Redirect.redirect(this,ListDetailActivity.class, bundle);
    }

    private void bindEvents(){
        listMenu.setOnClickListener(this);
        for (int i = 0;i < fabId.length; i++){
            fab[i].setOnClickListener(this);
        }
    }

    private void hideFABMenu(){
        rlAddBill.setVisibility(View.GONE);
        listMenu.setImageResource(android.R.drawable.ic_dialog_dialer);
        isOpen = false;
    }

    //查询数据
    private void selCon(){
        if (!Redirect.checkNetwork(ListActivity.this)) {
            Toast toast = Toast.makeText(ListActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            /*dialog = new ProgressDialog(ListActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("1111正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();*/
            // 创建子线程，分别进行Get和Post传输
            String data = "";
            if (createDate!=null&&!createDate.trim().equals("")){
                data = createDate;
                if ((createDate1!=null&&!createDate1.trim().equals(""))){
                    data += ","+createDate1;
                }
            }

            try {
                countNote = URLEncoder.encode(countNote,"utf-8");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

            String sql = "countBillCode="+countBillCode+"&createDate="+data+"&createPeople="+createPeople+"&countNote="+countNote+"&orderBy="+orderBy;
            new Thread(new MyCountThread(sql)).start();
        }
    }
    class MyCountThread implements Runnable{
        private String sql;

        public MyCountThread(String sql) {
            this.sql = sql;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/count/countSel"
                    + "?userUUID=" + user.getUserUUID()+"&"+sql;
                String info = WebService.executeHttpGet(path);
                ArrayList<AssetCountBill> countList = StrConvertObject.strConvertCount(info);

                /*获得用户权限*/
                String path1 = "http://" + ipStr + "/FixedAssService/authority/userFunc"
                    + "?userUUID=" + user.getUserUUID();
                String info1 = WebService.executeHttpGet(path1);
                ArrayList<Function> userFunc = StrConvertObject.strConvertFunction(info1);

                @Override
                public void run() {
                    initData(countList);
                    setUserFunc(userFunc);
                    /*dialog.dismiss();*/
                }
            });
        }
    }
    private void initData(ArrayList<AssetCountBill> countList){
        infoList = new ArrayList<>();
        for (AssetCountBill bill:countList){
            CountSel sel = new CountSel(bill.getCountBillCode(), bill.getcPeoInfo()==null?"":bill.getcPeoInfo().getUserName(), bill.getCreateDate(), bill.getCountNote());
            infoList.add(sel);
        }
        //lvx.add(infoList);
        checkboxAdapter = new CheckboxAdapter(this, infoList);

        lvx.setAdapter(checkboxAdapter);
    }

    private void selAll(View v){
        if (infoList.size()>0){
            if (selAll.getText().equals("全选")){
                for (CountSel count: infoList){
                    count.setCheck(true);
                }
                selAll.setText("全不选");
            }else {
                for (CountSel count: infoList){
                    count.setCheck(false);
                }
                selAll.setText("全选");
            }
            checkboxAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this, "请先新建盘点单！", Toast.LENGTH_SHORT).show();
        }
    }

    private void selNoAll(View v){
        for (CountSel count: infoList){
            count.setCheck(false);
        }
        checkboxAdapter.notifyDataSetChanged();
    }

    private void invSel(View v){
        for (CountSel count: infoList){
            if (count.isCheck()){
                count.setCheck(false);
            }else {
                count.setCheck(true);
            }
        }
        checkboxAdapter.notifyDataSetChanged();
    }

    /*清除数据*/
    private void cleanConfig(){
        //指定操作的文件名称
        SharedPreferences sh = ListActivity.super.getSharedPreferences("listConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();                      //编辑文件
        edit.clear();
        edit.commit();
    }

    /*设置权限*/
    private void setUserFunc(ArrayList<Function> userFunc){
        for (Function function:userFunc){
            switch (function.getFuncURL()){
                case "/countBill/add":
                    ll[0].setVisibility(View.VISIBLE);
                    break;
                case "/countBill/delByUUIDs":
                    ll[1].setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    //删除事件
    String countBillCodeStr = "";
    private void delete(){
        List<CountSel> countList = new ArrayList<>();
        for (CountSel count: infoList){
            if (count.isCheck()){
                countList.add(count);
            }
        }

        countBillCodeStr = "";
        for(int j=0; j<countList.size(); j++){
            countBillCodeStr += countList.get(j).getCountBillCode() + ",";
        }

        if (countBillCodeStr.trim().equals("")){
            Dialog dialog = new AlertDialog.Builder(ListActivity.this)
                    .setTitle("盘点单提示")
                    .setMessage("请先选择要删除的盘点单！")
                    .setPositiveButton("确定", null).create();
            dialog.show();
        }else {
            countBillCodeStr = countBillCodeStr.substring(0, countBillCodeStr.length()-1);
            Dialog dialog = new AlertDialog.Builder(ListActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("确定删除吗？")
                    .setMessage("确定删除盘点单号为"+countBillCodeStr+"的单据吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCode();
                        }
                    })
                    .setNegativeButton("取消",null).create();
            dialog.show();
        }
    }

    //删除数据
    private void deleteCode(){
        if (!Redirect.checkNetwork(ListActivity.this)) {
            Toast toast = Toast.makeText(ListActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            /*dialog = new ProgressDialog(ListActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();*/
            // 创建子线程，分别进行Get和Post传输
            new Thread(new DeleteThread()).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class DeleteThread implements Runnable {
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/count/delete"
                     + "?userUUID=" + user.getUserUUID()+"&countBillCode="+countBillCodeStr;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num == -1){
                        Dialog dialog = new AlertDialog.Builder(ListActivity.this)
                                .setTitle("盘点单提示")
                                .setMessage("删除失败，该盘点单已经进行过盘点不能在进行删除操作！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }else if (num>0){
                        Toast.makeText(ListActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                        selCon();
                    }else {
                        Toast.makeText(ListActivity.this,"删除失败，请重新进行删除操作",Toast.LENGTH_LONG).show();
                    }
                    /*dialog.dismiss();*/
                    selCon();
                }
            });
        }
    }
}
