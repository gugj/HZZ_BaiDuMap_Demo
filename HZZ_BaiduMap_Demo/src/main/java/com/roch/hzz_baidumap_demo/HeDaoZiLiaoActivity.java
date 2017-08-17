package com.roch.hzz_baidumap_demo;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.ShiJiHeDaoAdapter;
import com.roch.hzz_baidumap_demo.dialog.AddPopWindow;
import com.roch.hzz_baidumap_demo.entity.Ad_Cd;
import com.roch.hzz_baidumap_demo.entity.ListMenu;
import com.roch.hzz_baidumap_demo.entity.ShiJiHeDao;
import com.roch.hzz_baidumap_demo.entity.ShiJiHeDao_Result;
import com.roch.hzz_baidumap_demo.entity.WenTiLeiXin;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
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
 * 河道资料Activity
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 11:28
 */
public class HeDaoZiLiaoActivity extends CommonBaseActivity implements AddPopWindow.ShowMessageListener, AdapterView.OnItemClickListener {

    @BindView(R.id.lv_hedao_ziliao)
    PullToRefreshListView lvHedaoZiliao;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_filter)
    TextView tvFilter;
    private ShiJiHeDaoAdapter shiJiHeDaoAdapter;

    /**
     * 标志位---0：下拉刷新    1：上拉加载
     */
    private static int FRIST = 0;
    private int current_page = 1; //当前页码
    /**
     *  当前河道的级别---1.shi ;2.xian ;3.xiang ;4.cun---默认为：shi
     */
    private String current_type_adcd = "shi";
    private boolean flag_gaoji_chaxun=false;
    private List<ShiJiHeDao> shiJiHeDaos;

    /**
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_hedao_ziliao;
    }

    /**
     * 3.初始化Tool控件
     */
    @Override
    public void initToolbar() {
        super.initToolbar();
        tvSure.setVisibility(View.VISIBLE);
        tvFilter.setVisibility(View.VISIBLE);
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
        lvHedaoZiliao.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                FRIST = 0;
                current_page = 1;
                flag_gaoji_chaxun = false;
                shiJiHeDaoAdapter=null;

                // 获取请求服务器的基础查询条件参数：1.page;2.typeAdcd
                getParams();
                if(isXiaoWeiShuiTt){
                    MyApplication.getInstance().getHttpUtilsInstance().post(HeDaoZiLiaoActivity.this, URLs.XiaoWeiShuiTi_HeDao_List, params, null, MyConstans.FIRST);
                    System.out.println("河道资料页面下拉刷新时请求小微水体资料的网址为：==" + URLs.XiaoWeiShuiTi_HeDao_List + "?&page=" + String.valueOf(current_page));
                }else {
                    MyApplication.getInstance().getHttpUtilsInstance().post(HeDaoZiLiaoActivity.this, URLs.ShiJi_HeDao_List, params, null, MyConstans.FIRST);
                    System.out.println("河道资料页面下拉刷新时请求市县乡村数据的网址为：==" + URLs.ShiJi_HeDao_List + "?&page=" + String.valueOf(current_page));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                FRIST = 1;
                current_page++;

                // 获取请求服务器的基础查询条件参数：1.page;2.typeAdcd
                getParams();
                if (flag_gaoji_chaxun) { //带高级查询条件
                    // 获取请求服务时的高级查询条件参数：1.adCd；2.heDao_Name；3.heZhang_Name;4.heZhang_JiBie
                    getGaoJiParams();
                    if (isXiaoWeiShuiTt){
                        MyApplication.getInstance().getHttpUtilsInstance().post(HeDaoZiLiaoActivity.this, URLs.XiaoWeiShuiTi_HeDao_List, params, null, MyConstans.FIRST);
                        System.out.println("河道资料页面上拉加载时（带高级查询条件）请求小微水体资料的网址为：==" + URLs.XiaoWeiShuiTi_HeDao_List + "?&page=" + String.valueOf(current_page));
                    }else {
                        MyApplication.getInstance().getHttpUtilsInstance().post(HeDaoZiLiaoActivity.this, URLs.ShiJi_HeDao_List, params, null, MyConstans.FIRST);
                        System.out.println("河道资料页面上拉加载时（带高级查询条件）请求市县乡村资料的网址为：==" + URLs.ShiJi_HeDao_List + "?&page=" + String.valueOf(current_page));
                    }
                } else {
                    if (isXiaoWeiShuiTt){
                        MyApplication.getInstance().getHttpUtilsInstance().post(HeDaoZiLiaoActivity.this, URLs.XiaoWeiShuiTi_HeDao_List, params, null, MyConstans.FIRST);
                        System.out.println("河道资料页面上拉加载时（不带高级查询条件）请求小微水体资料的网址为：==" + URLs.XiaoWeiShuiTi_HeDao_List + "?&page=" + String.valueOf(current_page));
                    }else {
                        MyApplication.getInstance().getHttpUtilsInstance().post(HeDaoZiLiaoActivity.this, URLs.ShiJi_HeDao_List, params, null, MyConstans.FIRST);
                        System.out.println("河道资料页面上拉加载时（不带高级查询条件）请求市县乡村资料的网址为：==" + URLs.ShiJi_HeDao_List + "?&page=" + String.valueOf(current_page));
                    }
                }
            }
        });
        lvHedaoZiliao.setOnItemClickListener(this);
    }

    /**
     * 4.初始化数据
     */
    @Override
    public void initData() {
        // 获取请求服务器的基础查询条件参数：1.page;2.typeAdcd
        getParams();
        MyApplication.getInstance().getHttpUtilsInstance().post(HeDaoZiLiaoActivity.this, URLs.ShiJi_HeDao_List, params, null, MyConstans.FIRST);
        System.out.println("河道资料页面第一次进入页面时请求服务器中数据的网址为：==" + URLs.ShiJi_HeDao_List + "?&page=" + String.valueOf(current_page));
    }

    /**
     * 获取请求服务器的基础查询条件参数：1.page;2.typeAdcd
     * @return
     */
    public void getParams(){
        params.clear();
        params.put("page", String.valueOf(current_page));
        if(!isXiaoWeiShuiTt){ // 如果不是小微水体模式
            params.put("typeAdcd", current_type_adcd);
        }
    }

    @OnClick({R.id.tv_sure, R.id.tv_filter})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_sure: //点击高级查询按钮
