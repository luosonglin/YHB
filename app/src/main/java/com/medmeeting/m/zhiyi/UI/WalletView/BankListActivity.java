package com.medmeeting.m.zhiyi.UI.WalletView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.BankAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BankDto;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class BankListActivity extends AppCompatActivity {
    private static final String TAG = BankListActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mQuickAdapter;
    private List<BankDto> banks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        toolBar();

        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        //分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(BankListActivity.this, DividerItemDecoration.VERTICAL));
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);

        banks.add(new BankDto("中国工商银行", R.mipmap.bankimage_0));
        banks.add(new BankDto("中国农业银行", R.mipmap.bankimage_1));
        banks.add(new BankDto("中国银行", R.mipmap.bankimage_2));
        banks.add(new BankDto("中国建设银行", R.mipmap.bankimage_3));
        banks.add(new BankDto("交通银行", R.mipmap.bankimage_4));
        banks.add(new BankDto("中国邮政储蓄银行", R.mipmap.bankimage_5));
        banks.add(new BankDto("中信银行", R.mipmap.bankimage_6));
        banks.add(new BankDto("中国光大银行", R.mipmap.bankimage_7));
        banks.add(new BankDto("华夏银行", R.mipmap.bankimage_8));
        banks.add(new BankDto("中国民生银行", R.mipmap.bankimage_9));
        banks.add(new BankDto("广发银行", R.mipmap.bankimage_10));
        banks.add(new BankDto("深圳发展银行", R.mipmap.bankimage_11));
        banks.add(new BankDto("招商银行", R.mipmap.bankimage_12));
        banks.add(new BankDto("兴业银行", R.mipmap.bankimage_13));
        banks.add(new BankDto("上海浦东发展银行", R.mipmap.bankimage_14));
        banks.add(new BankDto("恒丰银行", R.mipmap.bankimage_15));
        banks.add(new BankDto("浙商银行", R.mipmap.bankimage_16));
        banks.add(new BankDto("渤海银行", R.mipmap.bankimage_17));

        mQuickAdapter = new BankAdapter(R.layout.item_bank, banks);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_CUSTOM);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6,true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);

        mQuickAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
            Intent intent = new Intent();
            intent.putExtra("bank", banks.get(position).getName());
            setResult(1, intent);
            finish();
        });
    }

    private void toolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
