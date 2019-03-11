package fixedass.xiangrong.com.fixedass.summary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
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
import fixedass.xiangrong.com.fixedass.tree.ClassActivity;
import fixedass.xiangrong.com.fixedass.tree.ClassFragment;
import fixedass.xiangrong.com.fixedass.tree.DeptActivity;
import fixedass.xiangrong.com.fixedass.tree.DeptFragment;

public class LedgerSelActivity extends Activity implements View.OnClickListener {
    /*private Button btnBack,btnQuit;*/
    private ImageView btnBack,btnQuit;
    private TextView title;
    private EditText useDept,useDeptUUID,className,classCode,bookDate,bookDate2;

    private String useDeptStr,useDeptUUIDStr,classNameStr,classCodeStr,bookDateStr,bookDate2Str;

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
        if (bundle.containsKey("deptName")){
            useDeptStr = bundle.getString("deptName");
            useDeptUUIDStr = bundle.getString("deptUUID");
            SharedPreferences ledgerConfig = LedgerSelActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = ledgerConfig.edit();
            edit.putString("useDept",useDeptStr);
            edit.putString("useDeptUUID", useDeptUUIDStr);
            edit.commit();
        }
        if (bundle.containsKey("className")){
            classNameStr = bundle.getString("className");
            classCodeStr = bundle.getString("classCode");
            SharedPreferences ledgerConfig = LedgerSelActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit = ledgerConfig.edit();
            edit.putString("className",classNameStr);
            edit.putString("classCode",classCodeStr);
            edit.commit();
        }

        SharedPreferences ledgerConfig = LedgerSelActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
        if (useDeptStr==null||useDeptStr.trim().equals("")){
            useDeptStr = ledgerConfig.getString("useDept", "");
            useDeptUUIDStr = ledgerConfig.getString("useDeptUUID", "");
        }
        if (classCodeStr==null||classCodeStr.trim().equals("")){
            classNameStr = ledgerConfig.getString("className","");
            classCodeStr = ledgerConfig.getString("classCode","");
        }
        bookDateStr = ledgerConfig.getString("bookDate","");
        bookDate2Str = ledgerConfig.getString("bookDate2","");

        initView();
        initEvent();
    }

    private void initView(){
        /*btnBack = (Button)this.findViewById(R.id.btnBack);
        btnQuit = (Button)this.findViewById(R.id.btnQuit);*/

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnQuit = (ImageView)findViewById(R.id.btnQuit);
        btnQuit.setImageResource(R.drawable.change);
        /*btnQuit.setText("筛选");*/
        title = (TextView)this.findViewById(R.id.title);
        title.setText(R.string.assetLedger_sel);

        useDept = (EditText) findViewById(R.id.useDept);
        useDept.setText(useDeptStr);
        useDeptUUID = (EditText) findViewById(R.id.useDeptUUID);
        useDeptUUID.setText(useDeptUUIDStr);
        className = (EditText) findViewById(R.id.className);
        className.setText(classNameStr);
        classCode = (EditText) findViewById(R.id.classCode);
        classCode.setText(classCodeStr);
        bookDate = (EditText) findViewById(R.id.bookDate);
        bookDate.setText(bookDateStr);
        bookDate2 = (EditText) findViewById(R.id.bookDate2);
        bookDate2.setText(bookDate2Str);
    }

    private void initEvent(){
        btnBack.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        useDept.setOnClickListener(this);
        bookDate.setOnClickListener(this);
        bookDate2.setOnClickListener(this);
        className.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putInt("fragid", fragid);
        switch (v.getId()){
            case R.id.btnBack:
                SharedPreferences ledgerConfig1 = LedgerSelActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit1 = ledgerConfig1.edit();
                edit1.putString("useDept","");
                edit1.putString("useDeptUUID", "");
                edit1.putString("className","");
                edit1.putString("classCode","");
                edit1.putString("bookDate","");
                edit1.putString("bookDate2","");
                edit1.commit();

                Redirect.redirect(this, LedgerActivity.class, bundle);
                break;
            case R.id.btnQuit:
                useDeptStr = useDept.getText().toString().trim();
                useDeptUUIDStr = useDeptUUID.getText().toString().trim();
                classNameStr = className.getText().toString().trim();
                classCodeStr = classCode.getText().toString().trim();
                bookDateStr = bookDate.getText().toString().trim();
                bookDate2Str = bookDate2.getText().toString().trim();

                System.out.println(useDeptStr+"——"+classNameStr+"——"+bookDateStr+"----"+bookDate2Str);

                SharedPreferences ledgerConfig = LedgerSelActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = ledgerConfig.edit();
                edit.putString("useDept",useDeptStr);
                edit.putString("useDeptUUID", useDeptUUIDStr);
                edit.putString("className",classNameStr);
                edit.putString("classCode",classCodeStr);
                edit.putString("bookDate",bookDateStr);
                edit.putString("bookDate2",bookDate2Str);
                edit.commit();

                Redirect.redirect(this, LedgerActivity.class, bundle);
                break;
            case R.id.bookDate:
                if (bookDateStr==null||bookDateStr.trim().equals("")){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    bookDateStr = format.format(new Date());
                }
                String[] split = bookDateStr.split("-");
                year = Integer.parseInt(split[0]);
                month = Integer.parseInt(split[1])-1;
                day = Integer.parseInt(split[2]);

                Redirect.hideIM(v, LedgerSelActivity.this);
                Dialog dialog = new DatePickerDialog(LedgerSelActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        bookDateStr = year+"-"+(month+1)+"-"+dayOfMonth;
                        bookDate.setText(bookDateStr);
                    }
                }, year,month,day);
                dialog.show();

                SharedPreferences ledgerConfig2 = LedgerSelActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit2 = ledgerConfig2.edit();
                edit2.putString("bookDate",bookDateStr);
                edit2.commit();

                break;
            case R.id.bookDate2:
                if (bookDate2Str==null||bookDate2Str.trim().equals("")){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    bookDate2Str = format.format(new Date());
                }
                String[] split1 = bookDate2Str.split("-");
                year2 = Integer.parseInt(split1[0]);
                month2 = Integer.parseInt(split1[1])-1;
                day2 = Integer.parseInt(split1[2]);

                Redirect.hideIM(v, LedgerSelActivity.this);
                Dialog dialog1 = new DatePickerDialog(LedgerSelActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        bookDate2Str = year+"-"+(month+1)+"-"+dayOfMonth;
                        bookDate2.setText(bookDate2Str);
                    }
                }, year2,month2,day2);
                dialog1.show();

                SharedPreferences ledgerConfig3 = LedgerSelActivity.super.getSharedPreferences("ledgerConfig", Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit3 = ledgerConfig3.edit();
                edit3.putString("bookDate2",bookDate2Str);
                edit3.commit();
                break;
            case R.id.useDept:
                Class<?> clazz = DeptFragment.class;
                Intent i = new Intent(LedgerSelActivity.this, DeptActivity.class);
                i.putExtra(DeptActivity.FRAGMENT_PARAM, clazz);
                i.putExtras(bundle);
                LedgerSelActivity.this.startActivity(i);
                break;
            case R.id.className:
                Class<?> clazz1 = ClassFragment.class;
                Intent intent = new Intent(LedgerSelActivity.this, ClassActivity.class);
                intent.putExtra(ClassActivity.FRAGMENT_PARAM,clazz1);
                intent.putExtras(bundle);
                LedgerSelActivity.this.startActivity(intent);
                break;
        }
    }
}
