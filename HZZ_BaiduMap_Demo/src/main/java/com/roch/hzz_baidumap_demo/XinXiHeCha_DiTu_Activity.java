package com.roch.hzz_baidumap_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.entity.XinXiHeCha;
import com.roch.hzz_baidumap_demo.service.LocationService;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 信息核查时，未确认（0）时进入该页面，显示地图
 * 作者：GuGaoJie
 * 时间：2017/7/14/014 17:50
 */
public class XinXiHeCha_DiTu_Activity extends CommonBaseActivity implements BaiduMap.OnMarkerClickListener {

    @BindView(R.id.tv_cur_location)
    TextView tvCurLocation;
    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.tv_xxsb)
    TextView tvXxsb;

    private BaiduMap mBaiduMap;
    private LocationService locService;
    /**
     * 百度地图添加覆盖物时覆盖物所有基本信息的载体
     */
    MarkerOptions markerOptions = new MarkerOptions();
    /**
     * 百度地图添加覆盖物时覆盖物图片的载体
     */
    BitmapDescriptor bitmapDescptor;

    private View popView;
    /**
     * 小弹窗中监测点的名称
     */
    private TextView tv_jiancedian_name;
    /**
     * 小弹窗中监测点的名称
     */
    private TextView tv_jiancedian_name_value;

    /**
     * 小弹窗中监测点类型的名称
     */
    private TextView tv_jiancedian_type;
    /**
     * 小弹窗中监测点类型的名称
     */
    private TextView tv_jiancedian_type_value;
    private TextView tv_xunheshijian;
    private TextView tv_xunheshijian_value;
    /**
     * 小弹窗中的详细按钮
     */
    private TextView tv_xiangxi;
    /**
     * 公司地址所在地的经纬度  34.7860810000, 113.6990870000
     */
    private LatLng rochLaLong = new LatLng(34.3886750000,113.6888280000);

    /**
     * 1.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_xxhc_dt;
    }

    XinXiHeCha xinXiHeCha_0;
    /**
     * 2.初始化传参数据---设置title标题
     */
    @Override
    public void initBundle() {
        super.initBundle();
        // 2-0.初始化百度地图参数---1.地图类型；2.缩放级别
        initMapConfig();

        xinXiHeCha_0= (XinXiHeCha) intent.getSerializableExtra("xinxihecha");
        String location = xinXiHeCha_0.getLocation();
        if(null != location){
            String[] split = location.split(",");
            if(split.length>1){
                mCurrentLon=Double.parseDouble(split[0]);
                mCurrentLat=Double.parseDouble(split[1]);
                LogUtil.println("经度：==="+mCurrentLon+",纬度：===="+mCurrentLat);
                // 给百度地图设置中心点和缩放级别
                setCenterAndZoom();
                // 添加当前位置的覆盖物
                addCurrentLocationOverlay();
                // 将popView添加到mMapView上，并给baiduMap设置覆盖物的点击监听
                addMarkerClickListener();
            }
        }
    }

    /**
     * 2-0.初始化百度地图参数---1.地图类型；2.缩放级别
     */
    private void initMapConfig() {
        //获取BaiduMap
        mBaiduMap = mMapView.getMap();
        //设置地图类型---普通
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设置缩放级别
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));

        // 3-0.初始化百度定位配置参数、设置定位监听、开始定位
        initLocationConfig();
    }

    /**
     * 3-0.初始化百度定位配置参数、设置定位监听、开始定位
     */
    private void initLocationConfig() {
        locService = ((MyApplication) this.getApplication()).locationService;
//        LocationClientOption mOption = locService.getDefaultLocationClientOption(); //此时已经配置过定位参数，直接返回过来
//        locService.setLocationOption(mOption); //此时又重复配置了定位参数
//        mOption.setCoorType("bd09ll");
//        locService.setLocationOption(mOption);
        locService.registerListener(listener);
        locService.start();
    }

    BDLocationListener listener = new BDLocationListener(){
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Common.mCurrentLat=bdLocation.getLatitude();
            Common.mCurrentLon=bdLocation.getLongitude();
            Common.mCurrentLocationAddr=bdLocation.getAddrStr(); //定位的详细位置
            LogUtil.println("信息核查时地图页面中---定位成功：===经度="+Common.mCurrentLon+",纬度="+Common.mCurrentLat);
            tvCurLocation.setText("当前定位位置："+bdLocation.getAddrStr()+"，经纬度为："+Common.mCurrentLon+","+Common.mCurrentLat);
        }
    };

    double mCurrentLat;
    double mCurrentLon;

    /**
     * 给百度地图设置中心点和缩放级别
     */
    private void setCenterAndZoom() {
        // 设置中心点
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(mCurrentLat, mCurrentLon));
        mBaiduMap.setMapStatus(mapStatusUpdate);

        // 设置缩放级别
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 添加当前位置的覆盖物
     */
    private void addCurrentLocationOverlay() {
       Bundle bundle=new Bundle();
        bundle.putSerializable("xinxihecha",xinXiHeCha_0);
        markerOptions.position(new LatLng(mCurrentLat, mCurrentLon));
        bitmapDescptor = BitmapDescriptorFactory.fromResource(R.drawable.hongse_2);
        markerOptions.icon(bitmapDescptor)
                     .extraInfo(bundle);
        // 掉下动画
        markerOptions.animateType(MarkerOptions.MarkerAnimateType.drop);
        markerOptions.period(10);
        // 添加覆盖物
        mBaiduMap.addOverlay(markerOptions);
    }

    /**
     * 将popView添加到mMapView上，并给baiduMap设置覆盖物的点击监听
     * 2016年11月11日
     */
    private void addMarkerClickListener() {
        ViewGroup.LayoutParams layoutParams=new MapViewLayoutParams
                .Builder()
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)
                .position(rochLaLong)
                .build();

        popView = View.inflate(this, R.layout.dianziditu_pop, null);
        tv_jiancedian_name=(TextView) popView.findViewById(R.id.tv_jiancedian_name);
        tv_jiancedian_name_value=(TextView) popView.findViewById(R.id.tv_jiancedian_name_value);
        tv_jiancedian_type=(TextView) popView.findViewById(R.id.tv_jiancedian_type);
        tv_jiancedian_type_value=(TextView) popView.findViewById(R.id.tv_jiancedian_type_value);
        tv_xunheshijian=(TextView) popView.findViewById(R.id.tv_xunheshijian);
        tv_xunheshijian_value=(TextView) popView.findViewById(R.id.tv_xunheshijian_value);
        tv_xiangxi=(TextView) popView.findViewById(R.id.tv_xiangxi);
        tv_xiangxi.setOnClickListener(this);

        mMapView.addView(popView, layoutParams);

        // 先隐藏点击时要出现的view
        popView.setVisibility(View.GONE);

        // 设置标签的点击事件监听
        mBaiduMap.setOnMarkerClickListener(this);
    }

    @OnClick(R.id.tv_xxsb)
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_xxsb:
                Intent intent=new Intent(this,XinXinHeCha_ShangBao_Activity.class);
                intent.putExtra("xinxihecha", xinXiHeCha_0);
                intent.putExtra(Common.TITLE_KEY, "信息上报---核查");
                startActivity(intent);
                break;

            case R.id.tv_xiangxi:
