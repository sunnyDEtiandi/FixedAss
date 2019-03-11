package fixedass.xiangrong.com.fixedass.tree;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xiangrong.library.model.TreeNode;
import com.xiangrong.library.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.AssetClass;
import fixedass.xiangrong.com.fixedass.bean.DataBean;
import fixedass.xiangrong.com.fixedass.count.ListAddActivity;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/**
 * @author Eileen
 * @create 2018-11-15
 * @Describe 部门树结构设计
 */
public class ClassListFragment extends Fragment implements TreeNode.TreeNodeClickListener {
    private AndroidTreeView tView;

    private Bundle bundle;

    private DataBean<AssetClass> clazzList;                 //获得所有资产类别
    private List<AssetClass> classList=new ArrayList<>();   //获得所有分类
    private List<AssetClass> firstClass=new ArrayList<>(), twoClass=new ArrayList<>() ,threeClass=new ArrayList<>() ,fourClass=new ArrayList<>();              //用于装各级分类的容器

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bundle = getArguments();
        clazzList = (DataBean<AssetClass>)bundle.getSerializable("classList");
        //避免报空指针异常,重新创建了mData,并添加了data
        List<AssetClass> data = clazzList.getData();
        classList.addAll(data);

        splitList();

        final View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        final ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);
        rootView.findViewById(R.id.status_bar).setVisibility(View.GONE);

        final TreeNode root = addProfileData();

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        tView.setDefaultNodeClickListener(ClassListFragment.this);
        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }

        return rootView;
    }

    private void splitList(){
        for (AssetClass clazz:classList){
            switch (clazz.getClassCode().length()){
                case 2:
                    firstClass.add(clazz);
                    break;
                case 4:
                    twoClass.add(clazz);
                    break;
                case 6:
                    threeClass.add(clazz);
                    break;
                case 8:
                    fourClass.add(clazz);
                    break;
            }
        }
    }

    private TreeNode addProfileData() {
        TreeNode root = TreeNode.root();
        for (AssetClass first:firstClass){
            TreeNode gen = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_info,first.getClassName(),first.getClassCode())).setViewHolder(new ProfileHolder(getActivity()));
            for (AssetClass two:twoClass){
                if (two.getParentClassCode().equals(first.getClassCode())){
                    TreeNode twoLevel = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_info,two.getClassName(),two.getClassCode())).setViewHolder(new HeaderHolder(getActivity(),0));
                    for (AssetClass three:threeClass){
                        if (three.getParentClassCode().equals(two.getClassCode())){
                            TreeNode threeLevel = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_info,three.getClassName(),three.getClassCode())).setViewHolder(new HeaderHolder(getActivity(),1));
                            for (AssetClass four:fourClass){
                                if (three.getClassCode().equals(four.getParentClassCode())){
                                    TreeNode fourLevel = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_info, four.getClassName(), four.getClassCode())).setViewHolder(new SocialViewHolder(getActivity(),1));
                                    threeLevel.addChildren(fourLevel);
                                }
                            }
                            twoLevel.addChildren(threeLevel);
                        }
                    }
                    gen.addChildren(twoLevel);
                }
            }
            root.addChildren(gen);
        }
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

    @Override
    public void onClick(TreeNode node, Object value) {
        if (value instanceof SocialViewHolder.SocialItem){
            SocialViewHolder.SocialItem item = (SocialViewHolder.SocialItem) value;

            /*Toast toast = Toast.makeText(getActivity(), ((SocialViewHolder.SocialItem)value).text, Toast.LENGTH_SHORT);
            toast.show();*/

            bundle.putString("className", item.text);
            bundle.putString("classCode", item.pUUID);
            Redirect.redirect(getActivity(), ListAddActivity.class, bundle);
            //Toast toast = Toast.makeText(getActivity(), ((IconTreeItemHolder.IconTreeItem)value).text, Toast.LENGTH_SHORT);
        }else if (value instanceof IconTreeItemHolder.IconTreeItem){
            List<TreeNode> children = node.getChildren();
            if (children.size()<1){
                IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;

                /*Toast toast = Toast.makeText(getActivity(), item.text, Toast.LENGTH_SHORT);
                toast.show();*/

                bundle.putString("className", item.text);
                bundle.putString("classCode", item.deptCode);
                Redirect.redirect(getActivity(), ListAddActivity.class, bundle);
            }
        }
    }
}
