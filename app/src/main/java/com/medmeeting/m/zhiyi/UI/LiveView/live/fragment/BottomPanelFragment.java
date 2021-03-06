package com.medmeeting.m.zhiyi.UI.LiveView.live.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget.InputPanel;


public class BottomPanelFragment extends Fragment {

    private static final String TAG = "BottomPanelFragment";

    private ViewGroup buttonPanel;
    private ImageView btnInput;
    private InputPanel inputPanel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottombar, container);
        buttonPanel = (ViewGroup) view.findViewById(R.id.button_panel);
        btnInput = (ImageView) view.findViewById(R.id.btn_input);
        inputPanel = (InputPanel) view.findViewById(R.id.input_panel);

        btnInput.setOnClickListener(v -> {
//                buttonPanel.setVisibility(View.GONE);

            if (inputPanel.getVisibility() != View.VISIBLE) {
                inputPanel.setVisibility(View.VISIBLE);
            } else {
                inputPanel.setVisibility(View.GONE);
            }
        });
        return view;
    }

    /**
     * back键或者空白区域点击事件处理
     *
     * @return 已处理true, 否则false
     */
    public boolean onBackAction() {
        if (inputPanel.onBackAction()) {
            return true;
        }
        if (buttonPanel.getVisibility() != View.VISIBLE) {
            inputPanel.setVisibility(View.GONE);
            buttonPanel.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    public void setInputPanelListener(InputPanel.InputPanelListener l) {
        inputPanel.setPanelListener(l);
    }
}
