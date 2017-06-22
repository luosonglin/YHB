package com.medmeeting.m.zhiyi.UI.LiveView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.ItemRemoveRecyclerView;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.MyAdapter;
import com.medmeeting.m.zhiyi.Widget.removeitemrecyclerview.OnItemClickListener;

import java.util.ArrayList;

public class MyLiveRoomActivity extends AppCompatActivity {

    private ItemRemoveRecyclerView mRecyclerView;
    private ArrayList<String> mList;
//    private ArrayList<BannerDto.BannersBean> mList;

    private Toolbar toolbar;

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mRecyclerView = (ItemRemoveRecyclerView) findViewById(R.id.id_item_remove_recyclerview);
        mList = new ArrayList<>();
//        mList.add(new BannerDto.BannersBean(0));
//        mList.add(new BannerDto.BannersBean(1));
//        mList.add(new BannerDto.BannersBean(2));
//        mList.add(new BannerDto.BannersBean(3));
        for (int i = 0; i < 10; i++) {
            mList.add(i + "");
        }

        final MyAdapter adapter = new MyAdapter(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MyLiveRoomActivity.this, "** " + mList.get(position) + " **", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {
                adapter.removeItem(position);
            }
        });
//        final MyLiveRoomAdapter adapter = new MyLiveRoomAdapter(R.layout.item_live_my_room, mList);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(MyLiveRoomActivity.this, "** " + mList.get(position) + " **", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDeleteClick(int position) {
////                adapter.removeItem(position);
//                adapter.remove(position);
//            }
//        });
    }
}