package com.roch.hzz_baidumap_demo.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.roch.hzz_baidumap_demo.KaiShiXunHeActivity;
import com.roch.hzz_baidumap_demo.MyApplication;
import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.entity.LatLonEntity;
import com.roch.hzz_baidumap_demo.entity.LatLonEntityDao;
import com.roch.hzz_baidumap_demo.entity.XunHeJiLu_Result;
import com.roch.hzz_baidumap_demo.greendao.GreenDaoManager;
import com.roch.hzz_baidumap_demo.service.LocationService;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.SharePreferencesUtil;
import com.roch.hzz_baidumap_demo.utils.StringUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements SensorEventListener{

    @BindView(R.id.bmapView)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationService locService;

    //定位数据
    private MyLocationData locData;
    private MyLocationData.Builder locDataBuilder=new MyLocationData.Builder();
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    //设置GPS定位时方向角度，顺时针0-360---默认为0
    private int mCurrentDirection = 0;
    private Double lastX = 0.0;
    /**
     * 百度地图添加覆盖物时覆盖物所有基本信息的载体
     */
    MarkerOptions markerOptions = new MarkerOptions();
    /**
     * 百度地图添加覆盖物时覆盖物图片的载体
     */
    BitmapDescriptor bitmapDescptor;
    private PolylineOptions polylineOptions=new PolylineOptions();
    //以下是实现定位图层相关变量
    private SensorManager mSensorManager;
    private MyLocationConfiguration.LocationMode mCurrentMode;

    LatLonEntityDao latLonEntityDao;

    @BindView(R.id.tv_cur_location)
    TextView tv_cur_location;
    String currentTime;
    /**
     * 自定义的接收开始巡河、结束巡河的广播
     */
    private MyBroadCastReceiver myBroadCastReceiver;

    /**
     * 1.获取该Fragment的布局View
     * @param mActivity
     * @return
     */
    @Override
    protected View getContenView(Activity mActivity) {
        return View.inflate(mActivity, R.layout.activity_main,null);
    }

    /**
     * 2.初始化百度地图参数---1.地图类型；2.缩放级别；3.开启定位图层
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
     * 3.初始化数据
     */
    @Override
    protected void initData() {
        //2.初始化百度地图参数---1.地图类型；2.缩放级别；3.开启定位图层
        initMapConfig();
        //获取数据库操作对象
        latLonEntityDao = GreenDaoManager.getInstance().getSession().getLatLonEntityDao();
        //获取当前时间
        currentTime= StringUtil.getCurrentlyDate();
        //获取传感器管理服务
        mSensorManager = (SensorManager)mActivity.getSystemService(mActivity.SENSOR_SERVICE);
        //3-0.初始化百度地图定位配置参数、设置定位监听、开始定位
        initLocationConfig();
    }

    /**
     * 3-0.初始化百度定位配置参数、设置定位监听、开始定位
     */
    private void initLocationConfig() {
        locService = ((MyApplication) mActivity.getApplication()).locationService;
//        LocationClientOption mOption = locService.getDefaultLocationClientOption(); //此时已经配置过定位参数，直接返回过来
//        locService.setLocationOption(mOption); //此时又重复配置了定位参数
//        mOption.setCoorType("bd09ll");
//        locService.setLocationOption(mOption);
        locService.registerListener(listener);

        myBroadCastReceiver = new MyBroadCastReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Common.Start_End_XunHe_BroadCast);
        mActivity.registerReceiver(myBroadCastReceiver, filter);
        locService.start();
    }

    /**
     * 自定义的接收开始巡河、结束巡河的广播
     */
    class MyBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Common.Start_End_XunHe_BroadCast.equals(intent.getAction())){
                if (intent.getBooleanExtra(Common.INTENT_KEY,true)){
                    ShowToast("开始巡河");
                    LogUtil.println("开始巡河---开始定位");
                    SharePreferencesUtil.saveStartSavePointsAndGuiJi(mActivity,true);
                    // 开始定位、注册定位监听
                    startLocationAndListener();
                }else {
                    LogUtil.println("结束巡河---结束定位");
                    SharePreferencesUtil.saveStartSavePointsAndGuiJi(mActivity,false);
                    // 提交巡河轨迹到服务器---结束巡河
                    commitXunHeGuiJiToServer();
                }
            }
        }
    }

    /**
     * 网络请求的参数---键值对
     */
    private Map<String,String> params=new HashMap<>();

    /**
     * 提交巡河轨迹到服务器---结束巡河
     */
    private void commitXunHeGuiJiToServer() {
        // 河道ID
       String heDaoId=SharePreferencesUtil.getXunShiHeDaoId(mActivity);
        // 巡河人员ID
       String userId=((KaiShiXunHeActivity) mActivity).getUserId();
        // 上报时间
        String time=StringUtil.getCurrentlyDateTime();
        // 巡河轨迹的点
        String locations="";
        LogUtil.println("保存在手机数据库中，巡河轨迹点的数量：==="+loc_Points.size());
//        for (int i = 0; i < loc_Points.size(); i++) {
//            LatLng latLng = loc_Points.get(i);
//            if(i != loc_Points.size()-1){
//                locations=locations+latLng.longitude+","+latLng.latitude+",";
//            }else {
//                locations=locations+latLng.longitude+","+latLng.latitude;
//            }
//        }
        for (int i = 0; i < loc_Points_test.size(); i++) {
            LatLng latLng = loc_Points_test.get(i);
            if(i != loc_Points_test.size()-1){
                locations=locations+latLng.longitude+","+latLng.latitude+",";
            }else {
                locations=locations+latLng.longitude+","+latLng.latitude;
            }
        }
        if(StringUtil.isEmpty(locations)){
            ShowToast("巡河轨迹点为0，请走动一下，重新定位");
        }
        params.put("userid",userId);
        params.put("rvId",heDaoId);
        params.put("ckTime",time);
        params.put("locations",locations);
        MyApplication.getInstance().getHttpUtilsInstance().post(this, URLs.Commit_XunHe_GuiJi, params, null, MyConstans.FIRST);
    }

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        LogUtil.println("巡河成功：===" + str);
        try {
            JSONArray array = new JSONArray(str);
            JSONObject jsonObject = array.getJSONObject(0);
            String str1 = jsonObject.toString();

            XunHeJiLu_Result xunHeJiLu_result = XunHeJiLu_Result.parseToT(str1, XunHeJiLu_Result.class);
            if (StringUtil.isNotEmpty(xunHeJiLu_result) && xunHeJiLu_result.getSuccess()) {
                ShowToast("巡河成功");
                // 清除开始巡河按钮的选择背景
//                ((KaiShiXunHeActivity) mActivity).getKaiShiXunHeView().setBackgroundResource(R.drawable.bg_select_no);
                Drawable topDrawable = getResources().getDrawable(R.drawable.start_normal);
                topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                ((TextView)((KaiShiXunHeActivity) mActivity).getKaiShiXunHeView()).setCompoundDrawables(null, topDrawable, null, null);
                ((TextView)((KaiShiXunHeActivity) mActivity).getKaiShiXunHeView()).setTextColor(getResources().getColor(R.color.black));
                //清除数据库中储存的定位轨迹点
                clearDBPoints();
                // 保存状态
                SharePreferencesUtil.saveIsStartXunHe(mActivity, false);
                SharePreferencesUtil.saveXunShiHeDaoId(mActivity, "");
                // 停止定位、取消定位监听
                stopLocationAndListener();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始定位、注册定位监听
     */
    private void startLocationAndListener() {
        locService.registerListener(listener);
        locService.start();
    }
    /**
     * 停止定位、取消定位监听
     */
    private void stopLocationAndListener() {
        locService.unregisterListener(listener);
        locService.stop();
    }

    @Override
    public void onFaileResult(String str, int flag) {
        super.onFaileResult(str, flag);
        ShowToast("巡河失败");
        LogUtil.println("巡河失败：==="+str);
    }

    /**
     * 清除数据库中储存的定位轨迹点
     */
    private void clearDBPoints() {
        latLonEntityDao.deleteAll();
        LogUtil.println("清除数据库中储存的定位轨迹点成功");
    }

    private boolean isFirstLoadLocData=true;
    /**
     * 定位轨迹点的集合----会筛选：1.定位的两个点不一样；2.定位的两个点距离有效----不符合会删除
     */
    private List<LatLng> loc_Points=new ArrayList<>();
    private List<LatLng> loc_Points_test=new ArrayList<>();
    private List<Double> loc_Lats=new ArrayList<>();
    private List<Double> loc_Lons=new ArrayList<>();
    private double lastLocLat=0.0;
    private double lastLocLon=0.0;
    private boolean isFirstLatLon=true;
    Random random=new Random();

    /***
     * 定位结果回调，在此方法中处理定位结果
     */
    BDLocationListener listener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // 定位结果描述：server定位失败，没有对应的位置信息
            if (location == null ) { // || mMapView == null
                LogUtil.println("定位结果描述：server定位失败，没有对应的位置信息");
                return;
            }
            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                LogUtil.println("当前为网络定位,定位位置=="+location.getAddrStr()+",运营商信息=="+location.getOperators());
            }
            if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                LogUtil.println("当前为离线定位，定位位置=="+location.getAddrStr());
            }
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                LogUtil.println("当前为GPS定位，定位位置=="+location.getAddrStr() +"卫星数目=="+location.getSatelliteNumber()+"速度=="+location.getSpeed());
            }

            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();

            String addrStr=location.getAddrStr(); //定位的详细位置
            tv_cur_location.setText("当前定位位置：" + addrStr + ",经纬度为：" + mCurrentLat + "," + mCurrentLon);

            if(SharePreferencesUtil.getStartSavePointsAndGuiJi(mActivity)){
                if(isFirstLatLon){
                    isFirstLatLon=false;
                    lastLocLat=mCurrentLat;
                    lastLocLon=mCurrentLon;
                }else {
                    lastLocLat+=0.001*random.nextInt(5);
                    lastLocLon+=0.001*random.nextInt(5);
                }

                loc_Points_test.add(new LatLng(lastLocLat, lastLocLon));
                huiZhiGuiJi_Test();

                loc_Lats.add(mCurrentLat);
                loc_Lons.add(mCurrentLon);
                if(loc_Lats.size()>2 && loc_Lons.size()>2){
                    double d1=loc_Lats.get(loc_Lats.size()-1);
                    double d2=loc_Lats.get(loc_Lats.size()-2);
                    double d3=loc_Lons.get(loc_Lons.size()-1);
                    double d4=loc_Lons.get(loc_Lons.size()-2);

                    // 不经过过滤直接将定位点插入数据库
//                    LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
//                    loc_Points.add(ll);
//                    LatLonEntity latLonEntity=new LatLonEntity(null,mCurrentLat,mCurrentLon,currentTime);
//                    latLonEntityDao.insert(latLonEntity);
//                    LogUtil.println("当前定位点插入数据库GreenDAO成功！！！！！！！");

                    if(d1!=d2 && d3!=d4){
                        LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
                        loc_Points.add(ll);
                    }else {
                        LogUtil.println("--------当前定位的两个点重复,删除掉---------");
                    }
                }
            }

            // 1、设置定位层数据---显示小蓝点---跟随定位位置移动
            if(isFirstLoadLocData){
                isFirstLoadLocData=false;
                //添加当前位置的覆盖物
//                addCurrentLocationOverlay();
                locData = locDataBuilder
                        .accuracy(mCurrentAccracy) //设置定位精度
                        .direction(mCurrentDirection) //设置GPS定位时方向角度，顺时针0-360
                        .latitude(mCurrentLat)
                        .longitude(mCurrentLon)
                        .build();
                //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
                mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
                //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
                mBaiduMap.setMyLocationData(locData);

                //给百度地图设置中心点和缩放级别
                setCenterAndZoom();
            }
            LogUtil.println("开始巡河页面---定位成功：地址===" + location.getAddrStr() + ",经纬度===" + mCurrentLat + "," + mCurrentLon);
            Common.mCurrentLat=mCurrentLat;
            Common.mCurrentLon=mCurrentLon;
            Common.mCurrentLocationAddr=addrStr;

            // 2、以动画方式更新地图状态---更新到当前定位位置---没有设置缩放级别
            if(SharePreferencesUtil.getStartSavePointsAndGuiJi(mActivity)){
                if(loc_Points.size()>2){
                    LogUtil.println("定位轨迹点数为=="+loc_Points.size()+"---开始矫正轨迹");
                    LatLng point1 = loc_Points.get(loc_Points.size()-1); //倒数第一个定位点
                    LatLng point2 = loc_Points.get(loc_Points.size()-2); //倒数第二个定位点
                    double distance= DistanceUtil.getDistance(point1, point2);
                    if(distance<10 || distance>500){ // 定位点无效
                        LogUtil.println("两个定位点的距离小于10m或者大于500m，删除掉");
                        loc_Points.remove(loc_Points.size()-1);
                    }else { // 定位点有效
                        LatLonEntity latLonEntity=new LatLonEntity(null,mCurrentLat,mCurrentLon,currentTime);
                        latLonEntityDao.insert(latLonEntity);
                        LogUtil.println("当前定位点插入数据库GreenDAO成功！！！！！！！");
                        //绘制定位的轨迹
                        huiZhiGuiJi();
                    }
                }
            }

