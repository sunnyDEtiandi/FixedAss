package fixedass.xiangrong.com.fixedass.asset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.User;

public class AssetAdapter extends BaseAdapter {
    private ArrayList<AssetStorage> stoList;
    private Context context;
    private int fragid;
    private User user;
    private ArrayList<AssetStorage> selStoList;
    private ArrayList<String> barCodeList;

    //记录checkbox的状态
    public HashMap<Integer, Boolean> selCheck = new HashMap<Integer, Boolean>();

    public AssetAdapter(ArrayList<AssetStorage> stoList, Context context, int fragid, User user, ArrayList<AssetStorage> selStoList) {
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
        convertView = inflater.inflate(R.layout.layout_asset, null);

        if(convertView!=null){
            AssetStorage storage = stoList.get(position);
            EditText barCode = (EditText) convertView.findViewById(R.id.barCode);
            barCode.setText(storage.getBarCode());
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

            CheckBox selCode = (CheckBox)convertView.findViewById(R.id.selCode);
            if(barCodeList!=null&&barCodeList.contains(storage.getBarCode())){
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
        }

        return convertView;
    }
}
