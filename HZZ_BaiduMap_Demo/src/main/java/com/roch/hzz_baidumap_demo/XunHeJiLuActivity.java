package com.roch.hzz_baidumap_demo;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.XunHeJiLuAdapter;
import com.roch.hzz_baidumap_demo.entity.WenTiLeiXin;
import com.roch.hzz_baidumap_demo.entity.XunHeJiLu;
import com.roch.hzz_baidumap_demo.entity.XunHeJiLu_Result;
import com.roch.hzz_baidumap_demo.utils.Common;
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
 * 巡河记录Activity
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 11:28
 */
public class XunHeJiLuActivity extends CommonBaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.lv_xunhe_jilu)
    PullToRefreshListView lvXunheJilu;

    /**
     * 标志位---0：下拉刷新    1：上拉加载
     */
    private static int FRIST = 0;
    private int current_page = 1; //当前页码
    private boolean flag_gaoji_chaxun=false;

    private XunHeJiLuAdapter xunHeJiLuAdapter;
    private List<XunHeJiLu> xunHeJiLus;
    private String user_ad_cd;

    /**
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_xunhe_jilu;
    }

    /**
     * 3.初始化Tool控件
     */
    @Override
    public void initToolbar() {
        super.initToolbar();
        tvSure.setVisibility(View.VISIBLE);
    }

    /**
     * 4.初始化数据
     */
    @Override
    public void initData() {
        user_ad_cd = SharePreferencesUtil.getLoginUserAdCd(this);
        // 获取请求服务时的基本查询条件：1.page;2.ad_cd
        getParams();
        MyApplication.getInstance().getHttpUtilsInstance().post(XunHeJiLuActivity.this, URLs.XunHe_JiLu_List, params, null, MyConstans.FIRST);
        System.out.println("巡河记录页面第一次进入页面时请求服务器中数据的网址为：==" + URLs.XunHe_JiLu_List + "&page=" + String.valueOf(current_page));
    }

    /**
     * 获取请求服务时的基本查询条件：1.page;2.ad_cd
     */
    private void getParams() {
        params.clear();
        params.put("page", String.valueOf(current_page));
        params.put("ad_cd", user_ad_cd);
    }

    /**
     * 网络请求的参数---键值对
     */
    private Map<String,String> params=new HashMap<>();

    /**
     * 3-0.初始化控件的监听
     */
    @Override
    public void initListener() {
        super.initListener();
        lvXunheJilu.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                FRIST = 0;
                current_page = 1;
                flag_gaoji_chaxun = false;

                // 获取请求服务时的基本查询条件：1.page;2.ad_cd
                getParams();
                MyApplication.getInstance().getHttpUtilsInstance().post(XunHeJiLuActivity.this, URLs.XunHe_JiLu_List, params, null, MyConstans.FIRST);
                LogUtil.println("巡河记录页面下拉刷新时请求服务器中数据的网址为：==" + URLs.XunHe_JiLu_List + "&page=" + String.valueOf(current_page));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                FRIST = 1;
                current_page++;

               // 获取请求服务时的基本查询条件：1.page;2.ad_cd
              getParams();
              if (flag_gaoji_chaxun) { //带高级查询条件
                  // 获取请求服务时的高级查询条件参数：1.ckTime_begin；2.ckTime_end；3.ctype
                  getGaoJiParams();
                  MyApplication.getInstance().getHttpUtilsInstance().post(XunHeJiLuActivity.this, URLs.XunHe_JiLu_List, params, null, MyConstans.FIRST);
                  LogUtil.println("巡河记录页面上拉加载时(带高级查询)请求服务器中数据(flag=1)的网址为：==" + URLs.XunHe_JiLu_List + "&page=" + String.valueOf(current_page));
              } else {
                  MyApplication.getInstance().getHttpUtilsInstance().post(XunHeJiLuActivity.this, URLs.XunHe_JiLu_List, params, null, MyConstans.FIRST);
                  LogUtil.println("巡河记录页面上拉加载时(不带高级查询)请求服务器中数据(flag=1)的网址为：==" + URLs.XunHe_JiLu_List + "&page=" + String.valueOf(current_page));
                }
            }
        });
        lvXunheJilu.setOnItemClickListener(this);
    }

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        switch (flag){
            case MyConstans.FIRST:
                lvXunheJilu.onRefreshComplete();
                LogUtil.println("请求服务器巡河记录List列表数据成功===" + str);

                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    XunHeJiLu_Result xunHeJiLu_result = XunHeJiLu_Result.parseToT(str1, XunHeJiLu_Result.class);
                    if (StringUtil.isNotEmpty(xunHeJiLu_result) && xunHeJiLu_result.getSuccess()) { //json--success:true
                        xunHeJiLus = xunHeJiLu_result.getJsondata();
                        if (StringUtil.isNotEmpty(xunHeJiLus)) {//返回的集合有数据  && xunHeJiLus.size() > 0
                            switch (FRIST){
                                case 0: //下拉刷新---置空
                                    if(xunHeJiLuAdapter==null){
                                        xunHeJiLuAdapter = new XunHeJiLuAdapter(XunHeJiLuActivity.this, xunHeJiLus);
                                        lvXunheJilu.setAdapter(xunHeJiLuAdapter);
                                    }else {
                                        xunHeJiLuAdapter.onRefsh(xunHeJiLus);
                                        if(xunHeJiLus.size()>0){
                                            lvXunheJilu.getRefreshableView().setSelection(0);
                                        }
                                    }
                                    break;

                                case 1: //上拉加载---追加
                                    if (xunHeJiLuAdapter == null) {
                                        xunHeJiLuAdapter = new XunHeJiLuAdapter(XunHeJiLuActivity.this, xunHeJiLus);
                                        lvXunheJilu.setAdapter(xunHeJiLuAdapter);
                                    }else {
                                        xunHeJiLuAdapter.addList(xunHeJiLus);
                                    }
                                    break;
                            }
                        }else { //返回的集合为空
                            ShowToast("没有更多数据了");
                        }
                    }else { //json---success:false
                        ShowToast("服务器网络异常");
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
        switch (flag){
            case MyConstans.FIRST:
                ShowToast("请求服务器失败");
                LogUtil.println("请求服务器巡河记录List列表数据失败===" + str);
                lvXunheJilu.onRefreshComplete();
                break;
        }
    }

    @OnClick(R.id.tv_sure)
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_sure:
                Intent intent=new Intent(this,SearchActivity_XunHe_JiLu.class);
                intent.putExtra(Common.TITLE_KEY,"高级查询");
                startActivityForResult(intent, 1);
                break;
        }
    }

    String startTime,endTime;
    WenTiLeiXin selectedWenTiLeiXing;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == 2){
                FRIST=0;
                current_page = 1;
                flag_gaoji_chaxun=true;

                selectedWenTiLeiXing= (WenTiLeiXin) data.getSerializableExtra("chuliFangshi");
                startTime=data.getStringExtra("startTime");
                endTime=data.getStringExtra("endTime");
                LogUtil.println("筛选过之后：开始时间=" + startTime + ",结束时间=" + endTime);

                // 获取请求服务时的基本查询条件：1.page;2.ad_cd
                getParams();
                // 获取请求服务时的高级查询条件参数：1.ckTime_begin；2.ckTime_end；3.ctype
                getGaoJiParams();
                MyApplication.getInstance().getHttpUtilsInstance().post(XunHeJiLuActivity.this, URLs.XunHe_JiLu_List, params, null, MyConstans.FIRST);
                LogUtil.println("巡河记录页面高级查询时(筛选条件过之后)请求服务器中数据(flag=0)的网址为：==" + URLs.XunHe_JiLu_List + "&page=" + String.valueOf(current_page));
            }
        }
    }

    /**
     * 获取请求服务时的高级查询条件参数：1.ckTime_begin；2.ckTime_end；3.ctype
     */
    private void getGaoJiParams() {
        if(StringUtil.isNotEmpty(startTime)){
            startTime=getTime(startTime);
            params.put("ckTime_begin",startTime);
        }
        if(StringUtil.isNotEmpty(endTime)){
            endTime=getTime(endTime);
            params.put("ckTime_end",endTime);
        }
        if(StringUtil.isNotEmpty(selectedWenTiLeiXing)){
            params.put("ctype",selectedWenTiLeiXing.getTypecode());
        }
    }

    /**
     * 获取完整的时间----传入时间不能为空
     */
    private String getTime(String time) {
        String[] split = time.split("-");
        if(split.length>=2){
            if(split[1].length()<=1){
                split[1]="0"+split[1];
            }
            if(split[2].length()<=1){
                split[2]="0"+split[2];
            }
            time=split[0]+"-"+split[1]+"-"+split[2];
        }
        return time;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        XunHeJiLu xunHeJiLu= (XunHeJiLu)parent.getItemAtPosition(position);
        if(null != xunHeJiLu){
            Intent intent=new Intent(this,XunHeGuiJiAcitivty.class);
            intent.putExtra("xunhejilu",xunHeJiLu);
            intent.putExtra(Common.TITLE_KEY,"巡河轨迹");
            startActivity(intent);
        }
    }
}
