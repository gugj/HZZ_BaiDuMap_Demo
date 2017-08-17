//package com.roch.hzz_baidumap_demo;
//
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//
//import com.baidu.location.LocationClient;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MyLocationConfiguration;
//import com.baidu.mapapi.map.MyLocationData;
//
///**
// * 作者：GuGaoJie
// * 时间：2017/6/14/014 17:15
// */
//public class MainAcitivity_Location extends AppCompatActivity implements SensorEventListener {
//
//    /**
//     * 百度地图的MapView
//     */
//    private MapView mMapView;
//    /**
//     * 百度地图的控制类BaiduMap
//     */
//    private BaiduMap mBaiduMap;
//    /**
//     * 百度地图定位客户端
//     */
//    LocationClient mLocClient;
//    /**
//     * 自定义的百度定位的监听者
//     */
////    public MyLocationListenner myListener = new MyLocationListenner();
//    /**
//     * 当前定位位置的维度
//     */
//    private double mCurrentLat = 0.0;
//    /**
//     * 当前定位位置的经度
//     */
//    private double mCurrentLon = 0.0;
//    /**
//     * 当前定位的精准度
//     */
//    private float mCurrentAccracy;
//    /**
//     * 百度定位的位置数据
//     */
//    private MyLocationData locData;
//    /**
//     * 当前开发者获取到的方向信息---默认为 0
//     */
//    private int mCurrentDirection = 0;
//    /**
//     * 是否首次定位
//     */
//    boolean isFirstLoc = true;
//    /**
//     * 传感器管理服务
//     */
//    private SensorManager mSensorManager;
//    private MyLocationConfiguration.LocationMode mCurrentMode;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
//        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
//        initView();
//
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
//    private void initView() {
//        mMapView = (MapView) findViewById(R.id.mapView);
//        mBaiduMap = mMapView.getMap();
//    }
//}
