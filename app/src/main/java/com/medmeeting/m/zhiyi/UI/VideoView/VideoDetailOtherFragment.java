package com.medmeeting.m.zhiyi.UI.VideoView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.VideoAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListEntity;
import com.medmeeting.m.zhiyi.UI.Entity.VideoListSearchEntity2;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 24/10/2017 5:34 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoDetailOtherFragment  extends Fragment {

    private static Integer roomId;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    public static VideoDetailOtherFragment newInstance(Integer classifys1) {
        VideoDetailOtherFragment fragment = new VideoDetailOtherFragment();
        Bundle args = new Bundle();
        args.putInt("videoId", classifys1);
        fragment.setArguments(args);

        roomId = classifys1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            roomId = getArguments().getInt("videoId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_detail_other, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new VideoAdapter(R.layout.item_video_others, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        getOtherVideos(roomId);
    }

    private void getOtherVideos(Integer roomId) {
        VideoListSearchEntity2 searchEntity = new VideoListSearchEntity2();
        searchEntity.setRoomId(roomId);
        HttpData.getInstance().HttpDataGetVideos2(new Observer<HttpResult3<VideoListEntity, Object>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(HttpResult3<VideoListEntity, Object> videoListEntityObjectHttpResult3) {
                if (!videoListEntityObjectHttpResult3.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), videoListEntityObjectHttpResult3.getMsg());
                    return;
                }
                mAdapter.addData(videoListEntityObjectHttpResult3.getData());
                mAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent i = new Intent(getActivity(), VideoDetailActivity.class);
                    i.putExtra("videoId", videoListEntityObjectHttpResult3.getData().get(position).getVideoId());
                    i.putExtra("title", videoListEntityObjectHttpResult3.getData().get(position).getTitle());
                    i.putExtra("photo", videoListEntityObjectHttpResult3.getData().get(position).getCoverPhoto());
                    startActivity(i);
                    getActivity().finish();
                });
            }
        }, searchEntity);
    }

}
