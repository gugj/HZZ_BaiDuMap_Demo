package com.roch.hzz_baidumap_demo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.PopupViewAdapter;
import com.roch.hzz_baidumap_demo.entity.WenTiLeiXin;
import com.roch.hzz_baidumap_demo.entity.WenTiLeiXin_Result;
import com.roch.hzz_baidumap_demo.time.JudgeDate;
import com.roch.hzz_baidumap_demo.time.ScreenInfo;
import com.roch.hzz_baidumap_demo.time.WheelMain;
import com.roch.hzz_baidumap_demo.utils.DensityUtil;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.StringUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;
import com.roch.hzz_baidumap_demo.view.MaxListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 巡河记录高级查询页面---增加了很多查询条件
 */
public class SearchActivity_XunHe_JiLu extends CommonBaseActivity implements OnItemClickListener {

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_chuli_fangshi)
    TextView tvChuliFangshi;

    /**
     * 筛选条件的集合---处理方式
     */
    private List<String> selectors_chuli_fangshi = new ArrayList<String>();

    /**
     * 弹出窗口PopupWindow
     */
    private PopupWindow popupWindow;

    /**
     * 弹出窗口PopupWindow中填充的View
     */
    private View popupView;

    /**
     * 时间选择滚轮的布局View
     */
    private View timeView;
    private TextView timeCancle;
    private TextView timeSure;
    private WheelMain wheelMain; // TimePicker
    private ScreenInfo screenInfo;
    private String srBirthDay = "";
    private SimpleDateFormat dateFormat;
    private PopupWindow timePop;// 时间选择器popup

    /**
     * 网络请求的参数---键值对
     */
    private Map<String,String> params=new HashMap<>();
    private List<WenTiLeiXin> wenTiLeiXins;
    private WenTiLeiXin selectedWenTiLeiXin;

    /**
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        screenInfo = new ScreenInfo(this);
        //时间选择滚轮的布局View
        timeView = LayoutInflater.from(this).inflate(R.layout.item_odertime, null);
        timeCancle = (TextView) timeView.findViewById(R.id.time_cancle);
        timeSure = (TextView) timeView.findViewById(R.id.time_sure);
        timePop = new PopupWindow(timeView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return R.layout.activity_search_gaoji_xunhe_jilu_new;
    }

    @Override
    public void initData() {
        super.initData();
        params.clear();
        params.put("typegroupcode", "ctype");
        MyApplication.getInstance().getHttpUtilsInstance().post(SearchActivity_XunHe_JiLu.this, URLs.XinXi_ShangBao_Qtype, params, null, MyConstans.FIRST);
        LogUtil.println("巡河记录高级查询页面请求服务器中处理方式的网址为：==" + URLs.XinXi_ShangBao_Qtype);
    }

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        LogUtil.println("请求处理方式成功：==="+str);
        try {
            JSONArray array = new JSONArray(str);
            JSONObject jsonObject = array.getJSONObject(0);
            String str1 = jsonObject.toString();

            WenTiLeiXin_Result wenTiLeiXin_result=WenTiLeiXin_Result.parseToT(str1,WenTiLeiXin_Result.class);
            if(null != wenTiLeiXin_result && wenTiLeiXin_result.getSuccess()){
                wenTiLeiXins = wenTiLeiXin_result.getJsondata();
                //初始化处理方式数据---将筛选条件添加到集合中
                initDataChuLiFangShi();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFaileResult(String str, int flag) {
        super.onFaileResult(str, flag);
        ShowToast("请求处理方式失败");
        LogUtil.println("请求处理方式失败：===" + str);
    }

    /**
     * 初始化处理方式数据---将筛选条件添加到集合中
     */
    private void initDataChuLiFangShi() {
        if(StringUtil.isNotEmpty(wenTiLeiXins)){
            for (WenTiLeiXin wenTiLeXing: wenTiLeiXins) {
                selectors_chuli_fangshi.add(wenTiLeXing.getTypename());
            }
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

        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        popupWindow.setOnDismissListener(new poponDismissListener());
        // 初始化屏幕的背景透明度为 1
        backgroundAlpha(1f);
        // 设置透明度
        setAlpha();
    }

    /**
     * 标志位---记录当前点击的筛选条件是哪个
     */
    private int selectTeye = -1;

    @OnClick({R.id.tv_start_time, R.id.tv_end_time,R.id.tv_chuli_fangshi,
             R.id.btn_quding, R.id.btn_quxiao})
    public void onClick(View view) { //,R.id.tv_house_shilian
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_start_time: // 开始时间
                selectTeye = R.id.tv_start_time;
                initTimePicker(timeView);
                initTimePop(1);
                break;

            case R.id.tv_end_time: // 结束时间
                selectTeye = R.id.tv_end_time;
                initTimePicker(timeView);
                initTimePop(2);
                break;

            case R.id.tv_chuli_fangshi: // 处理方式
                selectTeye = R.id.tv_chuli_fangshi;
                initPopup(selectors_chuli_fangshi);
                showPopupWindow();
                break;

            case R.id.btn_quxiao: //取消筛选
                finish();
                break;

            case R.id.btn_quding: //确定
                String startTime = tvStartTime.getText().toString();
                String endTime = tvEndTime.getText().toString();

                if(StringUtil.isNotEmpty(startTime) && StringUtil.isNotEmpty(endTime)){
                    long startTimeLong=StringUtil.stringToLong(startTime,"yyyy-MM-dd");
                    long endTimeLong=StringUtil.stringToLong(endTime,"yyyy-MM-dd");
                    if(startTimeLong > endTimeLong){
                        ShowToast("开始时间不能大于结束时间");
                        return;
                    }
                }
                Intent intent = new Intent();
                if(StringUtil.isNotEmpty(selectedWenTiLeiXin)){
                    intent.putExtra("chuliFangshi", selectedWenTiLeiXin);
                }
                intent.putExtra("startTime", startTime);
                intent.putExtra("endTime", endTime);
                setResult(2, intent);
                finish();
                break;

            default:
                selectTeye = -1;
                break;
        }
    }

    /**
     * 初始化时间选择器
     * @param v
     */
    private void initTimePicker(View v) {
        wheelMain = new WheelMain(v, false, true, true);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        WheelMain.setSTART_YEAR(calendar.get(Calendar.YEAR) - 60);
        WheelMain.setEND_YEAR(calendar.get(Calendar.YEAR));
        if (JudgeDate.isDate(srBirthDay, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(srBirthDay));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        wheelMain.initDateTimePicker(year, month, day);
    }

    /**
     * 选择日期
     */
    private void initTimePop(final int i) {
        timePop.setFocusable(true);
        timePop.setOutsideTouchable(false);
        timePop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePop.setAnimationStyle(R.style.PopupWindowTimerAnimation);
        timePop.showAtLocation(timeView, Gravity.BOTTOM, 0, ViewGroup.LayoutParams.WRAP_CONTENT);
        timeCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != timePop && timePop.isShowing()) {
                    timePop.setFocusable(false);
                    timePop.dismiss();
                }
            }
        });
        timeSure.setOnClickListener(new View.OnClickListener() {// 要求到货时间
            @Override
            public void onClick(View v) {
//                String times = wheelMain.getYearAndQuarter();
                String yearAndMonthAndDay = wheelMain.getYearAndMonthAndDay();
                if (!StringUtil.isEmpty(yearAndMonthAndDay)) {
                    LogUtil.println("筛选过之后选择的时间为：" + yearAndMonthAndDay);
                    String month = yearAndMonthAndDay.split("-")[1];
                    int months = Integer.parseInt(month) + 1;
                    String day = yearAndMonthAndDay.split("-")[2];
                    int days = Integer.parseInt(day) + 1;
                    String time = yearAndMonthAndDay.split("-")[0] + "-" + months + "-" + days;
                    if (i == 1) {
                        tvStartTime.setText(time);
                    } else if (i == 2) {
                        tvEndTime.setText(time);
                    }
                    srBirthDay = wheelMain.getYear();
                    timePop.setFocusable(false);
                    timePop.dismiss();
                }
            }
        });

        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        timePop.setOnDismissListener(new poponDismissListener());
        // 初始化屏幕的背景透明度为 1
        backgroundAlpha(1f);
        // 设置透明度
        setAlpha();
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
     * 当PopupWindow中的ListView上的Item被点击时调用此方法
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (selectTeye) {
            case R.id.tv_chuli_fangshi:
//			showToast("处理方式");
                tvChuliFangshi.setText(selectors_chuli_fangshi.get(position));
                selectedWenTiLeiXin = wenTiLeiXins.get(position);
                break;
        }
        popupWindow.setFocusable(false);
        popupWindow.dismiss();
    }


    float alpha = 1f;

    /**
     * 设置透明度
     */
    private void setAlpha() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha > 0.5f) {
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha -= 0.01f;
                    msg.obj = alpha;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    /**
     * 返回或者点击空白位置的时候将背景透明度改回来
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while (alpha < 1f) {
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("HeadPortrait", "alpha:" + alpha);
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        alpha += 0.01f;
                        msg.obj = alpha;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    backgroundAlpha((float) msg.obj);
                    break;
            }
        }
    };

    /**
     * 设置屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
