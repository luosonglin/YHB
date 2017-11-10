package com.medmeeting.m.zhiyi.UI.LiveView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.LiveView.live.animation.HeartLayout;
import com.medmeeting.m.zhiyi.UI.LiveView.live.fragment.BottomPanelFragment;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.LiveKit;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.controller.ChatListAdapter;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.message.GiftMessage;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget.ChatListView;
import com.medmeeting.m.zhiyi.UI.LiveView.live.liveshow.ui.widget.InputPanel;

import java.util.Random;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 10/11/2017 7:47 AM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class LivePlayerActivity2 extends AppCompatActivity implements Handler.Callback {

    private Handler handler = new Handler(this);
    private ChatListView chatListView;
    private ChatListAdapter chatListAdapter;
    private BottomPanelFragment bottomPanel;
    private ImageView btnDan;
    private ImageView btnGift;
    private ImageView btnHeart;
    private HeartLayout heartLayout;
    private Random random = new Random();

    private int programId;

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_player2);


        //init 互动view
        LiveKit.addEventHandler(handler);
        chatListView = (ChatListView) findViewById(R.id.chat_listview);
        chatListAdapter = new ChatListAdapter();
        chatListView.setAdapter(chatListAdapter);
        bottomPanel = (BottomPanelFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_bar);
        btnDan = (ImageView) bottomPanel.getView().findViewById(R.id.btn_dan);
        btnGift = (ImageView) bottomPanel.getView().findViewById(R.id.btn_gift);
        btnHeart = (ImageView) bottomPanel.getView().findViewById(R.id.btn_heart);
        heartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        btnDan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatListView.getVisibility() == View.VISIBLE) {
                    chatListView.setVisibility(View.GONE);
                    btnDan.setImageResource(R.mipmap.icon_dan_close);
                } else if (chatListView.getVisibility() == View.GONE) {
                    chatListView.setVisibility(View.VISIBLE);
                    btnDan.setImageResource(R.mipmap.icon_dan);
                }
            }
        });
        btnGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GiftMessage msg = new GiftMessage("2", "送您一个礼物");
                LiveKit.sendMessage(msg);
            }
        });
        btnHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        int rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                        heartLayout.addHeart(rgb);
                    }
                });
                GiftMessage msg = new GiftMessage("1", "点赞了");
                LiveKit.sendMessage(msg);
            }
        });
        bottomPanel.setInputPanelListener(new InputPanel.InputPanelListener() {
            @Override
            public void onSendClick(String text) {
                final TextMessage content = TextMessage.obtain(text);
                LiveKit.sendMessage(content);
            }
        });

        programId = getIntent().getExtras().getInt("programId");
        joinChatRoom(programId + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
//                final InformationNotificationMessage content = InformationNotificationMessage.obtain("离开了房间");
//                LiveKit.sendMessage(content);

                // 配合ios，tony说"去掉离开了房间"
//                TextMessage content = TextMessage.obtain("离开了房间");
//                LiveKit.sendMessage(content);
//                Log.e(TAG, content + " " + content.getUserInfo().getName());

                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
//                Toast.makeText(LivePlayerActivity.this, "退出聊天室成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
//                Toast.makeText(LivePlayerActivity.this, "退出聊天室失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * im
     */
    @Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case LiveKit.MESSAGE_ARRIVED: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SENT: {
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SEND_ERROR: {
                break;
            }
            default:
        }
        chatListAdapter.notifyDataSetChanged();
        return false;
    }

    private void joinChatRoom(final String roomId) {
        LiveKit.joinChatRoom(roomId, 15, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
//                final InformationNotificationMessage content = InformationNotificationMessage.obtain("进入了房间");
//                LiveKit.sendMessage(content);

                // 配合ios
                TextMessage content = TextMessage.obtain("进入了房间");
                LiveKit.sendMessage(content);

                Log.e(" joinChatRoom: ", content + "" + content.getUserInfo().getName());
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
//                Toast.makeText(LivePlayerActivity.this, "聊天室加入失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
                Log.e("", "聊天室加入失败! errorCode = " + errorCode);
            }
        });
    }
}
