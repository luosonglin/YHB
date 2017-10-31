package com.medmeeting.m.zhiyi.UI.LiveView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveRoomDto;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.ItemRemoveRecyclerView;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.MyAdapter;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.OnItemClickListener;

import java.util.ArrayList;

import rx.Observer;

public class MyLiveRoomActivity extends AppCompatActivity {

    private static final String TAG = MyLiveRoomActivity.class.getSimpleName();
    private ItemRemoveRecyclerView mRecyclerView;
    private ArrayList<LiveRoomDto> mList;

    private Toolbar toolbar;
    private TextView addTv;
    private MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_my_room);
        toolBar();
        initView();
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
        addTv = (TextView) findViewById(R.id.add);
        addTv.setOnClickListener(view -> {
            startActivity(new Intent(MyLiveRoomActivity.this, LiveBuildRoomActivity.class));
            finish();
        });
    }

    private void initView() {

        //内容
        mRecyclerView = (ItemRemoveRecyclerView) findViewById(R.id.id_item_remove_recyclerview);
        mList = new ArrayList<>();
        HttpData.getInstance().HttpDataGetLiveRoom(new Observer<HttpResult3<LiveRoomDto, Object>>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage()
                        + "\n" + e.getCause()
                        + "\n" + e.getLocalizedMessage()
                        + "\n" + e.getStackTrace());
            }

            @Override
            public void onNext(HttpResult3<LiveRoomDto, Object> liveRoomDtoHttpResult3) {
                mList.addAll(liveRoomDtoHttpResult3.getData());
                Log.e(TAG, "onNext");

                adapter = new MyAdapter(MyLiveRoomActivity.this, mList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MyLiveRoomActivity.this));
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MyLiveRoomActivity.this, MyLiveRoomDetailActivity.class);
                        intent.putExtra("roomId", mList.get(position).getId());
                        intent.putExtra("title", mList.get(position).getTitle());
                        intent.putExtra("coverPhoto", mList.get(position).getCoverPhoto());
                        intent.putExtra("number", mList.get(position).getNumber());
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(final int position) {
                        HttpData.getInstance().HttpDataDeleteLiveRoom(new Observer<HttpResult3>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "HttpDataDeleteLiveRoom onError: " + e.getMessage()
                                        + "\n" + e.getCause()
                                        + "\n" + e.getLocalizedMessage()
                                        + "\n" + e.getStackTrace());
                            }

                            @Override
                            public void onNext(HttpResult3 httpResult3) {
                                if (httpResult3.getStatus().equals("success")) {
                                    adapter.removeItem(position);
                                } else  {
                                    ToastUtils.show(MyLiveRoomActivity.this, httpResult3.getMsg());
                                }
                                Log.d(TAG, "onNext");
                            }
                        }, mList.get(position).getId());
                    }

                    @Override
                    public void onUpdateClick(int position) {
                        Intent intent = new Intent(MyLiveRoomActivity.this, LiveUpdateRoomActivity.class);
                        intent.putExtra("roomId", mList.get(position).getId());
                        intent.putExtra("coverPhoto", mList.get(position).getCoverPhoto());
                        intent.putExtra("labelIds", mList.get(position).getLabelIds());
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
