package fixedass.xiangrong.com.fixedass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.summary.InquireActivity;
import fixedass.xiangrong.com.fixedass.summary.LedgerActivity;
import fixedass.xiangrong.com.fixedass.summary.SumActivity;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/**
 * Created by Administrator on 2018/4/24.
 */

public class SummaryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_summary, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout assetLedger = (LinearLayout)getActivity().findViewById(R.id.assetLedger);
        LinearLayout assetInquire = (LinearLayout)getActivity().findViewById(R.id.assetInquire);
        LinearLayout assetSum = (LinearLayout)getActivity().findViewById(R.id.assetSum);

        final Bundle bundle = getArguments();
        assetLedger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(getContext(), LedgerActivity.class,bundle);
            }
        });

        assetInquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(getContext(), InquireActivity.class,bundle);
            }
        });

        assetSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(getContext(), SumActivity.class,bundle);
            }
        });
    }
}
