package fixedass.xiangrong.com.fixedass.tree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.xiangrong.library.model.TreeNode;

import fixedass.xiangrong.com.fixedass.R;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class HeaderHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {

    private PrintView arrowView;
    private int deptType;
    private View view;

    public HeaderHolder(Context context, int deptType) {
        super(context);
        this.deptType = deptType;
    }

    @Override
    public View createNodeView(TreeNode node, IconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_header_node, null, false);

        TextView tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);
        TextView deptCode = (TextView) view.findViewById(R.id.deptCode_value);
        deptCode.setText(value.deptCode);

        /*final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
        iconView.setIconText(context.getResources().getString(value.icon));*/

        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
        if(deptType==1){
            RelativeLayout.LayoutParams text_Name_Params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            text_Name_Params.setMargins(dip2px(40), dip2px(10), 0, 0);
            /*tvValue.setLayoutParams(text_Name_Params);*/
            arrowView.setLayoutParams(text_Name_Params);
        }
        if (node.isLeaf()) {
            arrowView.setVisibility(View.INVISIBLE);
        }

        if (node.isLastChild()){
            view.findViewById(R.id.bot_line).setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
        if (active){
            view.findViewById(R.id.bot_line).setVisibility(View.VISIBLE);
        }else{
            view.findViewById(R.id.bot_line).setVisibility(View.INVISIBLE);
        }

        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
