package com.roch.hzz_baidumap_demo.fragment;

import android.app.Activity;
import android.view.View;

import com.roch.hzz_baidumap_demo.R;

public class DaiBanShiXiangFragment extends BaseFragment {


    @Override
    protected View getContenView(Activity mActivity) {
        return View.inflate(mActivity, R.layout.fragment_daiban_shixiang,null);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
