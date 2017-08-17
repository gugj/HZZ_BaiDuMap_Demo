package com.roch.hzz_baidumap_demo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.ViewPagerAdapter;
import com.roch.hzz_baidumap_demo.entity.XinXiHeCha;
import com.roch.hzz_baidumap_demo.entity.XinXiHeChaPhoto;
import com.roch.hzz_baidumap_demo.entity.XinXiHeChaPhoto_Result;
import com.roch.hzz_baidumap_demo.fragment.JieBaoXinXiFragment;
import com.roch.hzz_baidumap_demo.fragment.QueRenXinXiFragment;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.ResourceUtil;
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
 * 信息核查页面条目点击----如果已确认---直接查看详情
 * 作者：GuGaoJie
 * 时间：2017/7/11/011 14:27
 */
public class XinXiHeCha_Detail_Activity extends CommonBaseActivity {

//    @BindView(R.id.lv_xinxi_hecha)
//    ListView lvXinxiHecha;
//
//    private XinXiHeChaDetailAdapter adapter;

    @BindView(R.id.ll_navibar)
    LinearLayout layout_title_name;

    @BindView(R.id.vp_pager)
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    /**
     * 当前fragment索引
     */
    private int currPage = 0;
    List<Fragment> list = new ArrayList<Fragment>();
    Bundle bundle = null;

    /**
     * table页标题
     */
    private String[] title_names;
    /**
     * 所有标题
     */
    List<View> listviews = new ArrayList<View>();
    /**
     * 网络请求的参数---键值对
     */
    private Map<String,String> params=new HashMap<>();
    /**
     * 接报信息的图片
     */
    private List<XinXiHeChaPhoto> jieBaoPhotos;
    /**
     * 确认信息的图片
     */
    private List<XinXiHeChaPhoto> queRenPhotos;
    private JieBaoXinXiFragment jieBaoXinXiFragment;
    private QueRenXinXiFragment queRenXinXiFragment;

