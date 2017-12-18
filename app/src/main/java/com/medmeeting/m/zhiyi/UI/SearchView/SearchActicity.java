package com.medmeeting.m.zhiyi.UI.SearchView;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.medmeeting.m.zhiyi.Constant.Data;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.IndexChildAdapter;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 29/11/2017 2:02 PM
 * @describe 搜索页
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class SearchActicity extends AppCompatActivity {

    private static EditText searchEdit;
    private static Button searchTv;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        searchEdit = (EditText) findViewById(R.id.search_edit);
        searchTv = (Button) findViewById(R.id.search_tv);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        //为ViewPager设置高度
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        viewPager.setLayoutParams(params);

        setUpViewPager(viewPager, "");
        tabLayout.setTabMode(TabLayout.MODE_FIXED); //tabLayout
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager, String word) {
        //如果是在fragment中使用viewpager, 记得要用getChildFragmentManager, 否则你会发现fragment异常的生命周期.
        IndexChildAdapter mIndexChildAdapter = new IndexChildAdapter(SearchActicity.this.getSupportFragmentManager());//.getSupportFragmentManager());//.getChildFragmentManager()

        mIndexChildAdapter.addFragment(SearchHistoryFragment.newInstance("0", word), "全部");
        mIndexChildAdapter.addFragment(SearchHistoryFragment.newInstance("1", word), "直播");
        mIndexChildAdapter.addFragment(SearchHistoryFragment.newInstance("2", word), "视频");
        mIndexChildAdapter.addFragment(SearchHistoryFragment.newInstance("3", word), "会议");
        mIndexChildAdapter.addFragment(SearchHistoryFragment.newInstance("4", word), "新闻");

        viewPager.setOffscreenPageLimit(3);//缓存view 的个数
        viewPager.setAdapter(mIndexChildAdapter);
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
                Data.getSearchHistory().add(searchEdit.getText().toString().trim());

                setUpViewPager(viewPager, searchEdit.getText().toString().trim() + "");

                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }

                break;
        }
    }

    public static void setEdit(String word) {
        searchEdit.setText(word);
    }
}

