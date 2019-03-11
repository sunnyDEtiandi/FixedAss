package fixedass.xiangrong.com.fixedass.manage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.asset.AssetAdapter;
import fixedass.xiangrong.com.fixedass.asset.TransferUpdAssetActivity;
import fixedass.xiangrong.com.fixedass.bean.Address;
import fixedass.xiangrong.com.fixedass.bean.AssetChange;
import fixedass.xiangrong.com.fixedass.bean.AssetOperate;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.DataBean;
import fixedass.xiangrong.com.fixedass.bean.DptPeople;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.Convert;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.tree.TransferUpdUserActivity;
import fixedass.xiangrong.com.fixedass.tree.TransferUpdUserFragment;
import fixedass.xiangrong.com.fixedass.zxing.android.CaptureActivity;
import fixedass.xiangrong.com.fixedass.zxing.common.Constant;

/*添加资产资产选择显示在界面上，把每个值传给其他两个activity*/
public class TransferUpdActivity extends Activity implements View.OnClickListener {
    /*private Button btnQuit,btnBack;*/
    private ImageView btnQuit,btnBack;
    private TextView title;
    private int fragid, state;
    private String ipStr, operbillCode;
    private User user;

    //新使用人、转移时间、新保管员、新存放地址、备注信息
    private EditText transferCode,newUsePeople,changeDate,newCareMan,newPlace,operNote;
    private String newUsePeopleStr,changeDateStr,newCareManStr,newPlaceStr,operNoteStr;                      //对应每个文本的内容

    private Spinner careMan, place;
    private String careManUUID, placeUUID, placeName;
    private int careManPosition = 0, newPlacePosition = 0;
    private ArrayAdapter<String> adapterPlaceReview;

    private String pName, pUUID;

    private ListView transferAsset;                                 //转移的资产
    private AssetAdapter assetAdapter;                              //适配器

    //添加资产、扫描资产、删除资产
    private TextView addAsset, scanAsset, delAsset;
    // 创建等待框
    private ProgressDialog dialog;
    // 返回主线程更新数据
    private static Handler handler = new Handler();

    /*转移的资产*/
    private DataBean<AssetStorage> stoData;
    private ArrayList<AssetStorage> stoList = new ArrayList<>();

