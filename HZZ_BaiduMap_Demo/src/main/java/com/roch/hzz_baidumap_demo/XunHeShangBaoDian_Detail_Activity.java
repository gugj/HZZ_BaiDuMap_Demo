package com.roch.hzz_baidumap_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.Gv_Photo_XinXiHeCha_Adapter;
import com.roch.hzz_baidumap_demo.adapter.XinXiHeChaDetailAdapter;
import com.roch.hzz_baidumap_demo.entity.MapEntity;
import com.roch.hzz_baidumap_demo.entity.Photo;
import com.roch.hzz_baidumap_demo.entity.XinXiHeChaPhoto;
import com.roch.hzz_baidumap_demo.entity.XinXiHeChaPhoto_Result;
import com.roch.hzz_baidumap_demo.entity.XunHeShangBao_Marker;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.StringUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 巡河时信息上报点的详情页面
 * 作者：GuGaoJie
 * 时间：2017/7/11/011 14:27
 */
public class XunHeShangBaoDian_Detail_Activity extends CommonBaseActivity implements AdapterView.OnItemClickListener {

    Bundle bundle = null;
    @BindView(R.id.lv_xinxi_hecha)
    ListView lvXinxiHecha;
    @BindView(R.id.gv_jiebao)
    GridView gvJiebao;
    Gv_Photo_XinXiHeCha_Adapter gv_photo_adapter;
    @BindView(R.id.tv_xiancheng_zhaopian)
    TextView tvXianchengZhaopian;

    /**
     * 网络请求的参数---键值对
     */
    private Map<String, String> params = new HashMap<>();
    /**
     * 接报信息的图片
     */
    private List<XinXiHeChaPhoto> jieBaoPhotos;
    private XinXiHeChaDetailAdapter adapter;

    /**
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_xinxi_hecha_detail0;
    }

    /**
     * 4.初始化控件的监听
     */
    @Override
    public void initListener() {
        super.initListener();
        gvJiebao.setOnItemClickListener(this);
    }

    /**
     * 5.初始化数据
     */
    @Override
    public void initData() {
        super.initData();
        XunHeShangBao_Marker xunHeShangBao_marker = (XunHeShangBao_Marker) intent.getSerializableExtra("xunHeShangBao_marker");
        if (null != xunHeShangBao_marker) {
            // 初始化接报信息的基本数据
//            initListViewData(xunHeShangBao_marker);

            params.put("id", xunHeShangBao_marker.getId());
            MyApplication.getInstance().getHttpUtilsInstance().post(XunHeShangBaoDian_Detail_Activity.this, URLs.XunHe_ShangBao_Marker_Detail, params, null, MyConstans.FIRST);
            LogUtil.println("根据巡河时信息上报点ID---获取详情的网址为：==" + URLs.XunHe_ShangBao_Marker_Detail);
        }
    }

    /**
     * 请求服务器中的照片数据
     */
    private void requestNetPhoto(){
        MyApplication.getInstance().getHttpUtilsInstance().post(XunHeShangBaoDian_Detail_Activity.this, URLs.Xinxi_HeCha_Tab_JieBao, params, null, MyConstans.SECOND,false);
        LogUtil.println("巡河上报点详情页面请求照片数据的网址为：==" + URLs.Xinxi_HeCha_Tab_JieBao);
    }

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        switch (flag) {
            case MyConstans.FIRST:
                LogUtil.println("请求巡河上报点详情成功：===" + str);
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    XunHeShangBao_Marker xunHeShangBao_marker = XunHeShangBao_Marker.parseToT(str1, XunHeShangBao_Marker.class);
                    if (StringUtil.isNotEmpty(xunHeShangBao_marker)) { //json--success:true
                        initListViewData(xunHeShangBao_marker);
                    } else { //json---success:false
                        ShowToast("服务器网络异常");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 请求服务器中的照片数据
                requestNetPhoto();
                break;

            case MyConstans.SECOND:
                LogUtil.println("请求图片数据成功：===" + str);
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    XinXiHeChaPhoto_Result xinXiHeChaPhoto_result = XinXiHeChaPhoto_Result.parseToT(str1, XinXiHeChaPhoto_Result.class);
                    if (null != xinXiHeChaPhoto_result && xinXiHeChaPhoto_result.getSuccess()) {
                        jieBaoPhotos = xinXiHeChaPhoto_result.getJsondata();
                        if (null != jieBaoPhotos) {
                            gv_photo_adapter = new Gv_Photo_XinXiHeCha_Adapter(this, jieBaoPhotos);
                            gvJiebao.setAdapter(gv_photo_adapter);
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
                ShowToast("请求巡河上报点详情失败");
                LogUtil.println("请求巡河上报点详情失败：===" + str);
                // 请求服务器中的照片数据
                requestNetPhoto();
                break;

            case MyConstans.SECOND:
                ShowToast("请求图片数据失败");
                LogUtil.println("请求图片数据失败：===" + str);
                break;
        }
    }

    /**
     * 5-0.初始化接报信息的基本数据
     */
    public void initListViewData(XunHeShangBao_Marker xunHeShangBao_marker) {
        if (StringUtil.isNotEmpty(xunHeShangBao_marker)) {
            List<MapEntity> mapEntities = new ArrayList<MapEntity>();
            MapEntity mapEntity = null;

            mapEntity = new MapEntity("河道名称", xunHeShangBao_marker.getRiverName());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("行政区划", xunHeShangBao_marker.getAdnm());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("上报人", xunHeShangBao_marker.getName());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("巡河时间", xunHeShangBao_marker.getCkTimeStr());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("问题类型", xunHeShangBao_marker.getQtype());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("问题描述", xunHeShangBao_marker.getDetail());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("处理状态", xunHeShangBao_marker.getStatus());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("处理结果", xunHeShangBao_marker.getHandle());
            mapEntities.add(mapEntity);

            adapter = new XinXiHeChaDetailAdapter(this, mapEntities);
            lvXinxiHecha.setAdapter(adapter);
        }
    }

    /**
     * 预览照片的数据源集合
     */
    private List<Photo> selectedPhotos = new ArrayList<>();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Photo photo;
        for (int i = 0; i < jieBaoPhotos.size(); i++) {
            photo = new Photo();
            photo.setUrl(URLs.IMAGE_URL + jieBaoPhotos.get(i).getFilePath());
            selectedPhotos.add(photo);
        }
        imageBrower(position, selectedPhotos);
    }

    /**
     * @param position
     * @param urls
     */
    protected void imageBrower(int position, List<Photo> urls) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) urls);
        bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS_KEY, bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
