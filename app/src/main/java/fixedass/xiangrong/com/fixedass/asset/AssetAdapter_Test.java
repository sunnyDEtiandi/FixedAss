package fixedass.xiangrong.com.fixedass.asset;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import fixedass.xiangrong.com.fixedass.bean.AssetFix;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.AssetTest;
import fixedass.xiangrong.com.fixedass.bean.User;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

public class AssetAdapter_Test extends BaseAdapter {
    private ArrayList<AssetStorage> stoList;
    private Context context;
    private int fragid;
    private User user;
    private ArrayList<AssetStorage> selStoList;
    private ArrayList<String> barCodeList;
    public static List<AssetTest> testList;
    private List<AssetTest> assetTestList;

    {
        testList = new ArrayList<>();
    }

    //记录checkbox的状态
    public HashMap<Integer, Boolean> selCheck = new HashMap<Integer, Boolean>();

    public AssetAdapter_Test(ArrayList<AssetStorage> stoList, Context context, int fragid, User user, ArrayList<AssetStorage> selStoList) {
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

    public AssetAdapter_Test(ArrayList<AssetStorage> stoList, Context context, int fragid, User user, ArrayList<AssetStorage> selStoList, List<AssetTest> assetTestList) {
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
        this.assetTestList = assetTestList;
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
        convertView = inflater.inflate(R.layout.layout_asset_test, null);

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

            /*检验信息*/
            Button writeInfo = (Button)convertView.findViewById(R.id.writeInfo);
            writeInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater1 = LayoutInflater.from(context);
                    final View view = inflater1.inflate(R.layout.dialog_test, null);

                    final EditText testCompany = (EditText) view.findViewById(R.id.testCompany);       //检验单位
                    final EditText testPeople = (EditText) view.findViewById(R.id.testPeople);          //检验人
                    final EditText testPrice = (EditText) view.findViewById(R.id.testPrice);            //检验修电话
                    final EditText testDate = (EditText) view.findViewById(R.id.testDate);                //检验日期
                    final EditText testListNote = (EditText) view.findViewById(R.id.testListNote);      //备注信息
                    final RadioGroup testState = (RadioGroup) view.findViewById(R.id.testState);              //检验结果

                    String testDateStr = testDate.getText().toString();
                    if (testDateStr==null||testDateStr.equals("")){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        testDateStr = format.format(new Date());
                    }
                    testDate.setText(testDateStr);
                    String[] split = testDateStr.split("-");
                    final Integer year = Integer.parseInt(split[0]);
                    final Integer month = Integer.parseInt(split[1])-1;
                    final Integer day = Integer.parseInt(split[2]);
                    testDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener(){
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    String date = year+"-"+(month+1)+"-"+dayOfMonth;
                                    testDate.setText(date);
                                }
                            }, year,month,day);
                            dialog.show();
                        }
                    });

                    if (assetTestList==null||assetTestList.isEmpty()){
                        for (AssetTest assetTest: testList){
                            if (assetTest.getBarCode().equals(barCodeStr)){
                                testCompany.setText(assetTest.getTestCompany());
                                testPeople.setText(assetTest.getTestPeople());
                                testPrice.setText(assetTest.getTestPrice()+"");
                                testDate.setText(assetTest.getTestDate());
                                testListNote.setText(assetTest.getTestListNote());

                                if (assetTest.getTestState()!=null&&!assetTest.getTestState().equals("")){
                                    String testStates = assetTest.getTestState();
                                    if (testStates.equals("检验成功")){
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
                        List<String> barCodeList = new ArrayList<>();
                        for (AssetTest assetTest: assetTestList){                                   //原检验信息
                            barCodeList.add(assetTest.getBarCode());

                            if (assetTest.getBarCode().equals(barCodeStr)){
                                testCompany.setText(assetTest.getTestCompany());
                                testPeople.setText(assetTest.getTestPeople());
                                testPrice.setText(assetTest.getTestPrice()+"");
                                testDate.setText(assetTest.getTestDate());
                                testListNote.setText(assetTest.getTestListNote());
                                if (assetTest.getTestState()!=null&&!assetTest.getTestState().equals("")){
                                    String testStates = assetTest.getTestState();
                                    if (testStates.equals("检验成功")){
                                        RadioButton agree = (RadioButton) view.findViewById(R.id.agree);
                                        agree.setChecked(true);
                                    }else {
                                        RadioButton disagree = (RadioButton) view.findViewById(R.id.disagree);
                                        disagree.setChecked(true);
                                    }
                                }
                                if (testList.size()>0){
                                    for (int m=0; m<testList.size(); m++){
                                        AssetTest test = testList.get(m);
                                        if (test.getBarCode().equals(assetTest.getBarCode())){
                                            testCompany.setText(test.getTestCompany());
                                            testPeople.setText(test.getTestPeople());
                                            testPrice.setText(test.getTestPrice()+"");
                                            testDate.setText(test.getTestDate());
                                            testListNote.setText(test.getTestListNote());
                                            if (test.getTestState()!=null&&!test.getTestState().equals("")){
                                                String testStates = test.getTestState();
                                                if (testStates.equals("检验成功")){
                                                    RadioButton agree = (RadioButton) view.findViewById(R.id.agree);
                                                    agree.setChecked(true);
                                                }else {
                                                    RadioButton disagree = (RadioButton) view.findViewById(R.id.disagree);
                                                    disagree.setChecked(true);
                                                }
                                            }
                                        }else {
                                            if (m==testList.size()-1){
                                                testList.add(assetTest);
                                            }
                                        }
                                    }
                                }
                                else {
                                    testList.add(assetTest);
                                }
                            }
                        }
                        if (!barCodeList.contains(barCodeStr)){
                            if (testList.size()>0){
                                for (int m=0; m<testList.size(); m++){
                                    AssetTest test = testList.get(m);
                                    if (test.getBarCode().equals(barCodeStr)){
                                        testCompany.setText(test.getTestCompany());
                                        testPeople.setText(test.getTestPeople());
                                        testPrice.setText(test.getTestPrice()+"");
                                        testDate.setText(test.getTestDate());
                                        testListNote.setText(test.getTestListNote());
                                        if (test.getTestState()!=null&&!test.getTestState().equals("")){
                                            String testStates = test.getTestState();
                                            if (testStates.equals("检验成功")){
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

                    /*填写检验信息*/
                    Dialog dialog = new AlertDialog.Builder(context)
                            .setTitle("检验信息")
                            .setView(view)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String testCompanyStr = testCompany.getText().toString();
                                    String testPeopleStr = testPeople.getText().toString();
                                    String testPriceStr = testPrice.getText().toString();
                                    float testPriceFloat = 0;
                                    if (testPriceStr!=null&&!testPriceStr.equals("")){
                                        testPriceFloat = Float.parseFloat(testPriceStr);
                                    }
                                    String testDateStr = testDate.getText().toString();
                                    String testListNoteStr = testListNote.getText().toString();
                                    String testStateStr = "";
                                    for (int j=0; j<testState.getChildCount(); j++){
                                        RadioButton rd = (RadioButton) testState.getChildAt(j);
                                        if (rd.isChecked()){
                                            testStateStr = rd.getText().toString();
                                            break;
                                        }
                                    }
                                    AssetTest test = new AssetTest(barCodeStr, testCompanyStr, testPeopleStr, testDateStr, testPriceFloat,
                                            testListNoteStr, testStateStr,user.getSysUUID());

                                    if (testList.size()>0){
                                        for (int m=0;m<testList.size(); m++){
                                            if (testList.get(m).getBarCode().equals(barCodeStr)){
                                                testList.set(m, test);
                                            }else {
                                                if (m==testList.size()-1){
                                                    testList.add(test);
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        testList.add(test);
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
