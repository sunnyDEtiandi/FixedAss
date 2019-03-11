package fixedass.xiangrong.com.fixedass.count;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fixedass.xiangrong.com.fixedass.MainActivity;
import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.Address;
import fixedass.xiangrong.com.fixedass.bean.AssetCountBill;
import fixedass.xiangrong.com.fixedass.bean.Dept;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tool.SpinnerAdapter;
import fixedass.xiangrong.com.fixedass.tool.StrConvertObject;

/**
 * @author Eileen
 * @create 2018-11-20
 * @Describe 资产盘点
 */
public class CounttingActivity extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private TextView title;
    private int fragid;
    private User user;

    private Spinner countbillCode, countDept, countPlace;
    private Button startCount;

    private String ipStr;

    private static Handler handler = new Handler();

    private String countBillCodeStr, countDeptNameStr, countPlaceNameStr;
    private Dept dept;
    private Address address;

    private List<String> codeList;
    private List<Dept> deptList;
    private List<Address> placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countting);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");
        if (bundle.containsKey("dept")){
            dept = (Dept) bundle.getSerializable("dept");
            countDeptNameStr = dept.getDeptName();

            address = (Address) bundle.getSerializable("address");
            countPlaceNameStr = address.getAddrName();

            countBillCodeStr = bundle.getString("countBillCode");
        }

        /*读取数据*/
        SharedPreferences sharedPreferences = CounttingActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        initView();
        initEvent();
        setCountbillCode();
    }

    private void initView(){
        /*btnBack = (Button)findViewById(R.id.btnBack);
        btnQuit = (Button)findViewById(R.id.btnQuit);*/
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setVisibility(View.GONE);

        title = (TextView)findViewById(R.id.title);
        title.setText(R.string.countting);

        countbillCode = (Spinner)findViewById(R.id.countbillCode);
        countDept = (Spinner)findViewById(R.id.countDept);
        countPlace = (Spinner)findViewById(R.id.countPlace);

        startCount = (Button)findViewById(R.id.startCount);
    }
    private void initEvent(){
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);

        countbillCode.setOnItemSelectedListener(this);
        countDept.setOnItemSelectedListener(this);
        countPlace.setOnItemSelectedListener(this);

        startCount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        switch (v.getId()){
            case R.id.btnBack:
                Redirect.redirect(CounttingActivity.this, MainActivity.class,bundle);
                break;
            case R.id.btnQuit:
                Redirect.quit(CounttingActivity.this);
                break;
            case R.id.startCount:
                bundle.putSerializable("dept", dept);
                bundle.putSerializable("address", address);
                bundle.putString("countBillCode", countBillCodeStr);
                Redirect.redirect(CounttingActivity.this, CountDataActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.countbillCode:
                countBillCodeStr = countbillCode.getAdapter().getItem(position).toString();
                setDeptPlace();
                break;
            case R.id.countDept:
                countDeptNameStr = deptList.get(position).getDeptName();
                dept = deptList.get(position);
                break;
            case R.id.countPlace:
                countPlaceNameStr = placeList.get(position).getAddrName();
                address = placeList.get(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid",fragid);
        bundle.putSerializable("user", user);
        Redirect.redirect(CounttingActivity.this, MainActivity.class,bundle);
    }

    //遍历盘点单号
    private void setCountbillCode(){
        if (!Redirect.checkNetwork(CounttingActivity.this)) {
            Toast toast = Toast.makeText(CounttingActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            // 创建子线程，分别进行Get和Post传输
            new Thread(new MyCodeThread()).start();
        }
    }
    class MyCodeThread implements Runnable{
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/count/countSel"
                     + "?userUUID=" + user.getUserUUID()+"&countBillCode=&createDate=&createPeople=&countNote=&orderBy=";
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    codeList = new ArrayList<>();

                    ArrayList<AssetCountBill> countList = StrConvertObject.strConvertCount(info);
                    for (AssetCountBill bill: countList){
                        codeList.add(bill.getCountBillCode());
                    }

                    SpinnerAdapter codeSpinner = new SpinnerAdapter(CounttingActivity.this, android.R.layout.simple_spinner_item, codeList);
                    countbillCode.setAdapter(codeSpinner);
                    if (countBillCodeStr!=""){
                        for (int j=0; j<codeList.size(); j++){
                            if (codeList.get(j).equals(countBillCodeStr)){
                                countbillCode.setSelection(j, true);
                            }
                        }
                    }
                }
            });
        }
    }

    //获得盘点单号对应的部门以及地址
    private void setDeptPlace(){
        if (!Redirect.checkNetwork(CounttingActivity.this)) {
            Toast toast = Toast.makeText(CounttingActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            new Thread(new DeptPlaceThread()).start();
        }
    }
    class DeptPlaceThread implements Runnable{
        @Override
        public void run() {
            String path = "http://" + ipStr + "/FixedAssService/count/selDept"
                 + "?userUUID=" + user.getUserUUID()+"&countbillCode="+countBillCodeStr;
            String info = WebService.executeHttpGet(path);

            String paths = "http://" + ipStr + "/FixedAssService/count/selPlace"
                + "?userUUID=" + user.getUserUUID()+"&countbillCode="+countBillCodeStr;
            String info1 = WebService.executeHttpGet(paths);

            deptList = StrConvertObject.strConvertDept(info);
            placeList = StrConvertObject.strConvertAddress(info1);

            final List<String> depts = new ArrayList<>();
            for (Dept dept: deptList){
                depts.add(dept.getDeptName());
            }

            final List<String> addresss = new ArrayList<>();
            for (Address address: placeList){
                addresss.add(address.getAddrName());
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    SpinnerAdapter deptSpinner = new SpinnerAdapter(CounttingActivity.this, android.R.layout.simple_spinner_item, depts);
                    countDept.setAdapter(deptSpinner);

                    SpinnerAdapter placeSpinner = new SpinnerAdapter(CounttingActivity.this, android.R.layout.simple_spinner_item, addresss);
                    countPlace.setAdapter(placeSpinner);

                    for (int i=0; i<depts.size(); i++){
                        if (depts.get(i).equals(countDeptNameStr)){
                            countDept.setSelection(i, true);
                        }
                    }

                    for (int i=0; i<addresss.size(); i++){
                        if (addresss.get(i).equals(countPlaceNameStr)){
                            countPlace.setSelection(i, true);
                        }
                    }
                }
            });
        }
    }
}
