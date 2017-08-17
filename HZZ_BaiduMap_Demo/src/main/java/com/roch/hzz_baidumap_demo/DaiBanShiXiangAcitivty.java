package com.roch.hzz_baidumap_demo;

import android.os.Bundle;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.utils.StringUtil;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * 待办事项页面
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 11:42
 */
public class DaiBanShiXiangAcitivty extends CommonBaseActivity {

    @BindView(R.id.tv_daiban_neirong)
    TextView tvDaibanNeirong;

    /**
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_daiban_shixiang;
    }

    /**
     * 3.初始化Tool控件
     */
    @Override
    public void initToolbar() {
        super.initToolbar();
        if (StringUtil.isEmpty(title)) {
            tvTitle.setText("待办事项");
        }
    }

    /**
     * 5.初始化数据
     */
    @Override
    public void initData() {
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            if(StringUtil.isEmpty(title) || StringUtil.isEmpty(content)){
                tvDaibanNeirong.setText("待办事项");
            }else {
                tvDaibanNeirong.setText(title + "：" + content);
            }
        }
    }

}
