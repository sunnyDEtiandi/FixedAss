package fixedass.xiangrong.com.fixedass.count;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import fixedass.xiangrong.com.fixedass.bean.AssetCountBill;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.manage.BorrowEditPopWin;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.view.ListViewEx;

/**
 * @author Eileen
 * @create 2018-11-20
 * @Describe 盘点查询
 */
public class ResultActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private TextView title/*,setSort,setEdit*/;
    private ImageView setSort,setEdit;

    private int fragid;
    private User user;

    private String ipStr;

    private static Handler handler = new Handler();
    private ProgressDialog dialog;  // 创建等待框

    //    查询条件
    private String[] popContents = new String[]{"创建时间","创建时间逆序"};       //排序
    private BorrowEditPopWin editPopWin;
    private String countBillCode, createDate, createDate1, orderBy="",createPeople,countNote;                                                //筛选

    private int year,month,day,year1,month1,day1;

    private List<Map<String, Object>> infoList;
    private ListViewEx lvx;
    private static String[] row = {"countBillCode","cPeoInfo","createDate","countNote","countResult"};
    private static String[] rowName = {"盘点单号","创建人","创建时间","盘点备注","盘点结果"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");

        /*读取数据*/
        SharedPreferences sharedPreferences = ResultActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");
        SharedPreferences resultConfig = ResultActivity.super.getSharedPreferences("resultConfig", Activity.MODE_PRIVATE);
        countBillCode = resultConfig.getString("countBillCode", "");
        createDate = resultConfig.getString("createDate", "");
        createDate1 = resultConfig.getString("createDate1", "");
        createPeople = resultConfig.getString("createPeople", "");
        countNote = resultConfig.getString("countNote","");

        initView();
        initEvent();
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
        // popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
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
                /*mTextView.setText(popContents[arg2]);*/
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
        title.setText(R.string.countResult);

        /*setSort = (TextView)findViewById(R.id.setSort);
        setEdit = (TextView)findViewById(R.id.setEdit);*/
        setSort = (ImageView)findViewById(R.id.setSort);
        setEdit = (ImageView)findViewById(R.id.setEdit);
        editPopWin = new BorrowEditPopWin(this,onClickListener);

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

        selCon();
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
                    SharedPreferences sh = ResultActivity.super.getSharedPreferences("resultConfig", Context.MODE_PRIVATE);
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
                    Redirect.hideIM(v, ResultActivity.this);
                    Dialog dialog = new DatePickerDialog(ResultActivity.this, new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String date = year+"-"+(month+1)+"-"+dayOfMonth;
                            editPopWin.borrowDate.setText(date);
                        }
                    }, year,month,day);
                    dialog.show();
                    break;
                case R.id.borrowDate1:
                    Redirect.hideIM(v, ResultActivity.this);
                    Dialog dialog1 = new DatePickerDialog(ResultActivity.this, new DatePickerDialog.OnDateSetListener(){
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
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        switch (v.getId()){
            case R.id.btnBack:
                cleanConfig();
                Redirect.redirect(ResultActivity.this, MainActivity.class,bundle);
                break;
            case R.id.btnQuit:
                cleanConfig();
                Redirect.quit(ResultActivity.this);
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
        }
    }

    @Override
    public void onBackPressed() {
        cleanConfig();
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        Redirect.redirect(ResultActivity.this, MainActivity.class,bundle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
        AssetCountBill bill = new AssetCountBill((String)map.get("countBillCode"),(String)map.get("cPeoInfo"),
                (String)map.get("createDate"),(String) map.get("countNote"),(String)map.get("countResult"));

        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);
        bundle.putSerializable("bill", bill);

        Redirect.redirect(this,ResultDetailActivity.class, bundle);
    }

    //查询数据
    private void selCon(){
        if (!Redirect.checkNetwork(ResultActivity.this)) {
            Toast toast = Toast.makeText(ResultActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(ResultActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            String data = "";
            if (createDate!=null&&!createDate.trim().equals("")){
                data = createDate;
                if ((createDate1!=null&&!createDate1.trim().equals(""))){
                    data += ","+createDate1;
                }
            }
            new Thread(new MyCountThread(data)).start();
        }
    }
    class MyCountThread implements Runnable{
        private String data;

        public MyCountThread(String data) {
            this.data = data;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                String sql = "countBillCode="+countBillCode+"&createDate="+data+"&createPeople="+createPeople+"&countNote="+countNote+"&orderBy="+orderBy;
                String path = "http://" + ipStr + "/FixedAssService/count/countSelResult"
                    + "?userUUID=" + user.getUserUUID()+"&"+sql;

                String info = WebService.executeHttpGet(path);
                @Override
                public void run() {
                    ArrayList<AssetCountBill> countList = StrConvertObject.strConvertCount(info);
                    initData(countList);
                    dialog.dismiss();
                }
            });
        }
    }
    private void initData(ArrayList<AssetCountBill> countList){
        infoList = new ArrayList<>();
        for (AssetCountBill bill:countList){
            Map<String, Object> map = new HashMap<>();
            map.put(row[0], bill.getCountBillCode());
            map.put(row[1], bill.getcPeoInfo()==null?"":bill.getcPeoInfo().getUserName());
            map.put(row[2], bill.getCreateDate());
            map.put(row[3], bill.getCountNote());
            map.put(row[4], bill.getSysUUID());
            infoList.add(map);
        }
        lvx.add(infoList);
    }

    /*清除数据*/
    private void cleanConfig(){
        //指定操作的文件名称
        SharedPreferences sh = ResultActivity.super.getSharedPreferences("resultConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();                      //编辑文件
        edit.clear();
        edit.commit();
    }
}
