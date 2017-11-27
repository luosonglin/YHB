package com.medmeeting.m.zhiyi.UI.IndexView;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.medmeeting.m.zhiyi.Base.BaseFragment;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.BlogAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.AdminEventActive;
import com.medmeeting.m.zhiyi.UI.Entity.Blog;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.Util.ConstanceValue;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 14/11/2017 2:43 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class NewsFragment extends BaseFragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout srl;

    private Integer mLabelId;
    protected List<Blog> mDatas = new ArrayList<>();
    protected BaseQuickAdapter mAdapter;

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.bind(this, rootView);
        return v;
    }

    public static NewsFragment newInstance(Integer labelId) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstanceValue.DATA, labelId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void bindViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
    }

    @Override
    protected void processLogic() {
        initCommonRecyclerView(createAdapter(), null);
        mLabelId = getArguments().getInt(ConstanceValue.DATA);
    }

    public RecyclerView initCommonRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    protected BaseQuickAdapter createAdapter() {
        return mAdapter = new BlogAdapter(mDatas);
    }

    @Override
    protected void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
//                News news = mDatas.get(i);
//                ///item_seo_url的值是item/6412427713050575361/  ,取出6412427713050575361
//                String itemId = news.item_seo_url.replace("item/", "").replace("/", "");
//                StringBuffer urlSb = new StringBuffer("http://m.toutiao.com/");
//                if (!itemId.startsWith("i"))
//                    urlSb.append("i");
//                urlSb.append(itemId).append("/info/");
//                String url = urlSb.toString();
//                if (news.article_genre.equals(ConstanceValue.ARTICLE_GENRE_VIDEO)) {
//                    //视频
//                    BaseNewsActivity.startVideo(mContext, url, news.group_id, itemId);
//                } else {
//                    BaseNewsActivity.startNews(mContext, url, news.group_id, itemId);
//                }
            }
        });
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
//        if (TextUtils.isEmpty(mTitleCode))
//            mTitleCode = getArguments().getString(ConstanceValue.DATA);
        getData();
    }

    private void getData() {
        if (mDatas.size() == 0) {

            //没加载过数据
//            if (loadingView == null) loadingView = get(R.id.loadingView);
//            loadingView.setVisibility(View.VISIBLE);
//            loadingView.showLoading();
        }
//        mvpPresenter.getNewsList(mTitleCode);
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", 100);
        map.put("labelId", mLabelId);
        HttpData.getInstance().HttpDataFindLabelBlogs(new Observer<HttpResult3<Blog, Object>>() {
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
                getHeaderView();
                mAdapter.setNewData(data.getData());
                Log.e(getActivity().getLocalClassName(), data.getData().get(0).getTitle());
            }
        }, map);
    }

    private View mHeaderView;
    private Banner mBanner;
    private List<String> bannerImages = new ArrayList<>();
    private List<String> bannerTitles = new ArrayList<>();

    private void getHeaderView() {
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_header, null);
        mBanner = (Banner) mHeaderView.findViewById(R.id.banner_news);
        HttpData.getInstance().HttpDataGetHomeBannerList(new Observer<HttpResult3<AdminEventActive, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity().getApplicationContext(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<AdminEventActive, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity().getApplicationContext(), data.getMsg());
                    return;
                }
                for (AdminEventActive i : data.getData()) {
                    bannerImages.add(i.getBanner());
                    bannerTitles.add(i.getTitle());
                }
                mBanner.setImages(bannerImages)
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                        .setBannerTitles(bannerTitles)
                        .setBannerAnimation(Transformer.BackgroundToForeground)
                        .setImageLoader(new GlideImageLoader())
                        .start();
                mBanner.setOnBannerClickListener(new OnBannerClickListener() {
                    @Override
                    public void OnBannerClick(int position) {
//                        Intent intent = new Intent(getActivity(), MeetingDetailActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("eventId", bannerDto.getBanners().get(position - 1).getId() + "");
//                        bundle.putString("eventTitle", bannerDto.getBanners().get(position - 1).getTitle());
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                    }
                });
                mAdapter.addHeaderView(mHeaderView);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
