package fixedass.xiangrong.com.fixedass.manage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import fixedass.xiangrong.com.fixedass.asset.TransferAssetActivity;
import fixedass.xiangrong.com.fixedass.bean.Address;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.DataBean;
import fixedass.xiangrong.com.fixedass.bean.DptPeople;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.Convert;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;
import fixedass.xiangrong.com.fixedass.tree.TransferUserActivity;
import fixedass.xiangrong.com.fixedass.tree.TransferUserFragment;
import fixedass.xiangrong.com.fixedass.zxing.android.CaptureActivity;
import fixedass.xiangrong.com.fixedass.zxing.common.Constant;

/*添加资产资产选择显示在界面上，把每个值传给其他两个activity*/
public class TransferAddActivity extends Activity implements View.OnClickListener {
    /*private Button btnQuit,btnBack;*/
    private ImageView btnQuit,btnBack;
    private TextView title;
    private int fragid;
    private String ipStr;
    private User user;

    //新使用人、转移时间、新保管员、新存放地址、备注信息
    private EditText newUsePeople,changeDate,operNote;
    private String pName,pUUID;                                     //转移人名称，编码
    private ArrayAdapter<String> adapterPlaceReview;
    private Spinner newCareMan,newPlace;                                       //保管员newCareMan,
    private String newCareManUUID,newPlaceUUID,newPlaceName;                                    //存放地址newPlaceName,
    private int careManPosition = 0,newPlacePosition = 0;                 //保管员以及存放地点的位置
    private String changeDateStr,operNoteStr;                      //对应每个文本的内容

    private ListView transferAsset;                                 //转移的资产
    private AssetAdapter assetAdapter;                              //适配器

    //添加资产、扫描资产、删除资产
    private TextView addAsset, scanAsset, delAsset;
    // 创建等待框
//    private ProgressDialog dialog;
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
        setContentView(R.layout.activity_transfer_add);

        ExitApplication.getInstance().addActivity(this);

        /*读取数据*/
        SharedPreferences sharedPreferences = TransferAddActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");
        if(bundle.containsKey("pName")){
            getCareMan();                                       //获得保管员
            pName = bundle.getString("pName");
            pUUID = bundle.getString("pUUID");

            //newCareManName = bundle.getString("newCareManName");
            newCareManUUID = bundle.getString("newCareManUUID");
            careManPosition = bundle.getInt("careManPosition");

            //newPlaceName = bundle.getString("newPlaceName");
            newPlaceUUID = bundle.getString("newPlaceUUID");
            newPlacePosition = bundle.getInt("newPlacePosition");
        }
        changeDateStr = bundle.getString("changeDate");
        operNoteStr = bundle.getString("operNote");
        if (bundle.containsKey("stoData")){
            stoData = (DataBean<AssetStorage>) bundle.getSerializable("stoData");
            List<AssetStorage> asList = stoData.getData();
            stoList.addAll(asList);
        }

