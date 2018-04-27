package com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.R;

import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class TextMsgView extends BaseMsgView {

    private TextView username;
    private TextView msgText;

    public TextMsgView(Context context) {
        super(context);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.msg_text_view, this);
//        username = (TextView) view.findViewById(R.id.user_name);
        msgText = (TextView) view.findViewById(R.id.msg_text);
    }

    @Override
    public void setContent(MessageContent msgContent) {
        TextMessage msg = (TextMessage) msgContent;
//        username.setText(msg.getUserInfo().getName() + ": ");

//        username.setText(msgContent.getUserInfo().getName() + "： ");
        if (msgContent.getUserInfo() == null) return;
        msgText.setText(msgContent.getUserInfo().getName() + "： " + msg.getContent());
        //msgContent.getUserInfo().getUserId() + " " +
    }
}
