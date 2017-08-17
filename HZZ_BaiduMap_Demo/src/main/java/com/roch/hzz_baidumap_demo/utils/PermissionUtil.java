package com.roch.hzz_baidumap_demo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.roch.hzz_baidumap_demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qianxiaoai on 2016/7/7.
 */
public class PermissionUtil {

    private static final String TAG = PermissionUtil.class.getSimpleName();
    public static final int CODE_RECORD_AUDIO = 0;
    public static final int CODE_GET_ACCOUNTS = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_CALENDAR = 3;
    public static final int CODE_CAMERA = 4;
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_BODY_SENSORS = 6;
    public static final int CODE_READ_EXTERNAL_STORAGE = 7;
    public static final int CODE_READ_SMS = 8;
    public static final int CODE_MULTI_PERMISSION = 100;

    // 九大敏感权限组
//    CALENDAR	    READ_CALENDAR， WRITE_CALENDAR *********
//    CAMERA	    CAMERA-------------------------------------------------
//    CONTACTS	    READ_CONTACTS ，WRITE_CONTACTS， GET_ACCOUNTS---------------------
//    LOCATION	    ACCESS_FINE_LOCATION , ACCESS_COARSE_LOCATION----------------
//    MICROPHONE	RECORD_AUDIO----------------------------------------------
//    PHONE	        READ_PHONE_STATE, CALL_PHONE READ_CALL_LOG , WRITE_CALL_LOG , ADD_VOICEMAIL , USE_SIP, PROCESS_OUTGOING_CALLS--------
//    SENSORS	    BODY_SENSORS ****************
//    SMS	        SEND_SMS RECEIVE_SMS , READ_SMS RECEIVE_WAP_PUSH , RECEIVE_MMS **************
//    STORAGE	    READ_EXTERNAL_STORAGE , WRITE_EXTERNAL_STORAGE-----------------------

    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALENDAR = Manifest.permission.WRITE_CALENDAR; //************************
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS;//*********
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;//*****

    private static final String[] requestPermissions = {
            PERMISSION_RECORD_AUDIO,
            PERMISSION_GET_ACCOUNTS,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_CALENDAR,
            PERMISSION_CAMERA,
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_BODY_SENSORS,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_READ_SMS
    };

