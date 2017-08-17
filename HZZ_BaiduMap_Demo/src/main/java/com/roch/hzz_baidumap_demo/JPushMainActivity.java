package com.roch.hzz_baidumap_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.entity.LoginUser;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.JPushUtil;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.Logger;
import com.roch.hzz_baidumap_demo.utils.SharePreferencesUtil;
import com.roch.hzz_baidumap_demo.utils.StringUtil;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public abstract class JPushMainActivity extends CommonBaseActivity {

    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerMessageReceiver();  // used for receive msg
        // 检查是否设置过标签
        checkTagOrSet();
    }

    /**
     * 检查是否设置过标签
     */
    private void checkTagOrSet() {
        // 获取设备标签Tag
        String tag= SharePreferencesUtil.getTag(this);
        // 获取当前登录用户
        LoginUser loginUser = SharePreferencesUtil.getLoginUser(this, Common.Login_User_Name);
        // 判断是否设置过标签----如果标签为空，或者设置过标签，但标签与当前登录用户ID不一致
        if(StringUtil.isEmpty(tag) || (null != loginUser && !tag.equals(loginUser.getId()) )){ // 没有设置
//            setTagAndAlias("hzzapp", "hzzalias");
            LogUtil.println("注册设备名：："+loginUser.getId());
            setTagAndAlias(loginUser.getId(), loginUser.getId());
            SharePreferencesUtil.saveTagAndAlias(loginUser.getId(),loginUser.getId(),this);
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.roch.jpush_demo.MESSAGE_RECEIVED_ACTION";
    public static final String NOTIFICATION_RECEIVED_ACTION = "com.roch.jpush_demo.NOTIFICATION_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        filter.addAction(NOTIFICATION_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

//    @OnClick(R.id.btn_setTagAndAlias)
//    public void onClick() {
//        Intent intent = new Intent(JPushMainActivity.this, PushSetActivity.class);
//        startActivity(intent);
//    }

    /**
     * 设置设备标签Tag和设备别名
     */
    public void setTagAndAlias(String tag,String alias){
//        PushSetActivity.getInstance().setTag(tag);
//        PushSetActivity.getInstance().setTagAndAlias(tag,alias);
        setTag(tag);
        setAlias(alias);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!"".equals(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }else if(NOTIFICATION_RECEIVED_ACTION.equals(intent.getAction())){
                    String Notification_Title = intent.getStringExtra(KEY_TITLE);
                    String Notification_Content = intent.getStringExtra(KEY_CONTENT);
                    LogUtil.println("通知栏标题===="+Notification_Title+",,通知栏内容==="+Notification_Content);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setCostomMsg(String msg) {
        System.out.println("接收到自定义的极光推送广播：=====" + msg);
    }

    public void setAlias(String alias){
        // 检查 alias 的有效性
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(JPushMainActivity.this, R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        //调用JPush API设置alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    /**
     * 设置设备标签Tag
     */
    public void setTag(String tag) {
        // 检查 tag 的有效性
        if (TextUtils.isEmpty(tag)) {
            Toast.makeText(JPushMainActivity.this, R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!JPushUtil.isValidTagAndAlias(sTagItme)) {
                Toast.makeText(JPushMainActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            tagSet.add(sTagItme);
        }

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
    }

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final int MSG_SET_TAGS_AND_ALIAS = 1003;
    private static final String TAG = "system.out";

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

}
