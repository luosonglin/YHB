package com.medmeeting.m.zhiyi.UI.IndexView;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.IndexChildAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.SearchUserRedAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.UserRedEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserRedSearchEntity;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 29/11/2017 2:02 PM
 * @describe 搜索页
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class SearchActicity extends AppCompatActivity {

    @Bind(R.id.search_edit)
    EditText searchEdit;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Bind(R.id.rv_list_user)
    RecyclerView rvUserList;
    private BaseQuickAdapter mUserAdapter;
    @Bind(R.id.rv_list_news)
    RecyclerView rvNewsList;
    private BaseQuickAdapter mNewsAdapter;
    @Bind(R.id.rv_list_meeting)
    RecyclerView rvMeetingList;
    private BaseQuickAdapter mMeetingAdapter;
    @Bind(R.id.rv_list_live)
    RecyclerView rvLiveList;
    private BaseQuickAdapter mLiveAdapter;

    View mUserHeaderView, mNewsHeaderView, mMeetingHeaderView, mLiveHeaderView;
    TextView mUserTypeView, mNewsTypeView, mMeetingTypeView, mLiveTypeView;
    TextView mUserMoreView, mNewsMoreView, mMeetingMoreView, mLiveMoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        //为ViewPager设置高度
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
//            params.height = getActivity().getWindowManager().getDefaultDisplay().getHeight();//800

        viewPager.setLayoutParams(params);

        setUpViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED); //tabLayout
        tabLayout.setupWithViewPager(viewPager);


        initSearchResultView();
    }

    private void setUpViewPager(ViewPager viewPager) {
        //如果是在fragment中使用viewpager, 记得要用getChildFragmentManager, 否则你会发现fragment异常的生命周期.
        IndexChildAdapter mIndexChildAdapter = new IndexChildAdapter(SearchActicity.this.getSupportFragmentManager());//.getSupportFragmentManager());//.getChildFragmentManager()

        mIndexChildAdapter.addFragment(SearchHistoryFragment.newInstance("0"), "全部");
        mIndexChildAdapter.addFragment(SearchHistoryFragment.newInstance("1"), "直播");
        mIndexChildAdapter.addFragment(SearchHistoryFragment.newInstance("2"), "会议");
        mIndexChildAdapter.addFragment(SearchHistoryFragment.newInstance("3"), "新闻");

        viewPager.setOffscreenPageLimit(3);//缓存view 的个数
        viewPager.setAdapter(mIndexChildAdapter);
    }


    private void initSearchResultView() {
        /**
         *相关用户
         */
        rvUserList.setVisibility(View.GONE);
        rvUserList.setLayoutManager(new LinearLayoutManager(SearchActicity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvUserList.setHasFixedSize(true);
        mUserAdapter = new SearchUserRedAdapter(R.layout.item_user_red, null);
        rvUserList.setAdapter(mUserAdapter);

        //头view
        mUserHeaderView = LayoutInflater.from(SearchActicity.this).inflate(R.layout.item_header, null);
        mUserTypeView = (TextView) mUserHeaderView.findViewById(R.id.type);
        mUserMoreView = (TextView) mUserHeaderView.findViewById(R.id.more);


        /**
         *相关新闻
         */
        rvNewsList.setVisibility(View.GONE);
        rvNewsList.setLayoutManager(new LinearLayoutManager(SearchActicity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvNewsList.setHasFixedSize(true);
        mNewsAdapter = new SearchUserRedAdapter(R.layout.item_user_red, null);
        rvNewsList.setAdapter(mNewsAdapter);

        //头view
        mNewsHeaderView = LayoutInflater.from(SearchActicity.this).inflate(R.layout.item_header, null);
        mNewsTypeView = (TextView) mNewsHeaderView.findViewById(R.id.type);
        mNewsMoreView = (TextView) mNewsHeaderView.findViewById(R.id.more);


        /**
         *相关会议
         */
        rvMeetingList.setVisibility(View.GONE);
        rvMeetingList.setLayoutManager(new LinearLayoutManager(SearchActicity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvMeetingList.setHasFixedSize(true);
        mMeetingAdapter = new SearchUserRedAdapter(R.layout.item_user_red, null);
        rvMeetingList.setAdapter(mMeetingAdapter);

        //头view
        mMeetingHeaderView = LayoutInflater.from(SearchActicity.this).inflate(R.layout.item_header, null);
        mMeetingTypeView = (TextView) mMeetingHeaderView.findViewById(R.id.type);
        mMeetingMoreView = (TextView) mMeetingHeaderView.findViewById(R.id.more);


        /**
         *相关直播
         */
        rvLiveList.setVisibility(View.GONE);
        rvLiveList.setLayoutManager(new LinearLayoutManager(SearchActicity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvLiveList.setHasFixedSize(true);
        mLiveAdapter = new SearchUserRedAdapter(R.layout.item_user_red, null);
        rvLiveList.setAdapter(mLiveAdapter);

        //头view
        mLiveHeaderView = LayoutInflater.from(SearchActicity.this).inflate(R.layout.item_header, null);
        mLiveTypeView = (TextView) mLiveHeaderView.findViewById(R.id.type);
        mLiveMoreView = (TextView) mLiveHeaderView.findViewById(R.id.more);


    }

    @OnClick({R.id.back, R.id.search_edit, R.id.search_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.search_edit:
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);

                break;
            case R.id.search_tv:
                Data.getSearchHistory().add(searchEdit.getText().toString().trim());
                tabLayout.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                rvUserList.setVisibility(View.VISIBLE);


                if (!searchEdit.getText().toString().trim().equals("")) {
                    searchUser(searchEdit.getText().toString().trim());


                }
                break;
        }
    }

    private void searchUser(String word) {
        UserRedSearchEntity userRedSearchEntity = new UserRedSearchEntity();
        userRedSearchEntity.setPageNum(1);
        userRedSearchEntity.setPageSize(100);
        userRedSearchEntity.setUserName(word);
        HttpData.getInstance().HttpDataGetRedUser(new Observer<HttpResult3<UserRedEntity, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(SearchActicity.this, e.getMessage());
                Log.e(getLocalClassName(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<UserRedEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(SearchActicity.this, data.getMsg());
                    return;
                }
                Log.e(getLocalClassName(), data.getData().get(0).getName() + "");
                mUserAdapter.setNewData(data.getData());


                mUserTypeView.setText("相关用户");
                mUserAdapter.addHeaderView(mUserHeaderView);
            }
        }, userRedSearchEntity);
    }
}

