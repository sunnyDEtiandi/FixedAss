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
public class SocialViewHolder extends TreeNode.BaseNodeViewHolder<SocialViewHolder.SocialItem> {

    /*private static final String[] NAMES = new String[]{"Bruce Wayne", "Clark Kent", "Barry Allen", "Hal Jordan"};*/
    private int deptType;

    public SocialViewHolder(Context context, int deptType) {
        super(context);
        this.deptType = deptType;
    }

    @Override
    public View createNodeView(TreeNode node, SocialItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_social_node, null, false);

        final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
        iconView.setIconText(context.getResources().getString(value.icon));
        if(deptType==1){
            RelativeLayout.LayoutParams text_Name_Params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            text_Name_Params.setMargins(dip2px(40), dip2px(10), 0, 0);
            iconView.setLayoutParams(text_Name_Params);
        }

        TextView userNameLabel = (TextView) view.findViewById(R.id.username);
        userNameLabel.setText(value.text);

        return view;
    }

    @Override
    public void toggle(boolean active) {
    }

    public static class SocialItem {
        public int icon;
        public String text;
        public String pUUID;

        public SocialItem(int icon, String text, String pUUID) {
            this.icon = icon;
            this.text = text;
            this.pUUID = pUUID;
        }
    }

    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
