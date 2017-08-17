package com.roch.hzz_baidumap_demo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.Gv_Photo_Adapter;
import com.roch.hzz_baidumap_demo.dialog.AddPopWindow;
import com.roch.hzz_baidumap_demo.entity.ListMenu;
import com.roch.hzz_baidumap_demo.entity.Photo;
import com.roch.hzz_baidumap_demo.entity.XinXiHeCha;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.StringUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.utils.ImageCaptureManager;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 信息核查列表中，如果是未确认状态，进入该信息核查确认上报页面
 * 作者：GuGaoJie
 * 时间：2017/7/11/011 15:23
 */
public class XinXinHeCha_ShangBao_Activity extends CommonBaseActivity implements AddPopWindow.ShowMessageListener, AdapterView.OnItemClickListener {

    @BindView(R.id.et_xxhc_content)
    EditText etXxhcContent;
    @BindView(R.id.gv_photo)
    GridView gvPhoto;

    @BindView(R.id.tv_sure)
    TextView tvSure;

    /**
     * GridView适配器的数据源
     */
    List<Photo> photos = new ArrayList<>();
    @BindView(R.id.tv_xxhc_location)
    TextView tvXxhcLocation;

    private ArrayList<String> selectedPhotos = new ArrayList<>();
    Gv_Photo_Adapter gv_photo_adapter;

    private XinXiHeCha xinXiHeCha;

    /**
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_xinxi_hecha_shangbao;
    }

    /**
     * 3.初始化Tool控件
     */
    @Override
    public void initToolbar() {
        super.initToolbar();
        tvSure.setVisibility(View.VISIBLE);
        tvSure.setText("上报");
    }

    /**
     * 4.初始化数据
     */
    @Override
    public void initData() {
        super.initData();
        captureManager = new ImageCaptureManager(this);
        Intent intent = getIntent();
        xinXiHeCha = (XinXiHeCha) intent.getSerializableExtra("xinxihecha");
//        if (StringUtil.isNotEmpty(xinXiHeCha)) {
//            etXxhcContent.setText(xinXiHeCha.getDetail());
//        }
         tvXxhcLocation.setText(Common.mCurrentLocationAddr);

        //拍照或选择照片Item
        Photo photo = new Photo();
        photo.setId("0");
        photo.setUrl(String.valueOf(R.drawable.add_photo_3));
        photos.add(photo);

        gv_photo_adapter = new Gv_Photo_Adapter(this, photos);
        gvPhoto.setAdapter(gv_photo_adapter);
        gvPhoto.setOnItemClickListener(this);

        captureManager = new ImageCaptureManager(this);
    }

