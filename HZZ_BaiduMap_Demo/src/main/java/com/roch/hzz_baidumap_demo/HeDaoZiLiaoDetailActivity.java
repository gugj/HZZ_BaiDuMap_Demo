package com.roch.hzz_baidumap_demo;

import android.widget.ListView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.XinXiHeChaDetailAdapter;
import com.roch.hzz_baidumap_demo.entity.HeDao;
import com.roch.hzz_baidumap_demo.entity.MapEntity;
import com.roch.hzz_baidumap_demo.entity.ShiJiHeDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 河道资料详情页面---市、县、乡、村、小微水体，个人资料管辖河道详情
 * 作者：GuGaoJie
 * 时间：2017/7/17/017 15:56
 */
public class HeDaoZiLiaoDetailActivity extends CommonBaseActivity {

    @BindView(R.id.lv_xinxi_hecha)
    ListView lvXinxiHecha;
    XinXiHeChaDetailAdapter adapter;
    private ShiJiHeDao shiJiHeDao;
    private HeDao heDao;

    @Override
    public int getContentView() {
        return R.layout.activity_hedao_ziliao_detail;
    }

    @Override
    public void initData() {
        super.initData();

        heDao = (HeDao) intent.getSerializableExtra("hedao");
        shiJiHeDao = (ShiJiHeDao) intent.getSerializableExtra("shiJiHeDao");
        boolean isXiaoWeiShuiTt=intent.getBooleanExtra("isXiaoWeiShuiTt", false);

        List<MapEntity> mapEntities = new ArrayList<MapEntity>();
        MapEntity mapEntity = null;
        if(null != shiJiHeDao){
            if(isXiaoWeiShuiTt){
                mapEntity = new MapEntity("名称", shiJiHeDao.getRiverName());
                mapEntities.add(mapEntity);
                mapEntity = new MapEntity("行政区", shiJiHeDao.getAdnm());
                mapEntities.add(mapEntity);
                mapEntity = new MapEntity("长度", shiJiHeDao.getRiverLength()+"km");
                mapEntities.add(mapEntity);
                mapEntity = new MapEntity("位置", shiJiHeDao.getLocation());
                mapEntities.add(mapEntity);
                mapEntity = new MapEntity("污染状况", shiJiHeDao.getMk());
                mapEntities.add(mapEntity);
            }else {
                mapEntity = new MapEntity("河道名称", shiJiHeDao.getRiverName());
                mapEntities.add(mapEntity);
                mapEntity = new MapEntity("河段名称", shiJiHeDao.getRiverAlias());
                mapEntities.add(mapEntity);
                mapEntity = new MapEntity("行政区划", shiJiHeDao.getAdnm());
                mapEntities.add(mapEntity);
                mapEntity = new MapEntity("河道长度", shiJiHeDao.getRiverLength()+"km");
                mapEntities.add(mapEntity);
                mapEntity = new MapEntity("起点", shiJiHeDao.getRiverStart());
                mapEntities.add(mapEntity);
                mapEntity = new MapEntity("终点", shiJiHeDao.getRiverEnd());
                mapEntities.add(mapEntity);
            }
        }else if(null != heDao){
            mapEntity = new MapEntity("河段名称", heDao.getRiverName());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("河段名称(别名)", heDao.getRiverAlias());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("长度", heDao.getRiverLength()+"km");
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("所属行政区", heDao.getAdNM());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("河道起点", heDao.getRiverStart());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("河道终点", heDao.getRiverEnd());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("上级河段", heDao.getFatherRiver());
            mapEntities.add(mapEntity);
        }
        adapter = new XinXiHeChaDetailAdapter(this, mapEntities);
        lvXinxiHecha.setAdapter(adapter);
    }
}