    //日期
    private Integer year,month,day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_upd);

        ExitApplication.getInstance().addActivity(this);

        /*读取数据*/
        SharedPreferences sharedPreferences = TransferUpdActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");
        operbillCode = bundle.getString("operbillCode");
        state = bundle.getInt("state");

        if(bundle.containsKey("newUsePeople")){
            newUsePeopleStr = bundle.getString("newUsePeople");
            newCareManStr = bundle.getString("newCareMan");
            newPlaceStr = bundle.getString("newPlace");
            changeDateStr = bundle.getString("changeDate");
            operNoteStr = bundle.getString("operNote");
        }

        if (bundle.containsKey("pName")){
            pName = bundle.getString("pName");
            pUUID = bundle.getString("pUUID");

            if (state==11){
                getCareMan();

                careManUUID = bundle.getString("newCareManUUID");
                careManPosition = bundle.getInt("careManPosition");

                placeUUID = bundle.getString("newPlaceUUID");
                newPlacePosition = bundle.getInt("newPlacePosition");
            }
        }

        if (bundle.containsKey("stoData")){
            stoData = (DataBean<AssetStorage>) bundle.getSerializable("stoData");
            List<AssetStorage> asList = stoData.getData();
            stoList.addAll(asList);
        }

        initView();
        initEvent();
    }

    private void initView(){
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setImageResource(R.drawable.save);

        title = (TextView)findViewById(R.id.title);
        title.setText("编辑资产转移单据");
        transferCode = (EditText)findViewById(R.id.transferCode);
        newUsePeople = (EditText)findViewById(R.id.newUsePeople);
        changeDate = (EditText)findViewById(R.id.changeDate);
        newCareMan = (EditText)findViewById(R.id.newCareMan);
        newPlace = (EditText)findViewById(R.id.newPlace);
        operNote = (EditText)findViewById(R.id.operNote);

        transferAsset = (ListView)findViewById(R.id.transferAsset);

        addAsset = (TextView) findViewById(R.id.addAsset);
        scanAsset = (TextView) findViewById(R.id.scanAsset);
        delAsset = (TextView) findViewById(R.id.delAsset);

        careMan = (Spinner) findViewById(R.id.careMan);
        place = (Spinner) findViewById(R.id.newPlaceUUID);
        if (state==11){
            careMan.setVisibility(View.VISIBLE);
            place.setVisibility(View.VISIBLE);

            newCareMan.setVisibility(View.GONE);
            newPlace.setVisibility(View.GONE);

            careMan.setPrompt("请选择保管员：");
            careMan.setSelection(careManPosition, true);

            place.setPrompt("请选择存放地点：");
            place.setSelection(newPlacePosition, true);
        }

        if (changeDateStr==null){
            initData();
        }else {
            transferCode.setText(operbillCode);

            if (state==11){
                newUsePeople.setText(pName);

                if (changeDateStr==null||changeDateStr.isEmpty()){
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date curDate = new Date(System.currentTimeMillis());
                    changeDateStr = df.format(curDate);
                    System.out.println("=====initEvent========="+changeDateStr);
                    changeDate.setText(changeDateStr);
                }
                String[] split = changeDateStr.split("-");
                year = Integer.parseInt(split[0]);
                month = Integer.parseInt(split[1])-1;
                day = Integer.parseInt(split[2]);
            }else {
                newUsePeople.setText(newUsePeopleStr);
            }

            changeDate.setText(changeDateStr);
            newCareMan.setText(newCareManStr);
            newPlace.setText(newPlaceStr);
            operNote.setText(operNoteStr);

            if (stoList.size()>0){
                setAsset(stoList);
            }
        }
    }

    private void initEvent(){
        btnQuit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        if (state==11){
            newUsePeople.setOnClickListener(this);
            changeDate.setOnClickListener(this);
        }

        addAsset.setOnClickListener(this);
        scanAsset.setOnClickListener(this);
        delAsset.setOnClickListener(this);
    }

    private void initData(){
        if (!Redirect.checkNetwork(this)) {
            Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
//            初始化借还单信息
            new Thread(new IntiDataThread()).start();
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);

        //获取文本框数据
        operNoteStr = operNote.getText().toString();
        switch (v.getId()){
            case R.id.btnBack:          //返回
                Redirect.redirect(TransferUpdActivity.this, TransferActivity.class, bundle);
                break;
            case R.id.btnQuit:          //保存
                if (state==11){
                    if (pUUID.trim().equals("")){
                        Dialog dialog = new AlertDialog.Builder(this)
                                .setTitle("资产转移提示")
                                .setMessage("新使用人不能为空！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                        break;
                    }
                    if (careManUUID.trim().equals("")){
                        Dialog dialog = new AlertDialog.Builder(this)
                                .setTitle("资产转移提示")
                                .setMessage("新保管员不能为空！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                        break;
                    }
                    if (placeUUID.trim().equals("")){
                        Dialog dialog = new AlertDialog.Builder(this)
                                .setTitle("资产转移提示")
                                .setMessage("新存放地址不能为空！")
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                        break;
                    }

                    placeName = adapterPlaceReview.getItem(newPlacePosition);
                    try {
                        placeName = URLEncoder.encode(placeName, "utf-8");
                    }catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    operNoteStr = URLEncoder.encode(operNoteStr, "utf-8");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                if (stoList.isEmpty()){
                    Dialog dialog = new AlertDialog.Builder(TransferUpdActivity.this)
                            .setTitle("资产转移提示")
                            .setMessage("请选择要转移的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }

                String assetUUID = "";
                for (AssetStorage sto: stoList){
                    assetUUID =  assetUUID+sto.getAssetUUID()+",";
                }
                assetUUID = assetUUID.substring(0,assetUUID.length()-1);

                String conSql = "";
                if (state==11){
                    conSql += "newPeople="+pUUID+"&changeDate="+changeDateStr+"&newPlaceCode="
                            +placeUUID+"&placeName="+placeName+"&careMan="+careManUUID+"&state="+state+"&";
                }
                conSql += "operbillCode="+operbillCode+"&state="+state+"&operNote="+operNoteStr+"&assetUUID="+assetUUID;

                update(conSql);
                break;
            case R.id.addAsset:                 //添加资产
                setBundle(bundle);
                Redirect.redirect(TransferUpdActivity.this, TransferUpdAssetActivity.class, bundle);
                break;
            case R.id.scanAsset:                //扫描资产
                ActivityCompat.requestPermissions(TransferUpdActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                Intent intent = new Intent(TransferUpdActivity.this, CaptureActivity.class);
                startActivityForResult(intent, Convert.REQUEST_CODE_SCAN);
                break;
            case  R.id.delAsset:                //删除资产
                HashMap<Integer, Boolean> selCheck = assetAdapter.selCheck;
                final List<AssetStorage> stoLists = new ArrayList<>();
                for(int j = 0; j< assetAdapter.getCount(); j++) {
                    if (selCheck.get(j) != null) {
                        @SuppressWarnings("unchecked")
                        AssetStorage sto = (AssetStorage) assetAdapter.getItem(j);
                        stoLists.add(sto);
                    }
                }
                if(stoLists.size()==0){
                    Dialog dialog = new AlertDialog.Builder(TransferUpdActivity.this)
                            .setTitle("资产转移提示")
                            .setMessage("请选择要删除的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    Dialog dialog = new AlertDialog.Builder(TransferUpdActivity.this)
                            .setTitle("资产转移提示")
                            .setMessage("是否要删除选择的资产！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for(int i=0;i<stoList.size();i++){
                                        if (stoLists.contains(stoList.get(i))){
                                            stoList.remove(i);
                                        }
                                    }
                                    setAsset(stoList);
                                }
                            })
                            .setNegativeButton("取消", null).create();
                    dialog.show();
                }
                break;
            case R.id.changeDate:
                Redirect.hideIM(v, this);
                Dialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year+"-"+(month+1)+"-"+dayOfMonth;
                        changeDate.setText(date);
                    }
                }, year,month,day);
                dialog.show();
                break;
            case R.id.newUsePeople:
                Redirect.hideIM(v, this);
                setBundle(bundle);
                Class<?> clazz = TransferUpdUserFragment.class;
                Intent i = new Intent(this, TransferUpdUserActivity.class);
                i.putExtra(TransferUpdUserActivity.FRAGMENT_PARAM, clazz);
                i.putExtras(bundle);
                TransferUpdActivity.this.startActivity(i);
                break;
        }
    }

    /*初始化数据*/
    public class IntiDataThread implements Runnable{
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/transfer/updateInfo"
                    + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCode;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    AssetOperate operate = null;
                    try {
                        JSONObject jsonObject = new JSONObject(info);
                        operate = StrConvertObject.setConvertAssetOperateTransfer(jsonObject);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if (operate!=null){
                        transferCode.setText(operate.getOperbillCode());
                        List<AssetChange> list = operate.getList();
                        AssetChange change = list.get(0);

                        newUsePeopleStr = change.getNewDeptPeople()==null?"":change.getNewDeptPeople().getpName();
                        newUsePeople.setText(newUsePeopleStr);
                        changeDateStr = change.getChangeDate();
                        changeDate.setText(changeDateStr);
                        newCareManStr = change.getCareManObj()==null?"":change.getCareManObj().getpName();
                        newCareMan.setText(newCareManStr);
                        newPlaceStr = change.getNewPlace();
                        newPlace.setText(newPlaceStr);
                        operNoteStr = operate.getOperNote();
                        operNote.setText(operNoteStr);

                        for (AssetChange change1: list){
                            stoList.add(change1.getStorage());
                        }
                        setAsset(stoList);
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    /*传递本方法的参数*/
    private void setBundle(Bundle bundle){
        bundle.putString("operbillCode", operbillCode);
        bundle.putString("newUsePeople",newUsePeopleStr);
        bundle.putString("changeDate",changeDateStr);
        bundle.putString("newCareMan", newCareManStr);
        bundle.putString("newPlace",newPlaceStr);
        bundle.putString("operNote",operNoteStr);
        bundle.putInt("state", state);

        if (pName!=null){
            bundle.putString("pName", pName);
            bundle.putString("pUUID", pUUID);

            bundle.putString("newCareManUUID", careManUUID);
            bundle.putInt("careManPosition", careManPosition);

            bundle.putString("newPlaceUUID", placeUUID);
            bundle.putInt("newPlacePosition", newPlacePosition);
        }

        if (stoList.size()>0){
            stoData = new DataBean<>();
            stoData.setData(stoList);
            bundle.putSerializable("stoData", stoData);
        }
    }

    //保存
    private void update(String conSql){
        if (!Redirect.checkNetwork(TransferUpdActivity.this)) {
            Toast toast = Toast.makeText(TransferUpdActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            /*dialog = new ProgressDialog(TransferAddActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();*/
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MyUpdThread(conSql)).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class MyUpdThread implements Runnable {
        private String conSql;

        public MyUpdThread(String conSql) {
            this.conSql = conSql;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/transfer/update"
                    + "?userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(TransferUpdActivity.this,"转移单修改成功",Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("fragid",fragid);
                        bundle.putSerializable("user",user);
                        Redirect.redirect(TransferUpdActivity.this, TransferActivity.class, bundle);
                    }else {
                        Toast.makeText(TransferUpdActivity.this,"转移单修改失败，请重新修改转移单",Toast.LENGTH_LONG).show();
                    }
                }
            });
//            dialog.dismiss();
        }
    }

    //设置用户选择的资产信息
    private void setAsset(ArrayList<AssetStorage> stoList){
        //建立Adapter绑定数据源
        assetAdapter = new AssetAdapter(stoList,this, fragid, user, this.stoList);
        //绑定Adapter
        transferAsset.setAdapter(assetAdapter);
    }

    /*获取保管员*/
    private void getCareMan(){
        if (!Redirect.checkNetwork(this)) {
            Toast toast = Toast.makeText(this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            new Thread(new CareManThread()).start();
            new Thread(new NewPlaceThread()).start();
        }
    }
    //保管员
    public class CareManThread implements Runnable{
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/transfer/getCareMan"
                        + "?sysUUID=" + user.getSysUUID()+"&pUUID="+pUUID;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(info);
                        List<CharSequence> careManNameList = new ArrayList<>();         //管理员的名称
                        final List<DptPeople> careManList = new ArrayList<>();          //管理员的编码

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            DptPeople dptPeople = new Gson().fromJson(jsonObject.toString(), DptPeople.class);
                            careManNameList.add(dptPeople.getpName());
                            careManList.add(dptPeople);
                        }
                        ArrayAdapter<CharSequence> adapterReview = new ArrayAdapter<CharSequence>(TransferUpdActivity.this,android.R.layout.simple_spinner_item,careManNameList);
                        adapterReview.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);           //设置下拉列表风格
                        careMan.setAdapter(adapterReview);                                                 //设置下拉列表选项内容
                        careMan.setSelection(careManPosition, true);
                        careMan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                /*显示的布局，在布局中显示的位置ID，将要显示的数据*/
                                DptPeople dptPeople = careManList.get(position);
                                setCareUUID(dptPeople);
                                setCareManPosition(position);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {  }
                        });
                        if (careManUUID==null||careManUUID.trim().equals("")){
                            DptPeople dptPeople = careManList.get(0);
                            setCareUUID(dptPeople);
                            setCareManPosition(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    //存放地址
    public class NewPlaceThread implements Runnable{
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/transfer/getPlace"
                        + "?sysUUID=" + user.getSysUUID()+"&pUUID="+pUUID;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(info);
                        List<String> newPlaceNameList = new ArrayList<>();         //存放地址的名称
                        final List<Address> newPlaceList = new ArrayList<>();          //存放地址

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Address address = new Gson().fromJson(jsonObject.toString(), Address.class);
                            newPlaceNameList.add(address.getAddrName());
                            newPlaceList.add(address);
                        }
                        adapterPlaceReview = new ArrayAdapter<String>(TransferUpdActivity.this,android.R.layout.simple_spinner_item,newPlaceNameList);
                        adapterPlaceReview.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);           //设置下拉列表风格
                        place.setAdapter(adapterPlaceReview);                                                 //设置下拉列表选项内容
                        place.setSelection(newPlacePosition, true);
                        place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                /*显示的布局，在布局中显示的位置ID，将要显示的数据*/
                                Address address = newPlaceList.get(position);
                                setNewPlace(address);
                                setNewPlacePosition(position);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {  }
                        });
                        if (placeUUID==null||placeUUID.trim().equals("")){
                            Address address = newPlaceList.get(0);
                            setNewPlace(address);
                            setNewPlacePosition(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    dialog.dismiss();
                }
            });
        }
    }

    String barCode = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == Convert.REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                barCode = data.getStringExtra(Constant.CODED_CONTENT);
                System.out.println("=====content===="+barCode);
                for (int i=0; i<stoList.size(); i++){
                    if (stoList.get(i).getBarCode().equals(barCode)){
                        break;
                    }
                    if (i==stoList.size()-1){
                        new Thread(new BarCodeThread()).start();
                    }
                }
                if (stoList.size()<1){
                    new Thread(new BarCodeThread()).start();
                }
            }
        }
    }
    class BarCodeThread implements Runnable{
        @Override
        public void run() {
            handler.post(new Runnable() {
                //查询数据
                String path = "http://" + ipStr + "/FixedAssService/sto/selBarCodes"
                    + "?userUUID=" + user.getUserUUID()+"&barCode="+barCode;
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    AssetStorage sto = StrConvertObject.convertSto(info);
                    if (sto.getState().equals("领用")){
                        stoList.add(sto);
                        setAsset(stoList);
                        stoData = new DataBean<>();
                        stoData.setData(stoList);
                    }else {
                        Dialog dialog = new AlertDialog.Builder(TransferUpdActivity.this)
                                .setTitle("资产转移提示")
                                .setMessage("您扫描的资产编码为："+sto.getBarCode()+"，该资产的资产状态为："+sto.getState())
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }
                }
            });
        }
    }

    public void setCareUUID(DptPeople dptPeople){ this.careManUUID = dptPeople.getpUUID(); }
    public void setCareManPosition(int position){ this.careManPosition = position; }
    public void setNewPlace(Address address) { this.placeUUID = address.getAddrUUID(); }
    public void setNewPlacePosition(int position){ this.newPlacePosition = position; }
}