    @OnClick({R.id.tv_sure})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_sure:
                // 检查信息核查上报的内容和照片---是否为空
                if (checkContentAndPhoto()) {
                    // 提交信息核查上报的内容和照片
                    commitContentAndPhoto();
                }
                break;
        }
    }

    PopupWindow popupWindow_bottem;
    float alpha = 1f;

    /**
     * 1.底部弹出PopupWindow，2.并设置里面布局view的点击监听，3.初始化屏幕的背景透明度为 1
     *
     * @param view
     */
    private void bottomwindow(View view) {
        if (popupWindow_bottem != null && popupWindow_bottem.isShowing()) {
            return;
        }
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.window_popup, null);
        popupWindow_bottem = new PopupWindow(layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //点击空白处时，隐藏掉pop窗口
        popupWindow_bottem.setFocusable(true);
        popupWindow_bottem.setBackgroundDrawable(new BitmapDrawable());
        //添加弹出、弹入的动画
        popupWindow_bottem.setAnimationStyle(R.style.Popupwindow_bottem);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow_bottem.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        //添加按键事件监听
        setButtonListeners(layout);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        popupWindow_bottem.setOnDismissListener(new poponDismissListener());
        // 初始化屏幕的背景透明度为 1
        backgroundAlpha(1f);
    }

    /**
     * 返回或者点击空白位置的时候将背景透明度改回来
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while (alpha < 1f) {
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("HeadPortrait", "alpha:" + alpha);
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        alpha += 0.01f;
                        msg.obj = alpha;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    backgroundAlpha((float) msg.obj);
                    break;
            }
        }
    };

    /**
     * 设置屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void setButtonListeners(LinearLayout layout) {
        Button camera = (Button) layout.findViewById(R.id.camera);
        Button gallery = (Button) layout.findViewById(R.id.gallery);
        Button cancel = (Button) layout.findViewById(R.id.cancel);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow_bottem != null && popupWindow_bottem.isShowing()) {
                    // 打开照片机
                    openCamera();
                    popupWindow_bottem.dismiss();
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow_bottem != null && popupWindow_bottem.isShowing()) {
                    //在此处添加你的按键处理 xxx
                    PhotoPicker.builder()
                            .setPhotoCount(9)
                            .setGridColumnCount(4)
                            .setShowCamera(false)
                            .start(XinXinHeCha_ShangBao_Activity.this);
                    popupWindow_bottem.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow_bottem != null && popupWindow_bottem.isShowing()) {
                    popupWindow_bottem.dismiss();
                }
            }
        });
    }

    /**
     * 网络请求的参数---键值对
     */
    private Map<String, String> params = new HashMap<>();
    /**
     * 网络请求的图片参数---键值对
     */
    private Map<String, File> fileParams = new HashMap<>();

    /**
     * 提交信息核查上报的内容和照片
     */
    private void commitContentAndPhoto() {
        params.put("cid", xinXiHeCha.getId());
        params.put("location", location);
        params.put("supinfo", content);
        //图片文件参数
        if (photos.size() > 1) { // 图片数量大于1，说明信息上报时上传的有图片
            showProgressDialog("正在加载中......", true);
            for (int i = 0; i < photos.size(); i++) {
                if (i != photos.size() - 1) {
                    // 使用鲁班进行压缩图片，压缩好后提交服务器
                    compressPhotoFile(i);
                }
            }
        }else { // 图片数量为0，此时没有图片，直接上传服务器
            // 提交上报内容和图片到服务器
            commitFileToServer();
        }
    }

    /**
     * 使用鲁班进行压缩图片，压缩好后提交服务器
     */
    private void compressPhotoFile(int i) {
        LogUtil.println("信息核查---上报时每一张照片***鲁班压缩之前***的路径为：===" + photos.get(i).getUrl());
        // 鲁班压缩图片
        final int finalI = i;
        Luban.with(this)
                .load(new File(photos.get(i).getUrl()))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        fileParams.put("photo" + finalI + ".jpg", file);
                        LogUtil.println("信息核查---上报时每一张照片***鲁班压缩后***的路径为：===" + file.getAbsolutePath());
                        if(finalI == photos.size()-2){
                            // 提交上报内容和图片到服务器
                            commitFileToServer();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(finalI == photos.size()-2){
                            // 提交上报内容和图片到服务器
                            commitFileToServer();
                        }
                    }
                }).launch();
    }

    /**
     * 提交上报内容和图片到服务器
     */
    private void commitFileToServer() {
        if(fileParams.size()>0){
            LogUtil.println("信息核查(带图片)---上报时的服务器地址为：===" + URLs.XinXi_HeCha_ShangBao);
            MyApplication.getInstance().getHttpUtilsInstance().post(XinXinHeCha_ShangBao_Activity.this,
                    URLs.XinXi_HeCha_ShangBao,
                    params,
                    fileParams,
                    MyConstans.FIRST);
        }else {
            LogUtil.println("信息核查(不带图片)---上报时的服务器地址为：===" + URLs.XinXi_HeCha_ShangBao_No_Photo);
            MyApplication.getInstance().getHttpUtilsInstance().post(XinXinHeCha_ShangBao_Activity.this,
                    URLs.XinXi_HeCha_ShangBao_No_Photo,
                    params,
                    null,
                    MyConstans.FIRST);
        }
    }

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        switch (flag) {
            case MyConstans.FIRST:
//                ShowToast("信息核查成功");
                LogUtil.println("信息核查---上报成功===" + str);
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    JSONObject object = new JSONObject(str1);
                    boolean success = object.optBoolean("success");
                    if (success) {
                        ShowToast("信息核查上报成功");
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFaileResult(String str, int flag) {
        super.onFaileResult(str, flag);
        switch (flag) {
            case MyConstans.FIRST:
                ShowToast("信息核查失败");
                LogUtil.println("信息核查---上报失败===" + str);
                break;
        }
    }

    String content;
    String location;

    /**
     * 检查信息核查上报的内容和照片
     */
    private boolean checkContentAndPhoto() {
        content = etXxhcContent.getText().toString().trim();
        location = Common.mCurrentLon + "," + Common.mCurrentLat;
        if (StringUtil.isEmpty(location)) {
            ShowToast("定位位置有误，请设置定位");
            return false;
        }
        if (StringUtil.isEmpty(content) && photos.size() <= 0) {
            ShowToast("上报内容和照片不能同时为空");
            return false;
        }
        return true;
    }

    @Override
    public void Message(Object object) {
        ListMenu menu = (ListMenu) object;
        if (menu.getName().equals("照片")) {
            // 打开图片上传的Activity
            PhotoPicker.builder()
                    .setPhotoCount(9)
                    .setGridColumnCount(4)
                    .setShowCamera(false)
                    .start(XinXinHeCha_ShangBao_Activity.this);
        } else if (menu.getName().equals("拍照")) {
            // 打开照相机
            openCamera();
        }
    }

    private ImageCaptureManager captureManager;

    private void openCamera() {
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Photo photoBea;
        if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            LogUtil.println("第三方---拍照完成后返回该页面");
            if (captureManager == null) {
                captureManager = new ImageCaptureManager(this);
            }
            captureManager.galleryAddPic();
            String path = captureManager.getCurrentPhotoPath();
            LogUtil.println("拍照以后当前拍照图片的路径===" + path);

            photoBea = new Photo();
            photoBea.setUrl(path);
            photoBea.setId("1");
            photos.add(0, photoBea);
            //有数据后刷新适配器
            gv_photo_adapter.notifyDataSetChanged();
            // 设置最后一个条目可见
            setLastItemVisible();
        } else if (resultCode == RESULT_OK) { // && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)
            if (requestCode == PhotoPicker.REQUEST_CODE) {
                LogUtil.println("第三方---选择照片完成后返回该页面");
                List<String> photo_paths = null;
                if (data != null) {
                    photo_paths = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }

                Photo photo;
                if (photo_paths != null) {
                    for (int i = 0; i < photo_paths.size(); i++) {
                        photo = new Photo();
                        photo.setId("1");
                        photo.setUrl(photo_paths.get(i));
                        photos.add(0, photo);
                    }
                }
                gv_photo_adapter.notifyDataSetChanged();
                // 设置最后一个条目可见
                setLastItemVisible();
            }
            if (requestCode == PhotoPreview.REQUEST_CODE) {
                LogUtil.println("第三方---预览照片完成后返回该页面");
                List<String> photo_paths = null;
                if (data != null) {
                    photo_paths = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }

                Photo photo;
                photos.clear();
                if (photo_paths != null) {
                    for (int i = 0; i < photo_paths.size(); i++) {
                        photo = new Photo();
                        photo.setId("1");
                        photo.setUrl(photo_paths.get(i));
                        photos.add(photo);
                    }
                }
                photo = new Photo();
                photo.setId("0");
                photo.setUrl(String.valueOf(R.drawable.add_photo_3));
                photos.add(photo);

                gv_photo_adapter.notifyDataSetChanged();
                // 设置最后一个条目可见
                setLastItemVisible();
            }
        }
    }

    /**
     * 设置最后一个条目可见
     */
    private void setLastItemVisible() {
        gvPhoto.post(new Runnable() {
            @Override
            public void run() {
                gvPhoto.setSelection(gv_photo_adapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Photo photo = (Photo) parent.getItemAtPosition(position);
        if ("0".equals(photo.getId())) {
            bottomwindow(gvPhoto);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (alpha > 0.5f) {
                        try {
                            //4是根据弹出动画时间和减少的透明度计算
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        //每次减少0.01，精度越高，变暗的效果越流畅
                        alpha -= 0.01f;
                        msg.obj = alpha;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        } else {
            selectedPhotos.clear();
            for (int i = 0; i < photos.size(); i++) {
                if (i != photos.size() - 1) {
                    selectedPhotos.add(photos.get(i).getUrl());
                }
            }
            LogUtil.println("点击查看照片时，总照片数=" + selectedPhotos.size() + ",当前点击位置=" + position);

            PhotoPreview.builder()
                    .setPhotos(selectedPhotos)
                    .setCurrentItem(position) // parent.getSelectedItemPosition()
                    .start(XinXinHeCha_ShangBao_Activity.this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) { // 隐藏软键盘，说明点击位置不在EditText上
                etXxhcContent.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }else { // 显示软键盘，说明点击位置在EditText上
                if(v.getId() == etXxhcContent.getId()){
                    etXxhcContent.requestFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

}
