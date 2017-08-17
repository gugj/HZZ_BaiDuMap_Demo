package com.roch.hzz_baidumap_demo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.roch.hzz_baidumap_demo.ImagePagerActivity;
import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.adapter.Gv_Photo_XinXiHeCha_Adapter;
import com.roch.hzz_baidumap_demo.adapter.XinXiHeChaDetailAdapter;
import com.roch.hzz_baidumap_demo.entity.MapEntity;
import com.roch.hzz_baidumap_demo.entity.Photo;
import com.roch.hzz_baidumap_demo.entity.XinXiHeCha;
import com.roch.hzz_baidumap_demo.entity.XinXiHeChaPhoto;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.StringUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 信息核查--接报信息fragment
 * 作者：GuGaoJie
 * 时间：2017/7/13/013 11:12
 */
public class JieBaoXinXiFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_xinxi_hecha)
    ListView lvXinxiHecha;
    @BindView(R.id.gv_jiebao)
    GridView gv_jiebao;

    /**
     * GridView适配器的数据源
     */
    List<XinXiHeChaPhoto> photos=new ArrayList<>();
    private XinXiHeChaDetailAdapter adapter;
    Gv_Photo_XinXiHeCha_Adapter gv_photo_adapter;

    /**
     * 1.获取该Fragment的布局View
     */
    @Override
    protected View getContenView(Activity mActivity) {
        return View.inflate(mActivity, R.layout.fragment_jiebao_xinxi_detail, null);
    }

    /**
     * 2.初始化数据
     */
    @Override
    protected void initData() {
        XinXiHeCha xinXiHeCha = (XinXiHeCha) bundle.getSerializable(Common.BUNDEL_KEY);
        if (StringUtil.isNotEmpty(xinXiHeCha)) {

            List<MapEntity> mapEntities = new ArrayList<MapEntity>();
            MapEntity mapEntity = null;

            mapEntity = new MapEntity("河道名称", xinXiHeCha.getReportRvinfoWxAndImgEntity().getRvName());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("上报时间", xinXiHeCha.getReportRvinfoWxAndImgEntity().getStimestr());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("问题描述", xinXiHeCha.getReportRvinfoWxAndImgEntity().getDetail());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("上报人", xinXiHeCha.getReportRvinfoWxAndImgEntity().getUname());
            mapEntities.add(mapEntity);
            if("0".equals(xinXiHeCha.getInfoStatus())){
                mapEntity = new MapEntity("确认状态", "未确认");
                mapEntities.add(mapEntity);
            }else if("1".equals(xinXiHeCha.getInfoStatus())){
                mapEntity = new MapEntity("确认状态", "已确认");
                mapEntities.add(mapEntity);
            }

            adapter = new XinXiHeChaDetailAdapter(mActivity, mapEntities);
            lvXinxiHecha.setAdapter(adapter);
        }
        gv_photo_adapter=new Gv_Photo_XinXiHeCha_Adapter(mActivity,photos);
        gv_jiebao.setAdapter(gv_photo_adapter);
        gv_jiebao.setOnItemClickListener(this);
    }

    /**
     * 设置接报信息照片的数据源
     * @param datas
     */
    public void setGvPhotoAdapterData(List<XinXiHeChaPhoto> datas){
        photos.addAll(datas);
        gv_photo_adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private List<Photo> selectedPhotos=new ArrayList<>();
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Photo photo;
        selectedPhotos.clear();
        for (int i = 0; i < photos.size(); i++) {
            photo=new Photo();
            photo.setUrl(URLs.IMAGE_URL+photos.get(i).getFilePath());
            selectedPhotos.add(photo);
        }
        imageBrower(position,selectedPhotos);
    }

    /**
     * @param position
     * @param urls
     */
    protected void imageBrower(int position, List<Photo> urls) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable)urls);
        bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS_KEY, bundle);
        startActivity(intent);
    }

}
