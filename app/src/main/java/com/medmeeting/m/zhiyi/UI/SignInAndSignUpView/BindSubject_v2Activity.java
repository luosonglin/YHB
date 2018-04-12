package com.medmeeting.m.zhiyi.UI.SignInAndSignUpView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.library.flowlayout.FlowLayoutManager;
import com.library.flowlayout.SpaceItemDecoration;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MainActivity;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.SubjectAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.TagDto;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 22/3/2018
 * @describe 绑定学科类目
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class BindSubject_v2Activity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.history_list)
    RecyclerView mRecyclerView;

    private BaseQuickAdapter mQuickAdapter;

    final List<TagDto> tags = new ArrayList<>();
    final List<TagDto> tags_confirm = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_subject_v2);
        ButterKnife.bind(this);

        toolBar();

        initView();
    }

    private void initView() {
        mRecyclerView.stopNestedScroll();
        //设置RecyclerView的显示模式  当前List模式
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        //设置每一个item间距
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(4));
        mRecyclerView.setLayoutManager(flowLayoutManager);


//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

//        mRecyclerView.setLayoutManager(new LinearLayoutManager(BindSubject_v2Activity.this));

        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);

        mQuickAdapter = new SubjectAdapter(R.layout.item_tag_subject, null); //item_tag
        mRecyclerView.setAdapter(mQuickAdapter);

        Map<String, Object> options = new HashMap<>();
        options.put("limit", 1000);
        options.put("item_tag", "COMMON");
        HttpData.getInstance().HttpDataGetVideoTags(new Observer<HttpResult3<TagDto, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3<TagDto, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(BindSubject_v2Activity.this, data.getMsg());
                    return;
                }
                mQuickAdapter.setNewData(data.getData());

                tags.addAll(data.getData());

                for (TagDto i : (List<TagDto>) mQuickAdapter.getData()) {
                    Log.e(getLocalClassName(), i.getLabelName());
                }

                tags_confirm.clear();
                mQuickAdapter.setOnRecyclerViewItemClickListener((view12, position) -> {

                });

                mQuickAdapter.setOnRecyclerViewItemChildClickListener((adapter, view, position) -> {
                    TagDto tagDto = (TagDto) adapter.getItem(position);
                    switch (view.getId()) {
                        case R.id.name:
                            if (!tags_confirm.contains(tagDto)) {
                                if(tags_confirm.size()>3){
                                    ToastUtils.show(BindSubject_v2Activity.this, "最多只能有3个标签");
                                    return;
                                }
                                tags_confirm.add(tagDto);
                                view.setBackgroundResource(R.drawable.textview_radius_blue_v3);
                            } else {
                                tags_confirm.remove(tagDto);
                                view.setBackgroundResource(R.drawable.textview_radius_grey_v2);
                            }
                            break;
                    }
                });
            }
        }, options);
    }

    private void toolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.start)
    public void onClick() {
        Object subjects = "";
        for (TagDto i : tags_confirm) {
            if (subjects.equals(""))
                subjects = "" + i.getId();
            else
                subjects += "," + i.getId();
        }

        Log.e(getLocalClassName(), subjects.toString());

        HttpData.getInstance().HttpDataUserSubject(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3 data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(BindSubject_v2Activity.this, data.getMsg());
                    return;
                }
                finish();
                startActivity(new Intent(BindSubject_v2Activity.this, MainActivity.class));
            }
        }, subjects);
    }
}
