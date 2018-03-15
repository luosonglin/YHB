package com.medmeeting.m.zhiyi.UI.MineView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.UI.Adapter.DepartmentLevelOneAdapter;
import com.medmeeting.m.zhiyi.UI.Adapter.DepartmentLevelTwoAdapter;
import com.medmeeting.m.zhiyi.UI.Entity.DepartmentLevelOne;
import com.medmeeting.m.zhiyi.UI.Entity.DepartmentLevelTwo;
import com.medmeeting.m.zhiyi.Util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseDepartmentActivity extends AppCompatActivity
        implements DepartmentLevelOneAdapter.OnRecyclerViewItemClickListener, DepartmentLevelTwoAdapter.OnRecyclerViewItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.level_one_recycler_view)
    RecyclerView levelOneRecyclerView;
    @BindView(R.id.level_one_swipe_refresh_lyt)
    SwipeRefreshLayout levelOneSwipeRefreshLyt;

    @BindView(R.id.level_two_recycler_view)
    RecyclerView levelTwoRecyclerView;
    @BindView(R.id.level_two_swipe_refresh_lyt)
    SwipeRefreshLayout levelTwoSwipeRefreshLyt;
    @BindView(R.id.content_choose_department)
    RelativeLayout contentChooseDepartment;

    private DepartmentLevelOneAdapter departmentLevelOneAdapter;
    private static List<DepartmentLevelOne> departmentLevelOnes = new ArrayList<>();

    private int page = 1;
    private boolean isLoadingData = false;
    private boolean isLoadComp = false;

    private DepartmentLevelTwoAdapter departmentLevelTwoAdapter;
    private static List<DepartmentLevelTwo> departmentLevelTwos = new ArrayList<>();

    //test data
    //科室的第二级数据
    private String[] data0 = {"外科", "普通外科", "胃肠外科", "甲状腺外科", "甲状腺乳腺外科", "肝脏外科", "胆胰外科", "血管外科",
            "肛肠外科", "骨科", "脊柱外科", "骨肿瘤科", "关节外科", "创伤骨科", "足踝外科", "运动医学科", "手外科", "神经外科",
            "心脏外科", "泌尿外科", "胸外科", "烧伤整形科科", "美容整形科科", "小儿外科"};
    private String[] data1 = {"内科", "呼吸内科", "消化内科", "神经内科", "老年医学科", "心脏内科", "感染科", "肾脏内科", "内分泌科", "风湿免疫科", "血液内科"};
    private String[] data2 = {"口腔科", "口腔内科", "口腔修复科", "口腔种植科", "牙周科", "牙体牙髓科", "口腔黏膜科", "口腔正畸科", "颌面外科", "预防口腔科", "儿童口腔科"};
    private String[] data3 = {"妇产科", "妇科", "产科", "计划生育科", "生殖内分泌科", "产前诊断科", "遗传咨询科"};
    private String[] data4 = {"儿科", "新生儿科", "小儿呼吸内科", "小儿肾内科", "小二消化科", "小儿血液科", "小儿心脏科", "小儿营养保健科", "小儿神经科", "小儿感染科", "小儿免疫科", "小儿皮肤科", "小儿内分泌科", "小儿精神科"};
    private String[] data5 = {"五官科", "耳鼻咽喉头颈外科", "眼科"};
    private String[] data6 = {"传染科", "肝病科", "结核科", "其他传染病科"};
    private String[] data7 = {"肿瘤科", "腹部肿瘤科", "头颈肿瘤科", "放疗化疗科", "胸部肿瘤科"};
    private String[] data8 = {"精神科"};
    private String[] data9 = {"麻醉科"};
    private String[] data10 = {"重症医学科"};
    private String[] data11 = {"皮肤性病科"};
    private String[] data12 = {"中医科"};
    private String[] data13 = {"中西医结合科", "中西医结合肿瘤内科"};
    private String[] data14 = {"急诊医学"};
    private String[] data15 = {"康复理疗科"};
    private String[] data16 = {"其他科室", "营养科", "影像科", "超声科", "核医学科", "检验科", "病理科", "疼痛科", "药剂科", "毒理科", "介入科", "职业病科", "全科医学", "高压氧料", "胃食管反流病科", "男科", "生殖中心", "体检中心"};

    //科室的第一级数据
