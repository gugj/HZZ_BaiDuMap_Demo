package com.roch.hzz_baidumap_demo;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
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
import com.roch.hzz_baidumap_demo.entity.LatLonEntity;
import com.roch.hzz_baidumap_demo.entity.LatLonEntityDao;
import com.roch.hzz_baidumap_demo.greendao.GreenDaoManager;
import com.roch.hzz_baidumap_demo.service.LocationService;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/15/015 10:06
 */
public class MainActivity_New extends AppCompatActivity implements SensorEventListener {

    private MapView mMapView = null;
    private TextView tv_cur_location;
    private BaiduMap mBaiduMap;
    private LocationService locService;
    private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>(); // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果

    //以下是实现定位图层相关变量
    private SensorManager mSensorManager;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    //设置GPS定位时方向角度，顺时针0-360---默认为0
    private int mCurrentDirection = 0;
    private Double lastX = 0.0;
    //定位数据
    private MyLocationData locData;
    private MyLocationData.Builder locDataBuilder=new MyLocationData.Builder();
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    LatLonEntityDao latLonEntityDao;
    String    currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "hzz_app-db", null);
//        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        latLonEntityDao = daoSession.getLatLonEntityDao();
        latLonEntityDao = GreenDaoManager.getInstance().getSession().getLatLonEntityDao();

        SimpleDateFormat formatter =  new SimpleDateFormat ("yyyy-MM-dd");
        Date curDate  = new Date(System.currentTimeMillis());//获取当前时间
        currentTime  =  formatter.format(curDate);
        LogUtil.println("当前的日期时间为：==="+currentTime);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        tv_cur_location = (TextView) findViewById(R.id.tv_cur_location);
        //查找MapView
        mMapView = (MapView) findViewById(R.id.bmapView);
        //获取BaiduMap
        mBaiduMap = mMapView.getMap();
        //设置地图类型---普通
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设置缩放级别
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));

        // 开启定位图层
//        mBaiduMap.setMyLocationEnabled(true);

        locService = ((MyApplication) getApplication()).locationService;
        LocationClientOption mOption = locService.getDefaultLocationClientOption(); //此时已经配置过定位参数，直接返回过来
//        locService.setLocationOption(mOption); //此时又重复配置了定位参数
        mOption.setCoorType("bd09ll");
        locService.setLocationOption(mOption);
        locService.registerListener(listener);
        locService.start();
    }

    /**
     * 百度地图添加覆盖物时覆盖物所有基本信息的载体
     */
    MarkerOptions markerOptions = new MarkerOptions();
    /**
     * 百度地图添加覆盖物时覆盖物图片的载体
     */
    BitmapDescriptor bitmapDescptor;
    private PolylineOptions polylineOptions=new PolylineOptions();

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
//        ToastUtil.show("绘制轨迹完成");
        }

//        LatLonEntity unique = latLonEntityDao.queryBuilder()
//                .where(LatLonEntityDao.Properties.Time.eq(currentTime))
//                .build()
//                .unique();
//        latLonEntityDao.update(unique);
        //删除给定的实体类集合
//        latLonEntityDao.deleteInTx(latLonEntities);

        //绘制定位的轨迹
//        if(loc_Points.size()>2){
//            mBaiduMap.clear();
//            //添加当前位置的覆盖物
//            addCurrentLocationOverlay();
//
//            LogUtil.println("定位轨迹点数为=="+loc_Points.size()+"---开始绘制轨迹");
//            OverlayOptions ooPolyline_guiji = polylineOptions
//                    .width(10)
//                    .color(Integer.valueOf(Color.RED))
////                .colorsValues(colors0)
//                    .points(loc_Points);
//            mBaiduMap.addOverlay(ooPolyline_guiji);
//        }

        //添加普通折线绘制
//        LatLng pt1 = new LatLng(39.93923, 116.357428);
//        LatLng pt2 = new LatLng(39.91923, 116.327428);
//        LatLng pt3 = new LatLng(39.89923, 116.347428);
//        LatLng pt4 = new LatLng(39.89923, 116.367428);
//        LatLng pt5 = new LatLng(39.91923, 116.387428);
//        List<LatLng> points0 = new ArrayList<LatLng>();
//        List<Integer> colors0 = new ArrayList<>();
//        colors0.add(Integer.valueOf(Color.GREEN));
//        points0.add(pt1);
//        points0.add(pt2);
//        points0.add(pt3);
//        points0.add(pt4);
//        points0.add(pt5);
//        OverlayOptions ooPolyline0 = new PolylineOptions()
//                .width(10)
//                .color(Integer.valueOf(Color.GREEN))
////                .colorsValues(colors0)
//                .points(points0);
//        mBaiduMap.addOverlay(ooPolyline0);

        // 添加分段折线颜色绘制
