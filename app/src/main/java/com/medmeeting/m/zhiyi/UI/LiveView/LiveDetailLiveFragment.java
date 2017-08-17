package com.medmeeting.m.zhiyi.UI.LiveView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.medmeeting.m.zhiyi.Constant.Constant;
import com.medmeeting.m.zhiyi.MVP.Presenter.LiveDetailListPresent;
import com.medmeeting.m.zhiyi.MVP.View.LiveDetailListView;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.LiveDetailAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.LiveDetailDto;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.snappydb.SnappydbException;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;
import com.xiaochao.lcrapiddeveloplibrary.container.DefaultHeader;
import com.xiaochao.lcrapiddeveloplibrary.viewtype.ProgressActivity;
import com.xiaochao.lcrapiddeveloplibrary.widget.SpringView;

import java.util.List;

public class LiveDetailLiveFragment extends Fragment implements BaseQuickAdapter.RequestLoadMoreListener,SpringView.OnFreshListener,LiveDetailListView {

    RecyclerView mRecyclerView;
    ProgressActivity progress;
    private Toolbar toolbar;
    private BaseQuickAdapter mQuickAdapter;
    private int PageIndex=1;
    private SpringView springView;
    private LiveDetailListPresent present;

    private static Integer roomId = 0;
    private String userId;

    public static LiveDetailLiveFragment newInstance(Integer roomId1) {
        LiveDetailLiveFragment fragment = new LiveDetailLiveFragment();
        Bundle args = new Bundle();
        args.putInt("roomId", roomId1);
        fragment.setArguments(args);

        roomId = roomId1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            roomId = getArguments().getInt("roomId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_detail_live, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
    }

    private void initView(View root) {
        try {
            userId = DBUtils.get(getActivity(), "userId");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_list);
        springView = (SpringView) root.findViewById(R.id.springview);
        //设置下拉刷新监听
        springView.setListener(this);
        //设置下拉刷新样式
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new DefaultHeader(getActivity()));

        progress = (ProgressActivity) root.findViewById(R.id.progress);
        //分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //设置RecyclerView的显示模式  当前List模式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //设置页面为加载中..
        progress.showLoading();
        //设置适配器
        mQuickAdapter = new LiveDetailAdapter(R.layout.item_live_detail, null, userId);
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6,true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);

        present = new LiveDetailListPresent(this);
        //请求网络数据
//        present.LoadData("1",PageIndex,false);
        present.LoadData(false, roomId);
    }

    private void initListener() {
        //设置自动加载监听
        mQuickAdapter.setOnLoadMoreListener(this);

        mQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "点击了"+position, Toast.LENGTH_SHORT).show();
            }
        });
        mQuickAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), "长按了"+position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    //自动加载
    @Override
    public void onLoadMoreRequested() {
        PageIndex++;
//        present.LoadData("1",PageIndex,true);
        present.LoadData(true, roomId);
    }
    //下拉刷新
    @Override
    public void onRefresh() {
        PageIndex=1;
//        present.LoadData("1",PageIndex,false);
        present.LoadData(false, roomId);
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
    public void newDatas(List<LiveDetailDto.EntityBean.ProgramListBean> newsList) {
        //进入显示的初始数据或者下拉刷新显示的数据
        mQuickAdapter.setNewData(newsList);//新增数据
        mQuickAdapter.openLoadMore(10,true);//设置是否可以下拉加载  以及加载条数
        springView.onFinishFreshAndLoad();//刷新完成
    }

    @Override
    public void addDatas(List<LiveDetailDto.EntityBean.ProgramListBean> addList) {
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
                present.LoadData(false, roomId);
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
        springView.onFinishFreshAndLoad();
        //设置无数据显示页面
        progress.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata),Constant.EMPTY_TITLE,Constant.EMPTY_CONTEXT);
    }

}