//            MapStatus.Builder builder = new MapStatus.Builder();
//            builder.target(ll);
            //以动画方式更新地图状态，动画耗时 300 ms
//            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            // 3、添加覆盖物---当前定位位置---图标自定义，然后更新地图状态，
            // 构建Marker图标----定位成功后，添加当前位置的覆盖物
//            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark);
            // 构建MarkerOption，用于在地图上添加Marker
//            OverlayOptions option = new MarkerOptions().position(ll).icon(bitmap);
            // 在地图上添加Marker，并显示
//            mBaiduMap.addOverlay(option);
            //改变地图状态
//            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(ll));

        }
    };

    /**
     * 添加当前位置的覆盖物
     */
    private void addCurrentLocationOverlay() {
        markerOptions.position(new LatLng(mCurrentLat, mCurrentLon));
        bitmapDescptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_current_location);
        markerOptions.icon(bitmapDescptor);
        // 添加灌溉覆盖物
        mBaiduMap.addOverlay(markerOptions);
    }

    /**
     * 给百度地图设置中心点和缩放级别
     */
    private void setCenterAndZoom() {
        // 设置中心点
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(mCurrentLat,mCurrentLon));
        mBaiduMap.setMapStatus(mapStatusUpdate);

        // 设置缩放级别
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 绘制定位的轨迹
     */
    private void huiZhiGuiJi() {
        List<LatLonEntity> latLonEntities = latLonEntityDao.queryBuilder()
                .where(LatLonEntityDao.Properties.Time.eq(currentTime))
                .orderDesc(LatLonEntityDao.Properties.Id)
                .build()
                .list();
        if(null!=latLonEntities && latLonEntities.size()>2){
            LogUtil.println("通过数据库的存储定位点，开始画轨迹++++++++++");
            loc_Points.clear();
            for (LatLonEntity latLngEntity:latLonEntities){
                loc_Points.add(new LatLng(latLngEntity.getLatitude(),latLngEntity.getLontitude()));
            }
            mBaiduMap.clear();
            //添加当前位置的覆盖物
            addCurrentLocationOverlay();

            OverlayOptions ooPolyline_guiji = polylineOptions
                    .width(10)
                    .color(Integer.valueOf(Color.RED))
//                .colorsValues(colors0)
                    .points(loc_Points);
            mBaiduMap.addOverlay(ooPolyline_guiji);
        }
    }

    private void huiZhiGuiJi_Test(){
        if(loc_Points_test.size()>=2){
            OverlayOptions ooPolyline_guiji = polylineOptions
                    .width(10)
                    .color(Integer.valueOf(Color.RED))
//                .colorsValues(colors0)
                    .points(loc_Points_test);
            mBaiduMap.addOverlay(ooPolyline_guiji);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        mContext.unregisterReceiver(myBroadCastReceiver);
//        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
        //绘制定位的轨迹----在这绘制轨迹，容易报错，会蹦
//        huiZhiGuiJi();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = locDataBuilder
                    .accuracy(mCurrentAccracy) //设置定位精度
                    .direction(mCurrentDirection) //设置GPS定位时方向角度，顺时针0-360
                    .latitude(mCurrentLat)
                    .longitude(mCurrentLon)
                    .build();
            //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
