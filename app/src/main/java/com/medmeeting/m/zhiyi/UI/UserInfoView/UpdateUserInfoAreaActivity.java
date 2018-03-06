package com.medmeeting.m.zhiyi.UI.UserInfoView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.CityAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BaseArea;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class UpdateUserInfoAreaActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.rv_list2)
    RecyclerView rvList2;

    private BaseQuickAdapter mQuickAdapter, mQuickAdapter2;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info_area);
        ButterKnife.bind(this);

        toolBar();

        rvList.setVisibility(View.VISIBLE);
        rvList2.setVisibility(View.GONE);

        //设置RecyclerView的显示模式  当前List模式
        rvList.setLayoutManager(new LinearLayoutManager(this));
        //如果Item高度固定  增加该属性能够提高效率
        rvList.setHasFixedSize(true);
        //分割线
        rvList.addItemDecoration(new DividerItemDecoration(UpdateUserInfoAreaActivity.this, DividerItemDecoration.VERTICAL));
        //设置适配器
        mQuickAdapter = new CityAdapter(R.layout.item_hospital, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //将适配器添加到RecyclerView
        rvList.setAdapter(mQuickAdapter);


        //设置RecyclerView的显示模式  当前List模式
        rvList2.setLayoutManager(new LinearLayoutManager(this));
        //如果Item高度固定  增加该属性能够提高效率
        rvList2.setHasFixedSize(true);
        //分割线
        rvList2.addItemDecoration(new DividerItemDecoration(UpdateUserInfoAreaActivity.this, DividerItemDecoration.VERTICAL));
        //设置适配器
        mQuickAdapter2 = new CityAdapter(R.layout.item_hospital, null);
        //设置加载动画
        mQuickAdapter2.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //将适配器添加到RecyclerView
        rvList2.setAdapter(mQuickAdapter2);

        getAreaService(0);
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> {
            if (rvList.getVisibility() == View.VISIBLE)
                finish();
            else {
                rvList.setVisibility(View.VISIBLE);
                rvList2.setVisibility(View.GONE);
            }
        });
    }

    private void getAreaService(Integer areaId) {
        HttpData.getInstance().HttpDataGetArea(new Observer<HttpResult3<BaseArea, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<BaseArea, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(UpdateUserInfoAreaActivity.this, data.getMsg());
                    return;
                }

                if (rvList.getVisibility() == View.VISIBLE) {
                    mQuickAdapter.setNewData(data.getData());
                    mQuickAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                        rvList.setVisibility(View.GONE);
                        rvList2.setVisibility(View.VISIBLE);
                        city = data.getData().get(position).getName();
                        getAreaService(data.getData().get(position).getBaseAreaid());
                    });
                } else {
                    mQuickAdapter2.setNewData(data.getData());
                    mQuickAdapter2.setOnRecyclerViewItemClickListener((view, position) -> {
                        Intent intent = new Intent();
                        intent.putExtra("area", city + " - " + data.getData().get(position).getName());
                        UpdateUserInfoAreaActivity.this.setResult(3, intent);
                        finish();
                    });
                }
            }
        }, areaId);
    }

}
