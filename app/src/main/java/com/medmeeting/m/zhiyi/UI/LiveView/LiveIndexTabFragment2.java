package com.medmeeting.m.zhiyi.UI.LiveView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medmeeting.m.zhiyi.R;

public class LiveIndexTabFragment2 extends Fragment {

    public static LiveIndexTabFragment2 newInstance() {
        LiveIndexTabFragment2 fragment = new LiveIndexTabFragment2();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_fragment2, container, false);
    }
}
