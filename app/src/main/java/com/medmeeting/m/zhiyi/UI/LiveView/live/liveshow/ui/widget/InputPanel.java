package com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

public class InputPanel extends LinearLayout {

    private final static String TAG = "InputPanel";

    private ViewGroup inputBar;
    private EditText textEditor;
    private TextView sendBtn;

    private InputPanelListener listener;

    public interface InputPanelListener {
        void onSendClick(String text);
    }

    public InputPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_input_panel, this);
        inputBar = (ViewGroup) findViewById(R.id.input_bar);
        textEditor = (EditText) findViewById(R.id.input_editor);
        sendBtn = (TextView) findViewById(R.id.input_send);

        textEditor.setOnFocusChangeListener((v, hasFocus) -> {
            inputBar.setSelected(hasFocus);
        });
        textEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sendBtn.setEnabled(!s.toString().isEmpty());
                int start = textEditor.getSelectionStart();
                int end = textEditor.getSelectionEnd();
                textEditor.removeTextChangedListener(this);
                textEditor.setText(s.toString(), TextView.BufferType.SPANNABLE);
                textEditor.setSelection(start, end);
                textEditor.addTextChangedListener(this);
            }
        });

        sendBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSendClick(textEditor.getText().toString());
                hideKeyboard();
            }
            textEditor.getText().clear();
        });

    }

    public void setPanelListener(InputPanelListener l) {
        listener = l;
    }

    /**
     * back键或者空白区域点击事件处理
     *
     * @return 已处理true, 否则false
     */
    public boolean onBackAction() {
        return false;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }
}
