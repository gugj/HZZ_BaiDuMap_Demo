package com.roch.hzz_baidumap_demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.roch.hzz_baidumap_demo.DaiBanShiXiangAcitivty;
import com.roch.hzz_baidumap_demo.JPushMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			System.out.println("[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				System.out.println("[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...
			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				System.out.println("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				//发送自定义自定义消息---广播
				processCustomMessage(context, bundle);
			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				System.out.println("[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				System.out.println("[MyReceiver] 接收到推送下来的通知的ID: ====" + notifactionId);
				//发送通知---广播
				processCustomNotification(context,bundle);
			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				System.out.println("[MyReceiver] 用户点击打开了通知");
				//打开自定义的Activity
				Intent i = new Intent(context, DaiBanShiXiangAcitivty.class);
				i.putExtras(bundle);
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);
			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				System.out.println("[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				System.out.println("[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				System.out.println("[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) { //发送的通知ID
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key)); //  key:cn.jpush.android.MSG_ID, value:3600297838
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					System.out.println("This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();
					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					System.out.println("Get message extra JSON error!");
				}
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	/**
	 * 发送自定义自定义消息---广播
	 */
	private void processCustomMessage(Context context, Bundle bundle) {
		if (JPushMainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(JPushMainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(JPushMainActivity.KEY_MESSAGE, message);
			if (!"".equals(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(JPushMainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}
	}

	/**
	 * 发送通知---广播
	 */
	private void processCustomNotification(Context context, Bundle bundle) {
		if (JPushMainActivity.isForeground) {
			String Notification_Content = bundle.getString("cn.jpush.android.ALERT");
			String Notification_Title = bundle.getString("cn.jpush.android.NOTIFICATION_CONTENT_TITLE");
			Intent msgIntent = new Intent(JPushMainActivity.NOTIFICATION_RECEIVED_ACTION);
			msgIntent.putExtra(JPushMainActivity.KEY_CONTENT, Notification_Content);
			msgIntent.putExtra(JPushMainActivity.KEY_TITLE, Notification_Title);

			System.out.println("[MyReceiver] 接收到推送下来的通知消息:内容==== " + bundle.getString("cn.jpush.android.ALERT")
					+ "，标题====" + bundle.getString("cn.jpush.android.NOTIFICATION_CONTENT_TITLE"));

			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}
	}

}
