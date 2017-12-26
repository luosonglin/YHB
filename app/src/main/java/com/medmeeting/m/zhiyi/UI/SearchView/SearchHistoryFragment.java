package com.medmeeting.m.zhiyi.UI.SearchView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.library.flowlayout.FlowLayoutManager;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.BlogAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.EventAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.MyOrderAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.MyPayLiveAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.SearchHistoryAdapter;
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
import com.medmeeting.m.zhiyi.UI.IndexView.NewsActivity;
import com.medmeeting.m.zhiyi.UI.LiveView.LiveProgramDetailActivity2;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingDetailActivity;
import com.medmeeting.m.zhiyi.UI.VideoView.LiveRedVipActivity;
import com.medmeeting.m.zhiyi.UI.VideoView.VideoDetailActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.SharedPreferencesMgr;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;

import static com.medmeeting.m.zhiyi.Util.ConstanceValue.HISTORY_WORD;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 29/11/2017 3:22 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class SearchHistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mQuickAdapter;

    private static final String TYPE = "type";
    private static final String WORD = "word";
    private String mType;//类型
    private static String mWord;//搜索词条


    private View mHeaderView;
    private ImageView mHeaderDeleteView;


    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    //搜索得出的数据Adapter
    @Bind(R.id.header_view_llyt)
    LinearLayout mUserHeaderView;
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

    View mNewsHeaderView, mMeetingHeaderView, mLiveHeaderView, mVideoHeaderView;
    TextView mNewsTypeView, mMeetingTypeView, mLiveTypeView, mVideoTypeView;
    TextView mNewsMoreView, mMeetingMoreView, mLiveMoreView, mVideoMoreView;


    public static SearchHistoryFragment newInstance(String type, String word) {
        SearchHistoryFragment fragment = new SearchHistoryFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putString(WORD, word);

        Log.e(type + "", word + "");
        mWord = word;

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
            mWord = getArguments().getString(WORD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_history, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.history_list);
        initSearchResultView();

        mRecyclerView.stopNestedScroll();
        //设置RecyclerView的显示模式  当前List模式
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        mRecyclerView.setLayoutManager(flowLayoutManager);

        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);

        //历史数据
        Set<String> hashSet = SharedPreferencesMgr.getList(HISTORY_WORD, new HashSet<>());
        List<String> hashSetList = new ArrayList<>(hashSet);
        //设置适配器
        mQuickAdapter = new SearchHistoryAdapter(R.layout.item_history, hashSetList);

        mRecyclerView.setAdapter(mQuickAdapter);
        mQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //点击历史搜索词条，进行搜索
                SearchActicity.setEdit(mQuickAdapter.getData().get(position) + "");
            }
        });

        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_search_history_header, null);
        mHeaderDeleteView = (ImageView) mHeaderView.findViewById(R.id.delete);
        mHeaderDeleteView.setOnClickListener(view1 -> new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("确定要删除历史搜索记录么？")
                .setPositiveButton("确定", (dialoginterface, i) -> {

                    //历史数据
                    hashSet.clear();
                    SharedPreferencesMgr.setList(HISTORY_WORD, hashSet);

                    hashSetList.clear();
                    mQuickAdapter.setNewData(hashSetList);
                    ToastUtils.show(getActivity(), "删除成功");
                })
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                .show());
        mQuickAdapter.addHeaderView(mHeaderView);

        if (TextUtils.isEmpty(mWord)) {
            mRecyclerView.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            if (!mWord.equals("")) {
                //开始搜索
                beginSearchService(mType, mWord);
            }
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void initSearchResultView() {
        /**
         *相关用户
         */
        rvUserList.setVisibility(View.GONE);
        rvUserList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvUserList.setHasFixedSize(true);
        mUserAdapter = new SearchUserRedAdapter(R.layout.item_user_red, null);
        rvUserList.setAdapter(mUserAdapter);


        /**
         *相关新闻
         */
        rvNewsList.setVisibility(View.GONE);
        if (mType.equals("0")) {
            rvNewsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
        } else {    //非"全部"标签页，RecyclerView的LayoutManager设置为可滚动
            rvNewsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        rvNewsList.setHasFixedSize(true);
        mNewsAdapter = new BlogAdapter(null);
        //设置加载动画
        mNewsAdapter.openLoadAnimation(com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mNewsAdapter.openLoadMore(6, true);
        rvNewsList.setAdapter(mNewsAdapter);

        //头view
        mNewsHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_header, null);
        mNewsTypeView = (TextView) mNewsHeaderView.findViewById(R.id.type);
        mNewsMoreView = (TextView) mNewsHeaderView.findViewById(R.id.more);


        /**
         *相关会议
         */
        rvMeetingList.setVisibility(View.GONE);
        if (mType.equals("0")) {
            rvMeetingList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
        } else {
            rvMeetingList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        rvMeetingList.setHasFixedSize(true);
        mMeetingAdapter = new EventAdapter(R.layout.item_meeting, null);
        rvMeetingList.setAdapter(mMeetingAdapter);

        //头view
        mMeetingHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_header, null);
        mMeetingTypeView = (TextView) mMeetingHeaderView.findViewById(R.id.type);
        mMeetingMoreView = (TextView) mMeetingHeaderView.findViewById(R.id.more);


        /**
         *相关直播
         */
        rvLiveList.setVisibility(View.GONE);
        if (mType.equals("0")) {
            rvLiveList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
        } else {
            rvLiveList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        rvLiveList.setHasFixedSize(true);
        mLiveAdapter = new MyPayLiveAdapter(R.layout.item_video_others, null);
        rvLiveList.setAdapter(mLiveAdapter);

        //头view
        mLiveHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_header, null);
        mLiveTypeView = (TextView) mLiveHeaderView.findViewById(R.id.type);
        mLiveMoreView = (TextView) mLiveHeaderView.findViewById(R.id.more);


        /**
         *相关视频
         */
        rvVideoList.setVisibility(View.GONE);
        if (mType.equals("0")) {
            rvVideoList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
        } else {
            rvVideoList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        rvVideoList.setHasFixedSize(true);
        mVideoAdapter = new MyOrderAdapter(R.layout.item_video_others, null);
        rvVideoList.setAdapter(mVideoAdapter);

        //头view
        mVideoHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_header, null);
        mVideoTypeView = (TextView) mVideoHeaderView.findViewById(R.id.type);
        mVideoMoreView = (TextView) mVideoHeaderView.findViewById(R.id.more);
    }

    /**
     * 开始搜索
     *
     * @param type
     * @param word
     */
    private void beginSearchService(String type, String word) {
        switch (type) {
            case "0":
                searchUser(word);
                searchNews(word);
                searchMeeting(word);
                searchLive(word);
                searchVideo(word);
                break;
            case "1":
                mUserHeaderView.setVisibility(View.GONE);
                searchLive(word);
                break;
            case "2":
                mUserHeaderView.setVisibility(View.GONE);
                searchVideo(word);
                break;
            case "3":
                mUserHeaderView.setVisibility(View.GONE);
                searchMeeting(word);
                break;
            case "4":
                mUserHeaderView.setVisibility(View.GONE);
                searchNews(word);
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
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Event, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }
                if (data.getData().size() == 0) {
                    rvMeetingList.setVisibility(View.GONE);
                    return;
                }
                rvMeetingList.setVisibility(View.VISIBLE);

                if (mType.equals("0")) {
                    mMeetingTypeView.setText("相关会议");
                    mMeetingMoreView.setVisibility(View.VISIBLE);
                    mMeetingMoreView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SearchActicity.setViewPager(3);
                        }
                    });
                    mMeetingAdapter.addHeaderView(mMeetingHeaderView);
                }
                if (mType.equals("0") && data.getData().size()>2) {
                    mMeetingAdapter.add(0, data.getData().get(0));
                    mMeetingAdapter.add(1, data.getData().get(1));
                }else {
                    mMeetingAdapter.setNewData(data.getData());
                }
                mMeetingAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent intent = new Intent(getActivity(), MeetingDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("eventId", data.getData().get(position).getId());
                    bundle.putString("sourceType", data.getData().get(position).getSourceType());
                    bundle.putString("eventTitle", data.getData().get(position).getTitle());
                    bundle.putString("photo", data.getData().get(position).getBanner());
                    bundle.putString("description", "大会时间：" + DateUtils.formatDate(data.getData().get(position).getStartDate(), DateUtils.TYPE_02)
                            + " 至 " + DateUtils.formatDate(data.getData().get(position).getEndDate(), DateUtils.TYPE_02)
                            + " 欢迎参加： " + data.getData().get(position).getTitle());
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
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Blog, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }
                if (data.getData().size() == 0) {
                    rvNewsList.setVisibility(View.GONE);
                    return;
                }
                rvNewsList.setVisibility(View.VISIBLE);


                if (mType.equals("0")) {
                    mNewsTypeView.setText("相关新闻");
                    mNewsMoreView.setVisibility(View.VISIBLE);
                    mNewsMoreView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SearchActicity.setViewPager(4);
                        }
                    });
                    mNewsAdapter.addHeaderView(mNewsHeaderView);
                }
                if (mType.equals("0") && data.getData().size()>2) {
                    mNewsAdapter.add(0, data.getData().get(0));
                    mNewsAdapter.add(1, data.getData().get(1));
                }else {
                    mNewsAdapter.setNewData(data.getData());
                }

                mNewsAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent intent = null;
                    switch (data.getData().get(position).getBlogType()) {
                        case "1":
                            intent = new Intent(getActivity(), NewsActivity.class);
                            intent.putExtra("blogId", data.getData().get(position).getId());
                            break;
                        case "2":
                            intent = new Intent(getActivity(), NewsActivity.class);
                            intent.putExtra("blogId", data.getData().get(position).getId());
                            break;
                        case "3":
                            intent = new Intent(getActivity(), VideoDetailActivity.class);
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
        searchEntity.setPageSize(1000);
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
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<VideoListEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }
                if (data.getData().size() == 0) {
                    rvVideoList.setVisibility(View.GONE);
                    return;
                }
                rvVideoList.setVisibility(View.VISIBLE);

                if (mType.equals("0")) {
                    mVideoTypeView.setText("相关视频");
                    mVideoMoreView.setVisibility(View.VISIBLE);
                    mVideoMoreView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SearchActicity.setViewPager(2);
                        }
                    });
                    mVideoAdapter.addHeaderView(mVideoHeaderView);
                }
                if (mType.equals("0") && data.getData().size()>2) {
                    mVideoAdapter.add(0, data.getData().get(0));
                    mVideoAdapter.add(1, data.getData().get(1));
                }else {
                    mVideoAdapter.setNewData(data.getData());
                }

                mVideoAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(getActivity(), VideoDetailActivity.class);
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
                ToastUtils.show(getActivity(), e.getMessage());
                Log.e(getActivity().getLocalClassName(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<LiveDto, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }

                if (data.getData().size() == 0) {
                    rvLiveList.setVisibility(View.GONE);
                    return;
                }
                rvLiveList.setVisibility(View.VISIBLE);

                if (mType.equals("0")) {
                    mLiveTypeView.setText("相关直播");
                    mLiveMoreView.setVisibility(View.VISIBLE);
                    mLiveMoreView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SearchActicity.setViewPager(1);
                        }
                    });
                    mLiveAdapter.addHeaderView(mLiveHeaderView);
                }
                if (mType.equals("0") && data.getData().size()>2) {
                    mLiveAdapter.add(0, data.getData().get(0));
                    mLiveAdapter.add(1, data.getData().get(1));
                }else {
                    mLiveAdapter.setNewData(data.getData());
                }

                mLiveAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(getActivity(), LiveProgramDetailActivity2.class);
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
                ToastUtils.show(getActivity(), e.getMessage());
                Log.e(getActivity().getLocalClassName(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<UserRedEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }

                if (data.getData().size() == 0) {
                    rvUserList.setVisibility(View.GONE);
                    mUserHeaderView.setVisibility(View.GONE);
                    return;
                }
                rvUserList.setVisibility(View.VISIBLE);
                mUserHeaderView.setVisibility(View.VISIBLE);

                mUserAdapter.setNewData(data.getData());
                mUserAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), LiveRedVipActivity.class);
                        intent.putExtra("userId", data.getData().get(position).getUserId());
                        startActivity(intent);
                    }
                });
            }
        }, userRedSearchEntity);
    }
}
