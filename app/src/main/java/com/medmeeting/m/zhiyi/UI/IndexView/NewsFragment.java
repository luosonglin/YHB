package com.medmeeting.m.zhiyi.UI.IndexView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medmeeting.m.zhiyi.Base.BaseFragment;
import com.medmeeting.m.zhiyi.R;
import com.medmeeting.m.zhiyi.Util.ConstanceValue;

import butterknife.ButterKnife;

/**
 * @author NapoleonRohaha_Songlin
 * @date on 14/11/2017 2:43 PM
 * @describe TODO
 * @email iluosonglin@gmail.com
 * @org Healife
 */
public class NewsFragment extends BaseFragment {

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.bind(this, rootView);
        return v;
    }

    public static NewsFragment newInstance(String code) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConstanceValue.DATA, code);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void bindViews(View view) {

    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
