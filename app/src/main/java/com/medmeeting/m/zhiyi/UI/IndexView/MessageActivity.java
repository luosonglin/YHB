package com.medmeeting.m.zhiyi.UI.IndexView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.PushUserMessage;
import com.medmeeting.m.zhiyi.UI.OtherVIew.BrowserActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.MessageAdapter;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.MessageItemRemoveRecyclerView;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 13/12/2017 3:57 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MessageActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private MessageItemRemoveRecyclerView mRecyclerView;
    private ArrayList<PushUserMessage> mList;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        initView();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> finish());
    }


    private void initView() {
        //内容
        mRecyclerView = (MessageItemRemoveRecyclerView) findViewById(R.id.id_item_remove_recyclerview);
        mList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", 100);
        HttpData.getInstance().HttpDataSelectAllPushList(new Observer<HttpResult3<PushUserMessage, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(HttpResult3<PushUserMessage, Object> data) {
                if (!data.getStatus().equals("success")) {
//                    ToastUtils.show(getLocalClassName(), data.getMsg());
                    return;
                }
                mList.addAll(data.getData());
                Log.e(getLocalClassName(), "onNext");

                adapter = new MessageAdapter(MessageActivity.this, mList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        BrowserActivity.launch(MessageActivity.this, "http://mobile.medmeeting.com/#/wv/message/"+data.getData().get(position).getMessageId(), "消息详情");
                    }

                    @Override
                    public void onDeleteClick(final int position) {
                        new AlertDialog.Builder(MessageActivity.this)
                                .setIcon(R.mipmap.logo)
                                .setTitle("")
                                .setMessage("确认删除？")
                                .setNegativeButton("算了", (dialogInterface, i) -> dialogInterface.dismiss())
                                .setPositiveButton("确认", (dialogInterface, i) -> {
                                    HttpData.getInstance().HttpDataDeletePush(new Observer<HttpResult3>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            ToastUtils.show(MessageActivity.this, e.getMessage());
                                        }

                                        @Override
                                        public void onNext(HttpResult3 httpResult3) {
                                            if (!httpResult3.getStatus().equals("success")) {
                                                ToastUtils.show(MessageActivity.this, httpResult3.getMsg());
                                                return;
                                            }
                                            adapter.removeItem(position);
                                        }
                                    }, data.getData().get(position).getMessageId());
                                })
                                .show();
                    }

                    @Override
                    public void onUpdateClick(int position) {

                    }
                });
            }
        }, map);
    }

}