    /**
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_xinxi_hecha_detail;
    }

    /**
     * 4.初始化数据
     */
    @Override
    public void initData() {
        super.initData();
        XinXiHeCha xinXiHeCha= (XinXiHeCha) intent.getSerializableExtra("xinxihecha");
        if(null!=xinXiHeCha){
            params.put("id", xinXiHeCha.getReportRvinfoWxAndImgEntity().getId());
            MyApplication.getInstance().getHttpUtilsInstance().post(XinXiHeCha_Detail_Activity.this, URLs.Xinxi_HeCha_Tab_JieBao, params, null, MyConstans.FIRST);
            LogUtil.println("信息核查详情tab页面--接报信息图片数据的网址为：==" + URLs.Xinxi_HeCha_Tab_JieBao);

            params.clear();
            params.put("cid", xinXiHeCha.getId());
            MyApplication.getInstance().getHttpUtilsInstance().post(XinXiHeCha_Detail_Activity.this, URLs.Xinxi_HeCha_Tab_QueRen, params, null, MyConstans.SECOND);
            LogUtil.println("信息核查详情tab页面--确认信息图片数据的网址为：==" + URLs.Xinxi_HeCha_Tab_QueRen);
        }

        // 初始化tab页的title标题和点击监听
        title_names = ResourceUtil.getInstance().getStringArrayById(R.array.tab_xinxihecha_detail_title);
        for (int i = 0; i < title_names.length; i++) {
            final TextView tv_title_name = new TextView(this);
            tv_title_name.setGravity(Gravity.CENTER);
            tv_title_name.setPadding(5, 5, 5, 5);
            int text_size = Common.TEXT_SIZE;
            tv_title_name.setTextSize(text_size);
            tv_title_name.setId(i);
            tv_title_name.setText(title_names[i]);
            layout_title_name.addView(tv_title_name, Common.Width / title_names.length,LinearLayout.LayoutParams.WRAP_CONTENT);
            listviews.add(tv_title_name);

            tv_title_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currPage = v.getId();
                    viewPager.setCurrentItem(currPage);
                }
            });
        }

        // 1.接报信息的fragment
        jieBaoXinXiFragment = new JieBaoXinXiFragment();
        bundle = new Bundle();
        bundle.putSerializable(Common.BUNDEL_KEY, xinXiHeCha);
        jieBaoXinXiFragment.setArguments(bundle);

        // 2.确认信息的fragment
        queRenXinXiFragment = new QueRenXinXiFragment();
        bundle = new Bundle();
        bundle.putSerializable(Common.BUNDEL_KEY, xinXiHeCha);
        queRenXinXiFragment.setArguments(bundle);

        // 将2个fragment添加到集合中
        list.add(jieBaoXinXiFragment);
        list.add(queRenXinXiFragment);

        viewPagerAdapter = new ViewPagerAdapter(list, XinXiHeCha_Detail_Activity.this);
        viewPagerAdapter.setTitle(title_names);
        //默认显示第一页
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < listviews.size(); i++) {
                    if (arg0 == i) {
                        listviews.get(arg0).setSelected(true);
                        TextView textView = (TextView) listviews.get(arg0);
                        textView.setTextColor(ResourceUtil.getInstance().getColorById(R.color.bule_155bbb));
                        Drawable drawable = ResourceUtil.getInstance().getDrawableByID(R.drawable.blueyes_03);
                        textView.setBackgroundDrawable(drawable);
                        currPage = arg0;
                    } else {
                        listviews.get(i).setSelected(false);
                        TextView textView = (TextView) listviews.get(i);
                        textView.setTextColor(ResourceUtil.getInstance().getColorById(R.color.black));
                        Drawable drawable = ResourceUtil.getInstance().getDrawableByID(R.drawable.blusno_03);
                        textView.setBackgroundDrawable(drawable);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        // 第一次进入页面时初始化状态
        for (int i = 0; i < listviews.size(); i++) {
            if (currPage == i) {
                listviews.get(currPage).setSelected(true);
                TextView textView = (TextView)listviews.get(currPage);
                textView.setTextColor(ResourceUtil.getInstance().getColorById(R.color.bule_155bbb));
                Drawable drawable = ResourceUtil.getInstance().getDrawableByID(R.drawable.blueyes_03);
                textView.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(currPage);
            }else {
                listviews.get(i).setSelected(false);
                TextView textView = (TextView)listviews.get(i);
                textView.setTextColor(ResourceUtil.getInstance().getColorById(R.color.black));
                Drawable drawable = ResourceUtil.getInstance().getDrawableByID(R.drawable.blusno_03);
                textView.setBackgroundDrawable(drawable);
            }
        }
    }

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        switch (flag){
            case MyConstans.FIRST:
//                ShowToast("接报信息请求成功");
                LogUtil.println("接报信息请求图片数据成功：===" + str);

                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    XinXiHeChaPhoto_Result xinXiHeChaPhoto_result=XinXiHeChaPhoto_Result.parseToT(str1,XinXiHeChaPhoto_Result.class);
                    if(null!=xinXiHeChaPhoto_result && xinXiHeChaPhoto_result.getSuccess()){
                        jieBaoPhotos = xinXiHeChaPhoto_result.getJsondata();
                        if(null != jieBaoPhotos){
                            jieBaoXinXiFragment.setGvPhotoAdapterData(jieBaoPhotos);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case MyConstans.SECOND:
//                ShowToast("确认信息请求成功");
                LogUtil.println("确认信息请求图片数据成功：===" + str);
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    XinXiHeChaPhoto_Result xinXiHeChaPhoto_result=XinXiHeChaPhoto_Result.parseToT(str1,XinXiHeChaPhoto_Result.class);
                    if(null!=xinXiHeChaPhoto_result && xinXiHeChaPhoto_result.getSuccess()){
                        queRenPhotos = xinXiHeChaPhoto_result.getJsondata();
                        if(null != queRenPhotos){
                            queRenXinXiFragment.setGvPhotoAdapterData(queRenPhotos);
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
        switch (flag){
            case MyConstans.FIRST:
                ShowToast("接报信息请求失败");
                LogUtil.println("接报信息请求图片数据失败：===" + str);
                break;

            case MyConstans.SECOND:
                ShowToast("确认信息请求失败");
                LogUtil.println("确认信息请求图片数据失败：===" + str);
                break;
        }
    }

    /**
     * 4.初始化数据
     */
//    @Override
//    public void initData() {
//        super.initData();
//        Intent intent = getIntent();
//        XinXiHeCha xinXiHeCha = (XinXiHeCha) intent.getSerializableExtra("xinxihecha");
//        if (StringUtil.isNotEmpty(xinXiHeCha)) {
//            List<MapEntity> mapEntities = new ArrayList<MapEntity>();
//            MapEntity mapEntity = null;
//
//            mapEntity = new MapEntity("河道名称", xinXiHeCha.getRvName());
//            mapEntities.add(mapEntity);
//            mapEntity = new MapEntity("上报人", xinXiHeCha.getUname());
//            mapEntities.add(mapEntity);
//            mapEntity = new MapEntity("位置", xinXiHeCha.getLocation());
//            mapEntities.add(mapEntity);
//            mapEntity = new MapEntity("问题详情", xinXiHeCha.getDetail());
//            mapEntities.add(mapEntity);
//            mapEntity = new MapEntity("处理状态", xinXiHeCha.getInfostatustxt());
//            mapEntities.add(mapEntity);
//            mapEntity = new MapEntity("处理结果", xinXiHeCha.getStatustxt());
//            mapEntities.add(mapEntity);
//
//            adapter = new XinXiHeChaDetailAdapter(this, mapEntities);
//            lvXinxiHecha.setAdapter(adapter);
//        }
//    }

}
