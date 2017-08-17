package com.roch.hzz_baidumap_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.entity.XunHeJiLu;
import com.roch.hzz_baidumap_demo.entity.XunHeJiLu_Result;
import com.roch.hzz_baidumap_demo.entity.XunHeShangBao_Marker;
import com.roch.hzz_baidumap_demo.entity.XunHeShangBao_Marker_Result;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.StringUtil;
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
 * 巡河轨迹详情Activity
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 11:42
 */
public class XunHeGuiJiAcitivty extends CommonBaseActivity implements BaiduMap.OnMarkerClickListener {

    @BindView(R.id.bmapView)
    MapView mMapView;
    /**
     * 网络请求的参数---键值对
     */
    private Map<String, String> params = new HashMap<>();
    private List<XunHeJiLu> xunHeJiLus;
    private List<XunHeShangBao_Marker> xunHeShangBao_markers;

    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private PolylineOptions polylineOptions=new PolylineOptions();
    /**
     * 巡河轨迹点的集合
     */
    private List<LatLng> loc_Points=new ArrayList<>();
    /**
     * 巡河时信息上报点的集合
     */
    private List<LatLng> xhsb_Points=new ArrayList<>();
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
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_xunhe_guiji;
    }

    /**
     * 4.初始化数据
     */
    @Override
    public void initData() {
        //4-0.初始化百度地图参数---1.地图类型；2.缩放级别
        initMapConfig();

        XunHeJiLu xunHeJiLu= (XunHeJiLu) intent.getSerializableExtra("xunhejilu");
        if(StringUtil.isNotEmpty(xunHeJiLu)){
            params.put("id",xunHeJiLu.getId());
        }
        MyApplication.getInstance().getHttpUtilsInstance().post(XunHeGuiJiAcitivty.this, URLs.XunHe_GuiJi_Detail, params, null, MyConstans.FIRST);
        System.out.println("巡河轨迹页面请求巡河轨迹的网址为：==" + URLs.XunHe_GuiJi_Detail + "?&id=" + xunHeJiLu.getId());
    }

    /**
     * 请求巡河上报点的数据
     */
    private void requestXunHeShangBaoDian(){
        MyApplication.getInstance().getHttpUtilsInstance().post(XunHeGuiJiAcitivty.this, URLs.XunHe_ShangBao_Marker, params, null, MyConstans.SECOND,false);
        System.out.println("巡河轨迹页面请求巡河时信息上报点的网址为：==" + URLs.XunHe_ShangBao_Marker);
    }

    /**
     * 4-0.初始化百度地图参数---1.地图类型；2.缩放级别
     */
    private void initMapConfig() {
        //获取BaiduMap
        mBaiduMap = mMapView.getMap();
        //设置地图类型---普通
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设置缩放级别
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //设置定位图层模式---正常
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_xiangxi: // 点击详情----将选中的巡河上报点的marker传过去
                Intent intent=new Intent(this,XunHeShangBaoDian_Detail_Activity.class);
                intent.putExtra("xunHeShangBao_marker",select_xunHeShangBao_marker);
                intent.putExtra(Common.TITLE_KEY,"巡河信息上报详情");
                startActivity(intent);
                break;
        }
    }

    private boolean loadedGuiji=false;
    private boolean loadedXinXiShangBao=false;

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        switch (flag) {
            case MyConstans.FIRST:
                LogUtil.println("请求巡河轨迹成功:===" + str);
                loadedGuiji=true;
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    XunHeJiLu_Result xunHeJiLu_result = XunHeJiLu_Result.parseToT(str1, XunHeJiLu_Result.class);
                    if (StringUtil.isNotEmpty(xunHeJiLu_result) && xunHeJiLu_result.getSuccess()) { //json--success:true
                        xunHeJiLus = xunHeJiLu_result.getJsondata();
                        if (StringUtil.isNotEmpty(xunHeJiLus) && xunHeJiLus.size() > 0) {//返回的集合有数据
                            for (XunHeJiLu xunHeJiLu: xunHeJiLus) {
                                String location = xunHeJiLu.getLocations();
                                if(StringUtil.isEmpty(location))continue;
                                String[] split = location.split(",");
                                if(split.length<2)continue;
                                LogUtil.println("服务器中储存的---巡河轨迹点---为==="+split.length/2);
                                for (int i = 0; i < split.length/2; i++) {
                                    loc_Points.add(new LatLng(Double.parseDouble(split[2 * i + 1]), Double.parseDouble(split[2 * i])));
                                }
//                                loc_Points.add(new LatLng(Double.parseDouble(split[1]),Double.parseDouble(split[0])));
                            }
//                            if(loc_Points.size()>=2){
//                                LogUtil.println("转化为坐标对象后---巡河轨迹点---集合为==="+loc_Points.size());
//                                //绘制巡河记录的轨迹
//                                huiZhiGuiJi();
//                            }
                        }
                    }else { //json---success:false
                        ShowToast("服务器网络异常");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 请求巡河上报点的数据
                requestXunHeShangBaoDian();
                break;

            case MyConstans.SECOND:
                LogUtil.println("请求巡河时信息上报点成功:===" + str);
                loadedXinXiShangBao=true;
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    XunHeShangBao_Marker_Result xunHeShangBao_marker_result = XunHeShangBao_Marker_Result.parseToT(str1, XunHeShangBao_Marker_Result.class);
                    if (StringUtil.isNotEmpty(xunHeShangBao_marker_result) && xunHeShangBao_marker_result.getSuccess()) { //json--success:true
                        xunHeShangBao_markers = xunHeShangBao_marker_result.getJsondata();
                        if (StringUtil.isNotEmpty(xunHeShangBao_markers) && xunHeShangBao_markers.size() > 0) {//返回的集合有数据
                            LogUtil.println("服务器中存储的***巡河上报点***为==="+xunHeShangBao_markers.size());
                            for (XunHeShangBao_Marker xunHeShangBao_marker: xunHeShangBao_markers) {
                                String location = xunHeShangBao_marker.getLocation();
                                if(StringUtil.isEmpty(location))continue;
                                String[] split = location.split(",");
                                if(split.length<2)continue;
                                xhsb_Points.add(new LatLng(Double.parseDouble(split[1]),Double.parseDouble(split[0])));
                            }
                            LogUtil.println("转化为坐标对象后***巡河上报点***集合为==="+xhsb_Points.size());
                            //绘制巡河记录的轨迹
                            huiZhiGuiJi();
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
                ShowToast("请求巡河轨迹失败");
                LogUtil.println("请求巡河轨迹失败:===" + str);
                loadedGuiji=true;
                // 请求巡河上报点的数据
                requestXunHeShangBaoDian();
                break;

            case MyConstans.SECOND:
                ShowToast("请求巡河时信息上报点失败");
                LogUtil.println("请求巡河时信息上报点失败:==="+str);
                break;
        }
    }

    /**
     * 绘制巡河记录的轨迹
     */
    private void huiZhiGuiJi() {
        if(loadedGuiji && loadedXinXiShangBao){
            mBaiduMap.clear();

            LatLng startLatLng = null;
            LatLng endLatLng = null;
            if(xhsb_Points.size()>0){
                LogUtil.println("根据巡河上报点-----给百度地图设置中心点和缩放级别");
                //给百度地图设置中心点和缩放级别
                setCenterAndZoom(xhsb_Points.get(0));
            }else if(loc_Points.size()>=2){
                startLatLng = loc_Points.get(0);
                endLatLng = loc_Points.get(loc_Points.size()-1);
                LogUtil.println("根据巡河轨迹点-----给百度地图设置中心点和缩放级别");
                //给百度地图设置中心点和缩放级别
                if(null != startLatLng){
                    setCenterAndZoom(startLatLng);
                }
            }

            if(null != startLatLng && null != endLatLng){
                //添加开始-结束巡河轨迹的覆盖物
                addStartEndLocationOverlay(startLatLng,endLatLng);
            }
            // 添加巡河上报点覆盖物
            addXunHeShangBaoDianOverlay();

            if(loc_Points.size()>=2){
                bundle=new Bundle();
                bundle.putString("markerType", "guiji");
                OverlayOptions ooPolyline_guiji = polylineOptions
                        .extraInfo(bundle)
                        .width(10)
                        .color(0xFF4169E1) // #FF3030  Integer.valueOf(Color.RED)   FF0000
//                .colorsValues(colors0)
                        .points(loc_Points);
                mBaiduMap.addOverlay(ooPolyline_guiji);
            }

            // 将popView添加到mMapView上，并给baiduMap设置覆盖物的点击监听
            addMarkerClickListener();
        }
    }

    /**
     * 给百度地图设置中心点和缩放级别
     * @param startLatLng
     */
    private void setCenterAndZoom(LatLng startLatLng) {
        // 设置中心点
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(startLatLng);
        mBaiduMap.setMapStatus(mapStatusUpdate);

        // 设置缩放级别
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(14);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    Bundle bundle;
    /**
     * 添加开始-结束巡河轨迹的覆盖物
     * @param startLatLng
     * @param endLatLng
     */
    private void addStartEndLocationOverlay(LatLng startLatLng, LatLng endLatLng) {
        markerOptions.position(startLatLng);
        markerOptions.title("起点");
        bundle=new Bundle();
        bundle.putString("markerType","start_end");
        markerOptions.extraInfo(bundle);
        bitmapDescptor = BitmapDescriptorFactory.fromResource(R.drawable.lvse);
        markerOptions.icon(bitmapDescptor);
        // 添加开始位置覆盖物
        mBaiduMap.addOverlay(markerOptions);

        markerOptions.position(endLatLng);
        markerOptions.title("终点");
        bundle=new Bundle();
        bundle.putString("markerType", "start_end");
        markerOptions.extraInfo(bundle);
        bitmapDescptor = BitmapDescriptorFactory.fromResource(R.drawable.hongse);
        markerOptions.icon(bitmapDescptor);
        // 添加开始位置覆盖物
        mBaiduMap.addOverlay(markerOptions);
    }

    /**
     * 添加巡河时信息上报点覆盖物
     */
    private void addXunHeShangBaoDianOverlay() {
        for (int i = 0; i < xhsb_Points.size(); i++) {
            markerOptions.position(xhsb_Points.get(i));
            markerOptions.title(xunHeShangBao_markers.get(i).getName());
            bundle=new Bundle();
            bundle.putString("markerType", "shangbaodian");
            bundle.putSerializable("xunHeShangBao_marker",xunHeShangBao_markers.get(i));
            markerOptions.extraInfo(bundle);
            if("1".equals(xunHeShangBao_markers.get(i).getFlag())){ // 1 有问题
                bitmapDescptor = BitmapDescriptorFactory.fromResource(R.drawable.hongse_2);
            }else { // 0 没有问题
                bitmapDescptor = BitmapDescriptorFactory.fromResource(R.drawable.lvse_2);
            }
            markerOptions.icon(bitmapDescptor);
            // 掉下动画
            markerOptions.animateType(MarkerOptions.MarkerAnimateType.drop);
            markerOptions.period(15);
            // 添加开始位置覆盖物
            mBaiduMap.addOverlay(markerOptions);
        }
    }

    /**
     * 标记popView是否显示
     */
    private boolean isShowIng=false;
    /**
     * 小弹窗中选中的巡河上报点对象
     */
    XunHeShangBao_Marker select_xunHeShangBao_marker;

    @Override
    public boolean onMarkerClick(Marker marker) {
        // 获取传过来的监测点对象
        Bundle bundle = marker.getExtraInfo();
        String markerType = bundle.getString("markerType");
        if("guiji".equals(markerType)){

        }else if("shangbaodian".equals(markerType)){
            // 获取位置
            LatLng position = marker.getPosition();
            MapViewLayoutParams layoutParams = new MapViewLayoutParams
                    .Builder()
                    .height(MapViewLayoutParams.WRAP_CONTENT)
                    .width(MapViewLayoutParams.WRAP_CONTENT)
                    .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)
                    .position(position)
                    .build();

            select_xunHeShangBao_marker= (XunHeShangBao_Marker) bundle.getSerializable("xunHeShangBao_marker");

            tv_jiancedian_name.setText("河道名称：");
            tv_jiancedian_name_value.setText(select_xunHeShangBao_marker.getRiverName());
            tv_jiancedian_type.setText("问题类型：");
            String qtype="";
            String[] split = select_xunHeShangBao_marker.getQtype().split(",");
            for (int i = 0; i < split.length; i++) {
                if("0".equals(split[i])){
                    qtype+="";
                }
            }
            tv_jiancedian_type_value.setText(select_xunHeShangBao_marker.getQtype());
            tv_xunheshijian.setText("巡河时间：");
            tv_xunheshijian_value.setText(select_xunHeShangBao_marker.getCkTimeStr());

            // 显示View
            popView.setVisibility(View.VISIBLE);
            isShowIng=true;
            // 更新view
            mMapView.updateViewLayout(popView, layoutParams);
        }else {
//            // 获取标签的标题文字
//            String title = marker.getTitle();
//            tv_jiancedian_type_value.setText(title);
//
//            // 获取位置
//            LatLng position = marker.getPosition();
//            MapViewLayoutParams layoutParams = new MapViewLayoutParams
//                    .Builder()
//                    .height(MapViewLayoutParams.WRAP_CONTENT)
//                    .width(MapViewLayoutParams.WRAP_CONTENT)
//                    .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)
//                    .position(position)
//                    .build();
//
//            ShowToast(title);
////            selected_DZDT_All = (DZDT_All) bundle.getSerializable("dzdt_All");
////            tv_jiancedian_name_value.setText(selected_DZDT_All.getMonitorName());
//            tv_jiancedian_name_value.setText("7777");
//            // 显示View
//            popView.setVisibility(View.VISIBLE);
//            isShowIng=true;
//            // 更新view
//            mMapView.updateViewLayout(popView, layoutParams);
        }
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
}