    /**
     * 一次请求申请权限---单个权限
     * @param activity
     * @param requestCode request code, e.g. if you need request CAMERA permission,parameters is PermissionUtils.CODE_CAMERA
     */
    public static void requestPermission(final Activity activity, final int requestCode, PermissionGrant permissionGrant) {
        if (activity == null) {
            return;
        }

        if (requestCode < 0 || requestCode >= requestPermissions.length) {
            return;
        }
        //获取将要申请的权限
        final String requestPermission = requestPermissions[requestCode];

        //如果是6.0以下的手机，ActivityCompat.checkSelfPermission()会始终等于PERMISSION_GRANTED，
        // 但是，如果用户关闭了你申请的权限，ActivityCompat.checkSelfPermission(),会导致程序崩溃(java.lang.RuntimeException: Unknown exception code: 1 msg null)，
        // 你可以使用try{}catch(){},处理异常，也可以在这个地方，低于23就什么都不做，
        // 个人建议try{}catch(){}单独处理，提示用户开启权限。
//        if (Build.VERSION.SDK_INT < 23) {
//            return;
//        }
        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
        } catch (RuntimeException e) {
//            Toast.makeText(activity, "检查是否有此权限时发生异常==="+requestPermission, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "检查是否有此权限时发生异常===" + e.getMessage());
            return;
        }

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) { //没有此权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) { //展示申请权限的对话框---因为没有这个权限（没有此权限，无法开启这个功能，请开启权限）
               // 1.之前请求过此权限，但用户拒绝，返回true
                //展示申请权限的对话框---因为没有这个权限（没有此权限，无法开启这个功能，请开启权限）
                System.out.println("之前请求过此权限，但用户拒绝，返回true----以下代码展示申请权限的对话框");
                shouldShowRationale(activity, requestCode, requestPermission);
            } else {  //不展示申请权限的对话框---因为有这个权限
                // 1.之前没有请求过此权限，第一次申请，返回false
                // 2.之前请求过此权限，但用户拒绝，并且选择不再提醒，返回false
                System.out.println("1.之前没有请求过此权限，第一次申请，shouldShowRequestPermissionRationale()返回false \n" +
                                   "2.之前请求过此权限，但用户拒绝，并且选择不再提醒，shouldShowRequestPermissionRationale()返回false" +
                                   "----以下使用系统源码(第一次或者重新)申请权限");
                ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestCode);
            }
        } else { //已经有此权限
//            Toast.makeText(activity, "已经有此权限:" + requestPermission, Toast.LENGTH_SHORT).show();
            permissionGrant.onPermissionGranted(requestCode);
        }
    }

    /**
     * 一次请求申请权限---多个权限
     * @param activity
     * @param grant
     */
    public static void requestMultiPermissions(final Activity activity,String[] requestPermissions, PermissionGrant grant) {

        //不应该展示申请权限说明的集合
        final List<String> permissionsList = getNoGrantedPermission(activity,requestPermissions, false);
        //应该展示申请权限说明的集合
        final List<String> shouldRationalePermissionsList = getNoGrantedPermission(activity,requestPermissions, true);

        if (permissionsList == null || shouldRationalePermissionsList == null) {
            return;
        }

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]), CODE_MULTI_PERMISSION);
        } else if (shouldRationalePermissionsList.size() > 0) {
            showMessageOKCancel(activity, "应该开启这些权限",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, shouldRationalePermissionsList.toArray(new String[shouldRationalePermissionsList.size()]), CODE_MULTI_PERMISSION);
                        }
                    });
        } else {
            grant.onPermissionGranted(CODE_MULTI_PERMISSION);
        }
    }

    /**
     * 一次申请一个或多个权限，用户选择后，结果回调此方法
     * @param activity
     * @param requestCode  Need consistent with requestPermission
     * @param permissions 所申请的权限的数组
     * @param grantResults
     */
    public static void requestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, PermissionGrant permissionGrant) {

        if (activity == null) {
            return;
        }
        if (requestCode == CODE_MULTI_PERMISSION) { //如果是一次申请的是多个权限
            requestMultiResult(activity, permissions, grantResults, permissionGrant);
            return;
        }
        if (requestCode < 0 || requestCode >= requestPermissions.length) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //申请了一个权限，并且用户同意
            permissionGrant.onPermissionGranted(requestCode);
        } else {
            String[] permissionsHint = activity.getResources().getStringArray(R.array.permissions);
            //打开进入权限设置页面
            openSettingActivity(activity, permissionsHint[requestCode]);
        }
    }

    /**
     * 一次申请多个权限，用户选择后，结果回调此方法
     * @param activity 申请权限的activity
     * @param permissions 所申请的权限的数组
     * @param grantResults 所申请的权限的处理结果的数据
     * @param permissionGrant
     */
    private static void requestMultiResult(Activity activity, String[] permissions, int[] grantResults, PermissionGrant permissionGrant) {
        if (activity == null) {
            return;
        }

        Map<String, Integer> perms = new HashMap<>();
        //没有被允许的权限的集合
        ArrayList<String> notGranted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            perms.put(permissions[i], grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permissions[i]);
            }
        }

        if (notGranted.size() == 0) {
//            Toast.makeText(activity, "all permission success" + notGranted, Toast.LENGTH_SHORT).show();
            permissionGrant.onPermissionGranted(CODE_MULTI_PERMISSION);
        } else {
            openSettingActivity(activity, "部分权限没有被允许，无法开启某些功能，请开启权限");
        }
    }

    /**
     * 展示申请权限的对话框---因为没有这个权限（没有此权限，无法开启这个功能，请开启权限）
     * @param activity
     * @param requestCode
     * @param requestPermission
     */
    private static void shouldShowRationale(final Activity activity, final int requestCode, final String requestPermission) {
        String[] permissionsHint = activity.getResources().getStringArray(R.array.permissions);
        //展示申请权限的对话框
        showMessageOKCancel(activity, permissionsHint[requestCode], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{requestPermission},
                        requestCode);
            }
        });
    }

    /**
     * 展示申请权限的对话框
     * @param context
     * @param message
     * @param okListener
     */
    private static void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    /**
     * 打开进入权限设置页面
     * @param activity
     * @param message
     */
    private static void openSettingActivity(final Activity activity, String message) {
        showMessageOKCancel(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
    }

    /**
     * 获取没有被允许的权限
     * @param activity
     * @param isShouldRationale true: return no granted and shouldShowRequestPermissionRationale permissions, false:return no granted and !shouldShowRequestPermissionRationale
     * @return
     */
    public static ArrayList<String> getNoGrantedPermission(Activity activity,String[] requestPermissions, boolean isShouldRationale) {

        ArrayList<String> permissions = new ArrayList<>();

        for (int i = 0; i < requestPermissions.length; i++) {
            String requestPermission = requestPermissions[i];
            int checkSelfPermission = -1;
            try {
                checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
            } catch (RuntimeException e) {
                Log.e(TAG, "RuntimeException:" + e.getMessage());
                return null;
            }

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                    if (isShouldRationale) {
                        permissions.add(requestPermission);
                    }
                } else {
                    if (!isShouldRationale) {
                        permissions.add(requestPermission);
                    }
                }
            }
        }
        return permissions;
    }

    /**
     * 动态申请权限的接口回调类
     */
    public interface PermissionGrant {
        /**
         * 当权限被授予的时候被调用
         * @param requestCode
         */
        void onPermissionGranted(int requestCode);
    }

}