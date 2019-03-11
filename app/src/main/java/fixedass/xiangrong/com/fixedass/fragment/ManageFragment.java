package fixedass.xiangrong.com.fixedass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.manage.BorrowActivity;
import fixedass.xiangrong.com.fixedass.manage.FixActivity;
import fixedass.xiangrong.com.fixedass.manage.ScrapActivity;
import fixedass.xiangrong.com.fixedass.manage.TestActivity;
import fixedass.xiangrong.com.fixedass.manage.TransferActivity;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/**
 * Created by Administrator on 2018/4/24.
 */

public class ManageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_manage, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout assetBorrow = (LinearLayout)getActivity().findViewById(R.id.assetBorrow);
        LinearLayout assetScrap = (LinearLayout)getActivity().findViewById(R.id.assetScrap);
        LinearLayout assetService = (LinearLayout)getActivity().findViewById(R.id.assetService);
        LinearLayout assetTest = (LinearLayout)getActivity().findViewById(R.id.assetTest);
        LinearLayout assetTransfer = (LinearLayout)getActivity().findViewById(R.id.assetTransfer);

        final Bundle bundle = getArguments();
        assetBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(getContext(), BorrowActivity.class,bundle);
            }
        });
        assetScrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(getContext(), ScrapActivity.class,bundle);
            }
        });
        assetService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(getContext(), FixActivity.class,bundle);
            }
        });
        assetTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(getContext(), TestActivity.class,bundle);
            }
        });
        assetTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(getContext(), TransferActivity.class,bundle);
            }
        });
    }
}
