package com.roch.hzz_baidumap_demo;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.HeDaoXuanZeAdapter;
import com.roch.hzz_baidumap_demo.adapter.ShiJiHeDaoAdapter;
import com.roch.hzz_baidumap_demo.entity.HeDao;
import com.roch.hzz_baidumap_demo.entity.HeDao_Result;
import com.roch.hzz_baidumap_demo.entity.LoginUser;
import com.roch.hzz_baidumap_demo.entity.ShiJiHeDao;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.SharePreferencesUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 用户管辖河道列表Activity
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 11:28
 */
public class UserGuanXiaHeDaoListActivity extends CommonBaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_hedao_ziliao)
    ListView lvHedaoZiliao;
//    PullToRefreshListView lvHedaoZiliao;


    /**
     * 标志位---0：下拉刷新    1：上拉加载
     */
    private static int FRIST = 0;

    private int current_page = 1; //当前页码
    private String loginUserId;

    private List<ShiJiHeDao> shiJiHeDaos;
    private ShiJiHeDaoAdapter shiJiHeDaoAdapter;

    /**
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_user_guanxia_hedao;
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
//        lvHedaoZiliao.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                //下拉刷新
//                FRIST = 0;
//                current_page = 1;
//                shiJiHeDaoAdapter = null;
//
//                params.clear();
//                // 获取请求服务器的基础查询条件参数：1.page;2.typeAdcd
//                getParams();
//                MyApplication.getInstance().getHttpUtilsInstance().post(UserGuanXiaHeDaoListActivity.this, URLs.ShiJi_HeDao_List, params, null, MyConstans.FIRST);
//                System.out.println("河道资料页面下拉刷新时请求服务器中数据的网址为：==" + URLs.ShiJi_HeDao_List + "?&page=" + String.valueOf(current_page));
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                //上拉加载
//                FRIST = 1;
//                current_page++;
//
//                params.clear();
//                // 获取请求服务器的基础查询条件参数：1.page;2.typeAdcd
//                getParams();
//                MyApplication.getInstance().getHttpUtilsInstance().post(UserGuanXiaHeDaoListActivity.this, URLs.ShiJi_HeDao_List, params, null, MyConstans.FIRST);
//                System.out.println("河道资料页面上拉加载时（不带高级查询条件）请求市县乡村资料的网址为：==" + URLs.ShiJi_HeDao_List + "?&page=" + String.valueOf(current_page));
//            }
//        });
        lvHedaoZiliao.setOnItemClickListener(this);
    }

    /**
     * 4.初始化数据
     */
    @Override
    public void initData() {
        LoginUser loginUser = SharePreferencesUtil.getLoginUser(this, Common.Login_User_Name);
        loginUserId = loginUser.getId();
        params.clear();
        // 获取请求服务器的基础查询条件参数：1.page;2.typeAdcd
        getParams();
        MyApplication.getInstance().getHttpUtilsInstance().post(UserGuanXiaHeDaoListActivity.this, URLs.HeZhang_GuanXia_HeDao_List, params, null, MyConstans.FIRST);
        System.out.println("第一次进入页面时请求服务器中数据的网址为：==" + URLs.HeZhang_GuanXia_HeDao_List + "?&page=" + String.valueOf(current_page));
    }

    /**
     * 获取请求服务器的基础查询条件参数：1.page;2.typeAdcd
     * @return
     */
    public void getParams(){
        params.put("page", String.valueOf(current_page));
        params.put("division", loginUserId);
    }

    /**
     * 河长对应的河道列表
     */
    List<HeDao> heDaos;
    HeDaoXuanZeAdapter heDaoXuanZeAdapter;

    @Override
    public void onSuccessResult(String str, int flag) {
        switch (flag){
            case MyConstans.FIRST:
//                lvHedaoZiliao.onRefreshComplete();
//                ShowToast("请求服务器成功");
                LogUtil.println("请求服务器成功==="+str);

                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    HeDao_Result heDao_result=HeDao_Result.parseToT(str1,HeDao_Result.class);
                    if(null != heDao_result && heDao_result.getSuccess()){
                        heDaos = heDao_result.getJsondata();
                        if(null != heDaos){
                            heDaoXuanZeAdapter=new HeDaoXuanZeAdapter(UserGuanXiaHeDaoListActivity.this,heDaos,2);
                            lvHedaoZiliao.setAdapter(heDaoXuanZeAdapter);
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
//        lvHedaoZiliao.onRefreshComplete();
        switch (flag){
            case MyConstans.FIRST:
                ShowToast("请求服务器失败");
                LogUtil.println("请求服务器失败==="+str);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,HeDaoZiLiaoDetailActivity.class);
        intent.putExtra("hedao",heDaos.get(position));
        intent.putExtra(Common.TITLE_KEY,"管辖河道详情");
        startActivity(intent);
    }
}
