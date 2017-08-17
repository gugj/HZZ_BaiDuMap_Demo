package com.roch.hzz_baidumap_demo;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;

public class MainActivity extends AppCompatActivity {

    /**
     * 百度地图的MapView
     */
    private MapView mMapView;
    /**
     * 百度地图的控制类BaiduMap
     */
    private BaiduMap mBaiduMap;
    /**
     * 百度地图SDK的广播监听
     */
//    private SDKReceiver mReceiver;
    /**
     * 百度地图定位客户端
     */
    LocationClient mLocClient;
    /**
     * 自定义的百度定位的监听者
     */
//    public MyLocationListenner myListener = new MyLocationListenner();
    /**
     * 当前定位位置的维度
     */
    private double mCurrentLat = 0.0;
    /**
     * 当前定位位置的经度
     */
    private double mCurrentLon = 0.0;
    /**
     * 当前定位的精准度
     */
    private float mCurrentAccracy;
    /**
     * 百度定位的位置数据
     */
    private MyLocationData locData;
    /**
     * 当前开发者获取到的方向信息---默认为 0
     */
    private int mCurrentDirection = 0;
    /**
     * 是否首次定位
     */
    boolean isFirstLoc = true;
    /**
     * 传感器管理服务
     */
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //初始化查找View
//        initView();
//        //初始化百度地图的配置
//        initBaiduMapConfig();
//        //初始化百度地图定位参数
//        initBaiduLocation();
//        //注册百度地图SDK的广播监听
//        registerBaiduSDKReceiver();
    }

//    /**
//     * 初始化百度地图定位参数
//     */
//    private void initBaiduLocation() {
//        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
//        // 定位初始化
//        mLocClient = new LocationClient(this);
//        mLocClient.registerLocationListener(myListener);
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true); // 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocClient.setLocOption(option);
//        mLocClient.start();
//    }
//
//    /**
//     * 注册百度地图SDK的广播监听
//     */
//    private void registerBaiduSDKReceiver() {
//        // 注册 SDK 广播监听者
//        IntentFilter iFilter = new IntentFilter();
//        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
//        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
//        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
//        mReceiver = new SDKReceiver();
//        registerReceiver(mReceiver, iFilter);
//    }
//
//    private Double lastX = 0.0;
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        double x = sensorEvent.values[SensorManager.DATA_X];
//        if (Math.abs(x - lastX) > 1.0) {
//            mCurrentDirection = (int) x;
//            locData = new MyLocationData.Builder()
//                    .accuracy(mCurrentAccracy) //设置定位的精准度
//                    .direction(mCurrentDirection) //此处设置开发者获取到的方向信息，顺时针0-360
//                    .latitude(mCurrentLat)
//                    .longitude(mCurrentLon)
//                    .build();
//            mBaiduMap.setMyLocationData(locData);
//        }
//        lastX = x;
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
//    /**
//     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
//     */
//    public class SDKReceiver extends BroadcastReceiver {
//        public void onReceive(Context context, Intent intent) {
//            String s = intent.getAction();
//            String result="";
//            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
//                result="key 验证出错! 错误码 :" + intent.getIntExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0) + " ; 请在 AndroidManifest.xml 文件中检查 key 设置";
//            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
//                result="key 验证成功! 功能可以正常使用";
//            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
//                result="网络出错";
//            }
//            if(!"".equals(result)){
//                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
//                LogUtil.println(result);
//            }
//        }
//    }
//
//    /**
//     * 初始化查找View
//     */
//    private void initView() {
//        mMapView = (MapView) findViewById(R.id.mapView);
//        mBaiduMap = mMapView.getMap();
//    }
//
//    /**
//     * 初始化百度地图的配置
//     */
//    private void initBaiduMapConfig() {
//        //普通地图
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//        //卫星地图
////        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
//        //空白地图, 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
////        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
//
//        //开启交通图
//        mBaiduMap.setTrafficEnabled(true);
//        // 开启定位图层
//        mBaiduMap.setMyLocationEnabled(true);
//    }
//
//    /**
//     * 自定义的百度定位的监听者
//     */
//    public class MyLocationListenner implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            // map view 销毁后不在处理新接收的位置
//            if (location == null || mMapView == null) {
//                return;
//            }
//            mCurrentLat = location.getLatitude();
//            mCurrentLon = location.getLongitude();
//            mCurrentAccracy = location.getRadius();
//            locData = new MyLocationData.Builder()
//                    .accuracy(mCurrentAccracy) //设置定位的精准度
//                    .direction(mCurrentDirection) // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .latitude(mCurrentLat)
//                    .longitude(mCurrentLon)
//                    .build();
//            mBaiduMap.setMyLocationData(locData);
//            if (isFirstLoc) {
//                isFirstLoc = false;
////                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
////                MapStatus.Builder builder = new MapStatus.Builder();
////                builder.target(ll).zoom(18.0f);
////                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//
//                mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
//                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(ll).zoom(18.0f);
//                builder.overlook(0);
//                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//            }
//        }
//
//        @Override
//        public void onConnectHotSpotMessage(String s, int i) {
//
//        }
//
//        public void onReceivePoi(BDLocation poiLocation) {
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        mMapView.onResume();
//        super.onResume();
//        //为系统的方向传感器注册监听器
//        mSensorManager.registerListener( this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
//    }
//
//    @Override
//    protected void onStop() {
//        //取消注册传感器监听
//        mSensorManager.unregisterListener(this);
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // 退出时销毁定位
//        mLocClient.stop();
//        // 关闭定位图层
//        mBaiduMap.setMyLocationEnabled(false);
//        mMapView.onDestroy();
//        mMapView = null;
//        // 取消监听 SDK 广播
//        unregisterReceiver(mReceiver);
//    }

}
