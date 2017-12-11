package com.medmeeting.m.zhiyi.UI.IndexView;

import android.content.Intent;
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
import com.medmeeting.m.zhiyi.UI.Adapter.BlogAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.EventAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.IndexChildAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.MyOrderAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.MyPayLiveAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.SearchUserRedAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.Blog;
import com.medmeeting.m.zhiyi.UI.Entity.Event;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDto;
import com.medmeeting.m.zhiyi.UI.Entity.LiveSearchDto;
import com.medmeeting.m.zhiyi.UI.Entity.UserRedEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserRedSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListSearchEntity;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailActivity2;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingDetailActivity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.HashMap;
import java.util.Map;

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
    private com.chad.library.adapter.base.BaseQuickAdapter mNewsAdapter;
    @Bind(R.id.rv_list_meeting)
    RecyclerView rvMeetingList;
    private BaseQuickAdapter mMeetingAdapter;
    @Bind(R.id.rv_list_live)
    RecyclerView rvLiveList;
    private BaseQuickAdapter mLiveAdapter;
    @Bind(R.id.rv_list_video)
    RecyclerView rvVideoList;
    private BaseQuickAdapter mVideoAdapter;

    View mUserHeaderView, mNewsHeaderView, mMeetingHeaderView, mLiveHeaderView, mVideoHeaderView;
    TextView mUserTypeView, mNewsTypeView, mMeetingTypeView, mLiveTypeView, mVideoTypeView;
    TextView mUserMoreView, mNewsMoreView, mMeetingMoreView, mLiveMoreView, mVideoMoreView;

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
//        rvNewsList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvNewsList.setHasFixedSize(true);
        mNewsAdapter = new BlogAdapter(null);
        //设置加载动画
        mNewsAdapter.openLoadAnimation(com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mNewsAdapter.openLoadMore(6, true);
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
//        rvMeetingList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvMeetingList.setHasFixedSize(true);
        mMeetingAdapter = new EventAdapter(R.layout.item_meeting, null);
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
//        rvLiveList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvLiveList.setHasFixedSize(true);
        mLiveAdapter = new MyPayLiveAdapter(R.layout.item_video_others, null);
        rvLiveList.setAdapter(mLiveAdapter);

        //头view
        mLiveHeaderView = LayoutInflater.from(SearchActicity.this).inflate(R.layout.item_header, null);
        mLiveTypeView = (TextView) mLiveHeaderView.findViewById(R.id.type);
        mLiveMoreView = (TextView) mLiveHeaderView.findViewById(R.id.more);


        /**
         *相关视频
         */
        rvVideoList.setVisibility(View.GONE);
        rvVideoList.setLayoutManager(new LinearLayoutManager(SearchActicity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
//        rvVideoList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvVideoList.setHasFixedSize(true);
        mVideoAdapter = new MyOrderAdapter(R.layout.item_video_others, null);
        rvVideoList.setAdapter(mVideoAdapter);

        //头view
        mVideoHeaderView = LayoutInflater.from(SearchActicity.this).inflate(R.layout.item_header, null);
        mVideoTypeView = (TextView) mVideoHeaderView.findViewById(R.id.type);
        mVideoMoreView = (TextView) mVideoHeaderView.findViewById(R.id.more);
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

                if (!searchEdit.getText().toString().trim().equals("")) {
                    searchUser(searchEdit.getText().toString().trim());

                    searchNews(searchEdit.getText().toString().trim());

                    searchMeeting(searchEdit.getText().toString().trim());

                    searchLive(searchEdit.getText().toString().trim());

                    searchVideo(searchEdit.getText().toString().trim());
                }
                break;
        }
    }

    private void searchMeeting(String word) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", 100);
        map.put("pojo", word);
        HttpData.getInstance().HttpDataSearchMeeting(new Observer<HttpResult3<Event, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(SearchActicity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Event, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(SearchActicity.this, data.getMsg());
                    return;
                }
                if (data.getData().size() == 0) {
                    rvMeetingList.setVisibility(View.GONE);
                    return;
                }
                rvMeetingList.setVisibility(View.VISIBLE);

                mMeetingAdapter.setNewData(data.getData());

                mMeetingTypeView.setText("相关会议");
                mMeetingAdapter.addHeaderView(mMeetingHeaderView);

                mMeetingAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent intent = new Intent(SearchActicity.this, MeetingDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("eventId", data.getData().get(position).getId());
                    bundle.putString("eventTitle", data.getData().get(position).getTitle());
                    bundle.putString("phone", "http://www.medmeeting.com/upload/banner/" + data.getData().get(position).getBanner());
                    bundle.putString("description", "时间： " + DateUtils.formatDate(data.getData().get(position).getStartDate(), DateUtils.TYPE_02)
                            + " ~ " + DateUtils.formatDate(data.getData().get(position).getEndDate(), DateUtils.TYPE_02)
                            + " \n "
                            + "地点： " + data.getData().get(position).getAddress());
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }
        }, map);
    }

    private void searchNews(String word) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", 100);
        map.put("pojo", word);
        HttpData.getInstance().HttpDataSearchBlog(new Observer<HttpResult3<Blog, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(SearchActicity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Blog, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(SearchActicity.this, data.getMsg());
                    return;
                }
                if (data.getData().size() == 0) {
                    rvNewsList.setVisibility(View.GONE);
                    return;
                }
                rvNewsList.setVisibility(View.VISIBLE);

                mNewsAdapter.setNewData(data.getData());

                mNewsTypeView.setText("相关新闻");
                mNewsAdapter.addHeaderView(mNewsHeaderView);

                mNewsAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent intent = null;
                    switch (data.getData().get(position).getBlogType()) {
                        case "1":
                            intent = new Intent(SearchActicity.this, NewsActivity.class);
                            intent.putExtra("blogId", data.getData().get(position).getId());
                            break;
                        case "2":
                            intent = new Intent(SearchActicity.this, NewsActivity.class);
                            intent.putExtra("blogId", data.getData().get(position).getId());
                            break;
                        case "3":
                            intent = new Intent(SearchActicity.this, VideoDetailActivity.class);
                            intent.putExtra("videoId", data.getData().get(position).getId());
                            break;
                    }
                    startActivity(intent);
                });
            }
        }, map);
    }

    private void searchVideo(String word) {
        VideoListSearchEntity searchEntity = new VideoListSearchEntity();
        searchEntity.setPageNum(1);
        searchEntity.setPageSize(100);
        searchEntity.setKeyword(word);
        searchEntity.setLabelId(null);
        searchEntity.setRoomId(null);
        searchEntity.setProgramId(null);
        searchEntity.setRoomNumber(null);
        searchEntity.setVideoUserId(null);
        HttpData.getInstance().HttpDataGetVideos(new Observer<HttpResult3<VideoListEntity, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(SearchActicity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<VideoListEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(SearchActicity.this, data.getMsg());
                    return;
                }
                if (data.getData().size() == 0) {
                    rvVideoList.setVisibility(View.GONE);
                    return;
                }
                rvVideoList.setVisibility(View.VISIBLE);

                mVideoAdapter.setNewData(data.getData());

                mVideoTypeView.setText("相关视频");
                mVideoAdapter.addHeaderView(mVideoHeaderView);

                mVideoAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(SearchActicity.this, VideoDetailActivity.class);
                    i.putExtra("videoId", data.getData().get(position).getVideoId());
                    startActivity(i);
                });
            }
        }, searchEntity);
    }

    private void searchLive(String word) {
        LiveSearchDto liveSearchDto = new LiveSearchDto();
        liveSearchDto.setKeyword(word);
        liveSearchDto.setRoomNumber(null);
        liveSearchDto.setLabelIds(null);
        HttpData.getInstance().HttpDataGetPrograms(new Observer<HttpResult3<LiveDto, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(SearchActicity.this, e.getMessage());
                Log.e(getLocalClassName(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<LiveDto, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(SearchActicity.this, data.getMsg());
                    return;
                }

                if (data.getData().size() == 0) {
                    rvLiveList.setVisibility(View.GONE);
                    return;
                }
                rvLiveList.setVisibility(View.VISIBLE);

                mLiveAdapter.setNewData(data.getData());

                mLiveTypeView.setText("相关直播");
                mLiveAdapter.addHeaderView(mLiveHeaderView);

                mLiveAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(SearchActicity.this, LiveProgramDetailActivity2.class);
                    i.putExtra("programId", data.getData().get(position).getId());
                    startActivity(i);
                });
            }
        }, liveSearchDto);
    }

    private void searchUser(String word) {
        UserRedSearchEntity userRedSearchEntity = new UserRedSearchEntity();
        userRedSearchEntity.setPageNum(1);
        userRedSearchEntity.setPageSize(100);
        userRedSearchEntity.setUserName(word);
        HttpData.getInstance().HttpDataSearchRedUser(new Observer<HttpResult3<UserRedEntity, Object>>() {
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

                if (data.getData().size() == 0) {
                    rvUserList.setVisibility(View.GONE);
                    return;
                }
                rvUserList.setVisibility(View.VISIBLE);

                mUserAdapter.setNewData(data.getData());

                mUserTypeView.setText("相关用户");
                mUserAdapter.addHeaderView(mUserHeaderView);
            }
        }, userRedSearchEntity);
    }
}

