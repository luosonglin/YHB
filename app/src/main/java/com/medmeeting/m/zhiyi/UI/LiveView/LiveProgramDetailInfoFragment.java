package com.medmeeting.m.zhiyi.UI.LiveView;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.Entity.LiveProgramDateilsEntity;
import com.medmeeting.m.zhiyi.UI.Entity.UserCollect;
import com.medmeeting.m.zhiyi.Util.DateUtil;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.likeview.RxShineButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;

public class LiveProgramDetailInfoFragment extends Fragment {


    @Bind(R.id.avatar)
    ImageView avatar;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.author_name)
    TextView authorName;
    @Bind(R.id.like)
    RxShineButton like;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.des)
    TextView des;
    private String TAG = LiveProgramDetailInfoFragment.class.getSimpleName();

    private static LiveProgramDateilsEntity mLiveProgramDateilsEntity;
    private static Integer mProgramId;

    public LiveProgramDetailInfoFragment() {
        // Required empty public constructor
    }

    public static LiveProgramDetailInfoFragment newInstance(LiveProgramDateilsEntity liveProgramDateilsEntity, Integer programId) {
        LiveProgramDetailInfoFragment fragment = new LiveProgramDetailInfoFragment();
        mLiveProgramDateilsEntity = liveProgramDateilsEntity;
        mProgramId = programId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_program_detail_info, container, false);
        ButterKnife.bind(this, view);

        like.init(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mLiveProgramDateilsEntity != null)
            initView(mLiveProgramDateilsEntity);
        else
            getLiveDetailService(mProgramId);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initView(LiveProgramDateilsEntity mLiveProgramDateilsEntity) {
        title.setText(mLiveProgramDateilsEntity.getTitle());
        authorName.setText(mLiveProgramDateilsEntity.getAuthorName());
        Glide.with(getActivity())
                .load(mLiveProgramDateilsEntity.getUserPic())// + "?imageMogr/v2/thumbnail/1400x700"
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(avatar);
        time.setText(DateUtil.formatDate(mLiveProgramDateilsEntity.getCreateTime(), DateUtil.TYPE_04));
        if (mLiveProgramDateilsEntity.getChargeType().equals("no")) {
            type.setText("观看：   公开免费");
        } else {
            type.setText("观看：    ¥ " + mLiveProgramDateilsEntity.getPrice());
        }
        des.setText(mLiveProgramDateilsEntity.getDes());

        if (mLiveProgramDateilsEntity.isCollectFlag()) {    //已收藏
            like.setBtnColor(Color.YELLOW);//初始颜色
        } else {
            like.setBtnColor(Color.GRAY);//初始颜色
        }
        like.setOnClickListener(view -> {
            like.setChecked(false);//不能再点
            collectService(mLiveProgramDateilsEntity.isCollectFlag());
        });
    }

    private void collectService(boolean oldCollected) {
        UserCollect userCollect = new UserCollect();
        userCollect.setServiceId(mLiveProgramDateilsEntity.getId());
        userCollect.setServiceType("PROG");
        HttpData.getInstance().HttpDataCollect(new Observer<HttpResult3>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult3 httpResult3) {
                if (!httpResult3.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), httpResult3.getMsg());
                    return;
                }
                if (oldCollected) {     //老状态是 已收藏
                    ToastUtils.show(getActivity(), "取消收藏");
                    like.setBtnFillColor(Color.GRAY);
                    like.setCancel();
                } else {
                    ToastUtils.show(getActivity(), "收藏成功");
                    like.setBtnFillColor(Color.YELLOW);
                    like.showAnim();
                }
                like.setChecked(true);
                getLiveDetailService(mLiveProgramDateilsEntity.getId());
            }
        }, userCollect);
    }

    public void getLiveDetailService(Integer programId) {
        HttpData.getInstance().HttpDataGetOpenProgramDetail(new Observer<HttpResult3<Object, LiveProgramDateilsEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, LiveProgramDateilsEntity> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(getActivity(), data.getMsg());
                    return;
                }

                title.setText(data.getEntity().getTitle());
                authorName.setText(data.getEntity().getAuthorName());
                Glide.with(getActivity())
                        .load(data.getEntity().getUserPic())// + "?imageMogr/v2/thumbnail/1400x700"
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .into(avatar);
                time.setText(DateUtil.formatDate(data.getEntity().getCreateTime(), DateUtil.TYPE_04));
                if (data.getEntity().getChargeType().equals("no")) {
                    type.setText("观看：   公开免费");
                } else {
                    type.setText("观看：    ¥ " + data.getEntity().getPrice());
                }
                des.setText(data.getEntity().getDes());

                if (data.getEntity().isCollectFlag()) {    //已收藏
                    like.setBtnColor(Color.YELLOW);//初始颜色
                } else {
                    like.setBtnColor(Color.GRAY);//初始颜色
                }
                like.setOnClickListener(view -> {
                    like.setChecked(false);//不能再点
                    collectService(data.getEntity().isCollectFlag());
                });
            }
        }, programId);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