        initView();
        initEvent();
    }

    private void initView(){
        /*btnQuit = (Button)findViewById(R.id.btnQuit);
        btnQuit.setText("保存");
        btnBack = (Button)findViewById(R.id.btnBack);*/

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setImageResource(R.drawable.save);

        title = (TextView)findViewById(R.id.title);
        title.setText("新建资产转移单据");

        newUsePeople = (EditText)findViewById(R.id.newUsePeople);
        newUsePeople.setText(pName);
        changeDate = (EditText)findViewById(R.id.changeDate);
        if (changeDateStr==""||changeDateStr==null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());
            changeDateStr = df.format(curDate);
        }
        changeDate.setText(changeDateStr);

        String[] split = changeDateStr.split("-");
        year = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1])-1;
        day = Integer.parseInt(split[2]);

        newCareMan = (Spinner) findViewById(R.id.newCareMan);
        newCareMan.setPrompt("请选择保管员：");
        newCareMan.setSelection(careManPosition, true);

        newPlace = (Spinner) findViewById(R.id.newPlace);
        newPlace.setPrompt("请选择存放地点：");
        newPlace.setSelection(newPlacePosition, true);

        operNote = (EditText)findViewById(R.id.operNote);
        operNote.setText(operNoteStr);

        transferAsset = (ListView)findViewById(R.id.transferAsset);
        if (stoList.size()>0){
            setAsset(stoList);
        }

        addAsset = (TextView) findViewById(R.id.addAsset);
        scanAsset = (TextView) findViewById(R.id.scanAsset);
        delAsset = (TextView) findViewById(R.id.delAsset);
    }

    private void initEvent(){
        btnQuit.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        newUsePeople.setOnClickListener(this);
        changeDate.setOnClickListener(this);

        addAsset.setOnClickListener(this);
        scanAsset.setOnClickListener(this);
        delAsset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user",user);

        //获取文本框数据
        changeDateStr = changeDate.getText().toString();
        operNoteStr = operNote.getText().toString();

        switch (v.getId()){
            case R.id.btnBack:          //返回
                Redirect.redirect(TransferAddActivity.this, TransferActivity.class, bundle);
                break;
            case R.id.btnQuit:          //保存
                if (pUUID.trim().equals("")){
                    Dialog dialog = new AlertDialog.Builder(TransferAddActivity.this)
                            .setTitle("资产转移提示")
                            .setMessage("新使用人不能为空！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }
                if (changeDateStr.trim().equals("")){
                    Dialog dialog = new AlertDialog.Builder(TransferAddActivity.this)
                            .setTitle("资产转移提示")
                            .setMessage("转移日期不能为空！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }
                System.out.println("======newCareManUUID========="+newCareManUUID);
                if (newCareManUUID.trim().equals("")){
                    Dialog dialog = new AlertDialog.Builder(TransferAddActivity.this)
                            .setTitle("资产转移提示")
                            .setMessage("新保管员不能为空！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }
                if (newPlaceUUID.trim().equals("")){
                    Dialog dialog = new AlertDialog.Builder(TransferAddActivity.this)
                            .setTitle("资产转移提示")
                            .setMessage("新存放地址不能为空！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }
                if (stoList.size()<1){
                    Dialog dialog = new AlertDialog.Builder(TransferAddActivity.this)
                            .setTitle("资产转移提示")
                            .setMessage("请选择要转移的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                    break;
                }
                newPlaceName = adapterPlaceReview.getItem(newPlacePosition);
                System.out.println("=======newPlaceName======"+newPlaceName);
                try {
                    operNoteStr = URLEncoder.encode(operNoteStr,"utf-8");
                    newPlaceName = URLEncoder.encode(newPlaceName, "utf-8");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                String assetUUID = "";
                for (AssetStorage sto: stoList){
                    assetUUID =  assetUUID+sto.getAssetUUID()+",";
                }
                assetUUID = assetUUID.substring(0,assetUUID.length()-1);

                String conSql = "newPeople="+pUUID+"&changeDate="+changeDateStr+"&careMan="+newCareManUUID+"&newPlaceCode="
                        +newPlaceUUID+"&newPlace="+newPlaceName+"&operNote="+operNoteStr+"&assetUUID="+assetUUID;

                save(conSql);
                break;
            case R.id.addAsset:                 //添加资产
                setBundle(bundle);
                Redirect.redirect(TransferAddActivity.this, TransferAssetActivity.class, bundle);
                Redirect.hideIM(v, TransferAddActivity.this);
                break;
            case R.id.scanAsset:                //扫描资产
//                Toast.makeText(TransferAddActivity.this, "扫描资产", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(TransferAddActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                Intent intent = new Intent(TransferAddActivity.this, CaptureActivity.class);
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
                    Dialog dialog = new AlertDialog.Builder(TransferAddActivity.this)
                            .setTitle("资产转移提示")
                            .setMessage("请选择要删除的资产！")
                            .setPositiveButton("确定", null).create();
                    dialog.show();
                }else{
                    Dialog dialog = new AlertDialog.Builder(TransferAddActivity.this)
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
                Redirect.hideIM(v, TransferAddActivity.this);
                Dialog dialog = new DatePickerDialog(TransferAddActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year+"-"+(month+1)+"-"+dayOfMonth;
                        changeDate.setText(date);
                    }
                }, year,month,day);
                dialog.show();
                break;
            case R.id.newUsePeople:
                Redirect.hideIM(v, TransferAddActivity.this);
                setBundle(bundle);
                Class<?> clazz = TransferUserFragment.class;
                Intent i = new Intent(TransferAddActivity.this, TransferUserActivity.class);
                i.putExtra(TransferUserActivity.FRAGMENT_PARAM, clazz);
                i.putExtras(bundle);
                TransferAddActivity.this.startActivity(i);
                break;
        }
    }

    /*获取保管员*/
    private void getCareMan(){
        if (!Redirect.checkNetwork(TransferAddActivity.this)) {
            Toast toast = Toast.makeText(TransferAddActivity.this,"网络未连接", Toast.LENGTH_SHORT);
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
                        ArrayAdapter<CharSequence> adapterReview = new ArrayAdapter<CharSequence>(TransferAddActivity.this,android.R.layout.simple_spinner_item,careManNameList);
                        adapterReview.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);           //设置下拉列表风格
                        newCareMan.setAdapter(adapterReview);                                                 //设置下拉列表选项内容
                        newCareMan.setSelection(careManPosition, true);
                        newCareMan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        if (newCareManUUID==null||newCareManUUID.trim().equals("")){
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
                        adapterPlaceReview = new ArrayAdapter<String>(TransferAddActivity.this,android.R.layout.simple_spinner_item,newPlaceNameList);
                        adapterPlaceReview.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);           //设置下拉列表风格
                        newPlace.setAdapter(adapterPlaceReview);                                                 //设置下拉列表选项内容
                        newPlace.setSelection(newPlacePosition, true);
                        newPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                /*显示的布局，在布局中显示的位置ID，将要显示的数据*/
                                Address address = newPlaceList.get(position);
                                setNewPlace(address);
                                setNewPlacePosition(position);
                                //setNewPlaceName(address.getAddrName());
                                //newPlaceName = adapterPlaceReview.getItem(position);
                                System.out.println("11111===newPlaceName=="+newPlaceName);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {  }
                        });
                        if (newPlaceUUID==null||newPlaceUUID.trim().equals("")){
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

    /*传递本方法的参数*/
    private void setBundle(Bundle bundle){
        if (pName!=null){
            bundle.putString("pName", pName);
            bundle.putString("pUUID", pUUID);

            bundle.putString("newCareManUUID", newCareManUUID);
            bundle.putInt("careManPosition", careManPosition);

            bundle.putString("newPlaceUUID",newPlaceUUID);
            bundle.putInt("newPlacePosition",newPlacePosition);
        }

        bundle.putString("changeDate", changeDateStr);

        bundle.putString("operNote", operNoteStr);

        if (stoList.size()>0){
            bundle.putSerializable("stoData", stoData);
        }
    }

    //保存
    private void save(String conSql){
        if (!Redirect.checkNetwork(TransferAddActivity.this)) {
            Toast toast = Toast.makeText(TransferAddActivity.this,"网络未连接", Toast.LENGTH_SHORT);
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
            new Thread(new MySaveThread(conSql)).start();
        }
    }
    // 子线程接收数据，主线程修改数据
    public class MySaveThread implements Runnable {
        private String conSql;

        public MySaveThread(String conSql) {
            this.conSql = conSql;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                /*所有的数据*/
                String path = "http://" + ipStr + "/FixedAssService/transfer/add"
                    + "?userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(TransferAddActivity.this,"转移单创建成功",Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("fragid",fragid);
                        bundle.putSerializable("user",user);
                        Redirect.redirect(TransferAddActivity.this, TransferActivity.class, bundle);
                    }else {
                        Toast.makeText(TransferAddActivity.this,"转移单创建失败，请重新创建转移单",Toast.LENGTH_LONG).show();
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
                        Dialog dialog = new AlertDialog.Builder(TransferAddActivity.this)
                                .setTitle("资产转移提示")
                                .setMessage("您扫描的资产编码为："+sto.getBarCode()+"，该资产的资产状态为："+sto.getState())
                                .setPositiveButton("确定", null).create();
                        dialog.show();
                    }
                }
            });
        }
    }

    public void setCareUUID(DptPeople dptPeople){ this.newCareManUUID = dptPeople.getpUUID(); }
    public void setCareManPosition(int position){ this.careManPosition = position; }
    public void setNewPlace(Address address) { this.newPlaceUUID = address.getAddrUUID(); }
    public void setNewPlacePosition(int position){ this.newPlacePosition = position; }
}
