package fixedass.xiangrong.com.fixedass.tool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.CountSel;

public class CheckboxAdapter extends BaseAdapter {
    Context context;
    List<CountSel> listData;
    //记录checkbox的状态
    private ViewHolder viewHolder;

    //构造函数
    public CheckboxAdapter(Context context,List<CountSel> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 重写View
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CountSel countSel = listData.get(position);

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_table_lists, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.list_table_tvhead1);
            viewHolder.countBillCode = (TextView)convertView.findViewById(R.id.list_table_tvhead2);
            viewHolder.cPeoInfo = (TextView)convertView.findViewById(R.id.list_table_tvhead3);
            viewHolder.createDate = (TextView)convertView.findViewById(R.id.list_table_tvhead4);
            viewHolder.countNote = (TextView)convertView.findViewById(R.id.list_table_tvhead5);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                countSel.setCheck(isChecked);
            }
        });

        viewHolder.countBillCode.setText(countSel.getCountBillCode());
        viewHolder.cPeoInfo.setText(countSel.getcPeoInfo());
        viewHolder.createDate.setText(countSel.getCreateDate());
        viewHolder.countNote.setText(countSel.getCountNote());
        viewHolder.checkBox.setChecked(countSel.isCheck());

        return convertView;
    }

    class ViewHolder{
        CheckBox checkBox;
        TextView countBillCode;
        TextView cPeoInfo;
        TextView createDate;
        TextView countNote;
    }
}
