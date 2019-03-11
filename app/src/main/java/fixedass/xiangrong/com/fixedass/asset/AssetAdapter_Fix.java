package fixedass.xiangrong.com.fixedass.asset;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetFix;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.manage.FixActivity;
import fixedass.xiangrong.com.fixedass.manage.FixAddActivity;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

public class AssetAdapter_Fix extends BaseAdapter {
    private ArrayList<AssetStorage> stoList;
    private Context context;
    private int fragid;
    private User user;
    private ArrayList<AssetStorage> selStoList;
    private ArrayList<String> barCodeList;
    public static List<AssetFix> fixList;
    private List<AssetFix> assetFixList;

    {
        fixList = new ArrayList<>();
    }

    //记录checkbox的状态
    public HashMap<Integer, Boolean> selCheck = new HashMap<Integer, Boolean>();

    public AssetAdapter_Fix(ArrayList<AssetStorage> stoList, Context context, int fragid, User user, ArrayList<AssetStorage> selStoList) {
        this.stoList = stoList;
        this.context = context;
        this.fragid = fragid;
        this.user = user;
        this.selStoList = selStoList;
        if (selStoList.size()>0){
            barCodeList = new ArrayList<>();
            for (AssetStorage sto: selStoList){
                barCodeList.add(sto.getBarCode());
            }
        }
    }

    public AssetAdapter_Fix(ArrayList<AssetStorage> stoList, Context context, int fragid, User user, ArrayList<AssetStorage> selStoList, List<AssetFix> assetFixList) {
        this.stoList = stoList;
        this.context = context;
        this.fragid = fragid;
        this.user = user;
        this.selStoList = selStoList;
        if (selStoList.size()>0){
            barCodeList = new ArrayList<>();
            for (AssetStorage sto: selStoList){
                barCodeList.add(sto.getBarCode());
            }
        }
        this.assetFixList = assetFixList;
    }

    @Override
    public int getCount() {
        return stoList.size();
    }

    @Override
    public Object getItem(int position) {
        return stoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.layout_asset_fix, null);

