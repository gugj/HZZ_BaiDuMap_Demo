package com.roch.hzz_baidumap_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.PopupViewAdapter;
import com.roch.hzz_baidumap_demo.entity.Ad_Cd;
import com.roch.hzz_baidumap_demo.entity.WenTiLeiXin;
import com.roch.hzz_baidumap_demo.entity.WenTiLeiXin_Result;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.DensityUtil;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.URLs;
import com.roch.hzz_baidumap_demo.view.MaxListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新的河道资料高级查询页面
 * 作者：GuGaoJie
 * 时间：2017/7/20/020 11:07
 */
public class SearchActivity_HeDao_ZiLiao extends CommonBaseActivity implements View.OnFocusChangeListener, AdapterView.OnItemClickListener {

    @BindView(R.id.et_hedao_name)
    EditText etHedaoName;
    @BindView(R.id.et_hezhang_name)
    EditText etHezhangName;
    @BindView(R.id.et_hezhang_jibie)
    TextView etHezhangJibie;
    @BindView(R.id.et_xingzhengqu)
    TextView etXingzhengqu;

    @BindView(R.id.btn_quxiao)
    Button btnQuxiao;
    @BindView(R.id.btn_quding)
    Button btnQuding;

    List<View> views=new ArrayList<>();
    List<View> editTextViews=new ArrayList<>();
    @BindView(R.id.view0)
    View view0;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.view3)
    View view3;
    private int selectPostion=0;

    /**
     * 弹出窗口PopupWindow
     */
    private PopupWindow popupWindow;

    /**
     * 弹出窗口PopupWindow中填充的View
     */
    private View popupView;
    private String current_type_adcd;
    private MyBroadCastReceiver receiver;

    /**
     * 1.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_search_hedao_ziliao;
    }

    /**
     * 4.初始化控件的监听
     */
    @Override
    public void initListener() {
        etHedaoName.setOnFocusChangeListener(this);
        etHezhangName.setOnFocusChangeListener(this);
        etHezhangJibie.setOnFocusChangeListener(this);
        etXingzhengqu.setOnFocusChangeListener(this);
        // 注册广播----接收市县乡村筛选后的数据
        registerBroadCastReceiver();
    }

    /**
     * 注册广播----接收市县乡村筛选后的数据
     */
    private void registerBroadCastReceiver() {
        IntentFilter filter=new IntentFilter("shi_xian_xiang_cun_data");
        receiver = new MyBroadCastReceiver();
        registerReceiver(receiver,filter);
    }

    /**
     * 5.初始化数据
     */
    @Override
    public void initData() {
        super.initData();
        current_type_adcd = intent.getStringExtra("current_type_adcd");
        views.add(view0);
        views.add(view1);

        editTextViews.add(etHedaoName);
        editTextViews.add(etHezhangName);
    }

    @OnClick({R.id.et_hedao_name,R.id.et_hezhang_name,R.id.et_hezhang_jibie, R.id.et_xingzhengqu, R.id.btn_quxiao, R.id.btn_quding})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.et_hezhang_jibie: // 河长级别
                params.clear();
                params.put("typegroupcode", "hzjb");
                MyApplication.getInstance().getHttpUtilsInstance().post(SearchActivity_HeDao_ZiLiao.this, URLs.XinXi_ShangBao_Qtype, params, null, MyConstans.FIRST);
                System.out.println("河道资料高级查询页面请求河长级别的网址为：==" + URLs.XinXi_ShangBao_Qtype);
                break;

            case R.id.et_xingzhengqu: // 行政区
                Intent intent=new Intent(this,SelectXingZhengQu_Shi.class);
                intent.putExtra(Common.TITLE_KEY,"行政区");
                intent.putExtra("current_type_adcd",current_type_adcd);
                startActivity(intent);
                break;

            case R.id.btn_quxiao: // 取消
                finish();
                break;

            case R.id.btn_quding: // 确定
                // 河道名称
                String heDao_Name = etHedaoName.getText().toString().trim();
                // 河段名称
                String heZhang_Name = etHezhangName.getText().toString().trim();
                Intent intent0 = new Intent();
                intent0.putExtra("selectedXZQ_Ad_Cd", selectedXZQ_Ad_Cd);
                intent0.putExtra("selectedHeZhangJiBie", selectedHeZhangJiBie);
                intent0.putExtra("heDao_Name", heDao_Name);
                intent0.putExtra("heZhang_Name", heZhang_Name);
                setResult(2, intent0);
                finish();
                break;
        }
    }

    /**
     * 切换View的选中状态
     */
    private void swichViewState() {
        for (int i = 0; i < views.size(); i++) {
            if(selectPostion == i){
                views.get(i).setBackgroundResource(R.color.green);
            }else {
                views.get(i).setBackgroundResource(R.color.gray);
            }
        }
    }

    /**
     * 网络请求的参数---键值对
     */
    private Map<String,String> params=new HashMap<>();
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        for (int i = 0; i < editTextViews.size(); i++) {
            if(editTextViews.get(i)==v && hasFocus){
                selectPostion=i;
                // 切换View的选中状态
                swichViewState();
            }
        }
    }

    private List<WenTiLeiXin> wenTiLeiXins;
    private List<String> heZhangJiBies=new ArrayList<>();

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        switch (flag) {
            case MyConstans.FIRST:
                LogUtil.println("请求河长级别数据成功:===" + str);
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    WenTiLeiXin_Result wenTiLeiXin_result = WenTiLeiXin_Result.parseToT(str1, WenTiLeiXin_Result.class);
                    if (null != wenTiLeiXin_result && wenTiLeiXin_result.getSuccess()) {
                        wenTiLeiXins = wenTiLeiXin_result.getJsondata();
                        if (null != wenTiLeiXins) {
                            heZhangJiBies.clear();
                            // 初始化河长级别的数据
                            initHzjbData();
                            initPopup(heZhangJiBies);
                            showPopupWindow();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 初始化PopupWindow的数据
     */
    private void initPopup(List<String> selectors) {
        popupView = View.inflate(this, R.layout.view_popup, null);
        MaxListView lv_popup = (MaxListView) popupView.findViewById(R.id.lv_popup);
        lv_popup.setListViewHeight(DensityUtil.dip2px(this, 200));
        PopupViewAdapter popupViewAdapter = new PopupViewAdapter(this, selectors);
        lv_popup.setAdapter(popupViewAdapter);
        lv_popup.setOnItemClickListener(this);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 显示PopupWindow
     */
    private void showPopupWindow() {
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setAnimationStyle(R.style.PopupWindowTimerAnimation);
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 初始化河长级别的数据
     */
    private void initHzjbData() {
        for (int i = 0; i < wenTiLeiXins.size(); i++) {
            heZhangJiBies.add(wenTiLeiXins.get(i).getTypename());
        }
    }

    WenTiLeiXin selectedHeZhangJiBie;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        etHezhangJibie.setText(heZhangJiBies.get(position));
        selectedHeZhangJiBie=wenTiLeiXins.get(position);
        popupWindow.setFocusable(false);
        popupWindow.dismiss();
    }

    Ad_Cd selectedXZQ_Ad_Cd;

    /**
     * 接收市县乡村数据筛选后的广播
     */
    class MyBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            selectedXZQ_Ad_Cd= (Ad_Cd) intent.getSerializableExtra("select_ad_cd");
            String select_ad_cd_name = intent.getStringExtra("select_ad_cd_name");
            etXingzhengqu.setText(select_ad_cd_name);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) { // 隐藏软键盘，说明点击位置不在EditText上
                etHedaoName.clearFocus();
                etHezhangName.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }else { // 显示软键盘，说明点击位置在EditText上
                if(v.getId() == etHedaoName.getId()){
                    etHedaoName.requestFocus();
                }else if(v.getId() == etHezhangName.getId()){
                    etHezhangName.requestFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
}
