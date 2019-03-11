package fixedass.xiangrong.com.fixedass.summary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IntRange;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.manage.BorrowActivity;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/**
 * Created by Administrator on 2018/4/25.
 */

public class EditPopWin extends PopupWindow {
    private Context mContext;
    private View view;

    private Button btn_save_pop,btn_cancle_pop;
    public EditText useDept,useDeptUUID,className,classCode,bookDate,bookDate2;

    public EditPopWin(Activity mContext, View.OnClickListener itemsOnClick) {
        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit, null);

        useDept = (EditText) view.findViewById(R.id.useDept);
        useDeptUUID = (EditText)view.findViewById(R.id.useDeptUUID);
        className = (EditText) view.findViewById(R.id.className);
        classCode = (EditText) view.findViewById(R.id.classCode);
        bookDate = (EditText) view.findViewById(R.id.bookDate);
        bookDate2 = (EditText) view.findViewById(R.id.bookDate2);

        btn_save_pop =  (Button) view.findViewById(R.id.btn_save_pop);
        btn_cancle_pop = (Button) view.findViewById(R.id.btn_cancel_pop);

        // 设置按钮监听
        btn_save_pop.setOnClickListener(itemsOnClick);
        btn_cancle_pop.setOnClickListener(itemsOnClick);
        bookDate.setOnClickListener(itemsOnClick);
        bookDate2.setOnClickListener(itemsOnClick);
        useDept.setOnClickListener(itemsOnClick);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
          /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = mContext.getWindow();
        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值

        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth((int) (d.getWidth() * 0.8));

        // 设置弹出窗体可点击
        this.setFocusable(true);
    }
}
