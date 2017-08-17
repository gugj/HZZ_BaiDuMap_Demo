package com.roch.hzz_baidumap_demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.roch.hzz_baidumap_demo.utils.PermissionUtil;

public class PermissionsFragment extends Fragment implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_permission, null);
        initView(root);
        return root;
    }

    private void initView(View root) {

        Button readSMS = (Button) root.findViewById(R.id.readSMS);
        Button readExternalStorage = (Button) root.findViewById(R.id.read_external_storage);
        Button body_sensors = (Button) root.findViewById(R.id.body_sensors);
        Button accessFineLocation = (Button) root.findViewById(R.id.access_fine_location);
        Button readPhoneState = (Button) root.findViewById(R.id.read_phone_state);
        Button write_calenar = (Button) root.findViewById(R.id.write_calenar);
        Button getAccounts = (Button) root.findViewById(R.id.get_accounts);
        Button recordAudio = (Button) root.findViewById(R.id.record_audio);
        Button showCamera = (Button) root.findViewById(R.id.show_camera);

        readSMS.setOnClickListener(this);
        readExternalStorage.setOnClickListener(this);
        body_sensors.setOnClickListener(this);
        accessFineLocation.setOnClickListener(this);
        readPhoneState.setOnClickListener(this);
        write_calenar.setOnClickListener(this);
        getAccounts.setOnClickListener(this);
        recordAudio.setOnClickListener(this);
        showCamera.setOnClickListener(this);
    }

    private PermissionUtil.PermissionGrant mPermissionGrant = new PermissionUtil.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtil.CODE_RECORD_AUDIO:
                    Toast.makeText(getActivity(), "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtil.CODE_GET_ACCOUNTS:
                    Toast.makeText(getActivity(), "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtil.CODE_READ_PHONE_STATE:
                    Toast.makeText(getActivity(), "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtil.CODE_CALENDAR:
                    Toast.makeText(getActivity(), "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtil.CODE_CAMERA:
                    Toast.makeText(getActivity(), "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtil.CODE_ACCESS_FINE_LOCATION:
                    Toast.makeText(getActivity(), "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtil.CODE_BODY_SENSORS:
                    Toast.makeText(getActivity(), "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtil.CODE_READ_EXTERNAL_STORAGE:
                    Toast.makeText(getActivity(), "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtil.CODE_READ_SMS:
                    Toast.makeText(getActivity(), "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.readSMS:
                PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_READ_SMS, mPermissionGrant);
                break;
            case R.id.read_external_storage:
                PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant);
                break;
            case R.id.body_sensors:
                PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_BODY_SENSORS, mPermissionGrant);
                break;
            case R.id.access_fine_location:
                PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_ACCESS_FINE_LOCATION, mPermissionGrant);
                break;
            case R.id.read_phone_state:
                PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_READ_PHONE_STATE, mPermissionGrant);
                break;
            case R.id.write_calenar:
                PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_CALENDAR, mPermissionGrant);
                break;
            case R.id.get_accounts:
                PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_GET_ACCOUNTS, mPermissionGrant);
                break;
            case R.id.record_audio:
                PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_RECORD_AUDIO, mPermissionGrant);
                break;
            case R.id.show_camera:
                PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_CAMERA, mPermissionGrant);
                break;
            default:
                break;
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.requestPermissionsResult(getActivity(), requestCode, permissions, grantResults, mPermissionGrant);
    }
}