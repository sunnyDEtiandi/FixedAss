package fixedass.xiangrong.com.fixedass.summary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.ExitApplication;
import fixedass.xiangrong.com.fixedass.tool.Redirect;
import fixedass.xiangrong.com.fixedass.tree.ClassSumActivity;
import fixedass.xiangrong.com.fixedass.tree.ClassSumFragment;
import fixedass.xiangrong.com.fixedass.tree.DeptSumActivity;
import fixedass.xiangrong.com.fixedass.tree.DeptSumFragment;
/**
 * @author Eileen
 * @create 2019-01-02
 * @Describe 资产统计查询
 */
public class SumSelActivity extends Activity implements View.OnClickListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private TextView title;
    private EditText useDept,useDeptUUID,className,classCode,useDate,useDate2;
    private TextView bookDateText;

    private String useDeptStr,useDeptUUIDStr,classNameStr,classCodeStr,useDateStr,useDate2Str;

    private int year,year2,month,month2,day,day2;

    private int fragid;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_sel);

        ExitApplication.getInstance().addActivity(this);	//记录所有的Activity

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragid = bundle.getInt("fragid");
        user = (User) bundle.getSerializable("user");
        if (bundle.containsKey("useDept")){
            useDeptStr = bundle.getString("useDept");
            useDeptUUIDStr = bundle.getString("useDeptUUID");
            SharedPreferences sumConfig = SumSelActivity.super.getSharedPreferences("sumConfig", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = sumConfig.edit();
            edit.putString("useDept",useDeptStr);
            edit.putString("useDeptUUID", useDeptUUIDStr);
            edit.commit();
        }
        if (bundle.containsKey("className")){
            classNameStr = bundle.getString("className");
            classCodeStr = bundle.getString("classCode");
            SharedPreferences sumConfig = SumSelActivity.super.getSharedPreferences("sumConfig", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = sumConfig.edit();
            edit.putString("className",classNameStr);
            edit.putString("classCode",classCodeStr);
            edit.commit();
        }

        SharedPreferences sumConfig = SumSelActivity.super.getSharedPreferences("sumConfig", Activity.MODE_PRIVATE);
        if (useDeptStr==null||useDeptStr.trim().equals("")){
            useDeptStr = sumConfig.getString("useDept", "");
            useDeptUUIDStr = sumConfig.getString("useDeptUUID", "");
        }
        if (classCodeStr==null||classCodeStr.trim().equals("")){
            classNameStr = sumConfig.getString("className","");
            classCodeStr = sumConfig.getString("classCode","");
        }
        useDateStr = sumConfig.getString("useDate","");
        useDate2Str = sumConfig.getString("useDate2","");

        initView();
        initEvent();
    }

    private void initView(){
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        /*btnQuit.setText("筛选");*/
        btnQuit.setImageResource(R.drawable.change);
        title = (TextView)this.findViewById(R.id.title);
        title.setText(R.string.assetSum_sel);

        useDept = (EditText) findViewById(R.id.useDept);
        useDept.setText(useDeptStr);
        useDeptUUID = (EditText) findViewById(R.id.useDeptUUID);
        useDeptUUID.setText(useDeptUUIDStr);
        className = (EditText) findViewById(R.id.className);
        className.setText(classNameStr);
        classCode = (EditText) findViewById(R.id.classCode);
        classCode.setText(classCodeStr);
        useDate = (EditText) findViewById(R.id.bookDate);
        useDate.setText(useDateStr);
        bookDateText = (TextView)findViewById(R.id.bookDateText);
        bookDateText.setText("使用日期");
        useDate2 = (EditText) findViewById(R.id.bookDate2);
        useDate2.setText(useDate2Str);
    }

    private void initEvent(){
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        useDept.setOnClickListener(this);
        useDate.setOnClickListener(this);
        useDate2.setOnClickListener(this);
        className.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putInt("fragid", fragid);
        switch (v.getId()){
            case R.id.btnBack:
                SharedPreferences ledgerConfig1 = SumSelActivity.super.getSharedPreferences("sumConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit1 = ledgerConfig1.edit();
                edit1.putString("useDept","");
                edit1.putString("useDeptUUID", "");
                edit1.putString("className","");
                edit1.putString("classCode","");
                edit1.putString("useDate","");
                edit1.putString("useDate2","");
                edit1.commit();

                Redirect.redirect(this, SumActivity.class, bundle);
                break;

            case R.id.btnQuit:

                useDeptStr = useDept.getText().toString().trim();
                useDeptUUIDStr = useDeptUUID.getText().toString().trim();
                classNameStr = className.getText().toString().trim();
                classCodeStr = classCode.getText().toString().trim();
                useDateStr = useDate.getText().toString().trim();
                useDate2Str = useDate2.getText().toString().trim();

                System.out.println(useDeptStr+"——"+classNameStr+"——"+useDateStr+"----"+useDate2Str);

                SharedPreferences ledgerConfig = SumSelActivity.super.getSharedPreferences("sumConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = ledgerConfig.edit();
                edit.putString("useDept",useDeptStr);
                edit.putString("useDeptUUID", useDeptUUIDStr);
                edit.putString("className",classNameStr);
                edit.putString("classCode",classCodeStr);
                edit.putString("useDate",useDateStr);
                edit.putString("useDate2",useDate2Str);
                edit.commit();

                Redirect.redirect(this, SumActivity.class, bundle);
                break;
            case R.id.bookDate:
                if (useDateStr==null||useDateStr.trim().equals("")){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    useDateStr = format.format(new Date());
                }
                String[] split = useDateStr.split("-");
                year = Integer.parseInt(split[0]);
                month = Integer.parseInt(split[1])-1;
                day = Integer.parseInt(split[2]);

                Redirect.hideIM(v, SumSelActivity.this);
                Dialog dialog = new DatePickerDialog(SumSelActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        useDateStr = year+"-"+(month+1)+"-"+dayOfMonth;
                        useDate.setText(useDateStr);
                    }
                }, year,month,day);
                dialog.show();

                SharedPreferences ledgerConfig2 = SumSelActivity.super.getSharedPreferences("sumConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit2 = ledgerConfig2.edit();
                edit2.putString("useDate",useDateStr);
                edit2.commit();

                break;
            case R.id.bookDate2:
                if (useDate2Str==null||useDate2Str.trim().equals("")){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    useDate2Str = format.format(new Date());
                }
                String[] split1 = useDate2Str.split("-");
                year2 = Integer.parseInt(split1[0]);
                month2 = Integer.parseInt(split1[1])-1;
                day2 = Integer.parseInt(split1[2]);

                Redirect.hideIM(v, SumSelActivity.this);
                Dialog dialog1 = new DatePickerDialog(SumSelActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        useDate2Str = year+"-"+(month+1)+"-"+dayOfMonth;
                        useDate2.setText(useDate2Str);
                    }
                }, year2,month2,day2);
                dialog1.show();

                SharedPreferences ledgerConfig3 = SumSelActivity.super.getSharedPreferences("sumConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit3 = ledgerConfig3.edit();
                edit3.putString("useDate2",useDate2Str);
                edit3.commit();
                break;
            case R.id.useDept:
                Class<?> clazz = DeptSumFragment.class;
                Intent i = new Intent(SumSelActivity.this, DeptSumActivity.class);
                i.putExtra(DeptSumActivity.FRAGMENT_PARAM, clazz);
                i.putExtras(bundle);
                SumSelActivity.this.startActivity(i);
                break;
            case R.id.className:
                Class<?> clazz1 = ClassSumFragment.class;
                Intent intent = new Intent(SumSelActivity.this, ClassSumActivity.class);
                intent.putExtra(ClassSumActivity.FRAGMENT_PARAM,clazz1);
                intent.putExtras(bundle);
                SumSelActivity.this.startActivity(intent);
                break;
        }
    }
}
