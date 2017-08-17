package com.roch.hzz_baidumap_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.roch.hzz_baidumap_demo.adapter.GV_Home_Adapter;
import com.roch.hzz_baidumap_demo.entity.Photo;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.SharePreferencesUtil;
import com.roch.hzz_baidumap_demo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/21/021 15:00
 */
public class MainActivity_Home extends JPushMainActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.gv_home)
    GridView gv_home;
    private String[] tabName = new String[]{"开始巡河", "巡河记录", "河道资料",
                                            "信息核查","待办事项", "个人资料"}; //
    private int[] resIds={R.drawable.hezhang_xunhe,R.drawable.xunhe_jilu,R.drawable.hedao_guanli,
                          R.drawable.shangbaohecha,R.drawable.daiban_shixiang, R.drawable.geren_ziliao}; //

    private List<Photo> photos = new ArrayList<>();

    /**
     * 1.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_main_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity_login_out(this);

        //获取手机的屏幕密度DPI、屏幕的宽度和高度
        initDensityDpi();

        //4.初始化巡河首页的数据
        initPhotoData();
        //5.设置首页GridView的适配器
        setAdapter();
    }

    /**
     * 4.初始化巡河首页的数据
     */
    public void initPhotoData() {
        for (int i : resIds){
            Photo photo=new Photo("0",String.valueOf(i));
            photos.add(photo);
        }
    }

    /**
     * 5.设置首页GridView的适配器
     */
    private void setAdapter() {
        GV_Home_Adapter gv_home_adapter = new GV_Home_Adapter(this, photos);
        gv_home.setAdapter(gv_home_adapter);
        gv_home.setOnItemClickListener(this);
    }

    /**
     * 当首页GridView的适配器的条目被点击时调用
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent();
        int flag=1;
        if ("开始巡河".equals(tabName[position])) {
            intent.setClass(this, KaiShiXunHeActivity.class);
        }else if("待办事项".equals(tabName[position])){
            intent.setClass(this, DaiBanShiXiangAcitivty.class);
        }else if("信息核查".equals(tabName[position])){
            intent.setClass(this, XinXiHeChaAcitivty.class);
        }else if("巡河记录".equals(tabName[position])){
            intent.setClass(this, XunHeJiLuActivity.class);
        }else if("河道资料".equals(tabName[position])){
            intent.setClass(this, HeDaoZiLiaoActivity.class);
        }else if("个人资料".equals(tabName[position])){
            intent.setClass(this, SettingActivity.class);
        }else {
            flag=0;
        }
        if(0==flag){
            return;
        }
        //打开Activity时将title标题传过去
        intent.putExtra(Common.TITLE_KEY, tabName[position]);
        startActivity(intent);
    }

    private int keyCodeCount = 0; // 按下返回键的次数
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                switch (keyCodeCount++) {
                    case 0:
                        ToastUtils.showNormalToast(this,"再按一次，退出程序");
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                keyCodeCount = 0;
                            }
                        }, 3000);
                        break;

                    case 1:
                        MyApplication.getInstance().finishActivity_login_out(this);
                        break;
                }
            }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 巡河状态----改为否，结束巡河
        SharePreferencesUtil.saveIsStartXunHe(this, false);
        // 巡视河道ID----改为空，没有选择巡视河道
        SharePreferencesUtil.saveXunShiHeDaoId(this, "");
        // 保存巡河轨迹点----改为否，不保存
        SharePreferencesUtil.saveStartSavePointsAndGuiJi(this,false);
    }
}
