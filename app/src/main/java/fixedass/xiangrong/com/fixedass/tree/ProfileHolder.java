package fixedass.xiangrong.com.fixedass.tree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.xiangrong.library.model.TreeNode;

import fixedass.xiangrong.com.fixedass.R;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class ProfileHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
    private PrintView arrowView;

    public ProfileHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, IconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_profile_node, null, false);

        TextView tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);
        TextView deptCode = (TextView) view.findViewById(R.id.deptCode_value);
        deptCode.setText(value.deptCode);

        final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
        iconView.setIconText(context.getResources().getString(value.icon));

        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
        if (node.isLeaf()) {
            arrowView.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleCustom;
    }
}
