package fixedass.xiangrong.com.fixedass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.fragment.CountFragment;
import fixedass.xiangrong.com.fixedass.fragment.ManageFragment;
import fixedass.xiangrong.com.fixedass.fragment.SummaryFragment;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.fragment.HomeFragment;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

public class MainActivity extends FragmentActivity implements OnClickListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private TextView title;

    // 底部菜单4个Linearlayout
    private LinearLayout ll_home;
    private LinearLayout ll_address;
    private LinearLayout ll_friend;
    private LinearLayout ll_setting;

    // 底部菜单4个ImageView
    private ImageView iv_home;
    private ImageView iv_address;
    private ImageView iv_friend;
    private ImageView iv_setting;

    // 底部菜单4个菜单标题
    private TextView tv_home;
    private TextView tv_address;
    private TextView tv_friend;
    private TextView tv_setting;

    // 4个Fragment
    private Fragment homeFragment;
    private Fragment summaryFragment;
    private Fragment countFragment;
    private Fragment manageFragment;

    private int fragid = 0;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExitApplication.getInstance().addActivity(this);

        /*获得传过来的值*/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User) bundle.getSerializable("user");

        // 初始化控件
        initView();
        // 初始化底部按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        initFragment(fragid);
    }

    private void initFragment(int index) {
        // 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        // 在每次点击后将所有的底部按钮(ImageView,TextView)颜色改为灰色，然后根据点击着色
        restartBotton();
        /*传递值*/
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (index) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_content, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                bundle.putInt("fragid",0);
                homeFragment.setArguments(bundle);
                /*iv_home.setImageResource(R.drawable.ic_home_blue_24dp);*/
                iv_home.setImageResource(R.drawable.index_blue);
                tv_home.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                if (summaryFragment == null) {
                    summaryFragment = new SummaryFragment();
                    transaction.add(R.id.fl_content, summaryFragment);
                } else {
                    transaction.show(summaryFragment);
                }
                bundle.putInt("fragid",1);
                summaryFragment.setArguments(bundle);
                iv_address.setImageResource(R.drawable.summary_blue);
                tv_address.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                if (countFragment == null) {
                    countFragment = new CountFragment();
                    transaction.add(R.id.fl_content, countFragment);
                } else {
                    transaction.show(countFragment);
                }
                bundle.putInt("fragid",2);
                countFragment.setArguments(bundle);
                iv_friend.setImageResource(R.drawable.count_blue);
                tv_friend.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 3:
                if (manageFragment == null) {
                    manageFragment = new ManageFragment();
                    transaction.add(R.id.fl_content, manageFragment);
                } else {
                    transaction.show(manageFragment);
                }
                bundle.putInt("fragid",3);
                manageFragment.setArguments(bundle);
                iv_setting.setImageResource(R.drawable.manager_blue);
                tv_setting.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            default:
                break;
        }
        // 提交事务
        transaction.commit();
    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (summaryFragment != null) {
            transaction.hide(summaryFragment);
        }
        if (countFragment != null) {
            transaction.hide(countFragment);
        }
        if (manageFragment != null) {
            transaction.hide(manageFragment);
        }

    }

    private void initEvent() {
        btnQuit.setOnClickListener(this);

        // 设置按钮监听
        ll_home.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        ll_friend.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
    }

    private void initView() {
        /*this.btnBack = (Button)findViewById(R.id.btnBack);
        this.btnQuit = (Button)findViewById(R.id.btnQuit);*/

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        this.btnBack.setVisibility(View.INVISIBLE);
        this.title = (TextView)findViewById(R.id.title);
        this.title.setText(R.string.app_name);

        // 底部菜单4个Linearlayout
        this.ll_home = (LinearLayout) findViewById(R.id.ll_home);
        this.ll_address = (LinearLayout) findViewById(R.id.ll_address);
        this.ll_friend = (LinearLayout) findViewById(R.id.ll_friend);
        this.ll_setting = (LinearLayout) findViewById(R.id.ll_setting);

        // 底部菜单4个ImageView
        this.iv_home = (ImageView) findViewById(R.id.iv_home);
        this.iv_address = (ImageView) findViewById(R.id.iv_address);
        this.iv_friend = (ImageView) findViewById(R.id.iv_friend);
        this.iv_setting = (ImageView) findViewById(R.id.iv_setting);

        // 底部菜单4个菜单标题
        this.tv_home = (TextView) findViewById(R.id.tv_home);
        this.tv_address = (TextView) findViewById(R.id.tv_address);
        this.tv_friend = (TextView) findViewById(R.id.tv_friend);
        this.tv_setting = (TextView) findViewById(R.id.tv_setting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnQuit:
                Redirect.quit(this);
                break;
            case R.id.ll_home:
                initFragment(0);
                break;
            case R.id.ll_address:
                initFragment(1);
                break;
            case R.id.ll_friend:
                initFragment(2);
                break;
            case R.id.ll_setting:
                initFragment(3);
                break;
            default:
                break;
        }
    }

    private void restartBotton() {
        // ImageView置为灰色
        //iv_home.setImageResource(R.drawable.ic_home_black_24dp);
        iv_home.setImageResource(R.drawable.index);
        iv_address.setImageResource(R.drawable.summary);
        iv_friend.setImageResource(R.drawable.count);
        iv_setting.setImageResource(R.drawable.manager);

        // TextView置为白色
        tv_home.setTextColor(getResources().getColor(R.color.textColor));
        tv_address.setTextColor(getResources().getColor(R.color.textColor));
        tv_friend.setTextColor(getResources().getColor(R.color.textColor));
        tv_setting.setTextColor(getResources().getColor(R.color.textColor));
    }

    //退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Redirect.quit(this);
                break;
            default:
                break;
        }
        return false;
    }
}
