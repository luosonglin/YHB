package com.medmeeting.m.zhiyi.UI.OtherVIew;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.MVP.Presenter.NewsListPresent;
import com.medmeeting.m.zhiyi.MVP.View.NewsListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.NewsAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.BannerDto;
import com.medmeeting.m.zhiyi.UI.Entity.BlogDto;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingDetailActivity;
import com.medmeeting.m.zhiyi.Widget.GlideImageLoader;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.RotationHeader;
import com.xiaochao.lcrapiddeveloplibrary.viewtype.ProgressActivity;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends Fragment
        implements BaseQuickAdapter.RequestLoadMoreListener,
            SpringView.OnFreshListener,
            NewsListView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.rv_list)
    RecyclerView rvList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = IndexFragment.class.getSimpleName();

    private Banner mBanner;
    private List<String> bannerImages = new ArrayList<>();
    private List<String> bannerTitles = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ProgressActivity progress;
    private BaseQuickAdapter mQuickAdapter;
    private int PageIndex = 1;
    private int PageSize = 10;
    private SpringView springView;
    private NewsListPresent present;

    private OnFragmentInteractionListener mListener;

    public IndexFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        ButterKnife.bind(this, view);

        mBanner = (Banner) view.findViewById(R.id.banner);
        HttpData.getInstance().HttpDataGetBanner(new Observer<BannerDto>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: "+e.getMessage()
                        +"\n"+e.getCause()
                        +"\n"+e.getLocalizedMessage()
                        +"\n"+e.getStackTrace());
            }

            @Override
            public void onNext(final BannerDto bannerDto) {
                for (BannerDto.BannersBean i : bannerDto.getBanners()) {
                    bannerImages.add("http://www.medmeeting.com/upload/banner/" + i.getBanner());
                    bannerTitles.add(i.getTitle());
                }
                mBanner.setImages(bannerImages)
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                        .setBannerTitles(bannerTitles)
                        .setBannerAnimation(Transformer.Tablet)
                        .setImageLoader(new GlideImageLoader())
                        .start();
                mBanner.setOnBannerClickListener(new OnBannerClickListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Log.e("--", "点击：" + position + "");

                        Intent intent = new Intent(getActivity(), MeetingDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId", bannerDto.getBanners().get(position-1).getId()+"");
                        bundle.putString("eventTitle", bannerDto.getBanners().get(position-1).getTitle());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        springView = (SpringView) view.findViewById(R.id.springview);
        //设置下拉刷新监听
        springView.setListener(this);
        //设置下拉刷新样式
        springView.setType(SpringView.Type.OVERLAP);
        springView.setHeader(new RotationHeader(getActivity()));
//        springView.setFooter(new DefaultFooter(this));
//        springView.setHeader(new RotationHeader(this));
//        springView.setFooter(new RotationFooter(this)); //mRecyclerView内部集成的自动加载  上啦加载用不上   在其他View使用

        progress = (ProgressActivity) view.findViewById(R.id.progress);
        //分割线
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //设置页面为加载中..
        progress.showLoading();
        //设置适配器
//        mQuickAdapter = new BannersAdapter(R.layout.list_view_item_layout, null);
        mQuickAdapter = new NewsAdapter(R.layout.item_news, null);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6,true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);
//        present = new BookListPresent(this);
        present = new NewsListPresent(this);
        //请求网络数据
//        present.LoadData("1",PageIndex,false);
        present.LoadData(false, PageIndex, PageSize);

        initListener();
        return view;
    }

    private void initListener() {
        mQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mQuickAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), "长按了" + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //自动加载
    @Override
    public void onLoadMoreRequested() {
        PageIndex++;
//        present.LoadData("1",PageIndex,true);
        present.LoadData(true, PageIndex, PageSize);
    }
    //下拉刷新
    @Override
    public void onRefresh() {
        PageIndex=1;
//        present.LoadData("1",PageIndex,false);
        present.LoadData(false, PageIndex, PageSize);
    }
    //上啦加载  mRecyclerView内部集成的自动加载  上啦加载用不上   在其他View使用
    @Override
    public void onLoadmore() {

    }
    /*
    * MVP模式的相关状态
    *
    * */
    @Override
    public void showProgress() {
        progress.showLoading();
    }

    @Override
    public void hideProgress() {
        progress.showContent();
    }

    @Override
    public void newDatas(List<BlogDto.BlogBean.ListBean> newsList) {
        //进入显示的初始数据或者下拉刷新显示的数据
        mQuickAdapter.setNewData(newsList);//新增数据
        mQuickAdapter.openLoadMore(10,true);//设置是否可以下拉加载  以及加载条数
        springView.onFinishFreshAndLoad();//刷新完成
    }

    @Override
    public void addDatas(List<BlogDto.BlogBean.ListBean> addList) {
        //新增自动加载的的数据
        mQuickAdapter.notifyDataChangedAfterLoadMore(addList, true);
    }

    @Override
    public void showLoadFailMsg() {
        //设置加载错误页显示
        progress.showError(getResources().getDrawable(R.mipmap.monkey_cry), Constant.ERROR_TITLE, Constant.ERROR_CONTEXT, Constant.ERROR_BUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageIndex=1;
//                present.LoadData("1",PageIndex,false);
                present.LoadData(false, PageIndex, PageSize);
            }
        });
    }

    @Override
    public void showLoadCompleteAllData() {
        //所有数据加载完成后显示
        mQuickAdapter.notifyDataChangedAfterLoadMore(false);
        View view = getActivity().getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
        mQuickAdapter.addFooterView(view);
    }

    @Override
    public void showNoData() {
        //设置无数据显示页面
        progress.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata), Constant.EMPTY_TITLE,Constant.EMPTY_CONTEXT);
    }

}
