package com.medmeeting.m.zhiyi.UI.MineView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.MyVideoAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BasePageSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.VideoInfoUserEntity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoUpdateActivity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.ItemRemoveRecyclerView;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.OnItemClickListener;

import java.util.ArrayList;

import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 31/10/2017 4:27 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class MyVideoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ItemRemoveRecyclerView mRecyclerView;
    private MyVideoAdapter mAdapter;
    private ArrayList<VideoInfoUserEntity> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        mRecyclerView = (ItemRemoveRecyclerView) findViewById(R.id.id_item_remove_recyclerview);
        mList = new ArrayList<>();
        getMyVideoService();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void getMyVideoService() {
        BasePageSearchEntity basePageSearchEntity = new BasePageSearchEntity();
        basePageSearchEntity.setPageNum(1);
        basePageSearchEntity.setPageSize(100);
        HttpData.getInstance().HttpDataGetMyVideo(new Observer<HttpResult3<VideoInfoUserEntity, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(MyVideoActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<VideoInfoUserEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(MyVideoActivity.this, data.getMsg());
                    return;
                }
                mList.addAll(data.getData());
                mAdapter = new MyVideoAdapter(MyVideoActivity.this, mList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MyVideoActivity.this));
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        startActivity(new Intent(MyVideoActivity.this, VideoDetailActivity.class)
                                .putExtra("videoId", data.getData().get(position).getVideoId())
                                .putExtra("title", data.getData().get(position).getTitle())
                                .putExtra("photo", data.getData().get(position).getCoverPhoto()));
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        new AlertDialog.Builder(MyVideoActivity.this)
                                .setIcon(R.mipmap.logo)
                                .setTitle("")
                                .setMessage("确认删除？")
                                .setNegativeButton("算了", (dialogInterface, i) -> dialogInterface.dismiss())
                                .setPositiveButton("确认", (dialogInterface, i) -> HttpData.getInstance().HttpDataDeleteVideo(new Observer<HttpResult3>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }

                                    @Override
                                    public void onNext(HttpResult3 httpResult3) {
                                        if (!httpResult3.getStatus().equals("success")) {
                                            ToastUtils.show(MyVideoActivity.this, httpResult3.getMsg());
                                        }
                                        mAdapter.removeItem(position);
                                        dialogInterface.dismiss();
                                    }
                                }, data.getData().get(position).getVideoId()))
                                .show();
                    }

                    @Override
                    public void onUpdateClick(int position) {
                        startActivity(new Intent(MyVideoActivity.this, VideoUpdateActivity.class)
                                .putExtra("videoId", data.getData().get(position).getVideoId()));
                    }
                });
            }
        }, basePageSearchEntity);
    }
}

