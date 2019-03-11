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
import fixedass.xiangrong.com.fixedass.bean.AssetOperate;
import fixedass.xiangrong.com.fixedass.bean.AssetScrap;
import fixedass.xiangrong.com.fixedass.bean.AssetScrapQuery;
import fixedass.xiangrong.com.fixedass.bean.Function;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;

/**
 * @author Eileen
 * @create 2018/10/9
 * @Describe 资产报废
 */
public class ScrapActivity extends Activity implements View.OnClickListener {
    private ImageView btnBack,btnQuit;
    private TextView title;
    private EditText barCode;
    private String barCodeValue;

    /*private TextView setSort,setEdit;*/
    private ImageView setSort,setEdit;
    private String[] popContents = new String[]{"创建时间","创建时间逆序","报废日期","报废日期逆序"};       //排序
    private BorrowEditPopWin editPopWin;
    private String operbillCode, scrapDate, scrapDate1;

    private ListView loadData;                          //加载适配器
    private ScrapStyleAdapter scrapStyleAdapter;        //适配器

    /*悬浮菜单设置*/
    private FloatingActionButton scrapMenu;    //悬浮菜单
    private Boolean isOpen = false;
    private RelativeLayout rlAddBill;
    private int[] llId = new int[]{R.id.ll01,R.id.ll02,R.id.ll03,R.id.ll04,R.id.ll05};
    private LinearLayout[] ll = new LinearLayout[llId.length];
    private int[] fabId = new int[]{R.id.miniFab01,R.id.miniFab02,R.id.miniFab03,R.id.miniFab04,R.id.miniFab05};
    private FloatingActionButton[] fab = new FloatingActionButton[fabId.length];

    private int fragid;
    private String ipStr;
    private User user;

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
                        conSql = "b.scrapDate";
                        break;
                    case 3:
                        conSql = "b.scrapDate1";
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
        setContentView(R.layout.activity_scrap);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");

        /*读取数据*/
        SharedPreferences sharedPreferences = ScrapActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        //查询条件
        SharedPreferences scrapConfig = ScrapActivity.super.getSharedPreferences("scrapConfig", Activity.MODE_PRIVATE);
        operbillCode = scrapConfig.getString("operbillCode", "");
        scrapDate = scrapConfig.getString("scraptDate", "");
        scrapDate1 = scrapConfig.getString("scrapDate1", "");
        barCodeValue = scrapConfig.getString("barCode","");

