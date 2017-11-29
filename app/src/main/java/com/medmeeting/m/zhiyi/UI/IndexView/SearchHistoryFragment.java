package com.medmeeting.m.zhiyi.UI.IndexView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.SearchHistoryAdapter;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> mHistoryData = new ArrayList<>();
    private static final String TYPE = "type";
    private String mType;//类型

    private View mHeaderView;
    private ImageView mHeaderDeleteView;

    public static SearchHistoryFragment newInstance(String type) {
        SearchHistoryFragment fragment = new SearchHistoryFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_history, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.history_list);
        mRecyclerView.stopNestedScroll();
        //设置RecyclerView的显示模式  当前List模式
        if (mType.equals("0")) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
        }
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL) );
        //如果Item高度固定  增加该属性能够提高效率
        mRecyclerView.setHasFixedSize(true);
        //历史数据
        mHistoryData.addAll(Data.getSearchHistory());
        //设置适配器
        if (mType.equals("0")) {
            mQuickAdapter = new SearchHistoryAdapter(R.layout.item_history, mHistoryData);
        }else {
            mQuickAdapter = new SearchHistoryAdapter(R.layout.item_history1, mHistoryData);
        }
        //设置加载动画
        mQuickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置是否自动加载以及加载个数
        mQuickAdapter.openLoadMore(6, true);
        //将适配器添加到RecyclerView
        mRecyclerView.setAdapter(mQuickAdapter);


        if(mType.equals("0")) {
            mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_search_history_header, null);
            mHeaderDeleteView = (ImageView) mHeaderView.findViewById(R.id.delete);
            mHeaderDeleteView.setOnClickListener(view1 -> new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("确定要删除历史搜索记录么？")
                    .setPositiveButton("确定", (dialoginterface, i) -> {
                        Data.clearSearchHistory();
                        ToastUtils.show(getActivity(), "删除成功");
                    })
                    .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show());
            mQuickAdapter.addHeaderView(mHeaderView);
        }
        Log.e("adadadad ", mType);
        Log.e("adadadad ", Data.getSearchHistory().size() + "");

        return view;
    }

}
