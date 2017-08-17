package com.roch.hzz_baidumap_demo;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.ShiXianXiangCunAdapter;
import com.roch.hzz_baidumap_demo.entity.Ac_Cd_Result;
import com.roch.hzz_baidumap_demo.entity.Ad_Cd;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/20/020 15:28
 */
public class SelectXingZhengQu_xiang extends CommonBaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_xzq)
    ListView lvXzq;

    /**
     * 网络请求的参数---键值对
     */
    private Map<String,String> params=new HashMap<>();
    private List<Ad_Cd> ad_cds;
    private String current_type_adcd;
    private String select_ad_cd_name;
    private Ad_Cd select_ad_cd;

    /**
     * 1.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_select_xingzhengqu;
    }

    /**
     * 4.初始化控件的监听
     */
    @Override
    public void initListener() {
        super.initListener();
        lvXzq.setOnItemClickListener(this);
    }

    /**
     * 5.初始化数据
     */
    @Override
    public void initData() {
        super.initData();
        MyApplication.getInstance().addActivity(this);
        current_type_adcd = intent.getStringExtra("current_type_adcd");
        select_ad_cd_name = intent.getStringExtra("select_ad_cd_name");
        select_ad_cd = (Ad_Cd) intent.getSerializableExtra("select_ad_cd");
        params.clear();
        params.put("adCd", select_ad_cd.getId());
        MyApplication.getInstance().getHttpUtilsInstance().post(SelectXingZhengQu_xiang.this, URLs.XingZhengQu, params, null, MyConstans.FIRST);
        System.out.println("河道资料高级查询页面请求乡级行政区的网址为：==" + URLs.XingZhengQu);
    }

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        switch (flag){
            case MyConstans.FIRST:
                LogUtil.println("请求乡级行政区成功：==="+str);
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    Ac_Cd_Result ad_cd_result=Ac_Cd_Result.parseToT(str1,Ac_Cd_Result.class);
                    if(null != ad_cd_result && ad_cd_result.getSuccess()){
                        ad_cds = ad_cd_result.getJsondata();
                        if(null != ad_cds){
                            xingZhengQus.clear();
                            // 初始化行政区数据
                            initXzqData();
                            ShiXianXiangCunAdapter popupViewAdapter = new ShiXianXiangCunAdapter(this, xingZhengQus);
                            lvXzq.setAdapter(popupViewAdapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private List<String> xingZhengQus=new ArrayList<>();

    /**
     * 初始化行政区数据
     */
    private void initXzqData(){
        for (int i = 0; i < ad_cds.size(); i++) {
            xingZhengQus.add(ad_cds.get(i).getAdNm());
        }
    }

    @Override
    public void onFaileResult(String str, int flag) {
        super.onFaileResult(str, flag);
        switch (flag){
            case MyConstans.FIRST:
                ShowToast("请求乡级行政区失败");
                LogUtil.println("请求乡级行政区失败:==="+str);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if("xiang".equals(current_type_adcd)){
            Intent intent=new Intent();
            intent.setAction("shi_xian_xiang_cun_data");
            intent.putExtra("select_ad_cd",ad_cds.get(position));
            intent.putExtra("select_ad_cd_name",select_ad_cd_name+" "+ad_cds.get(position).getAdNm());
            sendBroadcast(intent);
//            MyApplication.getInstance().finishActivity(SelectXingZhengQu_Shi.class);
//            MyApplication.getInstance().finishActivity(SelectXingZhengQu_Xian.class);
//            MyApplication.getInstance().finishActivity(this);
            MyApplication.getInstance().finishAllActivity();
        }else {
            Intent intent=new Intent(this,SelectXingZhengQu_cun.class);
            intent.putExtra(Common.TITLE_KEY,"行政区");
            intent.putExtra("select_ad_cd",ad_cds.get(position));
            intent.putExtra("select_ad_cd_name",select_ad_cd_name+" "+ad_cds.get(position).getAdNm());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                MyApplication.getInstance().finishActivity(this);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            MyApplication.getInstance().finishActivity(this);
        }
        return true;
    }
}
