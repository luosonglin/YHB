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
import android.widget.Button;
import android.widget.EditText;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.VideoCommandAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.AddVideoCommentEntity;
import com.medmeeting.m.zhiyi.UI.Entity.BasePageSearchEntity;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.VideoComment;
import com.medmeeting.m.zhiyi.UI.Entity.VideoCommentUserEntity;
import com.medmeeting.m.zhiyi.UI.IdentityView.ActivateActivity;
import com.medmeeting.m.zhiyi.Util.DBUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.snappydb.SnappydbException;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 24/10/2017 5:33 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoDetailCommandFragment extends Fragment {

    private static Integer videoId;
    @BindView(R.id.input_editor)
    EditText inputEditor;
    @BindView(R.id.input_send)
    Button inputSend;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    Unbinder unbinder;

    public static VideoDetailCommandFragment newInstance(Integer classifys1) {
        VideoDetailCommandFragment fragment = new VideoDetailCommandFragment();
        Bundle args = new Bundle();
        args.putInt("videoId", classifys1);
        fragment.setArguments(args);

        videoId = classifys1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            videoId = getArguments().getInt("videoId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_detail_command, container, false);
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new VideoCommandAdapter(R.layout.item_video_command, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.openLoadMore(8, true);
        mRecyclerView.setAdapter(mAdapter);
        getVideoComments();
    }

    private void getVideoComments() {
        BasePageSearchEntity searchentity = new BasePageSearchEntity();
        searchentity.setPageNum(0);
        searchentity.setPageSize(100);
        HttpData.getInstance().HttpDataGetVideoComments(new Observer<HttpResult3<VideoCommentUserEntity, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<VideoCommentUserEntity, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }
                if (mAdapter.getData() != null) {
                    mAdapter.setNewData(data.getData());
                } else {
                    mAdapter.addData(data.getData());
                }
            }
        }, videoId, searchentity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private String tocPortStatus;

    @OnClick(R.id.input_send)
    public void onClick() {
        try {
            tocPortStatus = DBUtils.get(getActivity(), "tocPortStatus");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        if (tocPortStatus == null || tocPortStatus.equals("wait_activation")) {
            startActivity(new Intent(getActivity(), ActivateActivity.class));
            return;
        }

        if (inputEditor.getText().toString().trim().equals("")) {
            ToastUtils.show(getActivity(), "不能发空评论");
            return;
        }
        AddVideoCommentEntity content = new AddVideoCommentEntity(inputEditor.getText().toString().trim());
        HttpData.getInstance().HttpDataAddComment(new Observer<HttpResult3<Object, VideoComment>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, VideoComment> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }
                getVideoComments();
                inputEditor.setText("");
            }
        }, videoId, content);

    }
}
