package fixedass.xiangrong.com.fixedass.tree;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiangrong.library.model.TreeNode;
import com.xiangrong.library.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.bean.DataBean;
import fixedass.xiangrong.com.fixedass.bean.Dept;
import fixedass.xiangrong.com.fixedass.bean.DptPeople;
import fixedass.xiangrong.com.fixedass.manage.TransferUpdActivity;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

public class TransferUpdUserFragment extends Fragment implements TreeNode.TreeNodeClickListener {
    private AndroidTreeView tView;

    private Bundle bundle;

    private Dept group;                             //集团
    private DataBean<Dept> twoDept, deptJstree;         //集团下的部门或者公司，公司下的部门
    private List<Dept> twoDeptList = new ArrayList<>();     //集团下的部门或者公司的集合
    private List<Dept> deptJstreeList = new ArrayList<>();   //公司下的部门的集合
    private DataBean<DptPeople> dptPeo;                        //集团下所有的部门员工
    private List<DptPeople> dptPeoList = new ArrayList<>();     //集团下所有部门员工的集合

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bundle = getArguments();
        group = (Dept)bundle.getSerializable("group");
        twoDept = (DataBean<Dept>)bundle.getSerializable("twoDept");
        //避免报空指针异常,重新创建了mData,并添加了data
        List<Dept> twoDeptData = twoDept.getData();
        twoDeptList.addAll(twoDeptData);

        deptJstree = (DataBean<Dept>)bundle.getSerializable("deptJstree");
        List<Dept> deptJstreeData  = deptJstree.getData();
        deptJstreeList.addAll(deptJstreeData);

        dptPeo = (DataBean<DptPeople>)bundle.getSerializable("dptPeoJstree") ;
        List<DptPeople> dptPeoData = dptPeo.getData();
        dptPeoList.addAll(dptPeoData);

        final View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        final ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);
        rootView.findViewById(R.id.status_bar).setVisibility(View.GONE);

        final TreeNode root = addProfileData();

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        tView.setDefaultNodeClickListener(TransferUpdUserFragment.this);
        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }

        return rootView;
    }

    private TreeNode addProfileData() {
        TreeNode root = TreeNode.root();
        TreeNode gen = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder,group.getDeptName(),group.getDeptCode())).setViewHolder(new ProfileHolder(getActivity()));

        /*添加部门人员*/
        for (DptPeople dp: dptPeoList){
            if (dp.getpDptCode().equals(group.getDeptCode())){
                TreeNode peoNode = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_people, dp.getpName(), dp.getpUUID())).setViewHolder(new SocialViewHolder(getActivity(),0));
                gen.addChildren(peoNode);
            }
        }

        for (Dept d: twoDeptList){
            TreeNode treeNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_people, d.getDeptName(),d.getDeptCode())).setViewHolder(new HeaderHolder(getActivity(),0));
            //公司以及集团直属部门下的员工
            for (DptPeople dp: dptPeoList){
                if (dp.getpDptCode().equals(d.getDeptCode())){
                    TreeNode peoNode = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_people, dp.getpName(), dp.getpUUID())).setViewHolder(new SocialViewHolder(getActivity(),0));
                    treeNode.addChildren(peoNode);
                }
            }

            if (d.getDeptType().equals("0")){       //公司
                for (Dept de: deptJstreeList){
                    //第三级的父级等于第二级的公司
                    if(de.getpDeptUUID().equals(d.getDeptUUID())){      //de.getDeptName()

                        //TreeNode deptNode = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_twitter)).setViewHolder(new SocialViewHolder(getActivity()));
                        //TreeNode twitter = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_twitter)).setViewHolder(new SocialViewHolder(getActivity()));
                        //部门
                        TreeNode deptNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_people, de.getDeptName(), de.getDeptCode() )).setViewHolder(new HeaderHolder(getActivity(), 1));
                        //该部门下的员工
                        for (DptPeople dptPeople: dptPeoList){
                            if(dptPeople.getpDptCode().equals(de.getDeptCode())){
                                TreeNode peoNode = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_people, dptPeople.getpName(), dptPeople.getpUUID())).setViewHolder(new SocialViewHolder(getActivity(), 1));
                                deptNode.addChildren(peoNode);
                            }
                        }
                        treeNode.addChildren(deptNode);
                    }
                }
            }
            gen.addChildren(treeNode);
        }


        root.addChildren(gen);
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

            bundle.putString("pName", item.text);
            bundle.putString("pUUID", item.pUUID);
            Redirect.redirect(getActivity(), TransferUpdActivity.class, bundle);
            //Toast toast = Toast.makeText(getActivity(), ((IconTreeItemHolder.IconTreeItem)value).text, Toast.LENGTH_SHORT);
        }
    }
}
