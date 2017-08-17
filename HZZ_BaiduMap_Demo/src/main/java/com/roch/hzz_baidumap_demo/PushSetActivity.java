package com.roch.hzz_baidumap_demo;

import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;
import com.roch.hzz_baidumap_demo.utils.JPushUtil;
import com.roch.hzz_baidumap_demo.utils.Logger;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.MultiActionsNotificationBuilder;
import cn.jpush.android.api.TagAliasCallback;

public class PushSetActivity extends InstrumentedActivity {

	private static final String TAG = "system.out";

	Button mSetTag;
	Button mSetAlias;
	Button mStyleBasic;
	Button mStyleAddActions;
	Button mStyleCustom;
	Button mSetPushTime;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
//		setContentView(R.layout.push_set_dialog);
//		init();
//		initListener();
	}

//	private void init() {
//		mSetTag = (Button) findViewById(R.id.bt_tag);
//		mSetAlias = (Button) findViewById(R.id.bt_alias);
//		mStyleAddActions = (Button) findViewById(R.id.setStyle0);
//		mStyleBasic = (Button) findViewById(R.id.setStyle1);
//		mStyleCustom = (Button) findViewById(R.id.setStyle2);
//		mSetPushTime = (Button) findViewById(R.id.bu_setTime);
//	}

//	private void initListener() {
//		mSetTag.setOnClickListener(this);
//		mSetAlias.setOnClickListener(this);
//		mStyleAddActions.setOnClickListener(this);
//		mStyleBasic.setOnClickListener(this);
//		mStyleCustom.setOnClickListener(this);
//		mSetPushTime.setOnClickListener(this);
//	}

