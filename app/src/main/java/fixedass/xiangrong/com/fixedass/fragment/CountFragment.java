package fixedass.xiangrong.com.fixedass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import fixedass.xiangrong.com.fixedass.R;
import fixedass.xiangrong.com.fixedass.count.CounttingActivity;
import fixedass.xiangrong.com.fixedass.count.ListActivity;
import fixedass.xiangrong.com.fixedass.count.ResultActivity;
import fixedass.xiangrong.com.fixedass.tool.Redirect;

/**
 * Created by Administrator on 2018/4/24.
 */

public class CountFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_count, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout countList = (LinearLayout)getActivity().findViewById(R.id.countList);
        LinearLayout countting = (LinearLayout)getActivity().findViewById(R.id.countting);
        LinearLayout countResult = (LinearLayout)getActivity().findViewById(R.id.countResult);

        final Bundle bundle = getArguments();
        countList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(getContext(), ListActivity.class, bundle);
            }
        });
        countting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(getContext(), CounttingActivity.class,bundle);
            }
        });
        countResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.redirect(getContext(), ResultActivity.class,bundle);
            }
        });
    }
}
