package fixedass.xiangrong.com.fixedass.manage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fixedass.xiangrong.com.fixedass.MainActivity;
import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetBorrow;
import fixedass.xiangrong.com.fixedass.bean.AssetBorrowQuery;
import fixedass.xiangrong.com.fixedass.bean.AssetOperate;
import fixedass.xiangrong.com.fixedass.bean.Function;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.tree.BorrowUserActivity;
import fixedass.xiangrong.com.fixedass.tree.BorrowUserFragment;

/**
 * @author Eileen
 * @create 2018/4/20
 * @Describe 资产借还
 */
public class BorrowActivity extends Activity implements View.OnClickListener {
    /*private Button btnQuit,btnBack;*/
    private ImageView btnQuit, btnBack;
    private TextView title/*,setSort,setEdit*/;
    private ImageView setSort,setEdit;
    private EditText barCode;
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    private int fragid;
    private String ipStr;
    private User user;

    private ProgressDialog dialog;  // 创建等待框

    /*悬浮菜单设置*/
    private FloatingActionButton borrowMenu;    //悬浮菜单
    private Boolean isOpen = false;
    private RelativeLayout rlAddBill;
    private int[] llId = new int[]{R.id.ll01,R.id.ll02,R.id.ll03,R.id.ll04,R.id.ll05,R.id.ll06};
    private LinearLayout[] ll = new LinearLayout[llId.length];
    private int[] fabId = new int[]{R.id.miniFab01,R.id.miniFab02,R.id.miniFab03,R.id.miniFab04,R.id.miniFab05,R.id.miniFab06};
    private FloatingActionButton[] fab = new FloatingActionButton[fabId.length];

    private ListView loadData;      //加载适配器
    private BorrowStyleAdapter borrowStyleAdapter;        //适配器

    /*获得所选的code*/
    private String operbillCodes = "";

    //查询条件
    private String[] popContents = new String[]{"创建时间","创建时间逆序","借用日期","借用日期逆序"};       //排序
    private BorrowEditPopWin editPopWin;
    private String borrowDepatment, borrowDate, borrowDate1;                                                //筛选
    private String barCodeValue;

    //jsonObject转object
    private Gson gson;
    private GsonBuilder builder;

    private User reviewUser;                        //审核人
    private int isCheck;                            //审核-是否同意
    private String borrowUUIDs;                     //归还的资产

    private Integer year, month, day,year1,month1,day1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");

        /*读取数据*/
        SharedPreferences sharedPreferences = BorrowActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");
        SharedPreferences borSharedPreferences = BorrowActivity.super.getSharedPreferences("borrowConfig", Activity.MODE_PRIVATE);
        borrowDepatment = borSharedPreferences.getString("borrowDepatment", "");
        borrowDate = borSharedPreferences.getString("borrowDate", "");
        borrowDate1 = borSharedPreferences.getString("borrowDate1", "");
        barCodeValue = borSharedPreferences.getString("barCode","");

        initView();
        initEvent();
        selCon();
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
                String conSql = "";
                switch (arg2){
                    case 0:
                        conSql = "a.createdate";
                        break;
                    case 1:
                        conSql = "a.createdate1";
                        break;
                    case 2:
                        conSql = "b.borrowDate";
                        break;
                    case 3:
                        conSql = "b.borrowDate1";
                        break;
                }
                orderBy(conSql);
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
        title.setText(R.string.assetBorrow);
        barCode = (EditText)findViewById(R.id.barCode);

        /*setSort = (TextView)findViewById(R.id.setSort);
        setEdit = (TextView)findViewById(R.id.setEdit);*/
        setSort = (ImageView)findViewById(R.id.setSort);
        setEdit = (ImageView)findViewById(R.id.setEdit);
        editPopWin = new BorrowEditPopWin(this,onClickListener);

        borrowMenu = (FloatingActionButton)findViewById(R.id.borrowMenu);
        rlAddBill = (RelativeLayout)findViewById(R.id.rlAddBill);
        for (int i = 0; i < llId.length;i++){
            ll[i] = (LinearLayout)findViewById(llId[i]);
        }
        for (int i = 0;i < fabId.length; i++){
            fab[i] = (FloatingActionButton)findViewById(fabId[i]);
        }

