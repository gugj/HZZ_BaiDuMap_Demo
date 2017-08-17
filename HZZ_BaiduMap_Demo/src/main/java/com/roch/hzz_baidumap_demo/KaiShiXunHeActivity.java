package com.roch.hzz_baidumap_demo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.HeDaoXuanZeAdapter;
import com.roch.hzz_baidumap_demo.dialog.NormalDailog;
import com.roch.hzz_baidumap_demo.entity.HeDao;
import com.roch.hzz_baidumap_demo.entity.HeDao_Result;
import com.roch.hzz_baidumap_demo.entity.LoginUser;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.DensityUtil;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.SharePreferencesUtil;
import com.roch.hzz_baidumap_demo.utils.StringUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开始巡河页面
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 10:24
 */
public class KaiShiXunHeActivity extends CommonBaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tv_kaishi_xunhe)
    TextView tvKaishiXunhe;
    @BindView(R.id.tv_jieshu_xunhe)
    TextView tv_jieshu_xunhe;
    @BindView(R.id.tv_xinxi_shangbao)
    TextView tvXinxiShangbao;

    Fragment[] fragments;
    FragmentManager manager;
    FragmentTransaction transaction;
    @BindView(R.id.tv_filter)
    TextView tvFilter;
    private LoginUser loginUser;
    private NetworkConnectChangedReceiver networkConnectChangedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //4.初级化Fragment数组，并控制显示第一个fragment
        initFragment();
    }

    /**
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_kaishi_xunhe;
    }

    /**
     * 3.初始化Tool控件
     */
    @Override
    public void initToolbar() {
        super.initToolbar();
        tvTitle.setText("巡河首页");
        tvFilter.setVisibility(View.GONE);
        tvFilter.setText("模拟巡河");

        // 监测网络是否可用，并注册广播
        if(!ischeackNet(this)){
            showNetworkDialog();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        registerReceiver(networkConnectChangedReceiver, filter);
    }

    /**
     * 4.初级化Fragment数组，并控制显示第一个fragment
     */
    private void initFragment() {
        fragments = new Fragment[1];
        manager = getSupportFragmentManager();
        fragments[0] = manager.findFragmentById(R.id.fragment_home);
        transaction = manager.beginTransaction()
                .hide(fragments[0]);
        transaction.show(fragments[0]).commit();
    }

    /**
     * 网络请求的参数---键值对
     */
    private Map<String, String> params = new HashMap<>();

    /**
     * 4-1.初始化数据
     */
    @Override
    public void initData() {
        super.initData();
        loginUser = SharePreferencesUtil.getLoginUser(this, Common.Login_User_Name);

        ButtonListener listener=new ButtonListener();
        tvXinxiShangbao.setOnTouchListener(listener);
        tv_jieshu_xunhe.setOnTouchListener(listener);
    }

    /**
     * 请求河长开始巡河时所管辖的河道
     */
    private void requestHeDaos() {
        if (null != loginUser) {
            params.put("division", loginUser.getId());
        }
        LogUtil.println("根据河长ID获取其管辖河道的网址：===" + URLs.HeZhang_GuanXia_HeDao_List);
        MyApplication.getInstance().getHttpUtilsInstance().post(KaiShiXunHeActivity.this,
                URLs.HeZhang_GuanXia_HeDao_List,
                params,
                null,
                MyConstans.FIRST);
    }

    /**
     * 河长对应的河道列表
     */
    List<HeDao> heDaos;
    HeDaoXuanZeAdapter heDaoXuanZeAdapter;

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        switch (flag) {
            case MyConstans.FIRST:
//                ShowToast("请求服务器成功");
                LogUtil.println("请求服务器成功===" + str);
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    HeDao_Result heDao_result = HeDao_Result.parseToT(str1, HeDao_Result.class);
                    if (null != heDao_result && heDao_result.getSuccess()) {
                        heDaos = heDao_result.getJsondata();
                        if (null != heDaos) {
                            heDaoXuanZeAdapter = new HeDaoXuanZeAdapter(KaiShiXunHeActivity.this, heDaos, 1);
                            // 弹出选择巡视河道的PopupWindow
                            showNomalPopup();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFaileResult(String str, int flag) {
        super.onFaileResult(str, flag);
        switch (flag) {
            case MyConstans.FIRST:
                ShowToast("请求服务器失败");
                LogUtil.println("请求服务器失败===" + str);
                break;
        }
    }

    @OnClick({R.id.tv_back, R.id.tv_kaishi_xunhe, R.id.tv_jieshu_xunhe, R.id.tv_xinxi_shangbao,R.id.tv_filter})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_filter: // 点击模拟巡河
                // 开始巡河
                startXunHe();
                break;

            case R.id.tv_kaishi_xunhe: // 点击开始巡河
                // 开始巡河
                startXunHe();
                break;

            case R.id.tv_jieshu_xunhe: // 点击结束巡河
                if (SharePreferencesUtil.getIsStartXunHe(this)) { // 正在巡河-->结束巡河
                    Intent intent1 = new Intent();
                    intent1.setAction(Common.Start_End_XunHe_BroadCast);
                    intent1.putExtra(Common.INTENT_KEY, false);
                    sendBroadcast(intent1);
                } else {
                    ShowToast("请先点击开始巡河");
                }
                break;

            case R.id.tv_xinxi_shangbao: // 信息上报
                if (StringUtil.isEmpty(SharePreferencesUtil.getXunShiHeDaoId(this))) { // 如果没有选择巡视的河道
                    ShowToast("请先点击开始巡河");
                } else {  // 已经选择过巡视的河道
                    Intent intent2 = new Intent();
                    intent2.setClass(this, XinXiShangBaoActivity.class);
                    intent2.putExtra("xunshihedao_id", SharePreferencesUtil.getXunShiHeDaoId(this));
                    intent2.putExtra(Common.TITLE_KEY, "信息上报");
                    startActivity(intent2);
                }
                break;
        }
    }

    /**
     * 开始巡河
     */
    private void startXunHe() {
        // 先判断是否正在巡河
        if (SharePreferencesUtil.getIsStartXunHe(this)) {  // 已经开始巡河
            ShowToast("正在巡河中......");
        } else { // 没有开始巡河
            // 弹出选择巡视河道的PopupWindow
            if (StringUtil.isEmpty(SharePreferencesUtil.getXunShiHeDaoId(this))) {
                if(ischeackNet(this)){
                    if(heDaoXuanZeAdapter == null){
                        // 请求河长开始巡河时所管辖的河道
                        requestHeDaos();
                    }else {
                        // 弹出选择巡视河道的PopupWindow
                        showNomalPopup();
                    }
                }else {
                    // 弹出对话框提示用户设置网络连接
                    showNetworkDialog();
                }
            } else {
                // 发送开始巡河的广播
                sendKaiShiXunHeBroadCast();
            }
        }
    }

    NormalDailog normalDailog;

    /**
     * 弹出选择巡视河道的PopupWindow
     */
    private void showNomalPopup() {
        if(null == normalDailog){
            normalDailog = new NormalDailog(this, R.style.popup_dialog_style, 4);
            normalDailog.show();
            normalDailog.setListViewHeight(DensityUtil.dip2px(this, 100));
            normalDailog.setTitleText("请选择巡视的河道");
            if (null != heDaoXuanZeAdapter)
                normalDailog.setListViewAdapter(heDaoXuanZeAdapter);
            normalDailog.getPopupListView().setOnItemClickListener(this);
//        normalDailog.setCancelable(false);
        }else {
            if(!normalDailog.isShowing()){
                normalDailog.show();
            }
        }
    }

    /**
     * 点击河道选择列表中的条目时调用此方法
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        normalDailog.dismiss();
//        tvKaishiXunhe.setBackgroundResource(R.drawable.bg_selelct);
        Drawable topDrawable = getResources().getDrawable(R.drawable.start_pressed);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        tvKaishiXunhe.setCompoundDrawables(null, topDrawable, null, null);
        tvKaishiXunhe.setTextColor(getResources().getColor(R.color.xunhe_bg));

        // 保存开始巡河状态
        SharePreferencesUtil.saveIsStartXunHe(this, true);
        // 保存选择巡河河道的ID
        SharePreferencesUtil.saveXunShiHeDaoId(this, heDaos.get(position).getId());
        // 发送开始巡河的广播
        sendKaiShiXunHeBroadCast();
        LogUtil.println("选择的巡视河道的ID:===" + heDaos.get(position).getId());
    }

    public String getUserId() {
        return loginUser.getId();
    }

    public View getKaiShiXunHeView(){
        return tvKaishiXunhe;
    }

    /**
     * 发送开始巡河的广播
     */
    private void sendKaiShiXunHeBroadCast() {
        Intent intent0 = new Intent();
        intent0.setAction(Common.Start_End_XunHe_BroadCast);
        intent0.putExtra(Common.INTENT_KEY, true);
        sendBroadcast(intent0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharePreferencesUtil.getIsStartXunHe(this)) { // 正在巡河
//            tvKaishiXunhe.setBackgroundResource(R.drawable.bg_selelct);
            Drawable topDrawable = getResources().getDrawable(R.drawable.start_pressed);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            tvKaishiXunhe.setCompoundDrawables(null, topDrawable, null, null);
            tvKaishiXunhe.setTextColor(getResources().getColor(R.color.xunhe_bg));
        }else { // 当前没有巡河
//            tvKaishiXunhe.setBackgroundResource(R.drawable.bg_select_no);
            Drawable topDrawable = getResources().getDrawable(R.drawable.start_normal);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            tvKaishiXunhe.setCompoundDrawables(null, topDrawable, null, null);
            tvKaishiXunhe.setTextColor(getResources().getColor(R.color.black));
        }
    }

    NormalDailog netWorkDialog;
    /**
     * 弹出对话框提示用户设置网络连接
     */
    public void showNetworkDialog() {
        if(null == netWorkDialog){
            netWorkDialog=new NormalDailog(this, R.style.popup_dialog_style,5);
            netWorkDialog.show();
            netWorkDialog.setTitleText("网络设置");
            netWorkDialog.setContentText("未连接网络 请检查WiFi或数据是否开启");
            netWorkDialog.setOnClickLinener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.normal_dialog_cancel) {
                        // 关闭对话框
                        dismissNetWorkDialog();
                    } else if (v.getId() == R.id.normal_dialog_done) {
                        // 关闭对话框
                        dismissNetWorkDialog();
                        // 打开设置网络界面
                        openNetSettingActivity();
                    }
                }
            });
        }else {
            if(!netWorkDialog.isShowing()){
                netWorkDialog.show();
            }
        }
    }

    /**
     * 打开设置网络界面
     */
    private void openNetSettingActivity() {
        Intent intent=null;
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        if(android.os.Build.VERSION.SDK_INT>10){
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        }else{
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        startActivityForResult(intent, 0);
    }

    /**
     * 关闭对话框
     */
    public void dismissNetWorkDialog(){
        if(netWorkDialog !=null && netWorkDialog.isShowing()){
            netWorkDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(!ischeackNet(this)){
                showNetworkDialog();
            }
        }
    }

    /**
     * 接收网络或WiFi打开或关闭的广播
     */
    public class NetworkConnectChangedReceiver extends BroadcastReceiver {

        private String getConnectionType(int type) {
            String connType = "";
            if (type == ConnectivityManager.TYPE_MOBILE) {
                connType = "3G网络数据";
            } else if (type == ConnectivityManager.TYPE_WIFI) {
                connType = "WIFI网络";
            }
            return connType;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                //获取联网状态的NetworkInfo对象
                NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    //如果当前的网络连接成功并且网络连接可用
                    if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                            LogUtil.println(getConnectionType(info.getType()) + "连上");
                            // 关闭对话框
                            dismissNetWorkDialog();
                        }
                    } else {
                        LogUtil.println(getConnectionType(info.getType()) + "断开");
                        // 显示对话框
                        showNetworkDialog();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkConnectChangedReceiver);
    }

    class ButtonListener implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            if(v.getId() == R.id.tv_xinxi_shangbao){
                if(event.getAction() == MotionEvent.ACTION_UP){
                    tvXinxiShangbao.setTextColor(getResources().getColor(R.color.black));
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    tvXinxiShangbao.setTextColor(getResources().getColor(R.color.xunhe_bg));
                }
            }else if(v.getId() == R.id.tv_jieshu_xunhe){
                if(event.getAction() == MotionEvent.ACTION_UP){
                    tv_jieshu_xunhe.setTextColor(getResources().getColor(R.color.black));
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    tv_jieshu_xunhe.setTextColor(getResources().getColor(R.color.xunhe_bg));
                }
            }
            return false;
        }
    }

}
