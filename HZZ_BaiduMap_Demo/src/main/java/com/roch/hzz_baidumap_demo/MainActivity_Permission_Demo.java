package com.roch.hzz_baidumap_demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.roch.hzz_baidumap_demo.utils.PermissionUtil;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/19/019 15:17
 */
public class MainActivity_Permission_Demo extends AppCompatActivity{

    private static final String TAG = MainActivity_Permission_Demo.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PermissionsFragment fragment = new PermissionsFragment();
        transaction.replace(R.id.content_fragment, fragment);
        transaction.commit();
    }

    public void showCamera(View view) {
        PermissionUtil.requestPermission(this, PermissionUtil.CODE_CAMERA, mPermissionGrant);
    }

    public void getAccounts(View view) {
        PermissionUtil.requestPermission(this, PermissionUtil.CODE_GET_ACCOUNTS, mPermissionGrant);
    }

    public void writeCalenar(View view) {
        PermissionUtil.requestPermission(this, PermissionUtil.CODE_CALENDAR, mPermissionGrant);
    }

    public void readPhoneState(View view) {
        PermissionUtil.requestPermission(this, PermissionUtil.CODE_READ_PHONE_STATE, mPermissionGrant);
    }

    public void accessFineLocation(View view) {
        PermissionUtil.requestPermission(this, PermissionUtil.CODE_ACCESS_FINE_LOCATION, mPermissionGrant);
    }

    public void bodySensors(View view) {
        PermissionUtil.requestPermission(this, PermissionUtil.CODE_BODY_SENSORS, mPermissionGrant);
    }

    public void readExternalStorage(View view) {
        PermissionUtil.requestPermission(this, PermissionUtil.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant);
    }

    public void readSMS(View view) {
        PermissionUtil.requestPermission(this, PermissionUtil.CODE_READ_SMS, mPermissionGrant);
    }

    public void recordAudio(View view) {
        PermissionUtil.requestPermission(this, PermissionUtil.CODE_RECORD_AUDIO, mPermissionGrant);
    }


    private PermissionUtil.PermissionGrant mPermissionGrant = new PermissionUtil.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtil.CODE_RECORD_AUDIO: // 0
                    Toast.makeText(MainActivity_Permission_Demo.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;

                case PermissionUtil.CODE_GET_ACCOUNTS: // 1
                    Toast.makeText(MainActivity_Permission_Demo.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                    break;

                case PermissionUtil.CODE_READ_PHONE_STATE: // 2
                    Toast.makeText(MainActivity_Permission_Demo.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;

                case PermissionUtil.CODE_CALENDAR: // 3
                    Toast.makeText(MainActivity_Permission_Demo.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                    break;

                case PermissionUtil.CODE_CAMERA: // 4
                    Toast.makeText(MainActivity_Permission_Demo.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;

                case PermissionUtil.CODE_ACCESS_FINE_LOCATION: // 5
                    Toast.makeText(MainActivity_Permission_Demo.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;

                case PermissionUtil.CODE_BODY_SENSORS: // 6
                    Toast.makeText(MainActivity_Permission_Demo.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;

                case PermissionUtil.CODE_READ_EXTERNAL_STORAGE: // 7
                    Toast.makeText(MainActivity_Permission_Demo.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;

                case PermissionUtil.CODE_READ_SMS: // 8
                    Toast.makeText(MainActivity_Permission_Demo.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;

                case PermissionUtil.CODE_MULTI_PERMISSION: // 100 一次申请多个权限，并且都被允许
                    break;
            }
        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

}
