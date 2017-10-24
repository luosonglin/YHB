package com.medmeeting.m.zhiyi.UI.VideoView;

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
import com.medmeeting.m.zhiyi.UI.Entity.VideoDetailsEntity;
import com.medmeeting.m.zhiyi.Util.DateUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @Bind(R.id.avatar)
    ImageView avatar;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.author_name)
    TextView authorName;
    @Bind(R.id.like)
    ImageView like;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.sum)
    TextView sum;
    @Bind(R.id.des)
    TextView des;

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

            }

            @Override
            public void onNext(HttpResult3<Object, VideoDetailsEntity> data) {
                Glide.with(getActivity())
                        .load(data.getEntity().getUserPic())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .into(avatar);
                title.setText(data.getEntity().getTitle());
                authorName.setText(data.getEntity().getAuthorName());
                time.setText("时间：   "+ DateUtil.formatDate(data.getEntity().getCreateTime(), DateUtil.TYPE_06));
                if (data.getEntity().getChargeType().equals("no")) {
                    type.setText("观看：公开免费");
                } else {
                    type.setText("观看： ¥ " + data.getEntity().getPrice());
                }
                sum.setText("观看 " +data.getEntity().getPlayCount() + "     收藏 " + data.getEntity().getCollectCount());
                des.setText(data.getEntity().getDes());
            }
        }, videoId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.avatar, R.id.like})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                break;
            case R.id.like:
                break;
        }
    }
}
