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
 * 信息核查--确认信息fragment
 * 作者：GuGaoJie
 * 时间：2017/7/13/013 11:12
 */
public class QueRenXinXiFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_xinxi_hecha)
    ListView lvXinxiHecha;
    private XinXiHeChaDetailAdapter adapter;

    @BindView(R.id.gv_jiebao)
    GridView gv_jiebao;

    /**
     * GridView适配器的数据源
     */
    List<XinXiHeChaPhoto> photos=new ArrayList<>();
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

            mapEntity = new MapEntity("催办时间", xinXiHeCha.getStimestr()); // getCbtime()
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("催办状态", xinXiHeCha.getRemStatusstr()); // getRemstatustxt()
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("补充信息", xinXiHeCha.getSupInfo());
            mapEntities.add(mapEntity);
//            mapEntity = new MapEntity("接报确认", xinXiHeCha.getInfostatustxt());
//            mapEntities.add(mapEntity);

            adapter = new XinXiHeChaDetailAdapter(mActivity, mapEntities);
            lvXinxiHecha.setAdapter(adapter);
        }
        gv_photo_adapter=new Gv_Photo_XinXiHeCha_Adapter(mActivity,photos);
        gv_jiebao.setAdapter(gv_photo_adapter);
        gv_jiebao.setOnItemClickListener(this);
    }

    /**
     * 设置确认信息照片的数据源
     * @param datas
     */
    public void setGvPhotoAdapterData(List<XinXiHeChaPhoto> datas){
        photos.addAll(datas);
        gv_photo_adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
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
