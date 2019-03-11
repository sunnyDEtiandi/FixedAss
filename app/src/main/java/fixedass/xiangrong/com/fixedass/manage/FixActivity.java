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
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import fixedass.xiangrong.com.fixedass.bean.AssetFix;
import fixedass.xiangrong.com.fixedass.bean.AssetFixQuery;
import fixedass.xiangrong.com.fixedass.bean.AssetOperate;
import fixedass.xiangrong.com.fixedass.bean.Function;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;

/**
 * @author Eileen
 * @create 2018/9/26
 * @Describe 资产维修
 */
public class FixActivity extends Activity implements View.OnClickListener {
    /*标题部分*/
    private ImageView btnBack,btnQuit;
    private TextView title;
    /*资产编码查询*/
    private EditText barCode;
    private String barCodeValue;
    /*添加查询--排序、筛选*/
    private ImageView setSort,setEdit;
    private String[] popContents = new String[]{"创建时间","创建时间逆序","维修日期","维修日期逆序"};       //排序
    private BorrowEditPopWin editPopWin;
    private String operbillCode, fixDate, fixDate1;                                                //筛选:维修单号、维修日期

    /*维修单信息*/
    private ListView loadData;      //加载适配器
    private FixStyleAdapter fixStyleAdapter;

    /*悬浮菜单设置*/
    private FloatingActionButton fixMenu;    //悬浮菜单
    private Boolean isOpen = false;
    private RelativeLayout rlAddBill;
    private int[] llId = new int[]{R.id.ll01,R.id.ll02,R.id.ll03,R.id.ll04,R.id.ll05};
    private LinearLayout[] ll = new LinearLayout[llId.length];
    private int[] fabId = new int[]{R.id.miniFab01,R.id.miniFab02,R.id.miniFab03,R.id.miniFab04,R.id.miniFab05};
    private FloatingActionButton[] fab = new FloatingActionButton[fabId.length];

    /*activity之间传递的参数*/
    private int fragid;
    private User user;
    private String ipStr;

    /*与数据库的交互*/
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    private ProgressDialog dialog;  // 创建等待框

    //jsonObject转object
    private Gson gson;
    private GsonBuilder builder;

    /*获得所选的code*/
    private String operbillCodes = "";

    private User reviewUser;                        //审核人
    private int isCheck;                            //审核-是否同意

    private Integer year, month, day;