//        List<LatLng> points = new ArrayList<LatLng>();
//        points.add(new LatLng(39.965,116.404));
//        points.add(new LatLng(39.925,116.454));
//        points.add(new LatLng(39.955,116.494));
//        points.add(new LatLng(39.905,116.554));
//        points.add(new LatLng(39.965,116.604));
        //构建分段颜色索引数组
//        List<Integer> colors = new ArrayList<>();
//        colors.add(Integer.valueOf(Color.BLUE));
//        colors.add(Integer.valueOf(Color.RED));
//        colors.add(Integer.valueOf(Color.YELLOW));
//        colors.add(Integer.valueOf(Color.GREEN));
//        OverlayOptions ooPolyline = new PolylineOptions()
//                .width(10)
//                .colorsValues(colors)
//                .points(points);
//        Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);

        // 添加文字覆盖物
//        LatLng llText = new LatLng(mCurrentLat, mCurrentLon);
//        OverlayOptions ooText = new TextOptions()
//                .bgColor(0xAAFFFF00)
//                .fontSize(24)
//                .fontColor(0xFFFF00FF)
//                .text("百度地图SDK")
//                .rotate(-30)
//                .position(llText);
//        mBaiduMap.addOverlay(ooText);

//        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                Toast.makeText(MainActivity_New.this, "点击了当前位置：" + marker.getPosition().latitude + "," + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
    }

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

    boolean isFirstLoadLocData=true;
    private List<LatLng> loc_Points=new ArrayList<>();
    private List<Double> loc_Lats=new ArrayList<>();
    private List<Double> loc_Lons=new ArrayList<>();
    /***
     * 定位结果回调，在此方法中处理定位结果
     */
    BDLocationListener listener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // 定位结果描述：server定位失败，没有对应的位置信息
            if (location == null || mMapView == null ) { // || location.getLocType() == BDLocation.TypeServerError
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
                LogUtil.println("当前为GPS定位，定位位置=="+location.getAddrStr()
//                        +",GPS质量=="+location.getGpsAccuracyStatus()
                        +"卫星数目=="+location.getSatelliteNumber()+"速度=="+location.getSpeed());
            }

            String addrStr=location.getAddrStr(); //定位的详细位置
            tv_cur_location.setText("当前定位位置："+addrStr);

            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            loc_Lats.add(mCurrentLat);
            loc_Lons.add(mCurrentLon);
            if(loc_Lats.size()>2 && loc_Lons.size()>2){
                double d1=loc_Lats.get(loc_Lats.size()-1);
                double d2=loc_Lats.get(loc_Lats.size()-2);
                double d3=loc_Lons.get(loc_Lons.size()-1);
                double d4=loc_Lons.get(loc_Lons.size()-2);
                if(d1!=d2 && d3!=d4){
                    LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
                    loc_Points.add(ll);
                    LatLonEntity latLonEntity=new LatLonEntity(null,mCurrentLat,mCurrentLon,currentTime);
                    latLonEntityDao.insert(latLonEntity);
                    LogUtil.println("当前定位点插入数据库GreenDAO成功！！！！！！！");
                }else {
                    LogUtil.println("--------当前定位的两个点重复,删除掉---------");
                }
            }

            // 1、设置定位层数据---显示小蓝点---跟随定位位置移动
            if(isFirstLoadLocData){
                isFirstLoadLocData=false;
                //添加当前位置的覆盖物
                addCurrentLocationOverlay();
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
            LogUtil.println("定位成功：地址===" + location.getAddrStr() + ",经纬度===" + mCurrentLat + "," + mCurrentLon);

            // 2、以动画方式更新地图状态---更新到当前定位位置---没有设置缩放级别
            if(loc_Points.size()>2){
                LogUtil.println("定位轨迹点数为=="+loc_Points.size()+"---开始矫正轨迹");
                LatLng point1 = loc_Points.get(loc_Points.size()-1); //倒数第一个定位点
                LatLng point2 = loc_Points.get(loc_Points.size()-2); //倒数第二个定位点
                double distance=DistanceUtil.getDistance(point1,point2);
                if(distance<10 || distance>100){
                    LogUtil.println("两个定位点的距离小于10m或者大于100m，删除掉");
                    loc_Points.remove(loc_Points.size()-1);
                }else {
                    //绘制定位的轨迹
                    huiZhiGuiJi();
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

//        @Override
//        public void onConnectHotSpotMessage(String s, int i) {
//
//        }
    };

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//		WriteLog.getInstance().close();
        locService.unregisterListener(listener);
        locService.stop();
        mMapView.onDestroy();
        mMapView = null;
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
        //绘制定位的轨迹
        huiZhiGuiJi();
    }

    @Override
    protected void onPause() {
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

    /**
     * 封装定位结果和时间的实体类
     * @author baidu
     */
    class LocationEntity {
        BDLocation location;
        long time;
    }

}