//                Intent intent=new Intent(HeDaoZiLiaoActivity.this,SearchActivity.class);
                Intent intent=new Intent(HeDaoZiLiaoActivity.this,SearchActivity_HeDao_ZiLiao.class);
                intent.putExtra(Common.TITLE_KEY,"高级查询");
                intent.putExtra("current_type_adcd",current_type_adcd);
                startActivityForResult(intent, 1);
                break;

            case R.id.tv_filter: //点击河道筛选按钮（市级、县级、乡级、村级、小微水体）
                int xPox = (int) (Common.Width * 0.7 - 10);
                AddPopWindow window = new AddPopWindow(HeDaoZiLiaoActivity.this, 2);
                window.setShowMessageListener(HeDaoZiLiaoActivity.this);
//                window.showPopupWindow(toolbar, xPox);
                window.showAtDropDownCenter(tvFilter,toolbar);
                break;
        }
    }

    @Override
    public void onSuccessResult(String str, int flag) {
        switch (flag){
            case MyConstans.FIRST:
                lvHedaoZiliao.onRefreshComplete();
                LogUtil.println("请求服务器成功==="+str);
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    ShiJiHeDao_Result shiJiHeDao_result = ShiJiHeDao_Result.parseToT(str1, ShiJiHeDao_Result.class);
                    if (StringUtil.isNotEmpty(shiJiHeDao_result) && shiJiHeDao_result.getSuccess()) { //json--success:true
                        shiJiHeDaos = shiJiHeDao_result.getJsondata();
                        if (StringUtil.isNotEmpty(shiJiHeDaos) && shiJiHeDaos.size() > 0) {//返回的集合有数据
                            switch (FRIST){
                                case 0: //下拉刷新---置空
                                    if(shiJiHeDaoAdapter==null){
                                        shiJiHeDaoAdapter = new ShiJiHeDaoAdapter(HeDaoZiLiaoActivity.this, shiJiHeDaos,isXiaoWeiShuiTt);
                                        lvHedaoZiliao.setAdapter(shiJiHeDaoAdapter);
                                    }else {
                                        shiJiHeDaoAdapter.onRefsh(shiJiHeDaos);
                                    }
                                    break;

                                case 1: //上拉加载---追加
                                    if (shiJiHeDaoAdapter == null) {
                                        shiJiHeDaoAdapter = new ShiJiHeDaoAdapter(HeDaoZiLiaoActivity.this, shiJiHeDaos,isXiaoWeiShuiTt);
                                        lvHedaoZiliao.setAdapter(shiJiHeDaoAdapter);
                                    }else {
                                        shiJiHeDaoAdapter.addList(shiJiHeDaos);
                                    }
                                    break;
                            }
                        }else { //返回的集合为空
                            if(FRIST == 0){
                                if(shiJiHeDaoAdapter == null){
                                    shiJiHeDaoAdapter = new ShiJiHeDaoAdapter(HeDaoZiLiaoActivity.this, shiJiHeDaos,isXiaoWeiShuiTt);
                                    lvHedaoZiliao.setAdapter(shiJiHeDaoAdapter);
                                }
                            }
                            ShowToast("没有更多数据");
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
        lvHedaoZiliao.onRefreshComplete();
        switch (flag){
            case MyConstans.FIRST:
                ShowToast("请求服务器失败");
                LogUtil.println("请求服务器失败==="+str);
                break;
        }
    }

    //高级查询条件
    Ad_Cd selectedXZQ_Ad_Cd;
    WenTiLeiXin selectedHeZhangJiBie;
    String heDao_Name,heZhang_Name;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==2){
                FRIST=0;
                current_page = 1;
                flag_gaoji_chaxun=true;
                shiJiHeDaoAdapter=null;

                selectedXZQ_Ad_Cd= (Ad_Cd) data.getSerializableExtra("selectedXZQ_Ad_Cd");
                selectedHeZhangJiBie= (WenTiLeiXin) data.getSerializableExtra("selectedHeZhangJiBie");
                heDao_Name= data.getStringExtra("heDao_Name");
                heZhang_Name= data.getStringExtra("heZhang_Name");

                // 获取请求服务器的基础查询条件参数：1.page;2.typeAdcd
                getParams();
                // 获取请求服务时的高级查询条件参数：1.adCd；2.heDao_Name；3.heZhang_Name;4.heZhang_JiBie
                getGaoJiParams();
                if (isXiaoWeiShuiTt){
                    MyApplication.getInstance().getHttpUtilsInstance().post(HeDaoZiLiaoActivity.this, URLs.XiaoWeiShuiTi_HeDao_List, params, null, MyConstans.FIRST);
                    System.out.println("河道资料页面（高级查询筛选后）请求小微水体资料的网址为：==" + URLs.ShiJi_HeDao_List + "?&page=" + String.valueOf(current_page));
                }else {
                    MyApplication.getInstance().getHttpUtilsInstance().post(HeDaoZiLiaoActivity.this, URLs.ShiJi_HeDao_List, params, null, MyConstans.FIRST);
                    System.out.println("河道资料页面（高级查询筛选后）请求市县乡村资料的网址为：==" + URLs.ShiJi_HeDao_List + "?&page=" + String.valueOf(current_page));
                }
            }
        }
    }

    /**
     * 获取请求服务时的高级查询条件参数：1.adCd；2.heDao_Name；3.heZhang_Name;4.heZhang_JiBie
     */
    private void getGaoJiParams() {
        if(StringUtil.isNotEmpty(selectedXZQ_Ad_Cd)){
            params.put("adCd",selectedXZQ_Ad_Cd.getId());
        }
        if(StringUtil.isNotEmpty(heDao_Name)){
            params.put("riverName",heDao_Name);
        }
        if(StringUtil.isNotEmpty(heZhang_Name)){
            params.put("riverAlias",heZhang_Name);
        }
        if(StringUtil.isNotEmpty(selectedHeZhangJiBie)){
            params.put("heZhang_JiBie",selectedHeZhangJiBie.getTypecode());
        }
    }

    boolean isXiaoWeiShuiTt=false;

    @Override
    public void Message(Object object) {
        ListMenu menu = (ListMenu) object;
        FRIST = 0;
        current_page = 1;
        shiJiHeDaoAdapter=null;
        if("市级河道".equals(menu.getName())){
            isXiaoWeiShuiTt=false;
            current_type_adcd="shi";
            // 请求服务中市、县、乡、村的河道资料
            requestNetData();
        }else if("县级河道".equals(menu.getName())){
            isXiaoWeiShuiTt=false;
            current_type_adcd="xian";
            // 请求服务中市、县、乡、村的河道资料
            requestNetData();
        }else if("乡级河道".equals(menu.getName())){
            isXiaoWeiShuiTt=false;
            current_type_adcd="xiang";
            // 请求服务中市、县、乡、村的河道资料
            requestNetData();
        }else if("村级河道".equals(menu.getName())){
            isXiaoWeiShuiTt=false;
            current_type_adcd="cun";
            // 请求服务中市、县、乡、村的河道资料
            requestNetData();
        }else if("小微水体".equals(menu.getName())){
            isXiaoWeiShuiTt=true;
            current_type_adcd="xiaoweishuiti";
            // 请求服务器中小微水体的资料
            requestNetData_SmallRever();
        }
    }

    /**
     * 请求服务中市、县、乡、村的河道资料
     */
    public void requestNetData(){
        // 获取请求服务器的基础查询条件参数：1.page;2.typeAdcd
        getParams();
        MyApplication.getInstance().getHttpUtilsInstance().post(HeDaoZiLiaoActivity.this, URLs.ShiJi_HeDao_List, params, null, MyConstans.FIRST);
        System.out.println("河道资料页面请求服务器中市县乡村河道的网址为：==" + URLs.ShiJi_HeDao_List + "?&page=" + String.valueOf(current_page));
    }

    /**
     * 请求服务器中小微水体的资料
     */
    public void requestNetData_SmallRever(){
        // 获取请求服务器的基础查询条件参数：1.page
        getParams();
        MyApplication.getInstance().getHttpUtilsInstance().post(HeDaoZiLiaoActivity.this, URLs.XiaoWeiShuiTi_HeDao_List, params, null, MyConstans.FIRST);
        System.out.println("河道资料页面请求服务器中小微水体资料的网址为：==" + URLs.XiaoWeiShuiTi_HeDao_List + "?&page=" + String.valueOf(current_page));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ShiJiHeDao shiJiHeDao=(ShiJiHeDao)parent.getItemAtPosition(position);
        if(null != shiJiHeDao){
            Intent intent=new Intent(this,HeDaoZiLiaoDetailActivity.class);
            intent.putExtra("shiJiHeDao",shiJiHeDao);
            intent.putExtra("isXiaoWeiShuiTt",isXiaoWeiShuiTt);
            intent.putExtra(Common.TITLE_KEY,"河道资料详情");
            startActivity(intent);
        }
    }
}