//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//			case R.id.bt_tag: //设置设备标签Tag
//				setTag();
//				break;
//
//			case R.id.bt_alias: //设置设备别名Alias
//				setAlias();
//				break;
//
//			case R.id.setStyle0: //设置定制通知栏样式
//				setAddActionsStyle();
//				break;
//
//			case R.id.setStyle1: //设置定制通知栏样式---Basic
//				setStyleBasic();
//				break;
//
//			case R.id.setStyle2: //设置定制通知栏样式---Custom
//				setStyleCustom();
//				break;
//
//			case R.id.bu_setTime:
////				Intent intent = new Intent(PushSetActivity.this, SettingActivity.class);
////				startActivity(intent);
//				break;
//		}
//	}

	private static PushSetActivity instance=new PushSetActivity();

	public static PushSetActivity getInstance(){
		if(null==instance){
			instance=new PushSetActivity();
		}
		return instance;
	}

	/**
	 * 设置设备标签Tag
	 */
	public void setTag(String tag) {
//		EditText tagEdit = (EditText) findViewById(R.id.et_tag);
//		String tag = tagEdit.getText().toString().trim();

		// 检查 tag 的有效性
		if (TextUtils.isEmpty(tag)) {
			Toast.makeText(PushSetActivity.this, R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
			return;
		}

		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!JPushUtil.isValidTagAndAlias(sTagItme)) {
				Toast.makeText(PushSetActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
				return;
			}
			tagSet.add(sTagItme);
		}

		//调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
	}

	/**
	 * 设置设备别名
	 */
	public void setAlias(String alias) {
//		EditText aliasEdit = (EditText) findViewById(R.id.et_alias);
//		String alias = aliasEdit.getText().toString().trim();

		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(PushSetActivity.this, R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!JPushUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(PushSetActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
			return;
		}

		//调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	/**
	 * 设置设备标签Tag和设备别名
	 */
	public void setTagAndAlias(String tag,String alias) {
		// 检查 tag 的有效性
		if (TextUtils.isEmpty(tag)) {
			Toast.makeText(PushSetActivity.this, R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!JPushUtil.isValidTagAndAlias(sTagItme)) {
				Toast.makeText(PushSetActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
				return;
			}
			tagSet.add(sTagItme);
		}

		// 检查 alias 的有效性
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(PushSetActivity.this, R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!JPushUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(PushSetActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
			return;
		}

		Bundle bundle=new Bundle();
		bundle.putSerializable("tagSet", (Serializable) tagSet);
//		bundle.putParcelable("tagSet", (Parcelable) tagSet);
		bundle.putString("alias",alias);
		Message msg = mHandler.obtainMessage();
		msg.what=MSG_SET_TAGS_AND_ALIAS;
		msg.setData(bundle);
		//调用JPush API设置Tag和Alias
		mHandler.sendMessage(msg);
	}

	/**
	 * 设置通知提示方式 - 基础属性
	 */
	public void setStyleBasic() {
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(PushSetActivity.this);
		builder.statusBarDrawable = R.drawable.ic_launcher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
		builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
		JPushInterface.setPushNotificationBuilder(1, builder);
		Toast.makeText(PushSetActivity.this, "Basic Builder - 1", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 设置通知栏样式 - 定义通知栏Layout
	 */
	public void setStyleCustom() {
		CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(PushSetActivity.this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
		builder.layoutIconDrawable = R.drawable.ic_launcher;
		builder.developerArg0 = "developerArg2";
		JPushInterface.setPushNotificationBuilder(2, builder);
		Toast.makeText(PushSetActivity.this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 设置定制通知栏样式
	 */
	public void setAddActionsStyle() {
		MultiActionsNotificationBuilder builder = new MultiActionsNotificationBuilder(PushSetActivity.this);
		builder.addJPushAction(R.drawable.jpush_ic_richpush_actionbar_back, "first", "my_extra1");
		builder.addJPushAction(R.drawable.jpush_ic_richpush_actionbar_back, "second", "my_extra2");
		builder.addJPushAction(R.drawable.jpush_ic_richpush_actionbar_back, "third", "my_extra3");
		JPushInterface.setPushNotificationBuilder(10, builder);

		Toast.makeText(PushSetActivity.this, "AddActions Builder - 10", Toast.LENGTH_SHORT).show();
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
				case 0:
					logs = "设置设备别名(Alias)成功";
					Logger.i(TAG, logs);
					break;

				case 6002:
					logs = "设置设备别名(Alias)失败---超时，请60s后重试";
					Logger.i(TAG, logs);
					if (JPushUtil.isConnected(getApplicationContext())) {
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
					} else {
						Logger.i(TAG, "没有网络");
					}
					break;

				default:
					logs = "设置设备别名(Alias)失败，错误码 === " + code;
					Logger.e(TAG, logs);
			}
			JPushUtil.showToast(logs, getApplicationContext());
		}
	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
				case 0:
					logs = "设置设备标签(Tag)成功";
					Logger.i(TAG, logs);
					break;

				case 6002:
					logs = "设置设备标签(Tag)失败---超时，请60s后重试";
					Logger.i(TAG, logs);
					if (JPushUtil.isConnected(getApplicationContext())) {
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
					} else {
						Logger.i(TAG, "没有网络");
					}
					break;

				default:
					logs = "设置设备标签(Tag)失败，错误码 === " + code;
					Logger.e(TAG, logs);
			}
			JPushUtil.showToast(logs, getApplicationContext());
		}
	};

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	private static final int MSG_SET_TAGS_AND_ALIAS = 1003;

	public final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_SET_ALIAS: //设置设备别名Alias
					Logger.d(TAG, "开始设置设备别名Alias");
					JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
					break;

				case MSG_SET_TAGS: //设置设备标签Tag
					Logger.d(TAG, "开始设置设备标签Tag");
					JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
					break;

				case MSG_SET_TAGS_AND_ALIAS: //设置设备标签Tag和设备别名Alias
					Logger.d(TAG, "开始设置设备标签Tag和设备别名Alias");
					Bundle bundle=msg.getData();
					JPushInterface.setAliasAndTags(getApplicationContext(), bundle.getString("alias"), (Set<String>)bundle.getSerializable("tagSet"), mTagsCallback);
					break;

				default:
					Logger.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};

}