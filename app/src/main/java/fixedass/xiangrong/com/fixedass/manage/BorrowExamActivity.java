package fixedass.xiangrong.com.fixedass.manage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tree.BorrowExamUserActivity;
import fixedass.xiangrong.com.fixedass.tree.BorrowExamUserFragment;
import fixedass.xiangrong.com.fixedass.tree.BorrowUserActivity;
import fixedass.xiangrong.com.fixedass.tree.BorrowUserFragment;

public class BorrowExamActivity extends Activity implements View.OnClickListener {
    private int fragid;
    private User user;
    private String ipStr, operbillCodeStr;

    private EditText operbillCode,borrowPeople,examinePeople,examineDate,examineNote;
    private String pName, pUUID, examinePeoStr, examineDateStr, examineNoteStr;
    private Spinner reviewPeople;
    private User reviewUser;                                                //审核人
    private ImageView btnBack, btnQuit;
    private TextView title;

    private ProgressDialog dialog;
    private static Handler handler = new Handler();

    private Gson gson;
    private GsonBuilder builder;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_borrow_exam);

        ExitApplication.getInstance().addActivity(this);

        Intent intent = getIntent();
        bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User)bundle.getSerializable("user");
        operbillCodeStr = bundle.getString("operbillCode");

        if (bundle.containsKey("pName")){
            pName = bundle.getString("pName");
            pUUID = bundle.getString("pUUID");

            examineNoteStr = bundle.getString("examineNote");
            reviewUser = (User)bundle.getSerializable("reviewUser");
        }

        /*读取数据*/
        SharedPreferences sharedPreferences = BorrowExamActivity.super.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        initView();
        initEvent();
        initData();
    }

    private void initView(){
        operbillCode = (EditText) findViewById(R.id.operbillCode);

        TableRow borrow = (TableRow) findViewById(R.id.borrow);
        borrow.setVisibility(View.VISIBLE);
        borrowPeople = (EditText) findViewById(R.id.borrowPeople);
        borrowPeople.setText(pName);

        examinePeople = (EditText) findViewById(R.id.examinePeople);
        examineDate = (EditText) findViewById(R.id.examineDate);
        examineNote = (EditText) findViewById(R.id.examineNote);
        examineNote.setText(examineNoteStr);
        reviewPeople = (Spinner) findViewById(R.id.reviewPeople);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnQuit = (ImageView) findViewById(R.id.btnQuit);
        btnQuit.setImageResource(R.drawable.save);

        title = (TextView) findViewById(R.id.title);
        title.setText("借用单报审");

        builder = new GsonBuilder();
        gson = builder.create();
    }

    private void initEvent(){
        borrowPeople.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
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
            // 创建子线程，分别进行Get和Post传输
            new Thread(new ExamineInfoImpThread()).start();
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("fragid", fragid);
        bundle.putSerializable("user", user);
        bundle.putString("operbillCode", operbillCodeStr);

        examinePeoStr = examinePeople.getText().toString();
        examineDateStr = examineDate.getText().toString();
        examineNoteStr = examineNote.getText().toString();

        switch (v.getId()){
            case R.id.borrowPeople:                                         //借用人
                setBundle(bundle);
                Class<?> clazz = BorrowExamUserFragment.class;
                Intent i = new Intent(this, BorrowExamUserActivity.class);
                i.putExtra(BorrowExamUserActivity.FRAGMENT_PARAM, clazz);
                i.putExtras(bundle);
                BorrowExamActivity.this.startActivity(i);
                break;
            case R.id.btnBack:                                              //返回
                Redirect.redirect(this, BorrowActivity.class, bundle);
                break;
            case R.id.btnQuit:                                              //保存
                save();
                break;
        }
    }

    public class ExamineInfoImpThread implements Runnable{
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/borrow/examineInfo"
                        + "?userUUID=" + user.getUserUUID()+"&operbillCode="+operbillCodeStr;
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

                            operbillCode.setText(user.getUserPhone());
                            examinePeople.setText(user.getUserName());
                            examineDate.setText(user.getUserAddr());

                            reviewPeople.setPrompt("请选择审核人：");
                            //获得下拉列表适配器
                            ArrayAdapter<CharSequence> adapterReview = new ArrayAdapter<CharSequence>(BorrowExamActivity.this,android.R.layout.simple_spinner_item,userNameList);
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
                            for (int i=0; i<reviewPeople.getAdapter().getCount(); i++){
                                if (reviewPeople.getAdapter().getItem(i).equals(reviewUser)){
                                    reviewPeople.setSelection(i, true);
                                    break;
                                }
                            }
                        }else{
                            Toast.makeText(BorrowExamActivity.this,msg,Toast.LENGTH_LONG).show();
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

    /*传递本方法的参数*/
    private void setBundle(Bundle bundle){
        bundle.putString("examineNote", examineNoteStr);
        bundle.putSerializable("reviewUser", reviewUser);
    }

    /*保存信息*/
    private void save(){
        String reviewU = reviewUser.getUserUUID();
        try {
            examineNoteStr = URLEncoder.encode(examineNoteStr,"utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        String conSql = "operbillCode="+operbillCodeStr+"&borrowPeople="+pUUID+"&reviewPeople="+reviewU+"&examineNote="+examineNoteStr;
        exam(conSql);
    }

    //报审
    private void exam(String conSql){
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
                String path = "http://" + ipStr + "/FixedAssService/borrow/importDefine"
                        + "?userUUID=" + user.getUserUUID()+"&"+conSql;
                String info  = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    int num = Integer.parseInt(info);
                    if (num>0){
                        Toast.makeText(BorrowExamActivity.this,"报审成功",Toast.LENGTH_LONG).show();
                        Redirect.redirect(BorrowExamActivity.this, BorrowActivity.class, bundle);
                    }else {
                        Toast.makeText(BorrowExamActivity.this,"报审失败，请重新进行报审操作",Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.dismiss();
        }
    }

    public void setReviewUser(User reviewUser) {
        this.reviewUser = reviewUser;
    }
}
