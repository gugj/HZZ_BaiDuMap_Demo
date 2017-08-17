package com.roch.hzz_baidumap_demo.service;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 百度定位的服务类---管理定位、配置定位参数、控制定位开关等
 * @author baidu
 */
public class LocationService {

	/**
	 * 百度定位的客户端
	 */
	private LocationClient client = null;
	/**
	 * 百度定位的客户端的配置信息类
	 */
	private LocationClientOption mOption,DIYoption;
	private Object objLock = new Object();

	/***
	 * 百度定位的服务类的构造方法
	 * @param locationContext
	 */
	public LocationService(Context locationContext){
		synchronized (objLock) {
			if(client == null){
				client = new LocationClient(locationContext);
				client.setLocOption(getDefaultLocationClientOption());
			}
		}
	}
	
	/***
	 * 注册百度定位的监听类---BDLocationListener
	 * @param listener
	 * @return
	 */
	public boolean registerListener(BDLocationListener listener){
		boolean isSuccess = false;
		if(listener != null){
			client.registerLocationListener(listener);
			isSuccess = true;
		}
		return  isSuccess;
	}

	/***
	 * 解除注册百度定位的监听类---BDLocationListener
	 * @param listener
	 * @return
	 */
	public void unregisterListener(BDLocationListener listener){
		if(listener != null){
			client.unRegisterLocationListener(listener);
		}
	}
	
	/***
	 * 设置百度地图定位的客户端的配置信息类---LocationClientOption
	 * @param option
	 * @return isSuccess
	 */
	public boolean setLocationOption(LocationClientOption option){
		boolean isSuccess = false;
		if(option != null){
			if(client.isStarted())
				client.stop();
			DIYoption = option;
			client.setLocOption(option);
		}
		return isSuccess;
	}

	/**
	 * 获取百度地图定位的客户端的配置信息类---LocationClientOption
	 * @return
	 */
	public LocationClientOption getOption(){
		return DIYoption;
	}

	/**
	 * 设置百度地图定位的客户端的配置信息类---LocationClientOption---设置基本定位参数
	 * @return DefaultLocationClientOption
	 */
	public LocationClientOption getDefaultLocationClientOption(){
		if(mOption == null){
//			mOption = new LocationClientOption();
//			mOption.setOpenGps(true); //打开GPRS
//			mOption.setAddrType("all");//返回的定位结果包含地址信息
//			mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
//			mOption.setPriority(LocationClientOption.GpsOnly); // 设置GPS优先
//			mOption.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//			mOption.disableCache(false);//禁止启用缓存定位
//
////			mOption = new LocationClientOption(mOption);
//			mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//		    mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//		    mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
//		    mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
//		    mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//		    mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//		    mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//		    mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//		    mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//		    mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用

			mOption = new LocationClientOption();
			mOption.setOpenGps(true); //打开GPRS--------------------------
			mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
			mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
			mOption.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
			mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
			mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
			mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
			mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
			mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
			mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
			mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
			mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

//			mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
		}
		return mOption;
	}

	/**
	 * 开始定位
	 */
	public void start(){
		synchronized (objLock) {
			if(client != null && !client.isStarted()){
				client.start();
			}
		}
	}

	/**
	 * 停止定位
	 */
	public void stop(){
		synchronized (objLock) {
			if(client != null && client.isStarted()){
				client.stop();
			}
		}
	}
	
//	public boolean requestHotSpotState(){
//		return client.requestHotSpotState();
//	}
	
}