        initView();
        initEvent();
    }

    private void initView(){
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setVisibility(View.GONE);

        title = (TextView)findViewById(R.id.title);
        title.setText(R.string.assetScrap);
        barCode = (EditText)findViewById(R.id.barCode);

        setSort = (ImageView)findViewById(R.id.setSort);
        setEdit = (ImageView)findViewById(R.id.setEdit);
        editPopWin = new BorrowEditPopWin(this,onClickListener);

        loadData = (ListView)findViewById(R.id.loadData);

        scrapMenu = (FloatingActionButton)findViewById(R.id.scrapMenu);
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
                Redirect.redirect(ScrapActivity.this, MainActivity.class,bundle);
                break;
            case R.id.btnQuit:
                cleanConfig();
                Redirect.quit(ScrapActivity.this);
                break;
            case R.id.setEdit:
                int[] location = new int[2];
                setEdit.getLocationOnScreen(location);
                int x = location[0];            //540
                int y = location[1];            //433

                editPopWin.query1.setText("报废单号");
                editPopWin.query2.setText("报废时间");

                editPopWin.borrowDepatment.setText(operbillCode);
                editPopWin.borrowDate.setText(scrapDate);
                editPopWin.borrowDate1.setText(scrapDate1);
                editPopWin.showAtLocation(v, Gravity.LEFT, x, -(y%100+17));
                break;
            case R.id.scrapMenu:
                /*关闭键盘*/
                int icon = isOpen? android.R.drawable.ic_dialog_dialer:android.R.drawable.ic_menu_close_clear_cancel;
                scrapMenu.setImageResource(icon);
                isOpen = !isOpen;
                rlAddBill.setVisibility(isOpen ? View.VISIBLE : View.GONE);
                break;
            case R.id.miniFab01:                                                                    //新增
                Redirect.redirect(ScrapActivity.this, ScrapAddActivity.class, bundle);
                hideFABMenu();
                break;
            case R.id.miniFab02:                                                        //报审
                HashMap<Integer, Boolean> selCheck = scrapStyleAdapter.selCheck;
                int selNum = 0;                                                         //选中的数量
                int scrapNoNum = 0;                                                     //没填写报废单的数量
                String codeRemeberNo = "";                                                //记录不是报审的报废单
                operbillCodes = "";                                                    //记录所选择的报废单号
                Integer examState = 0;
                for(int j = 0; j< scrapStyleAdapter.getCount(); j++) {
                    if (selCheck.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetScrapQuery assetScrapQuery = (AssetScrapQuery) scrapStyleAdapter.getItem(j);
                        operbillCodes += assetScrapQuery.getOperbillCode()+",";
                        examState = assetScrapQuery.getAssetOperate().getState();
                        if (examState!=0 && examState!=11 && examState!=8){
                            codeRemeberNo += assetScrapQuery.getOperbillCode()+",";
                        }
                        List<AssetScrap> list = assetScrapQuery.getAssetOperate().getList();
                        for (AssetScrap scrap: list){
                            if (scrap.getUnuseInfo()==null||scrap.getUnuseInfo().trim().equals("")){
                                scrapNoNum++;
                            }
                        }
                        selNum ++;
                    }
                }
                if(selNum==0){
                    Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                            .setTitle("资产报废提示")
                            .setMessage("请先选择要报审的报废单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (selNum>1){
                    Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                            .setTitle("资产报废提示")
                            .setMessage("每次只能操作一个报废单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    if(!codeRemeberNo.equals("")){                                 //选中不是为报审的报废单
                        Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                                .setTitle("资产报废提示")
                                .setMessage("该报废单不能报审！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }
                    else if (scrapNoNum>0){
                        Dialog dialog = new AlertDialog.Builder(this)
                                .setTitle("资产报废提示")
                                .setMessage("请填写资产的报废信息，其中报废原因是必填项！")
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
                HashMap<Integer, Boolean> selLook = scrapStyleAdapter.selCheck;
                int selLookNum = 0;                                                         //选中的数量
                String codeRemeberNoLook = "";                                              //记录不是报审的报废单
                operbillCodes = "";                                                         //记录所选择的报废单号
                Integer throughState = 0;
                for(int j = 0; j< scrapStyleAdapter.getCount(); j++) {
                    if (selLook.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetScrapQuery assetScrapQuery = (AssetScrapQuery) scrapStyleAdapter.getItem(j);
                        operbillCodes += assetScrapQuery.getOperbillCode()+",";
                        throughState = assetScrapQuery.getAssetOperate().getState();
                        if (throughState!=1 && throughState!=9){
                            codeRemeberNoLook += assetScrapQuery.getOperbillCode()+",";
                        }
                        selLookNum ++;
                    }
                }
                if(selLookNum==0){
                    Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                            .setTitle("资产报废提示")
                            .setMessage("请先选择要审核的报废单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (selLookNum>1){
                    Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                            .setTitle("资产报废提示")
                            .setMessage("每次只能操作一个报废单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    if(!codeRemeberNoLook.equals("")){                                 //选中不是为报审的报废单
                        Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                                .setTitle("资产报废提示")
                                .setMessage("该报废单不能审核！")
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
                HashMap<Integer, Boolean> state = scrapStyleAdapter.selCheck;
                int noSelNum = 0;
                String codeRemeber = "";
                operbillCodes = "";
                for(int j = 0; j< scrapStyleAdapter.getCount(); j++) {
                    if (state.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetScrapQuery assetScrapQuery = (AssetScrapQuery) scrapStyleAdapter.getItem(j);
                        operbillCodes += assetScrapQuery.getOperbillCode()+",";
                        if (assetScrapQuery.getAssetOperate().getState()!=0
                                && assetScrapQuery.getAssetOperate().getState()!=11
                                && assetScrapQuery.getAssetOperate().getState()!=8){
                            codeRemeber += assetScrapQuery.getOperbillCode()+",";
                        }
                    }else {
                        noSelNum ++;
                    }
                }
                if(noSelNum== scrapStyleAdapter.getCount()){
                    /*Toast.makeText(BorrowActivity.this,"请先选择要删除的报废单！",Toast.LENGTH_LONG).show();*/
                    Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                            .setTitle("资产报废提示")
                            .setMessage("请先选择要删除的报废单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (!codeRemeber.equals("")){
                    /*进行删除操作*/
                    Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("资产报废提示")
                            .setMessage("只能删除未报审、导入新增或者导入未报审的报废单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    /*进行删除操作*/
                    Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("确定删除吗？")
                            .setMessage("确定删除报废单号为"+operbillCodes.substring(0,operbillCodes.length()-1)+"的单据吗？")
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
            case R.id.miniFab05:                                                                    //编辑
                HashMap<Integer, Boolean> updState = scrapStyleAdapter.selCheck;
                int updNum = 0;                                                         //选中的数量
                String codeRemeberUpd = "";                                              //记录不是报审的报废单
                operbillCodes = "";                                                         //记录所选择的报废单号
                Integer stateUpd = 0;
                for(int j = 0; j< scrapStyleAdapter.getCount(); j++) {
                    if (updState.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetScrapQuery assetScrapQuery = (AssetScrapQuery) scrapStyleAdapter.getItem(j);
                        operbillCodes += assetScrapQuery.getOperbillCode()+",";
                        stateUpd = assetScrapQuery.getAssetOperate().getState();
                        if (stateUpd!=0 && stateUpd!=11){
                            codeRemeberUpd += assetScrapQuery.getOperbillCode()+",";
                        }
                        updNum ++;
                    }
                }
                if(updNum==0){
                    Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                            .setTitle("资产报废提示")
                            .setMessage("请先选择要修改的报废单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else if (updNum>1){
                    Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                            .setTitle("资产报废提示")
                            .setMessage("每次只能操作一个报废单！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    if(!codeRemeberUpd.equals("")){                                 //选中不是为报审的报废单
                        Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
                                .setTitle("资产报废提示")
                                .setMessage("不能修改该报废单！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }else{                                                      //进行报审
                        operbillCodes = operbillCodes.substring(0, operbillCodes.length()-1);
                        bundle.putString("operbillCode", operbillCodes);
                        Redirect.redirect(this,ScrapUpdActivity.class, bundle);
                    }
                }
                hideFABMenu();
                break;
        }
    }

    View.OnKeyListener returnKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                Redirect.hideIM(v,ScrapActivity.this);
                /*barCode.setText(barCode.getText().toString());// 添加这句后实现效果*/
                barCode.selectAll();
                barCodeValue = barCode.getText().toString();

                //指定操作的文件名称
                SharedPreferences sh = ScrapActivity.super.getSharedPreferences("scrapConfig", Context.MODE_PRIVATE);
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
                    scrapDate = editPopWin.borrowDate.getText().toString().trim();
                    scrapDate1 = editPopWin.borrowDate1.getText().toString().trim();

                    //指定操作的文件名称
                    SharedPreferences sh = ScrapActivity.super.getSharedPreferences("scrapConfig", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sh.edit();                      //编辑文件
                    edit.putString("operbillCode", operbillCode);          //保存
                    edit.putString("scrapDate", scrapDate);
                    edit.putString("scrapDate1", scrapDate1);
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
                    scrapDate = editPopWin.borrowDate.getText().toString().trim();
                    scrapDate1 = editPopWin.borrowDate1.getText().toString().trim();

                    editPopWin.dismiss();
                    selCon();
                    break;
                case R.id.borrowDate:
                    Redirect.hideIM(v, ScrapActivity.this);
                    Dialog dialog = new DatePickerDialog(ScrapActivity.this, new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String date = year+"-"+(month+1)+"-"+dayOfMonth;
                            editPopWin.borrowDate.setText(date);
                        }
                    }, year,month,day);
                    dialog.show();
                    break;
                case R.id.borrowDate1:
                    Redirect.hideIM(v, ScrapActivity.this);
                    Dialog dialog1 = new DatePickerDialog(ScrapActivity.this, new DatePickerDialog.OnDateSetListener(){
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

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        Redirect.redirect(ScrapActivity.this, MainActivity.class,bundle);
    }

    private void bindEvents(){
        scrapMenu.setOnClickListener(this);
        for (int i = 0;i < fabId.length; i++){
            fab[i].setOnClickListener(this);
        }
    }

    private void hideFABMenu(){
        rlAddBill.setVisibility(View.GONE);
        scrapMenu.setImageResource(android.R.drawable.ic_dialog_dialer);
        isOpen = false;
    }

    /*清除数据*/
    private void cleanConfig(){
        //指定操作的文件名称
        SharedPreferences sh = ScrapActivity.super.getSharedPreferences("scrapConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();                      //编辑文件
        edit.clear();
        edit.commit();
    }

    /*与数据库的交互*/
    /*排序*/
    private void orderBy(String conSql){
        if (!Redirect.checkNetwork(ScrapActivity.this)) {
            Toast toast = Toast.makeText(ScrapActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(ScrapActivity.this);
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
                String path1 = "http://" + ipStr + "/FixedAssService/scrap/getOrder"
                    + "?deptUUID=" + user.getDeptUUID()+"&userUUID=" + user.getUserUUID()+"&conSql="+conSql;
                String info = WebService.executeHttpGet(path1);

                @Override
                public void run() {
                    ArrayList<Function> userFunc = StrConvertObject.strConvertFunction(funcInfo);
                    ArrayList<AssetScrapQuery> scrapList = StrConvertObject.setConvertAssetScrapQuery(info);

                    setUserFunc(userFunc);
                    setScrapList(scrapList);
                }
            });
            dialog.dismiss();
        }
    }

    //筛选
    private void selCon(){
        String conSql = "operbillCode="+operbillCode+"&scrapDate="+scrapDate+"&scrapDate1="+scrapDate1+"&barCode="+barCodeValue;
        if (!Redirect.checkNetwork(ScrapActivity.this)) {
            Toast toast = Toast.makeText(ScrapActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(ScrapActivity.this);
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

                String path1 = "http://" + ipStr + "/FixedAssService/scrap/getSel"
                    + "?deptUUID=" + user.getDeptUUID()+"&userUUID=" + user.getUserUUID()+"&"+conSql;
                String info = WebService.executeHttpGet(path1);
                /*所有的数据*/
                @Override
                public void run() {
                    ArrayList<Function> userFunc = StrConvertObject.strConvertFunction(funcInfo);
                    ArrayList<AssetScrapQuery> scrapList = StrConvertObject.setConvertAssetScrapQuery(info);
                    setUserFunc(userFunc);
                    setScrapList(scrapList);
                }
            });
            dialog.dismiss();
        }
    }

    /*获取报审信息*/
    private void examineInfo(Integer state){
        if (!Redirect.checkNetwork(ScrapActivity.this)) {
            Toast toast = Toast.makeText(ScrapActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(ScrapActivity.this);
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
                String path = "http://" + ipStr + "/FixedAssService/scrap/examineInfo"
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

                            LayoutInflater inflater = LayoutInflater.from(ScrapActivity.this);     //创建对象
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
                            ArrayAdapter<CharSequence> adapterReview = new ArrayAdapter<CharSequence>(ScrapActivity.this,android.R.layout.simple_spinner_item,userNameList);
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
                            Dialog dialog1 = new AlertDialog.Builder(ScrapActivity.this)
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
                                            Redirect.hideIM(dialogView, ScrapActivity.this);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Redirect.hideIM(dialogView,ScrapActivity.this);
                                        }
                                    })
                                    .create();
                            dialog1.show();

                        }else{
                            Toast.makeText(ScrapActivity.this,msg,Toast.LENGTH_LONG).show();
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
        if (!Redirect.checkNetwork(ScrapActivity.this)) {
            Toast toast = Toast.makeText(ScrapActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(ScrapActivity.this);
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
                String path = "http://" + ipStr + "/FixedAssService/scrap/examine"
                    + "?userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(ScrapActivity.this,"报审成功",Toast.LENGTH_LONG).show();
                        selCon();
                    }else {
                        Toast.makeText(ScrapActivity.this,"报审失败，请重新进行报审操作",Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    /*获取审核信息*/
    private void lookThroughInfo(Integer throughState){
        if (!Redirect.checkNetwork(ScrapActivity.this)) {
            Toast toast = Toast.makeText(ScrapActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(ScrapActivity.this);
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
                String path = "http://" + ipStr + "/FixedAssService/scrap/lookThroughInfo"
                    + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(info);
                        String msg = jsonArray.getJSONObject(0).getString("msg");
                        if(msg.equals("")){
                            JSONObject scrapOperateObject = jsonArray.getJSONObject(1);
                            AssetOperate<AssetScrap> operateScrap = StrConvertObject.setConvertAssetOperateScrap(scrapOperateObject);

                            LayoutInflater inflater = LayoutInflater.from(ScrapActivity.this);                      //创建对象
                            final View dialogView = inflater.inflate(R.layout.dialog_lookthrough,null);   //将布局文件转换成view
                            final EditText operbillCode = (EditText)dialogView.findViewById(R.id.operbillCode);   //单号
                            operbillCode.setText(operateScrap.getOperbillCode());
                            EditText examinePeople = (EditText) dialogView.findViewById(R.id.examinePeople);       //报审人
                            examinePeople.setText(operateScrap.getExamUser().getUserName());
                            EditText examineDate = (EditText) dialogView.findViewById(R.id.examineDate);            //报审日期
                            examineDate.setText(operateScrap.getExamdate());
                            final RadioGroup isAgree = (RadioGroup)dialogView.findViewById(R.id.isAgree);                 //是否同意

                            TableRow setThroughNote = (TableRow)dialogView.findViewById(R.id.setThroughNote);
                            if (throughState==9){
                                setThroughNote.setVisibility(View.GONE);
                            }

                            final EditText throughNote = (EditText)dialogView.findViewById(R.id.throughNote);             //审核意见
                            Dialog dialog = new AlertDialog.Builder(ScrapActivity.this)
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
                                            Redirect.hideIM(dialogView, ScrapActivity.this);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Redirect.hideIM(dialogView, ScrapActivity.this);
                                        }
                                    })
                                    .create();
                            dialog.show();
                        }else{
                            Toast.makeText(ScrapActivity.this,msg,Toast.LENGTH_LONG).show();
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

    //审核
    private void lookThrough(String conSql){
        if (!Redirect.checkNetwork(ScrapActivity.this)) {
            Toast toast = Toast.makeText(ScrapActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(ScrapActivity.this);
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
                String path = "http://" + ipStr + "/FixedAssService/scrap/lookThrough"
                    + "?userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(ScrapActivity.this,"审核成功",Toast.LENGTH_LONG).show();
                        selCon();
                    }
                    else {
                        Toast.makeText(ScrapActivity.this,"审核失败，请重新进行审核操作",Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    private void delete(){
        if (!Redirect.checkNetwork(ScrapActivity.this)) {
            Toast toast = Toast.makeText(ScrapActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            /*dialog = new ProgressDialog(ScrapActivity.this);
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
                String path = "http://" + ipStr + "/FixedAssService/scrap/delete"
                    + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodes;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(ScrapActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                        selCon();
                    }else {
                        Toast.makeText(ScrapActivity.this,"删除失败，请重新进行删除操作",Toast.LENGTH_LONG).show();
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
                case "/assetScrap/add":
                    ll[0].setVisibility(View.VISIBLE);
                    break;
                case "/assetScrap/exam":
                    ll[1].setVisibility(View.VISIBLE);
                    break;
                case "/assetScrap/lookThrough":
                    ll[2].setVisibility(View.VISIBLE);
                    break;
                case "/assetScrap/delete":
                    ll[3].setVisibility(View.VISIBLE);
                    break;
                case "/assetScrap/update":
                    ll[4].setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    /*设置借用单号*/
    private void setScrapList(ArrayList<AssetScrapQuery> scrapList){
        //  建立Adapter绑定数据源
        scrapStyleAdapter = new ScrapStyleAdapter(scrapList, this, fragid, user);
        //绑定Adapter
        loadData.setAdapter(scrapStyleAdapter);
    }
    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }
    public void setReviewUser(User reviewUser) {
        this.reviewUser = reviewUser;
    }
}