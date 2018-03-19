package com.medmeeting.m.zhiyi.UI.VideoView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.medmeeting.m.zhiyi.UI.Entity.UserCollect;
import com.medmeeting.m.zhiyi.UI.Entity.VideoDetailsEntity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.GlideCircleTransform;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.medmeeting.m.zhiyi.Widget.likeview.RxShineButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 24/10/2017 5:34 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class VideoDetailInfomationFragment extends Fragment {

    private static Integer videoId;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.author_name)
    TextView authorName;
    @BindView(R.id.like)
    RxShineButton like;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.sum)
    TextView sum;
    @BindView(R.id.des)
    TextView des;

    Unbinder unbinder;

    public static VideoDetailInfomationFragment newInstance(Integer classifys1) {
        VideoDetailInfomationFragment fragment = new VideoDetailInfomationFragment();
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
        View view = inflater.inflate(R.layout.fragment_video_detail_information, container, false);
        ButterKnife.bind(this, view);
        unbinder = ButterKnife.bind(this, view);


        like.init(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        HttpData.getInstance().HttpDataGetVideoDetail(new Observer<HttpResult3<Object, VideoDetailsEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getActivity(), e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Object, VideoDetailsEntity> data) {
                Glide.with(getActivity())
                        .load(data.getEntity().getUserPic())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .crossFade()
                        .placeholder(R.mipmap.avator_default)
                        .transform(new GlideCircleTransform(getActivity()))
                        .into(avatar);
                avatar.setOnClickListener(view -> {
                    Intent intent = new Intent(getActivity(), LiveRedVipActivity.class);
                    intent.putExtra("userId", data.getEntity().getUserId());
                    startActivity(intent);
                });
                title.setText(data.getEntity().getTitle());
                if (data.getEntity().getAuthorTitle().equals("") || data.getEntity().getAuthorTitle() == null) {
                    authorName.setText(data.getEntity().getAuthorName());
                } else {
                    authorName.setText(data.getEntity().getAuthorName() + " | " + data.getEntity().getAuthorTitle());
                }
                time.setText("时间：   " + DateUtils.formatDate(data.getEntity().getCreateTime(), DateUtils.TYPE_06));
                if (data.getEntity().getChargeType().equals("no")) {
                    type.setText("观看：   公开免费");
                } else {
                    type.setText("观看：    ¥ " + data.getEntity().getPrice());
                }
                sum.setText("观看 " + data.getEntity().getPlayCount() + "      收藏 " + data.getEntity().getCollectCount());
                des.setText(data.getEntity().getDes());

                if (data.getEntity().isCollectFlag()) {    //已收藏
                    like.setBtnColor(Color.YELLOW);//初始颜色
//                    like.setBtnFillColor(Color.GRAY);//点击后颜色
                } else {
                    like.setBtnColor(Color.GRAY);//初始颜色
//                    like.setBtnFillColor(Color.YELLOW);//点击后颜色
                }
                like.setOnClickListener(view -> {
                    like.setChecked(false);//不能再点
                    collectService(data.getEntity().isCollectFlag());
                });
            }
        }, videoId);
    }

    private void collectService(boolean oldCollected) {
        UserCollect userCollect = new UserCollect();
        userCollect.setServiceId(videoId);
        userCollect.setServiceType("VIDEO");
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
                    ToastUtils.show(getActivity(), "成功收藏");
                    like.setBtnFillColor(Color.YELLOW);
                    like.showAnim();
                }
                like.setChecked(true);
                initView();
            }
        }, userCollect);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                break;
        }
    }
}