//    private String[] data00 = {"\uE900" + " " + data0[0], "\uE900" + " " + data1[0], "\uE900" + " " + data2[0], "\uE900" + " " + data3[0], "\uE900" + " " + data4[0],
//            "\uE900" + " " + data5[0], "\uE900" + " " + data6[0], "\uE900" + " " + data7[0], "\uE900" + " " + data8[0], "\uE900" + " " + data9[0],
//            "\uE900" + " " + data10[0], "\uE900" + " " + data11[0], "\uE900" + " " + data12[0], "\uE900" + " " + data13[0], "\uE900" + " " + data14[0],
//            "\uE900" + " " + data15[0], "\uE900" + " " + data16[0]};

    private String[] data00 = {" " + data0[0],  " " + data1[0],  " " + data2[0], " " + data3[0],  " " + data4[0],
            " " + data5[0],  " " + data6[0],  " " + data7[0], " " + data8[0],  " " + data9[0],
             " " + data10[0],  " " + data11[0], " " + data12[0],  " " + data13[0],  " " + data14[0],
             " " + data15[0],  " " + data16[0]};

    private String[][] data01 = {
            {"外科", "普通外科", "胃肠外科", "甲状腺外科", "甲状腺乳腺外科", "肝脏外科", "胆胰外科", "血管外科",
                    "肛肠外科", "骨科", "脊柱外科", "骨肿瘤科", "关节外科", "创伤骨科", "足踝外科", "运动医学科", "手外科", "神经外科",
                    "心脏外科", "泌尿外科", "胸外科", "烧伤整形科科", "美容整形科科", "小儿外科"},
            {"内科", "呼吸内科", "消化内科", "神经内科", "老年医学科", "心脏内科", "感染科", "肾脏内科", "内分泌科", "风湿免疫科", "血液内科"},
            {"口腔科", "口腔内科", "口腔修复科", "口腔种植科", "牙周科", "牙体牙髓科", "口腔黏膜科", "口腔正畸科", "颌面外科", "预防口腔科", "儿童口腔科"},
            {"妇产科", "妇科", "产科", "计划生育科", "生殖内分泌科", "产前诊断科", "遗传咨询科"},
            {"儿科", "新生儿科", "小儿呼吸内科", "小儿肾内科", "小二消化科", "小儿血液科", "小儿心脏科", "小儿营养保健科", "小儿神经科", "小儿感染科", "小儿免疫科", "小儿皮肤科", "小儿内分泌科", "小儿精神科"},
            {"五官科", "耳鼻咽喉头颈外科", "眼科"},
            {"传染科", "肝病科", "结核科", "其他传染病科"},
            {"肿瘤科", "腹部肿瘤科", "头颈肿瘤科", "放疗化疗科", "胸部肿瘤科"},
            {"精神科"},
            {"麻醉科"},
            {"重症医学科"},
            {"皮肤性病科"},
            {"中医科"},
            {"中西医结合科", "中西医结合肿瘤内科"},
            {"急诊医学"},
            {"康复理疗科"},
            {"其他科室", "营养科", "影像科", "超声科", "核医学科", "检验科", "病理科", "疼痛科", "药剂科", "毒理科", "介入科", "职业病科", "全科医学", "高压氧料", "胃食管反流病科", "男科", "生殖中心", "体检中心"}
    };

    //用于填充二级菜单
    private String[] data = new String[24];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_department);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationOnClickListener(view -> finish());

        initView();
        loadData();

        initSecondView();
        loadSecondData(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
        departmentLevelOneAdapter = new DepartmentLevelOneAdapter(this);
        departmentLevelOneAdapter.setOnRecyclerViewItemClickListener(this);

        levelOneRecyclerView.setLayoutManager(new LinearLayoutManager(levelOneRecyclerView.getContext()));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        levelOneRecyclerView.setHasFixedSize(true);
        //设置adapter
        levelOneRecyclerView.setAdapter(departmentLevelOneAdapter);
        //设置Item增加、移除动画
        levelOneRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        //添加分割线
//        levelOneRecyclerView.addItemDecoration(new DividerItemDecoration(
//                this, DividerItemDecoration.HORIZONTAL));
        levelOneRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //向上滑动不做反应
                if (dy < 0) return;

                //元素总数
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                //在当前页面可见的页面
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                //屏幕上最后可见元素的位置
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();

                //最后一个可见的元素+当前可见元素数 >= 总数
                if (lastVisibleItemPosition + visibleItemCount >= totalItemCount && !isLoadingData) {
                    //假如数据已经加载完毕，并且最后一个元素也完全显示了，才会显示没有更多商品
                    if (isLoadComp) {
                        if (lastVisibleItemPosition + 1 == totalItemCount) {
                            ToastUtils.show(ChooseDepartmentActivity.this, "没有更多消息");
                        }
                    } else {
                        loadData();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        levelOneSwipeRefreshLyt.setColorSchemeResources(R.color.colorAccent);
        levelOneSwipeRefreshLyt.setOnRefreshListener(() -> {
            levelOneSwipeRefreshLyt.setRefreshing(false);
            loadData();
        });
    }

    private void loadData() {

        departmentLevelOnes.clear();
        for (String i : data00) {
            departmentLevelOnes.add(new DepartmentLevelOne(i));
        }

        levelOneSwipeRefreshLyt.setRefreshing(false);
        isLoadingData = false;

        boolean clear = true;
        departmentLevelOneAdapter.setupDepartmentLevelOne(departmentLevelOnes, clear);
    }

    private void initSecondView() {
        departmentLevelTwoAdapter = new DepartmentLevelTwoAdapter(this);
        departmentLevelTwoAdapter.setOnRecyclerViewItemClickListener(this);

        levelTwoRecyclerView.setLayoutManager(new LinearLayoutManager(levelTwoRecyclerView.getContext()));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        levelTwoRecyclerView.setHasFixedSize(true);
        levelTwoRecyclerView.setAdapter(departmentLevelTwoAdapter);
        levelTwoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        levelTwoSwipeRefreshLyt.setColorSchemeResources(R.color.colorAccent);
        levelTwoSwipeRefreshLyt.setOnRefreshListener(() -> levelTwoSwipeRefreshLyt.setRefreshing(false));
    }

    private void loadSecondData(String[] data) {
        if (data == null || data.length == 0)
            data = data0;

        Log.i("test22", data.length + data.toString());
        for (String i : data) {
            Log.i("test2", i);
        }

        departmentLevelTwos.clear();
        for (String i : data) {
            departmentLevelTwos.add(new DepartmentLevelTwo(i));
        }

        levelTwoSwipeRefreshLyt.setRefreshing(false);
        isLoadingData = false;

        boolean clear = true;
        departmentLevelTwoAdapter.setupDepartmentLevelTwo(departmentLevelTwos, clear);
    }

    int j = 0;  //j为一级菜单选定的第几项

    //第1级
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemClick(View view, DepartmentLevelOneAdapter.DepartmentLevelOneViewHolder viewHolder, DepartmentLevelOne departmentLevelOne, int position) {
        Log.i("viewHolder ", viewHolder.getPosition() + " " + viewHolder.getAdapterPosition() + " " + viewHolder.getLayoutPosition() + " " + position);
       /* if (viewHolder.getAdapterPosition() == position) {
            viewHolder.getName().setText(Html.fromHtml("<font color=\"#32A2F8\">" + departmentLevelOne.getName() + "</font>"));
            viewHolder.getLine().setBackground(this.getResources().getDrawable(R.color.colorAccent));
        } else {
            viewHolder.getName().setText(Html.fromHtml("<font color=\"#000000\">" + departmentLevelOne.getName() + "</font>"));
            viewHolder.getLine().setBackground(this.getResources().getDrawable(R.color.colorPrimaryDark));
        }*/
        loadSecondData(data01[position]);
    }

    //第2级
    @Override
    public void onItemClick(View view, DepartmentLevelTwoAdapter.DepartmentLevelTwoViewHolder viewHolder, DepartmentLevelTwo departmentLevelTwo, int position) {
        Intent intent = new Intent();
        intent.putExtra("department", departmentLevelTwo.getName());

        this.setResult(0, intent);
        finish();
    }
}

