package com.medmeeting.m.zhiyi.UI.SearchView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.medmeeting.m.zhiyi.Data.HttpData.HttpData;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.EventAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.Event;
import com.medmeeting.m.zhiyi.UI.Entity.HttpResult3;
import com.medmeeting.m.zhiyi.UI.MeetingView.MeetingDetailActivity;
import com.medmeeting.m.zhiyi.Util.DateUtils;
import com.medmeeting.m.zhiyi.Util.ToastUtils;
import com.xiaochao.lcrapiddeveloplibrary.BaseQuickAdapter;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 25/12/2017 10:54 AM
 * @describe 会议搜索页
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class SearchMeetingActivity extends AppCompatActivity {

    private EditText searchEdit;
    private Button searchTv;

    private RecyclerView rvMeetingList;
    private BaseQuickAdapter mMeetingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meeting);
        ButterKnife.bind(this);

        searchEdit = (EditText) findViewById(R.id.search_edit);
        searchTv = (Button) findViewById(R.id.search_tv);

        rvMeetingList = (RecyclerView) findViewById(R.id.rv_list_meeting);
        rvMeetingList.setVisibility(View.GONE);
        rvMeetingList.setLayoutManager(new LinearLayoutManager(SearchMeetingActivity.this, LinearLayoutManager.VERTICAL, false));
        rvMeetingList.setHasFixedSize(true);
        mMeetingAdapter = new EventAdapter(R.layout.item_meeting, null);
        rvMeetingList.setAdapter(mMeetingAdapter);

    }


    @OnClick({R.id.back, R.id.search_edit, R.id.search_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.search_edit:

                break;
            case R.id.search_tv:
                searchMeeting(searchEdit.getText().toString().trim());

                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }

                break;
        }
    }

    private void searchMeeting(String word) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageSize", 1000);
        map.put("pojo", word);
        HttpData.getInstance().HttpDataGetAllEventList(new Observer<HttpResult3<Event, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(SearchMeetingActivity.this, e.getMessage());
            }

            @Override
            public void onNext(HttpResult3<Event, Object> data) {
                if (!data.getStatus().equals("success")) {
                    ToastUtils.show(SearchMeetingActivity.this, data.getMsg());
                    return;
                }
                if (data.getData().size() == 0) {
                    rvMeetingList.setVisibility(View.GONE);
                    return;
                }
                rvMeetingList.setVisibility(View.VISIBLE);

                mMeetingAdapter.setNewData(data.getData());
                mMeetingAdapter.setOnRecyclerViewItemClickListener((view, position) -> {
                    Intent intent = new Intent(SearchMeetingActivity.this, MeetingDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("eventId", data.getData().get(position).getId());
                    bundle.putString("sourceType", data.getData().get(position).getSourceType());
                    bundle.putString("eventTitle", data.getData().get(position).getTitle());
                    bundle.putString("photo", data.getData().get(position).getBanner());
                    bundle.putString("description", "大会时间：" + DateUtils.formatDate(data.getData().get(position).getStartDate(), DateUtils.TYPE_02)
                            + " 至 " + DateUtils.formatDate(data.getData().get(position).getEndDate(), DateUtils.TYPE_02)
                            + " 欢迎参加： " + data.getData().get(position).getTitle());
                    intent.putExtras(bundle);
                    startActivity(intent);

                });
            }
        }, map);
    }
}