//                ShowToast("详细");
                Intent intent1=new Intent(this,XinXiHeCha_Detail0_Activity.class);
                intent1.putExtra("xinxihecha", xinXiHeCha_0);
                intent1.putExtra(Common.TITLE_KEY, "信息核查详情");
                startActivity(intent1);
                break;
        }
    }

    /**
     * 标记popView是否显示
     */
    private boolean isShowIng=false;

    @Override
    public boolean onMarkerClick(Marker marker) {
        // 获取传过来的监测点对象
//        Bundle bundle = marker.getExtraInfo();
//        // 获取标签的标题文字
//        String title = marker.getTitle();
        tv_jiancedian_name.setText("河道位置：");
        tv_jiancedian_name_value.setText(xinXiHeCha_0.getReportRvinfoWxAndImgEntity().getRvName());
        tv_jiancedian_type.setText("上报人：");
        tv_jiancedian_type_value.setText(xinXiHeCha_0.getReportRvinfoWxAndImgEntity().getUname());
        tv_xunheshijian.setText("问题描述：");
        if(null != xinXiHeCha_0.getReportRvinfoWxAndImgEntity().getDetail() && xinXiHeCha_0.getReportRvinfoWxAndImgEntity().getDetail().length()>30){
            tv_xunheshijian_value.setText(xinXiHeCha_0.getReportRvinfoWxAndImgEntity().getDetail().substring(0,30));
        }else {
            tv_xunheshijian_value.setText(xinXiHeCha_0.getReportRvinfoWxAndImgEntity().getDetail());
        }

        // 获取位置
        LatLng position = marker.getPosition();
        MapViewLayoutParams layoutParams = new MapViewLayoutParams
                .Builder()
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)
                .position(position)
                .build();
        // 显示View
        popView.setVisibility(View.VISIBLE);
        isShowIng=true;
        // 更新view
        mMapView.updateViewLayout(popView, layoutParams);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(isShowIng){
                    popView.setVisibility(View.GONE);
                    isShowIng=false;
                }else {
                    finish();
                }
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locService.unregisterListener(listener);
        locService.stop();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
        unbinder.unbind();
    }
}
