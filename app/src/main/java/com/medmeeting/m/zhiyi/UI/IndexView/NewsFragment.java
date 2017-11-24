package com.medmeeting.m.zhiyi.UI.IndexView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medmeeting.m.zhiyi.Base.BaseFragment;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.Util.ConstanceValue;

import butterknife.ButterKnife;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 14/11/2017 2:43 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class NewsFragment extends BaseFragment {

//    @Bind(R.id.recyclerView)
//    RecyclerView recyclerView;
//    @Bind(R.id.srl)
//    SwipeRefreshLayout srl;

//    private String mTitleCode = "";
//    protected List<Blog> mDatas = new ArrayList<>();
//    protected BaseQuickAdapter mAdapter;

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.bind(this, rootView);
        return v;
    }

    public static NewsFragment newInstance(String code) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConstanceValue.DATA, code);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void bindViews(View view) {

    }

    @Override
    protected void processLogic() {
//        initCommonRecyclerView(createAdapter(), null);
//        mTitleCode = getArguments().getString(ConstanceValue.DATA);
    }

//    public RecyclerView initCommonRecyclerView(BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        if (decoration != null) {
//            recyclerView.addItemDecoration(decoration);
//        }
//        recyclerView.setAdapter(adapter);
//        return recyclerView;
//    }
//
//    protected BaseQuickAdapter createAdapter() {
//        return mAdapter = new BlogAdapter(mDatas);
//    }

    @Override
    protected void setListener() {
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getData();
//            }
//        });
//        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int i) {
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
//            }
//        });
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
//        if (TextUtils.isEmpty(mTitleCode))
//            mTitleCode = getArguments().getString(ConstanceValue.DATA);
//        getData();
    }

    private void getData() {
//        if (mDatas.size() == 0) {
//
//            //没加载过数据
////            if (loadingView == null) loadingView = get(R.id.loadingView);
////            loadingView.setVisibility(View.VISIBLE);
////            loadingView.showLoading();
//        }
////        mvpPresenter.getNewsList(mTitleCode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
