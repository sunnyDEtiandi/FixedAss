package fixedass.xiangrong.com.fixedass.asset;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.AssetScrap;
import fixedass.xiangrong.com.fixedass.bean.AssetTest;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

public class AssetAdapter_Scrap extends BaseAdapter {
    private ArrayList<AssetStorage> stoList;
    private Context context;
    private int fragid;
    private User user;
    private ArrayList<AssetStorage> selStoList;
    private ArrayList<String> barCodeList;
    public static List<AssetScrap> scrapList;
    private List<AssetScrap> assetScrapList;

    {
        scrapList = new ArrayList<>();
    }

    //记录checkbox的状态
    public HashMap<Integer, Boolean> selCheck = new HashMap<Integer, Boolean>();

    public AssetAdapter_Scrap(ArrayList<AssetStorage> stoList, Context context, int fragid, User user, ArrayList<AssetStorage> selStoList) {
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

    public AssetAdapter_Scrap(ArrayList<AssetStorage> stoList, Context context, int fragid, User user, ArrayList<AssetStorage> selStoList, List<AssetScrap> assetScrapList) {
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
        this.assetScrapList = assetScrapList;
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
        convertView = inflater.inflate(R.layout.layout_asset_scrap, null);

        if(convertView!=null){
            final AssetStorage storage = stoList.get(position);
            EditText barCode = (EditText) convertView.findViewById(R.id.barCode);
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
                    final View view = inflater1.inflate(R.layout.dialog_scrap, null);

                    final EditText scrapDate = (EditText) view.findViewById(R.id.scrapDate);
                    final EditText unuseInfo = (EditText) view.findViewById(R.id.unuseInfo);
                    final EditText unuseListNote = (EditText) view.findViewById(R.id.unuseListNote);

                    String scrapDateStr = scrapDate.getText().toString();
                    if (scrapDateStr==null||scrapDateStr.equals("")){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        scrapDateStr = format.format(new Date());
                    }
                    scrapDate.setText(scrapDateStr);
                    String[] split = scrapDateStr.split("-");
                    final Integer year = Integer.parseInt(split[0]);
                    final Integer month = Integer.parseInt(split[1])-1;
                    final Integer day = Integer.parseInt(split[2]);
                    scrapDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener(){
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    String date = year+"-"+(month+1)+"-"+dayOfMonth;
                                    scrapDate.setText(date);
                                }
                            }, year,month,day);
                            dialog.show();
                        }
                    });

                    if (assetScrapList==null||assetScrapList.isEmpty()){
                        for (AssetScrap assetScrap: scrapList){
                            if (assetScrap.getBarCode().equals(barCodeStr)){
                                scrapDate.setText(assetScrap.getScrapDate());
                                unuseInfo.setText(assetScrap.getUnuseInfo());
                                unuseListNote.setText(assetScrap.getUnuseListNote());
                            }
                        }
                    }else {
                        List<String> barcodeList = new ArrayList<>();
                        for (AssetScrap assetScrap: assetScrapList){
                            barcodeList.add(assetScrap.getBarCode());

                            if (assetScrap.getBarCode().equals(barCodeStr)){
                                scrapDate.setText(assetScrap.getScrapDate());
                                unuseInfo.setText(assetScrap.getUnuseInfo());
                                unuseListNote.setText(assetScrap.getUnuseListNote());

                                if (scrapList.size()>0){
                                    for (int m=0; m<scrapList.size(); m++){
                                        AssetScrap scrap = scrapList.get(m);
                                        if (scrap.getBarCode().equals(assetScrap.getBarCode())){
                                            scrapDate.setText(scrap.getScrapDate());
                                            unuseInfo.setText(scrap.getUnuseInfo());
                                            unuseListNote.setText(scrap.getUnuseListNote());
                                        }else {
                                            if (m==scrapList.size()-1){
                                                scrapList.add(assetScrap);
                                            }
                                        }
                                    }
                                }
                                else {
                                    scrapList.add(assetScrap);
                                }
                            }
                        }
                        if (!barcodeList.contains(barCodeStr)){
                            if (scrapList.size()>0){
                                for (int m=0; m<scrapList.size();m++){
                                    AssetScrap scrap = scrapList.get(m);
                                    if (scrap.getBarCode().equals(barCodeStr)){
                                        scrapDate.setText(scrap.getScrapDate());
                                        unuseInfo.setText(scrap.getUnuseInfo());
                                        unuseListNote.setText(scrap.getUnuseListNote());
                                    }
                                }
                            }
                        }
                    }

                    Dialog dialog = new AlertDialog.Builder(context)
                            .setTitle("报废信息")
                            .setView(view)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String scrapDateStr = scrapDate.getText().toString();
                                    String unuseInfoStr = unuseInfo.getText().toString();
                                    String unuseListNoteStr = unuseListNote.getText().toString();
                                    AssetScrap scrap = new AssetScrap(barCodeStr, unuseListNoteStr, unuseInfoStr,
                                            scrapDateStr, user.getSysUUID());

                                    if (scrapList.size()>0){
                                        for (int m=0;m<scrapList.size(); m++){
                                            if (scrapList.get(m).getBarCode().equals(barCodeStr)){
                                                scrapList.set(m, scrap);
                                            }else {
                                                if (m==scrapList.size()-1){
                                                    scrapList.add(scrap);
                                                    Log.e("操作", "add fixList"+scrapList.size());
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        scrapList.add(scrap);
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
