package com.roch.hzz_baidumap_demo;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.XinXiHeChaAdapter;
import com.roch.hzz_baidumap_demo.entity.LoginUser;
import com.roch.hzz_baidumap_demo.entity.XinXiHeCha;
import com.roch.hzz_baidumap_demo.entity.XinXiHeCha_Result;
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

/**
 * 信息核查Activity
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 11:42
 */
public class XinXiHeChaAcitivty extends CommonBaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_xunhe_jilu)
    PullToRefreshListView lvXunheJilu;

    /**
     * 网络请求的参数---键值对
     */
    private Map<String,String> params=new HashMap<>();
    private static int FRIST = 0;
    private int current_page = 1; //当前页码
    private boolean flag_gaoji_chaxun=false;

    private List<XinXiHeCha> xinXiHeChas;
    private XinXiHeChaAdapter xinXiHeChaAdapter;
    private LoginUser loginUser;

    /**
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_xinxi_hecha;
    }

    /**
     * 4.初始化数据
     */
    @Override
    public void initData() {
        loginUser = SharePreferencesUtil.getLoginUser(this, Common.Login_User_Name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取请求服务时的基本查询条件：1.page;2.uid
        getParams();
        if(null != loginUser){
            MyApplication.getInstance().getHttpUtilsInstance().post(XinXiHeChaAcitivty.this, URLs.Xinxi_HeCha_List, params, null, MyConstans.FIRST);
            LogUtil.println("信息核查页面onResume()时请求服务器中数据的网址为：==" + URLs.Xinxi_HeCha_List + "&page=" + String.valueOf(current_page));
        }
    }

    /**
     * 获取请求服务时的基本查询条件：1.page;2.uid
     */
    private void getParams() {
        params.clear();
        params.put("page", String.valueOf(current_page));
        if(null != loginUser)
            params.put("uid", loginUser.getId());
    }

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

                // 获取请求服务时的基本查询条件：1.page;2.uid
                getParams();
                MyApplication.getInstance().getHttpUtilsInstance().post(XinXiHeChaAcitivty.this, URLs.Xinxi_HeCha_List, params, null, MyConstans.FIRST);
                LogUtil.println("信息核查页面下拉刷新时请求服务器中数据的网址为：==" + URLs.Xinxi_HeCha_List + "&page=" + String.valueOf(current_page));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                FRIST = 1;
                current_page++;

                // 获取请求服务时的基本查询条件：1.page;2.uid
                getParams();
                if (flag_gaoji_chaxun) { //带查询条件
                    MyApplication.getInstance().getHttpUtilsInstance().post(XinXiHeChaAcitivty.this, URLs.Xinxi_HeCha_List, params, null, MyConstans.FIRST);
                    LogUtil.println("信息核查页面上拉加载时(带高级查询)请求服务器中数据(flag=1)的网址为：==" + URLs.Xinxi_HeCha_List + "&page=" + String.valueOf(current_page));
                } else {
                    MyApplication.getInstance().getHttpUtilsInstance().post(XinXiHeChaAcitivty.this, URLs.Xinxi_HeCha_List, params, null, MyConstans.FIRST);
                    LogUtil.println("信息核查页面上拉加载时(不带高级查询)请求服务器中数据(flag=1)的网址为：==" + URLs.Xinxi_HeCha_List + "&page=" + String.valueOf(current_page));
                }
            }
        });
        lvXunheJilu.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        XinXiHeCha xinXiHeCha = (XinXiHeCha) parent.getItemAtPosition(position);
        if(null != xinXiHeCha){
            if("0".equals(xinXiHeCha.getInfoStatus())){ // 未确认---进入信息上报页面
                Intent intent=new Intent(this,XinXiHeCha_DiTu_Activity.class);
                intent.putExtra("xinxihecha", xinXiHeCha);
                intent.putExtra(Common.TITLE_KEY, "信息上报---核查");
                startActivity(intent);
            }else if("1".equals(xinXiHeCha.getInfoStatus())){ // 已确认---直接查看详情
                Intent intent=new Intent(this,XinXiHeCha_Detail_Activity.class);
                intent.putExtra("xinxihecha", xinXiHeCha);
                intent.putExtra(Common.TITLE_KEY, "信息核查详情");
                startActivity(intent);
            }
        }
    }

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        switch (flag){
            case MyConstans.FIRST:
                lvXunheJilu.onRefreshComplete();
                LogUtil.println("请求服务器成功===" + str);

                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    XinXiHeCha_Result xinXiHeCha_result = XinXiHeCha_Result.parseToT(str1, XinXiHeCha_Result.class);
                    if (StringUtil.isNotEmpty(xinXiHeCha_result) && xinXiHeCha_result.getSuccess()) { //json--success:true
                        xinXiHeChas = xinXiHeCha_result.getJsondata();
                        if (StringUtil.isNotEmpty(xinXiHeChas)) {//返回的集合有数据  && xunHeJiLus.size() > 0
                            switch (FRIST){
                                case 0: //下拉刷新---置空
                                    if(xinXiHeChaAdapter==null){
                                        xinXiHeChaAdapter = new XinXiHeChaAdapter(XinXiHeChaAcitivty.this, xinXiHeChas);
                                        lvXunheJilu.setAdapter(xinXiHeChaAdapter);
                                    }else {
                                        xinXiHeChaAdapter.onRefsh(xinXiHeChas);
                                        if(xinXiHeChas.size()>0){
                                            lvXunheJilu.getRefreshableView().setSelection(0);
                                        }
                                    }
                                    if(xinXiHeChas.size()<=0){
                                        ShowToast("暂无数据");
                                    }
                                    break;

                                case 1: //上拉加载---追加
                                    if (xinXiHeChaAdapter == null) {
                                        xinXiHeChaAdapter = new XinXiHeChaAdapter(XinXiHeChaAcitivty.this, xinXiHeChas);
                                        lvXunheJilu.setAdapter(xinXiHeChaAdapter);
                                    }else {
                                        xinXiHeChaAdapter.addList(xinXiHeChas);
                                    }
                                    if(xinXiHeChas.size()<=0){
                                        ShowToast("没有更多数据了");
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
                LogUtil.println("请求服务器失败===" + str);
                lvXunheJilu.onRefreshComplete();
                break;
        }
    }

}