        if(convertView!=null){
            final AssetStorage storage = stoList.get(position);
            final EditText barCode = (EditText) convertView.findViewById(R.id.barCode);
            final String barCodeStr = storage.getBarCode();
            barCode.setText(barCodeStr);
            EditText assetClass = (EditText) convertView.findViewById(R.id.assetClass);
            assetClass.setText(storage.getClassName());
            EditText assetName = (EditText) convertView.findViewById(R.id.assetName);
            assetName.setText(storage.getAssName());
            EditText useCompany = (EditText) convertView.findViewById(R.id.useCompany);
            useCompany.setText(storage.getCompanyInfo()==null?"":storage.getCompanyInfo().getDeptName());
            EditText useDept = (EditText) convertView.findViewById(R.id.useDept);
            useDept.setText(storage.getDeptInfo()==null?"":storage.getDeptInfo().getDeptName());
            EditText usePeople = (EditText) convertView.findViewById(R.id.usePeople);
            usePeople.setText(storage.getUsePeopleEntity()==null?"":storage.getUsePeopleEntity().getpName());
            EditText careMan = (EditText) convertView.findViewById(R.id.careMan);
            careMan.setText(storage.getCareUser()==null?"":storage.getCareUser().getpName());
            EditText store = (EditText) convertView.findViewById(R.id.store);
            store.setText(storage.getAddress()==null?"":storage.getAddress().getAddrName());

            CheckBox selCode = (CheckBox) convertView.findViewById(R.id.selCode);
            if(barCodeList!=null && barCodeList.contains(storage.getBarCode())){
                selCheck.put(position, true);
            }
            selCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selCheck.put(position, isChecked);
                    } else {
                        selCheck.remove(position);
                    }
                }
            });
            selCode.setChecked((selCheck.get(position) == null ? false : true));

            /*维修信息*/
            Button writeInfo = (Button)convertView.findViewById(R.id.writeInfo);
            writeInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater1 = LayoutInflater.from(context);
                    final View view = inflater1.inflate(R.layout.dialog_fix, null);

                    final EditText fixCompany = (EditText) view.findViewById(R.id.fixCompany);       //维修单位
                    final EditText fixPeople = (EditText) view.findViewById(R.id.fixPeople);          //维修人
                    final EditText fixTelephone = (EditText) view.findViewById(R.id.fixTelephone);       //维修电话
                    final EditText fixPrice = (EditText) view.findViewById(R.id.fixPrice);                //维修费用
                    final EditText fixDate = (EditText) view.findViewById(R.id.fixDate);              //报修日期
                    final EditText fixInfo = (EditText) view.findViewById(R.id.fixInfo);             //故障描述
                    final EditText fixListNote = (EditText) view.findViewById(R.id.fixListNote);      //备注信息
                    final RadioGroup fixState = (RadioGroup) view.findViewById(R.id.fixState);              //维修结果

                    String fixDateStr = fixDate.getText().toString();
                    if (fixDateStr==null||fixDateStr.equals("")){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        fixDateStr = format.format(new Date());
                    }
                    fixDate.setText(fixDateStr);
                    String[] split = fixDateStr.split("-");
                    final Integer year = Integer.parseInt(split[0]);
                    final Integer month = Integer.parseInt(split[1])-1;
                    final Integer day = Integer.parseInt(split[2]);
                    fixDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener(){
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    String date = year+"-"+(month+1)+"-"+dayOfMonth;
                                    fixDate.setText(date);
                                }
                            }, year,month,day);
                            dialog.show();
                        }
                    });

                    if (assetFixList==null||assetFixList.isEmpty()){
                        for (AssetFix assetFix: fixList){
                            if (assetFix.getBarCode().equals(barCodeStr)){
                                fixCompany.setText(assetFix.getFixCompany());
                                fixPeople.setText(assetFix.getFixPeople());
                                fixTelephone.setText(assetFix.getFixTelephone());
                                fixPrice.setText(assetFix.getFixPrice()+"");
                                fixDate.setText(assetFix.getFixDate());
                                fixInfo.setText(assetFix.getFixInfo());
                                fixListNote.setText(assetFix.getFixListNote());

                                if (assetFix.getFixState()!=null&&!assetFix.getFixState().equals("")){
                                    String fixStates = assetFix.getFixState();
                                    if (fixStates.equals("维修成功")){
                                        RadioButton agree = (RadioButton) view.findViewById(R.id.agree);
                                        agree.setChecked(true);
                                    }else {
                                        RadioButton disagree = (RadioButton) view.findViewById(R.id.disagree);
                                        disagree.setChecked(true);
                                    }
                                }
                            }
                        }
                    }else {
                        List<String> barcodeList = new ArrayList<>();
                        for (AssetFix assetFix: assetFixList){                                      //原维修信息
                            barcodeList.add(assetFix.getBarCode());

                            if (assetFix.getBarCode().equals(barCodeStr)){
                                fixCompany.setText(assetFix.getFixCompany());
                                fixPeople.setText(assetFix.getFixPeople());
                                fixTelephone.setText(assetFix.getFixTelephone());
                                fixPrice.setText(assetFix.getFixPrice()+"");
                                fixDate.setText(assetFix.getFixDate());
                                fixInfo.setText(assetFix.getFixInfo());
                                fixListNote.setText(assetFix.getFixListNote());
                                if (assetFix.getFixState()!=null&&!assetFix.getFixState().equals("")){
                                    String fixStates = assetFix.getFixState();
                                    if (fixStates.equals("维修成功")){
                                        RadioButton agree = (RadioButton) view.findViewById(R.id.agree);
                                        agree.setChecked(true);
                                    }else {
                                        RadioButton disagree = (RadioButton) view.findViewById(R.id.disagree);
                                        disagree.setChecked(true);
                                    }
                                }
                                if (fixList.size()>0){
                                    for (int m=0;m<fixList.size();m++){
                                        AssetFix fix = fixList.get(m);
                                        if (fix.getBarCode().equals(assetFix.getBarCode())){
                                            fixCompany.setText(fix.getFixCompany());
                                            fixPeople.setText(fix.getFixPeople());
                                            fixTelephone.setText(fix.getFixTelephone());
                                            fixPrice.setText(fix.getFixPrice()+"");
                                            fixDate.setText(fix.getFixDate());
                                            fixInfo.setText(fix.getFixInfo());
                                            fixListNote.setText(fix.getFixListNote());
                                            if (fix.getFixState()!=null&&!fix.getFixState().equals("")){
                                                String fixStates = fix.getFixState();
                                                if (fixStates.equals("维修成功")){
                                                    RadioButton agree = (RadioButton) view.findViewById(R.id.agree);
                                                    agree.setChecked(true);
                                                }else {
                                                    RadioButton disagree = (RadioButton) view.findViewById(R.id.disagree);
                                                    disagree.setChecked(true);
                                                }
                                            }
                                        }else {
                                            if (m==fixList.size()-1){
                                                fixList.add(assetFix);
                                            }
                                        }
                                    }
                                }
                                else {
                                    fixList.add(assetFix);
                                }
                            }
                        }
                        if (!barcodeList.contains(barCodeStr)){
                            if (fixList.size()>0){
                                for (int m=0;m<fixList.size();m++){
                                    AssetFix fix = fixList.get(m);
                                    if (fix.getBarCode().equals(barCodeStr)){
                                        fixCompany.setText(fix.getFixCompany());
                                        fixPeople.setText(fix.getFixPeople());
                                        fixTelephone.setText(fix.getFixTelephone());
                                        fixPrice.setText(fix.getFixPrice()+"");
                                        fixDate.setText(fix.getFixDate());
                                        fixInfo.setText(fix.getFixInfo());
                                        fixListNote.setText(fix.getFixListNote());
                                        if (fix.getFixState()!=null&&!fix.getFixState().equals("")){
                                            String fixStates = fix.getFixState();
                                            if (fixStates.equals("维修成功")){
                                                RadioButton agree = (RadioButton) view.findViewById(R.id.agree);
                                                agree.setChecked(true);
                                            }else {
                                                RadioButton disagree = (RadioButton) view.findViewById(R.id.disagree);
                                                disagree.setChecked(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    /*填写维修信息*/
                    Dialog dialog = new AlertDialog.Builder(context)
                            .setTitle("维修信息")
                            .setView(view)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String fixCompanyStr = fixCompany.getText().toString();
                                    String fixPeopleStr = fixPeople.getText().toString();
                                    String fixTelephoneStr = fixTelephone.getText().toString();
                                    String fixPriceStr = fixPrice.getText().toString();
                                    float fixPriceFloat = 0;
                                    if (!fixPriceStr.equals("")&&fixPriceStr!=null){
                                        fixPriceFloat = Float.parseFloat(fixPriceStr);
                                    }
                                    String fixDateStr = fixDate.getText().toString();
                                    String fixInfoStr = fixInfo.getText().toString();
                                    String fixListNoteStr = fixListNote.getText().toString();
                                    String fixStateStr = "";
                                    for (int j = 0; j < fixState.getChildCount(); j++) {
                                        RadioButton rd = (RadioButton) fixState.getChildAt(j);
                                        if (rd.isChecked()){
                                            fixStateStr = rd.getText().toString();
                                            break;
                                        }
                                    }
                                    AssetFix fix = new AssetFix(barCodeStr,fixInfoStr,fixCompanyStr,fixPeopleStr,fixTelephoneStr,
                                            fixDateStr,fixPriceFloat,fixListNoteStr,fixStateStr,user.getSysUUID());

                                    if (fixList.size()>0){
                                        for (int m=0;m<fixList.size();m++){
                                            if (fixList.get(m).getBarCode().equals(barCodeStr)){
                                                fixList.set(m,fix);
                                            }else {
                                                if (m==fixList.size()-1){
                                                    fixList.add(fix);
                                                    Log.e("操作", "add fixList"+fixList.size());
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        fixList.add(fix);
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Redirect.hideIM(view,context);
                                }
                            }).create();
                    dialog.show();
                }
            });
        }
        return convertView;
    }
}
