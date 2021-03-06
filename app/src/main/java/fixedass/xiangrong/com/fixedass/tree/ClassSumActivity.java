package fixedass.xiangrong.com.fixedass.tree;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetClass;
import fixedass.xiangrong.com.fixedass.bean.DataBean;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.service.WebService;
import fixedass.xiangrong.com.fixedass.summary.SumSelActivity;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/**
 * @author Eileen
 * @create 2018-11-15
 * @Describe 部门树结构
 */
public class ClassSumActivity extends Activity{
    public final static String FRAGMENT_PARAM = "fragment";
    // 创建等待框
    private ProgressDialog dialog;
    // 返回主线程更新数据
    private static Handler handler = new Handler();
    private User user;              //用户信息
    private String ipStr;           //服务器地址

    private Class<?> fragmentClass; //要跳转的地方
    //jsonObject转object
    private Gson gson;
    private GsonBuilder builder;
    private Bundle bundle;

    /*标题部分*/
    /*private Button btnQuit,btnBack;*/
    private ImageView btnQuit,btnBack;
    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept);

        ExitApplication.getInstance().addActivity(this);

        initView();

        if(savedInstanceState == null){
            loadClass();
        }
    }

    private void initView(){
        bundle = getIntent().getExtras();
        fragmentClass = (Class<?>)bundle.get(FRAGMENT_PARAM);
        user = (User)bundle.getSerializable("user");

        /*读取数据*/
        SharedPreferences sharedPreferences = this.getSharedPreferences("ipConfig", Activity.MODE_PRIVATE);
        ipStr = sharedPreferences.getString("ipStr","");

        //这两句代码必须的，为的是初始化出来gson这个对象，才能拿来用
        builder = new GsonBuilder();
        gson = builder.create();

        /*标题部分的控件*/
        /*btnQuit = (Button)findViewById(R.id.btnQuit);
        btnBack = (Button)findViewById(R.id.btnBack);
        btnQuit.setText("保存");*/

        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnBack = (ImageView)findViewById(R.id.btnBack);
//        btnBack.setImageResource(R.drawable.save);
        btnQuit.setVisibility(View.GONE) ;     //表示隐藏
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(ClassSumActivity.this, SumSelActivity.class, bundle);
            }
        });
        title = (TextView)findViewById(R.id.title);
        title.setText("资产类别选择");
    }

    /*获得部门的最高级*/
    private void loadClass(){
        if (!Redirect.checkNetwork(ClassSumActivity.this)) {
            Toast toast = Toast.makeText(ClassSumActivity.this,"网络未连接", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            // 提示框
            dialog = new ProgressDialog(ClassSumActivity.this);
            dialog.setTitle("提示");
            dialog.setMessage("正在加载数据，请稍后...");
            dialog.setCancelable(false);
            dialog.show();
            // 创建子线程，分别进行Get和Post传输
            new Thread(new LoadThread()).start();
        }
    }
    // 子线程接收数据，主线程修改数据--加载跟数据
    public class LoadThread implements Runnable {
        @Override
        public void run() {
            handler.post(new Runnable() {
                String path = "http://" + ipStr + "/FixedAssService/clazz/allClass"
                    + "?userUUID=" + user.getUserUUID();
                String info = WebService.executeHttpGet(path);

                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(info);
                        List<AssetClass> classList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                            AssetClass clazz = gson.fromJson(jsonObject.toString(), AssetClass.class);
                            classList.add(clazz);
                        }

                        DataBean<AssetClass> bean = new DataBean<>();
                        bean.setData(classList);
                        bundle.putSerializable("classList", bean);

                        Fragment f = Fragment.instantiate(ClassSumActivity.this, fragmentClass.getName());
                        f.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fragment, f, fragmentClass.getName()).commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}
