package com.roch.hzz_baidumap_demo.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.roch.hzz_baidumap_demo.R;

public class XinXiShangBaoFragment extends BaseFragment {


    private GridView gv_home;
    private EditText et_shangbao_neirong;

    /**
     * 1.获取该Fragment的布局View
     */
    @Override
    protected View getContenView(Activity mActivity) {
        return View.inflate(mActivity, R.layout.fragment_xinxi_shangbao, null);
    }

    /**
     * 2.在contentView中查找View控件
     */
    protected void findView(View contentView) {
        gv_home = (GridView) contentView.findViewById(R.id.gv_home);
        et_shangbao_neirong = (EditText) contentView.findViewById(R.id.et_shangbao_neirong);
    }

    /**
     * 3.初始化数据
     */
    @Override
    protected void initData() {

    }

}