    /*初始化排序列表*/
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
                        conSql = "a.createtime";
                        break;
                    case 1:
                        conSql = "a.createtime1";
                        break;
                    case 2:
                        conSql = "b.fixDate";
                        break;
                    case 3:
                        conSql = "b.fixDate1";
                        break;
                }
                orderBy(conSql);
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
        setContentView(R.layout.activity_fix);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");

        /*读取数据*/
        SharedPreferences sharedPreferences = FixActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");
        SharedPreferences fixConfig = FixActivity.super.getSharedPreferences("fixConfig", Activity.MODE_PRIVATE);
        operbillCode = fixConfig.getString("operbillCode", "");
        fixDate = fixConfig.getString("fixDate", "");
        fixDate1 = fixConfig.getString("fixDate1", "");
        barCodeValue = fixConfig.getString("barCode","");

        initView();
        initEvent();
    }

    private void initView(){
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setVisibility(View.GONE);

        title = (TextView)findViewById(R.id.title);
        title.setText(R.string.assetFix);
        barCode = (EditText)findViewById(R.id.barCode);

        setSort = (ImageView)findViewById(R.id.setSort);
        setEdit = (ImageView)findViewById(R.id.setEdit);
        editPopWin = new BorrowEditPopWin(this,onClickListener);

        loadData = (ListView)findViewById(R.id.loadData);

        fixMenu = (FloatingActionButton)findViewById(R.id.fixMenu);
        rlAddBill = (RelativeLayout)findViewById(R.id.rlAddBill);
        for (int i = 0; i < llId.length;i++){
            ll[i] = (LinearLayout)findViewById(llId[i]);
        }
        for (int i = 0;i < fabId.length; i++){
            fab[i] = (FloatingActionButton)findViewById(fabId[i]);
        }

        //这两句代码必须的，为的是初始化出来gson这个对象，才能拿来用
        builder = new GsonBuilder();
        gson = builder.create();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String date = df.format(curDate);
        String[] split = date.split("-");
        year = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1])-1;
        day = Integer.parseInt(split[2]);

        selCon();
        bindEvents();
    }

    private void initEvent(){
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        barCode.setOnKeyListener(returnKey);
        setSort.setOnClickListener(this);
        setEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        switch (v.getId()){
            case R.id.btnBack:
                cleanConfig();
                Redirect.redirect(FixActivity.this, MainActivity.class,bundle);
                break;
            case R.id.btnQuit:
                cleanConfig();
                Redirect.quit(FixActivity.this);
                break;
            case R.id.setEdit:
                int[] location = new int[2];
                setEdit.getLocationOnScreen(location);
                int x = location[0];            //540
                int y = location[1];            //433

                editPopWin.query1.setText("维修单号");
                editPopWin.query2.setText("维修时间");

                editPopWin.borrowDepatment.setText(operbillCode);
                editPopWin.borrowDate.setText(fixDate);
                editPopWin.borrowDate1.setText(fixDate1);
                editPopWin.showAtLocation(v, Gravity.LEFT, x, -(y%100+17));
                break;
            case R.id.fixMenu:
                /*关闭键盘*/
                int icon = isOpen? android.R.drawable.ic_dialog_dialer:android.R.drawable.ic_menu_close_clear_cancel;
                fixMenu.setImageResource(icon);
                isOpen = !isOpen;
                rlAddBill.setVisibility(isOpen ? View.VISIBLE : View.GONE);
                break;
            case R.id.miniFab01:                                                                    //新增
                Redirect.redirect(FixActivity.this, FixAddActivity.class, bundle);
                hideFABMenu();
                break;
            case R.id.miniFab02:                                                                    //报审
                HashMap<Integer, Boolean> selCheck = fixStyleAdapter.selCheck;
                int selNum = 0;                                                                 //选中的数量
                int fixNoNum = 0;                                                           //维修信息没写的数量
                String codeRemeberNo = "";                                                //记录不是报审的借用单
                operbillCodes = "";                                                    //记录所选择的借用单号
                Integer examState = 0;
                for(int j = 0; j< fixStyleAdapter.getCount(); j++) {
                    if (selCheck.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetFixQuery assetFixQuery = (AssetFixQuery) fixStyleAdapter.getItem(j);
                        operbillCodes += assetFixQuery.getOperbillCode()+",";
                        examState = assetFixQuery.getAssetOperate().getState();
                        if (examState!=0 && examState!=11 && examState!=8){
                            codeRemeberNo += assetFixQuery.getOperbillCode()+",";
                        }
                        List<AssetFix> list = assetFixQuery.getAssetOperate().getList();
                        for (AssetFix fix: list){
                            if (fix.getFixState()==null||fix.getFixState().trim().equals("")||
                                    fix.getFixCompany()==null||fix.getFixCompany().trim().equals("")){
                                fixNoNum++;
                            }
                        }
                        selNum ++;
                    }
                }
                if(selNum==0){
                    Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                            .setTitle("资产维修提示")
                            .setMessage("请先选择要报审的维修单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (selNum>1){
                    Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                            .setTitle("资产维修提示")
                            .setMessage("每次只能操作一个维修单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    if(!codeRemeberNo.equals("")){                                 //选中不是为报审的借用单
                        Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                                .setTitle("资产维修提示")
                                .setMessage("该维修单不能报审！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }else if (fixNoNum>0){
                        Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                                .setTitle("资产维修提示")
                                .setMessage("请填写资产的维修信息，其中维修单位和维修结果是必填项！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }
                    else{                                                      //进行报审
                        operbillCodes = operbillCodes.substring(0, operbillCodes.length()-1);

                        //获取报审的信息
                        examineInfo(examState);
                    }
                }
                hideFABMenu();
                break;
            case R.id.miniFab03:                                                                    //审核
                HashMap<Integer, Boolean> selLook = fixStyleAdapter.selCheck;
                int selLookNum = 0;                                                         //选中的数量
                String codeRemeberNoLook = "";                                              //记录不是报审的借用单
                operbillCodes = "";                                                         //记录所选择的借用单号
                Integer throughState = 0;
                for(int j = 0; j< fixStyleAdapter.getCount(); j++) {
                    if (selLook.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetFixQuery assetFixQuery = (AssetFixQuery) fixStyleAdapter.getItem(j);
                        operbillCodes += assetFixQuery.getOperbillCode()+",";
                        throughState = assetFixQuery.getAssetOperate().getState();
                        if (throughState!=1 && throughState!=9){
                            codeRemeberNoLook += assetFixQuery.getOperbillCode()+",";
                        }
                        selLookNum ++;
                    }
                }
                if(selLookNum==0){
                    Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                            .setTitle("资产维修提示")
                            .setMessage("请先选择要审核的维修单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (selLookNum>1){
                    Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                            .setTitle("资产维修提示")
                            .setMessage("每次只能操作一个维修单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    if(!codeRemeberNoLook.equals("")){                                 //选中不是为报审的借用单
                        Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                                .setTitle("资产维修提示")
                                .setMessage("该维修单不能审核！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }else{                                                      //进行报审
                        operbillCodes = operbillCodes.substring(0, operbillCodes.length()-1);

                        //获取报审的信息
                        lookThroughInfo(throughState);
                    }
                }
                hideFABMenu();
                break;
            case R.id.miniFab04:                                                                    //删除
                HashMap<Integer, Boolean> state = fixStyleAdapter.selCheck;
                int noSelNum = 0;
                String codeRemeber = "";
                operbillCodes = "";
                for(int j = 0; j< fixStyleAdapter.getCount(); j++) {
                    if (state.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetFixQuery assetFixQuery = (AssetFixQuery) fixStyleAdapter.getItem(j);
                        operbillCodes += assetFixQuery.getOperbillCode()+",";
                        if (assetFixQuery.getAssetOperate().getState()!=0
                                && assetFixQuery.getAssetOperate().getState()!=11
                                && assetFixQuery.getAssetOperate().getState()!=8){
                            codeRemeber += assetFixQuery.getOperbillCode()+",";
                        }
                    }else {
                        noSelNum ++;
                    }
                }
                if(noSelNum== fixStyleAdapter.getCount()){
                    /*Toast.makeText(BorrowActivity.this,"请先选择要删除的借用单！",Toast.LENGTH_LONG).show();*/
                    Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                            .setTitle("资产维修提示")
                            .setMessage("请先选择要删除的维修单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (!codeRemeber.equals("")){
                    /*进行删除操作*/
                    Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("资产维修提示")
                            .setMessage("只能删除未报审、导入新增或者导入未报审的维修单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    /*进行删除操作*/
                    Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("确定删除吗？")
                            .setMessage("确定删除维修单号为"+operbillCodes.substring(0,operbillCodes.length()-1)+"的单据吗？")
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
            case R.id.miniFab05:                                                                    //修改
                HashMap<Integer, Boolean> updState = fixStyleAdapter.selCheck;
                int updNum = 0;                                                         //选中的数量
                String codeRemeberUpd = "";                                              //记录不是报审的借用单
                operbillCodes = "";                                                         //记录所选择的借用单号
                Integer stateUpd = 0;
                for(int j = 0; j< fixStyleAdapter.getCount(); j++) {
                    if (updState.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetFixQuery assetFixQuery = (AssetFixQuery) fixStyleAdapter.getItem(j);
                        operbillCodes += assetFixQuery.getOperbillCode()+",";
                        stateUpd = assetFixQuery.getAssetOperate().getState();
                        if (stateUpd!=0 && stateUpd!=11 && stateUpd!=8){
                            codeRemeberUpd += assetFixQuery.getOperbillCode()+",";
                        }
                        updNum ++;
                    }
                }
                if(updNum==0){
                    Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                            .setTitle("资产维修提示")
                            .setMessage("请先选择要修改的维修单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (updNum>1){
                    Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                            .setTitle("资产维修提示")
                            .setMessage("每次只能操作一个维修单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    if(!codeRemeberUpd.equals("")){                                 //选中不是为报审的借用单
                        Dialog dialog = new AlertDialog.Builder(FixActivity.this)
                                .setTitle("资产维修提示")
                                .setMessage("不能修改该维修单！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }else{                                                      //进行报审
                        operbillCodes = operbillCodes.substring(0, operbillCodes.length()-1);
                        bundle.putString("operbillCode", operbillCodes);
                        bundle.putInt("state", stateUpd);
                        Redirect.redirect(this,FixUpdActivity.class, bundle);
                    }
                }
                hideFABMenu();
                break;
        }
    }

    /*回车事件*/
    View.OnKeyListener returnKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                Redirect.hideIM(v,FixActivity.this);
                /*barCode.setText(barCode.getText().toString());// 添加这句后实现效果*/
                barCode.selectAll();
                barCodeValue = barCode.getText().toString();

                //指定操作的文件名称
                SharedPreferences sh = FixActivity.super.getSharedPreferences("fixConfig", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sh.edit();                      //编辑文件
                edit.putString("barCode", barCodeValue);          //保存
                edit.commit();

                //根据编码查询资产信息
                selCon();
                return true;
            }
            return false;
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save_pop:
                    operbillCode = editPopWin.borrowDepatment.getText().toString().trim();
                    fixDate = editPopWin.borrowDate.getText().toString().trim();
                    fixDate1 = editPopWin.borrowDate1.getText().toString().trim();

                    //指定操作的文件名称
                    SharedPreferences sh = FixActivity.super.getSharedPreferences("fixConfig", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sh.edit();                      //编辑文件
                    edit.putString("operbillCode", operbillCode);          //保存
                    edit.putString("fixDate", fixDate);
                    edit.putString("fixDate1", fixDate1);
                    edit.commit();

                    selCon();
                    editPopWin.dismiss();
                    break;
                case R.id.btn_cancel_pop:
                    editPopWin.borrowDepatment.setText("");
                    editPopWin.borrowDate.setText("");
                    editPopWin.borrowDate1.setText("");
                    cleanConfig();

                    operbillCode = editPopWin.borrowDepatment.getText().toString().trim();
                    fixDate = editPopWin.borrowDate.getText().toString().trim();
                    fixDate1 = editPopWin.borrowDate1.getText().toString().trim();

                    editPopWin.dismiss();
                    selCon();
                    break;
                case R.id.borrowDate:
                    Redirect.hideIM(v, FixActivity.this);
                    Dialog dialog = new DatePickerDialog(FixActivity.this, new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String date = year+"-"+(month+1)+"-"+dayOfMonth;
                            editPopWin.borrowDate.setText(date);
                        }
                    }, year,month,day);
                    dialog.show();
                    break;
                case R.id.borrowDate1:
                    Redirect.hideIM(v, FixActivity.this);
                    Dialog dialog1 = new DatePickerDialog(FixActivity.this, new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String date = year+"-"+(month+1)+"-"+dayOfMonth;
                            editPopWin.borrowDate1.setText(date);
                        }
                    }, year,month,day);
                    dialog1.show();
                    break;
            }
        }
    };

    /*返回按钮事件*/
    @Override
    public void onBackPressed() {
        cleanConfig();
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        Redirect.redirect(FixActivity.this, MainActivity.class,bundle);
    }

    private void bindEvents(){
        fixMenu.setOnClickListener(this);
        for (int i = 0;i < fabId.length; i++){
            fab[i].setOnClickListener(this);
        }
    }

    private void hideFABMenu(){
        rlAddBill.setVisibility(View.GONE);
        fixMenu.setImageResource(android.R.drawable.ic_dialog_dialer);
        isOpen = false;
    }

    /*清除数据*/
    private void cleanConfig(){
        //指定操作的文件名称
        SharedPreferences sh = FixActivity.super.getSharedPreferences("fixConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();                      //编辑文件
        edit.clear();
        edit.commit();
    }

    /*与数据库的交互*/
    /*排序*/
    private void orderBy(String conSql){
        if (!Redirect.checkNetwork(FixActivity.this)) {
            Toast toast = Toast.makeText(FixActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(FixActivity.this);
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
                String funcInfo = WebService.executeHttpGet(path);

                /*所有的数据*/
                String path1 = "http://" + ipStr + "/FixedAssService/fix/getOrder"
                    + "?deptUUID=" + user.getDeptUUID()+"&userUUID=" + user.getUserUUID()+"&conSql="+conSql;
                String info = WebService.executeHttpGet(path1);

                @Override
                public void run() {
                    ArrayList<Function> userFunc = StrConvertObject.strConvertFunction(funcInfo);
                    ArrayList<AssetFixQuery> fixList = StrConvertObject.setConvertAssetFixQuery(info);
                    setUserFunc(userFunc);
                    setFixList(fixList);
                }

            });
            dialog.dismiss();
        }
    }

    //筛选
    private void selCon(){
        String conSql = "operbillCode="+operbillCode+"&fixDate="+fixDate+"&fixDate1="+fixDate1+"&barCode="+barCodeValue;
        if (!Redirect.checkNetwork(FixActivity.this)) {
            Toast toast = Toast.makeText(FixActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(FixActivity.this);
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
                String funcInfo = WebService.executeHttpGet(path);

                String path1 = "http://" + ipStr + "/FixedAssService/fix/getSel"
                    + "?deptUUID=" + user.getDeptUUID()+"&userUUID=" + user.getUserUUID()+"&"+conSql;
                String info = WebService.executeHttpGet(path1);

                /*所有的数据*/
                @Override
                public void run() {
                    ArrayList<Function> userFunc = StrConvertObject.strConvertFunction(funcInfo);
                    ArrayList<AssetFixQuery> fixList = StrConvertObject.setConvertAssetFixQuery(info);
                    setUserFunc(userFunc);
                    setFixList(fixList);
                }
            });
            dialog.dismiss();
        }
    }

    /*获取报审信息*/
    private void examineInfo(Integer state){
        if (!Redirect.checkNetwork(FixActivity.this)) {
            Toast toast = Toast.makeText(FixActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(FixActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new ExamineInfoThread(state)).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class ExamineInfoThread implements Runnable {
        private Integer state;

        public ExamineInfoThread(Integer state) {
            this.state = state;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/fix/examineInfo"
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

                            LayoutInflater inflater = LayoutInflater.from(FixActivity.this);     //创建对象
                            final View dialogView = inflater.inflate(R.layout.dialog_examination,null);   //将布局文件转换成view

                            /*报审日期以及报审意见*/
                            TableRow setDate = (TableRow)dialogView.findViewById(R.id.setDate);
                            TableRow setNote = (TableRow)dialogView.findViewById(R.id.setNote);
                            if (state==8){
                                setDate.setVisibility(View.GONE);
                                setNote.setVisibility(View.GONE);
                            }

                            final EditText operbillCode = (EditText)dialogView.findViewById(R.id.operbillCode);   //单号
                            operbillCode.setText(user.getUserPhone());
                            final EditText examinePeople = (EditText)dialogView.findViewById(R.id.examinePeople); //报审人
                            examinePeople.setText(user.getUserName());
                            final EditText examineDate = (EditText)dialogView.findViewById(R.id.examineDate);     //报审日期
                            examineDate.setText(user.getUserAddr());

                            Spinner reviewPeople = (Spinner)dialogView.findViewById(R.id.reviewPeople);        //审核人
                            reviewPeople.setPrompt("请选择审核人：");

                            //获得下拉列表适配器
                            ArrayAdapter<CharSequence> adapterReview = new ArrayAdapter<CharSequence>(FixActivity.this,android.R.layout.simple_spinner_item,userNameList);
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
                            Dialog dialog1 = new AlertDialog.Builder(FixActivity.this)
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
                                            String conSql = "operbillCode="+operbill+"&state="+state+"&reviewPeople="+reviewU+"&examineNote="+examineN;
                                            exam(conSql);
                                            //隐藏键盘
                                            Redirect.hideIM(dialogView, FixActivity.this);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Redirect.hideIM(dialogView,FixActivity.this);
                                        }
                                    })
                                    .create();
                            dialog1.show();
                        }else{
                            Toast.makeText(FixActivity.this,msg,Toast.LENGTH_LONG).show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }finally {
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    //报审
    private void exam(String conSql){
        if (!Redirect.checkNetwork(FixActivity.this)) {
            Toast toast = Toast.makeText(FixActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(FixActivity.this);
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
                String path = "http://" + ipStr + "/FixedAssService/fix/examine"
                    + "?userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(FixActivity.this,"报审成功",Toast.LENGTH_LONG).show();
                        selCon();
                    }else {
                        Toast.makeText(FixActivity.this,"报审失败，请重新进行报审操作",Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    /*获取审核信息*/
    private void lookThroughInfo(Integer throughState){
        if (!Redirect.checkNetwork(FixActivity.this)) {
            Toast toast = Toast.makeText(FixActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(FixActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new LookThroughInfoThread(throughState)).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class LookThroughInfoThread implements Runnable {
        private Integer throughState;

        public LookThroughInfoThread(Integer throughState) {
            this.throughState = throughState;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/fix/lookThroughInfo"
                    + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(info);
                        String msg = jsonArray.getJSONObject(0).getString("msg");
                        if(msg.equals("")){
                            JSONObject fixOperateObject = jsonArray.getJSONObject(1);
                            AssetOperate<AssetFix> operateFix = StrConvertObject.setConvertAssetOperateFix(fixOperateObject);

                            LayoutInflater inflater = LayoutInflater.from(FixActivity.this);                      //创建对象
                            final View dialogView = inflater.inflate(R.layout.dialog_lookthrough,null);   //将布局文件转换成view
                            final EditText operbillCode = (EditText)dialogView.findViewById(R.id.operbillCode);   //单号
                            operbillCode.setText(operateFix.getOperbillCode());
                            EditText examinePeople = (EditText) dialogView.findViewById(R.id.examinePeople);       //报审人
                            examinePeople.setText(operateFix.getExamUser().getUserName());
                            EditText examineDate = (EditText) dialogView.findViewById(R.id.examineDate);            //报审日期
                            examineDate.setText(operateFix.getExamdate());
                            final RadioGroup isAgree = (RadioGroup)dialogView.findViewById(R.id.isAgree);                 //是否同意

                            TableRow setThroughNote = (TableRow)dialogView.findViewById(R.id.setThroughNote);
                            if (throughState==9){
                                setThroughNote.setVisibility(View.GONE);
                            }

                            final EditText throughNote = (EditText)dialogView.findViewById(R.id.throughNote);             //审核意见
                            Dialog dialog = new AlertDialog.Builder(FixActivity.this)
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

                                            String conSql = "operbillCode="+operbill+"&state="+throughState+
                                                    "&reviewnote="+reviewnote+"&ischeck="+isCheck;
                                            lookThrough(conSql);
                                            //隐藏键盘
                                            Redirect.hideIM(dialogView, FixActivity.this);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Redirect.hideIM(dialogView, FixActivity.this);
                                        }
                                    })
                                    .create();
                            dialog.show();
                        }else{
                            Toast.makeText(FixActivity.this,msg,Toast.LENGTH_LONG).show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    finally {
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    //审核
    private void lookThrough(String conSql){
        if (!Redirect.checkNetwork(FixActivity.this)) {
            Toast toast = Toast.makeText(FixActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(FixActivity.this);
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
                String path = "http://" + ipStr + "/FixedAssService/fix/lookThrough"
                    + "?userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(FixActivity.this,"审核成功",Toast.LENGTH_LONG).show();
                        selCon();
                    }
                    else if (num==-10){
                        Toast.makeText(FixActivity.this,"您无审核该检验单的权限！",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(FixActivity.this,"审核失败，请重新进行审核操作",Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    private void delete(){
        if (!Redirect.checkNetwork(FixActivity.this)) {
            Toast toast = Toast.makeText(FixActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            /*dialog = new ProgressDialog(FixActivity.this);
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
                String path = "http://" + ipStr + "/FixedAssService/fix/delete"
                    + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(FixActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                        selCon();
                    }else {
                        Toast.makeText(FixActivity.this,"删除失败，请重新进行删除操作",Toast.LENGTH_LONG).show();
                    }
                }
            });
//            dialog.dismiss();
        }
    }

    /*设置权限*/
    private void setUserFunc(ArrayList<Function> userFunc){
        for (Function function:userFunc){
            switch (function.getFuncURL()){
                case "/assetFix/add":
                    ll[0].setVisibility(View.VISIBLE);
                    break;
                case "/assetFix/delete":
                    ll[3].setVisibility(View.VISIBLE);
                    break;
                case "/assetFix/exam":
                    ll[1].setVisibility(View.VISIBLE);
                    break;
                case "/assetFix/lookThrough":
                    ll[2].setVisibility(View.VISIBLE);
                    break;
                case "/assetFix/update":
                    ll[4].setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    /*设置借用单号*/
    private void setFixList(ArrayList<AssetFixQuery> fixList){
        //  建立Adapter绑定数据源
        fixStyleAdapter = new FixStyleAdapter(fixList, this, fragid, user);
        //绑定Adapter
        loadData.setAdapter(fixStyleAdapter);
    }
    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }
    public void setReviewUser(User reviewUser) {
        this.reviewUser = reviewUser;
    }
}