        loadData = (ListView)findViewById(R.id.loadData);

        //这两句代码必须的，为的是初始化出来gson这个对象，才能拿来用
        builder = new GsonBuilder();
        gson = builder.create();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String date = df.format(curDate);
        String[] split = date.split("-");
        if (borrowDate!=null&&!borrowDate.trim().equals("")){
            split = borrowDate.split("-");
        }
        year = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1])-1;
        day = Integer.parseInt(split[2]);

        String[] split1 = date.split("-");
        if (borrowDate1!=null&&!borrowDate1.trim().equals("")){
            split1 = borrowDate1.split("-");
        }
        year1 = Integer.parseInt(split1[0]);
        month1 = Integer.parseInt(split1[1]);
        day1 = Integer.parseInt(split1[2]);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save_pop:
                    borrowDepatment = editPopWin.borrowDepatment.getText().toString().trim();
                    borrowDate = editPopWin.borrowDate.getText().toString().trim();
                    borrowDate1 = editPopWin.borrowDate1.getText().toString().trim();

                    //指定操作的文件名称
                    SharedPreferences sh = BorrowActivity.super.getSharedPreferences("borrowConfig", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sh.edit();                      //编辑文件
                    edit.putString("borrowDepatment", borrowDepatment);          //保存
                    edit.putString("borrowDate", borrowDate);
                    edit.putString("borrowDate1", borrowDate);
                    edit.commit();

                    selCon();
                    editPopWin.dismiss();
                    break;
                case R.id.btn_cancel_pop:
                    editPopWin.borrowDepatment.setText("");
                    editPopWin.borrowDate.setText("");
                    editPopWin.borrowDate1.setText("");
                    cleanConfig();

                    borrowDepatment = editPopWin.borrowDepatment.getText().toString().trim();
                    borrowDate = editPopWin.borrowDate.getText().toString().trim();
                    borrowDate1 = editPopWin.borrowDate1.getText().toString().trim();

                    editPopWin.dismiss();
                    selCon();
                    break;
                case R.id.borrowDate:
                    Redirect.hideIM(v, BorrowActivity.this);
                    Dialog dialog = new DatePickerDialog(BorrowActivity.this, new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String date = year+"-"+(month+1)+"-"+dayOfMonth;
                            editPopWin.borrowDate.setText(date);
                        }
                    }, year,month,day);
                    dialog.show();
                    break;
                case R.id.borrowDate1:
                    Redirect.hideIM(v, BorrowActivity.this);
                    Dialog dialog1 = new DatePickerDialog(BorrowActivity.this, new DatePickerDialog.OnDateSetListener(){
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


    private void bindEvents(){
        borrowMenu.setOnClickListener(this);
        for (int i = 0;i < fabId.length; i++){
            fab[i].setOnClickListener(this);
        }
    }

    private void initEvent(){
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        barCode.setOnKeyListener(returnKey);
        setEdit.setOnClickListener(this);
    }


    View.OnKeyListener returnKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                Redirect.hideIM(v,BorrowActivity.this);
                    /*barCode.setText(barCode.getText().toString());// 添加这句后实现效果*/
                barCode.selectAll();
                barCodeValue = barCode.getText().toString();

                //指定操作的文件名称
                SharedPreferences sh = BorrowActivity.super.getSharedPreferences("borrowConfig", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sh.edit();                      //编辑文件
                edit.putString("barCode", barCodeValue);          //保存
                edit.commit();
                selCon();
                //根据编码查询资产信息
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);

        switch (v.getId()){
            case R.id.btnBack:
                //清除缓存数据
                cleanConfig();
                Redirect.redirect(BorrowActivity.this, MainActivity.class,bundle);
                break;
            case R.id.btnQuit:
                cleanConfig();
                Redirect.quit(BorrowActivity.this);
                break;
            case R.id.setEdit:
                int[] location = new int[2];
                setEdit.getLocationOnScreen(location);
                int x = location[0];            //540
                int y = location[1];            //433

                editPopWin.borrowDepatment.setText(borrowDepatment);
                editPopWin.borrowDate.setText(borrowDate);
                editPopWin.borrowDate1.setText(borrowDate1);
                editPopWin.showAtLocation(v, Gravity.LEFT, x, -(y%100+17));
                break;
            case R.id.borrowMenu:
                /*关闭键盘*/
                /*Redirect.hideIM(v, BorrowActivity.this);*/
                int icon = isOpen? android.R.drawable.ic_dialog_dialer:android.R.drawable.ic_menu_close_clear_cancel;
                borrowMenu.setImageResource(icon);
                isOpen = !isOpen;
                rlAddBill.setVisibility(isOpen ? View.VISIBLE : View.GONE);
                break;
            case R.id.miniFab01:                                                                //新增
                Redirect.redirect(BorrowActivity.this, BorrowAddActivity.class, bundle);
                hideFABMenu();
                break;
            case R.id.miniFab02:                                                                //报审
                HashMap<Integer, Boolean> selCheck = borrowStyleAdapter.selCheck;
                int selNum = 0;                                                         //选中的数量
                String codeRemeberNo = "";                                                //记录不是报审的借用单
                operbillCodes = "";                                                    //记录所选择的借用单号
                Integer stateExam = 0;
                for(int j = 0; j< borrowStyleAdapter.getCount(); j++) {
                    if (selCheck.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetBorrowQuery assetBorrowQuery = (AssetBorrowQuery) borrowStyleAdapter.getItem(j);
                        operbillCodes += assetBorrowQuery.getOperbillCode()+",";
                        stateExam = assetBorrowQuery.getAssetOperate().getState();
                        if (stateExam!=0 && stateExam!=8){
                            codeRemeberNo += assetBorrowQuery.getOperbillCode()+",";
                        }
                        selNum ++;
                    }
                }
                if(selNum==0){
                    Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("请先选择要报审的借用单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (selNum>1){
                    Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("每次只能操作一个借用单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    if(!codeRemeberNo.equals("")){                                 //选中不是为报审的借用单
                        Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                                .setTitle("资产借还提示")
                                .setMessage("该借用单不能报审！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }else{                                                      //进行报审
                        operbillCodes = operbillCodes.substring(0, operbillCodes.length()-1);

                        if (stateExam==0){
                            //获取报审的信息
                            examineInfo();
                        }else if (stateExam==8){
                            bundle.putString("operbillCode", operbillCodes);
                            Redirect.redirect(this, BorrowExamActivity.class, bundle);
                        }
                    }
                }
                hideFABMenu();
                break;
            case R.id.miniFab03:                                                                    //审核
                HashMap<Integer, Boolean> selLook = borrowStyleAdapter.selCheck;
                int selLookNum = 0;                                                         //选中的数量
                String codeRemeberNoLook = "";                                              //记录不是报审的借用单
                operbillCodes = "";                                                         //记录所选择的借用单号
                Integer stateLook = 0;
                for(int j = 0; j< borrowStyleAdapter.getCount(); j++) {
                    if (selLook.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetBorrowQuery assetBorrowQuery = (AssetBorrowQuery) borrowStyleAdapter.getItem(j);
                        operbillCodes += assetBorrowQuery.getOperbillCode()+",";
                        stateLook = assetBorrowQuery.getAssetOperate().getState();
                        if (stateLook!=1 && stateLook!=9){
                            codeRemeberNoLook += assetBorrowQuery.getOperbillCode()+",";
                        }
                        selLookNum ++;
                    }
                }
                if(selLookNum==0){
                    Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("请先选择要审核的借用单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (selLookNum>1){
                    Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("每次只能操作一个借用单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    if(!codeRemeberNoLook.equals("")){                                 //选中不是为报审的借用单
                        Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                                .setTitle("资产借还提示")
                                .setMessage("该借用单不能审核！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }else{                                                      //进行报审
                        operbillCodes = operbillCodes.substring(0, operbillCodes.length()-1);

                        if (stateLook==1 || stateLook==9){
                            //获取报审的信息
                            lookThroughInfo();
                        }
                        /*else if (stateLook==9){
                            lookThroughInfoImp();
                        }*/
                    }
                }
                hideFABMenu();
                break;
            case R.id.miniFab04:                                                            //归还
                HashMap<Integer, Boolean> selReturn = borrowStyleAdapter.selCheck;
                int selReturnNum = 0;                                                         //选中的数量
                String codeRemeberNoReturn = "";                                              //记录不是报审的借用单
                operbillCodes = "";                                                         //记录所选择的借用单号
                Integer stateReturn = 0;
                for(int j = 0; j< borrowStyleAdapter.getCount(); j++) {
                    if (selReturn.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetBorrowQuery assetBorrowQuery = (AssetBorrowQuery) borrowStyleAdapter.getItem(j);
                        operbillCodes += assetBorrowQuery.getOperbillCode()+",";

                        stateReturn = assetBorrowQuery.getAssetOperate().getState();
                        if (stateReturn!=4 && stateReturn!=5 && stateReturn!=7){                              //不是未归还/部分归还
                            codeRemeberNoReturn += assetBorrowQuery.getOperbillCode()+",";
                        }
                        selReturnNum ++;
                    }
                }
                if(selReturnNum==0){
                    Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("请先选择要归还的借还单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (selReturnNum>1){
                    Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("每次只能操作一个借用单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    if(!codeRemeberNoReturn.equals("")){                                 //选中不是为未归还或者部分归还的借用单
                        Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                                .setTitle("资产借还提示")
                                .setMessage("该借用单不能归还！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }
                    else if (stateReturn==7){
                        Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                                .setTitle("资产借还提示")
                                .setMessage("借用单不能重复归还！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }
                    else{                                                      //进行报审
                        operbillCodes = operbillCodes.substring(0, operbillCodes.length()-1);

                        //获取报审的信息
                        returnInfo();
                    }
                }
                hideFABMenu();
                break;
            case R.id.miniFab05:                                                            //删除
                HashMap<Integer, Boolean> state = borrowStyleAdapter.selCheck;
                int noSelNum = 0;
                String codeRemeber = "";
                operbillCodes = "";
                for(int j = 0; j< borrowStyleAdapter.getCount(); j++) {
                    if (state.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetBorrowQuery assetBorrowQuery = (AssetBorrowQuery) borrowStyleAdapter.getItem(j);
                        operbillCodes += assetBorrowQuery.getOperbillCode()+",";
                        if (assetBorrowQuery.getAssetOperate().getState()!=0){
                            codeRemeber += assetBorrowQuery.getOperbillCode()+",";
                        }
                    }else {
                        noSelNum ++;
                    }
                }
                if(noSelNum== borrowStyleAdapter.getCount()){
                    /*Toast.makeText(BorrowActivity.this,"请先选择要删除的借用单！",Toast.LENGTH_LONG).show();*/
                    Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setTitle("资产借用提示")
                            .setMessage("请先选择要删除的借用单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (!codeRemeber.equals("")){
                    /*进行删除操作*/
                    Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("资产借用提示")
                            .setMessage("只能删除未报审借用单！借还单号为"+codeRemeber.substring(0,codeRemeber.length()-1)+"的单据不是未报审的借用单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    /*进行删除操作*/
                    Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("确定删除吗？")
                            .setMessage("确定删除借还单号为"+operbillCodes.substring(0,operbillCodes.length()-1)+"的单据吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    delete();
                                }
                            })
                            .setNegativeButton("取消",null).create();
                    dialog.show();
                }
                //显示选择内容
                /*Toast.makeText(this, "111"+operbillCodes, Toast.LENGTH_LONG).show();*/
                hideFABMenu();
                break;
            case R.id.miniFab06:                                                                    //编辑
                HashMap<Integer, Boolean> updState = borrowStyleAdapter.selCheck;
                int selNumUpd = 0;
                String codeRemeberUpd = "";
                operbillCodes = "";
                Integer stateUpd = 0;
                for(int j = 0; j< borrowStyleAdapter.getCount(); j++) {
                    if (updState.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetBorrowQuery assetBorrowQuery = (AssetBorrowQuery) borrowStyleAdapter.getItem(j);
                        operbillCodes += assetBorrowQuery.getOperbillCode()+",";
                        stateUpd = assetBorrowQuery.getAssetOperate().getState();
                        if (stateUpd!=0 && stateUpd!=11 ){
                            codeRemeberUpd += assetBorrowQuery.getOperbillCode()+",";
                        }
                        selNumUpd ++;
                    }
                }

                if(selNumUpd==0){
                    Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("请先选择要修改的借还单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (selNumUpd>1){
                    Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setTitle("资产借还提示")
                            .setMessage("每次只能操作一个借用单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    if(!codeRemeberUpd.equals("")){                                 //选中不是为未报审或者导入新增的借用单
                        Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("资产借用提示")
                                .setMessage("只能修改未报审或者导入新增状态的借用单！借还单号为"+codeRemeberUpd.substring(0,codeRemeberUpd.length()-1)+"的单据不是未报审或者导入新增状态的借用单！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }else{                                                      //进行报审
                        operbillCodes = operbillCodes.substring(0, operbillCodes.length()-1);
                        bundle.putString("operbillCode", operbillCodes);

                        if (stateUpd==0 || stateUpd==11) {
                            bundle.putInt("state", stateUpd);
                            Redirect.redirect(this, BorrowUpdActivity.class, bundle);
                        }
                    }
                }
                hideFABMenu();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        cleanConfig();
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);
        Redirect.redirect(BorrowActivity.this, MainActivity.class,bundle);
    }

    /*设置权限*/
    private void setUserFunc(ArrayList<Function> userFunc){
        for (Function function:userFunc){
            switch (function.getFuncURL()){
                case "/assetBorrow/add":
                    ll[0].setVisibility(View.VISIBLE);
                    break;
                case "/assetBorrow/update":
                    ll[5].setVisibility(View.VISIBLE);
                    break;
                case "/assetBorrow/delete":
                    ll[4].setVisibility(View.VISIBLE);
                    break;
                case "/assetBorrow/exam":
                    ll[1].setVisibility(View.VISIBLE);
                    break;
                case "/assetBorrow/lookThrough":
                    ll[2].setVisibility(View.VISIBLE);
                    break;
                case "/assetBorrow/return":
                    ll[3].setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
    /*设置借用单号*/
    private void setBorrowList(ArrayList<AssetBorrowQuery> borrowList){
        //  建立Adapter绑定数据源
        borrowStyleAdapter = new BorrowStyleAdapter(borrowList,this, fragid, user);
        //绑定Adapter
        loadData.setAdapter(borrowStyleAdapter);
    }

    //排序
    private void orderBy(String conSql){
        if (!Redirect.checkNetwork(BorrowActivity.this)) {
            Toast toast = Toast.makeText(BorrowActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(BorrowActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MyOrderThread(conSql)).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class MyOrderThread implements Runnable {
        private String conSql;

        public MyOrderThread(String conSql) {
            this.conSql = conSql;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                /*获得用户权限*/
                String path = "http://" + ipStr + "/FixedAssService/authority/userFunc"
                    + "?userUUID=" + user.getUserUUID();
                String info = WebService.executeHttpGet(path);
                ArrayList<Function> userFunc = StrConvertObject.strConvertFunction(info);
                /*所有的数据*/
                String path1 = "http://" + ipStr + "/FixedAssService/borrow/getOrder"
                    + "?deptUUID=" + user.getDeptUUID()+"&userUUID=" + user.getUserUUID()+"&conSql="+conSql;
                String info1 = WebService.executeHttpGet(path1);
                ArrayList<AssetBorrowQuery> borrowList = StrConvertObject.setConvertAssetBorrowQuery(info1);
                @Override
                public void run() {
                    setUserFunc(userFunc);
                    setBorrowList(borrowList);
                }
            });
            dialog.dismiss();
        }
    }

    //筛选
    private void selCon(){
        if (!Redirect.checkNetwork(BorrowActivity.this)) {
            Toast toast = Toast.makeText(BorrowActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            String borrowDepatmentStr = "";
            try {
                borrowDepatmentStr = URLEncoder.encode(borrowDepatment,"utf-8");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            String conSql = "borrowDepatment="+borrowDepatmentStr+"&borrowDate="+borrowDate+"&borrowDate1="+borrowDate1+"&barCode="+barCodeValue;

            // 提示框
            dialog = new ProgressDialog(BorrowActivity.this);
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
                /*获得用户权限*/
                String path = "http://" + ipStr + "/FixedAssService/authority/userFunc"
                        + "?userUUID=" + user.getUserUUID();
                String info = WebService.executeHttpGet(path);
                ArrayList<Function> userFunc = StrConvertObject.strConvertFunction(info);

                /*所有的数据*/
                String path1 = "http://" + ipStr + "/FixedAssService/borrow/getSel"
                    + "?deptUUID=" + user.getDeptUUID()+"&userUUID=" + user.getUserUUID()+"&"+conSql;
                String info1 = WebService.executeHttpGet(path1);
                ArrayList<AssetBorrowQuery> borrowList = StrConvertObject.setConvertAssetBorrowQuery(info1);

                @Override
                public void run() {
                    setUserFunc(userFunc);
                    setBorrowList(borrowList);
                }
            });
            dialog.dismiss();
        }
    }

    /*获取报审信息*/
    private void examineInfo(){
        if (!Redirect.checkNetwork(BorrowActivity.this)) {
            Toast toast = Toast.makeText(BorrowActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(BorrowActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new ExamineInfoThread()).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class ExamineInfoThread implements Runnable {
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/borrow/examineInfo"
                    + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(info);
                        String msg = jsonArray.getJSONObject(0).getString("msg");
                        if(msg.equals("")){
                            JSONObject userObject = jsonArray.getJSONObject(1);
                            User user = gson.fromJson(userObject.toString(),User.class);
                            JSONArray userArray = jsonArray.getJSONArray(2);                        //审核人
                            List<CharSequence> userNameList = new ArrayList<>();
                            final List<User> userList = new ArrayList<>();
                            for(int i=0;i<userArray.length();i++){
                                JSONObject jsonObject1 = userArray.getJSONObject(i);
                                User user1 = gson.fromJson(jsonObject1.toString(), User.class);
                                String userName = user1.getUserName();
                                userNameList.add(userName);
                                userList.add(user1);
                            }

                            LayoutInflater inflater = LayoutInflater.from(BorrowActivity.this);     //创建对象
                            final View dialogView = inflater.inflate(R.layout.dialog_examination,null);   //将布局文件转换成view

                            final EditText operbillCode = (EditText)dialogView.findViewById(R.id.operbillCode);   //单号
                            operbillCode.setText(user.getUserPhone());
                            final EditText examinePeople = (EditText)dialogView.findViewById(R.id.examinePeople); //报审人
                            examinePeople.setText(user.getUserName());
                            final EditText examineDate = (EditText)dialogView.findViewById(R.id.examineDate);     //报审日期
                            examineDate.setText(user.getUserAddr());

                            Spinner reviewPeople = (Spinner)dialogView.findViewById(R.id.reviewPeople);        //审核人
                            reviewPeople.setPrompt("请选择审核人：");

                            //获得下拉列表适配器
                            ArrayAdapter<CharSequence> adapterReview = new ArrayAdapter<CharSequence>(BorrowActivity.this,android.R.layout.simple_spinner_item,userNameList);
                            adapterReview.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);           //设置下拉列表风格
                            reviewPeople.setAdapter(adapterReview);                                                 //设置下拉列表选项内容
                            reviewPeople.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    /*显示的布局，在布局中显示的位置ID，将要显示的数据*/
                                   User reviewUser = userList.get(position);
                                   setReviewUser(reviewUser);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {  }
                            });

                            final EditText examineNote = (EditText)dialogView.findViewById(R.id.examineNote);         //报审意见
                            Dialog dialog1 = new AlertDialog.Builder(BorrowActivity.this)
                                    .setTitle(R.string.examination)
                                    .setView(dialogView)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String operbill = operbillCode.getText().toString();
                                            String reviewU = reviewUser.getUserUUID();
                                            String examineN = examineNote.getText().toString();
                                            try {
                                                examineN = URLEncoder.encode(examineN,"utf-8");
                                            }catch (UnsupportedEncodingException e){
                                                e.printStackTrace();
                                            }
                                            String conSql = "operbillCode="+operbill+"&reviewPeople="+reviewU+"&examineNote="+examineN;
                                            exam(conSql);
                                            //隐藏键盘
                                            Redirect.hideIM(dialogView, BorrowActivity.this);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Redirect.hideIM(dialogView,BorrowActivity.this);
                                        }
                                    })
                                    .create();
                            dialog1.show();

                        }else{
                            Toast.makeText(BorrowActivity.this,msg,Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    //报审
    private void exam(String conSql){
        if (!Redirect.checkNetwork(BorrowActivity.this)) {
            Toast toast = Toast.makeText(BorrowActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(BorrowActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MyExamThread(conSql)).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class MyExamThread implements Runnable {
        private String conSql;

        public MyExamThread(String conSql) {
            this.conSql = conSql;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/borrow/examine"
                    + "?userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(BorrowActivity.this,"报审成功",Toast.LENGTH_LONG).show();
                        selCon();
                    }else {
                        Toast.makeText(BorrowActivity.this,"报审失败，请重新进行报审操作",Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    /*获取审核信息*/
    private void lookThroughInfo(){
        if (!Redirect.checkNetwork(BorrowActivity.this)) {
            Toast toast = Toast.makeText(BorrowActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(BorrowActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new LookThroughInfoThread()).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class LookThroughInfoThread implements Runnable {
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/borrow/lookThroughInfo"
                    + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(info);
                        String msg = jsonArray.getJSONObject(0).getString("msg");
                        if(msg.equals("")){
                            JSONObject borrowOperateObject = jsonArray.getJSONObject(1);
                            AssetOperate<AssetBorrow> operateBorrow = StrConvertObject.setConvertAssetOperateBorrow(borrowOperateObject);

                            LayoutInflater inflater = LayoutInflater.from(BorrowActivity.this);              //创建对象
                            final View dialogView = inflater.inflate(R.layout.dialog_lookthrough,null);   //将布局文件转换成view
                            final EditText operbillCode = (EditText)dialogView.findViewById(R.id.operbillCode);   //单号
                            operbillCode.setText(operateBorrow.getOperbillCode());
                            EditText examinePeople = (EditText) dialogView.findViewById(R.id.examinePeople);       //报审人
                            examinePeople.setText(operateBorrow.getExamUser().getUserName());
                            EditText examineDate = (EditText) dialogView.findViewById(R.id.examineDate);            //报审日期
                            examineDate.setText(operateBorrow.getExamdate());
                            final RadioGroup isAgree = (RadioGroup)dialogView.findViewById(R.id.isAgree);                 //是否同意
                            final EditText throughNote = (EditText)dialogView.findViewById(R.id.throughNote);             //审核意见

                            Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                                    .setTitle(R.string.lookThroughList)
                                    .setView(dialogView)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String operbill = operbillCode.getText().toString();
                                            String reviewnote = throughNote.getText().toString();
                                            try {
                                                reviewnote = URLEncoder.encode(reviewnote,"utf-8");
                                            }catch (UnsupportedEncodingException e){
                                                e.printStackTrace();
                                            }
                                            for (int i = 0; i < isAgree.getChildCount(); i++) {
                                                RadioButton rd = (RadioButton) isAgree.getChildAt(i);
                                                if (rd.isChecked()) {
                                                    boolean flag = rd.getText().toString().trim().equals("同意");
                                                    int rem = 1;
                                                    if(flag){
                                                        rem = 0;
                                                    }
                                                    setIsCheck(rem);
                                                    break;
                                                }
                                            }

                                            String conSql = "operbillCode="+operbill+"&reviewnote="+reviewnote+"&ischeck="+isCheck;
                                            lookThrough(conSql);
                                            //隐藏键盘
                                            Redirect.hideIM(dialogView, BorrowActivity.this);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Redirect.hideIM(dialogView, BorrowActivity.this);
                                        }
                                    })
                                    .create();
                            dialog.show();
                        }else{
                            Toast.makeText(BorrowActivity.this,msg,Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    //审核
    private void lookThrough(String conSql){
        if (!Redirect.checkNetwork(BorrowActivity.this)) {
            Toast toast = Toast.makeText(BorrowActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(BorrowActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MyLookThroughThread(conSql)).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class MyLookThroughThread implements Runnable {
        private String conSql;

        public MyLookThroughThread(String conSql) {
            this.conSql = conSql;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/borrow/lookThrough"
                    + "?userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(BorrowActivity.this,"审核成功",Toast.LENGTH_LONG).show();
                        selCon();
                    }else {
                        Toast.makeText(BorrowActivity.this,"审核失败，请重新进行审核操作",Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    /*获取归还信息*/
    private void returnInfo(){
        if (!Redirect.checkNetwork(BorrowActivity.this)) {
            Toast toast = Toast.makeText(BorrowActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(BorrowActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new ReturnInfoThread()).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class ReturnInfoThread implements Runnable {
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/borrow/returnInfo"
                    + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(info);
                        AssetOperate<AssetBorrow> operateBorrow = StrConvertObject.setConvertAssetOperateBorrow(jsonObject);

                        String uuids = "";
                        for (AssetBorrow ab: operateBorrow.getList()){
                            uuids = uuids + ab.getBorrowUUID()+",";
                        }
                        setBorrowUUIDs(uuids);
                        AssetBorrow borrow = operateBorrow.getList().get(0);

                        LayoutInflater inflater = LayoutInflater.from(BorrowActivity.this);     //创建对象
                        View dialogView = inflater.inflate(R.layout.dialog_return,null);   //将布局文件转换成view

                        EditText borrowPeople = (EditText)dialogView.findViewById(R.id.borrowPeople);
                        borrowPeople.setText(borrow.getBorrowPeople());
                        EditText operbillCode = (EditText)dialogView.findViewById(R.id.operbillCode);
                        operbillCode.setText(operateBorrow.getOperbillCode());
                        EditText borrowDate = (EditText)dialogView.findViewById(R.id.borrowDate);
                        borrowDate.setText(borrow.getBorrowDate());
                        EditText borrowDays = (EditText)dialogView.findViewById(R.id.borrowDays);
                        borrowDays.setText(borrow.getBorrowDays()+"月");

                        Dialog dialog = new AlertDialog.Builder(BorrowActivity.this)
                            .setTitle(R.string.returnList)
                            .setView(dialogView)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    assetReturn();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create();
                        dialog.show();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    //归还
    private void assetReturn(){
        if (!Redirect.checkNetwork(BorrowActivity.this)) {
            Toast toast = Toast.makeText(BorrowActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(BorrowActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MyAssetReturnThread()).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class MyAssetReturnThread implements Runnable {
        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/borrow/return"
                    + "?userUUID=" + user.getUserUUID()+"&borrowUUIDs="+borrowUUIDs;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(BorrowActivity.this,"归还成功",Toast.LENGTH_LONG).show();
                        selCon();
                    }else {
                        Toast.makeText(BorrowActivity.this,"归还失败，请重新进行归还操作",Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    /* 删除操作 */
    /*获得所有的借还资产*/
    private void delete(){
        if (!Redirect.checkNetwork(BorrowActivity.this)) {
            Toast toast = Toast.makeText(BorrowActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(BorrowActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new DeleteThread()).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class DeleteThread implements Runnable {
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/borrow/delete"
                    + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(BorrowActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                        selCon();
                    }else {
                        Toast.makeText(BorrowActivity.this,"删除失败，请重新进行删除操作",Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    private void hideFABMenu(){
        rlAddBill.setVisibility(View.GONE);
        borrowMenu.setImageResource(android.R.drawable.ic_dialog_dialer);
        isOpen = false;
    }

    /*清除数据*/
    private void cleanConfig(){
        //指定操作的文件名称
        SharedPreferences sh = BorrowActivity.super.getSharedPreferences("borrowConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();                      //编辑文件
        edit.clear();
        edit.commit();
    }

    public void setReviewUser(User reviewUser) {
        this.reviewUser = reviewUser;
    }
    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }
    public void setBorrowUUIDs(String borrowUUIDs) {
        this.borrowUUIDs = borrowUUIDs;
    }
}
