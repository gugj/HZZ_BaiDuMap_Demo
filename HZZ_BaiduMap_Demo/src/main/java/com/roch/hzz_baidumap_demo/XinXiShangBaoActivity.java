package com.roch.hzz_baidumap_demo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.Gv_Photo_Adapter;
import com.roch.hzz_baidumap_demo.adapter.QtypeAdapter;
import com.roch.hzz_baidumap_demo.entity.LoginUser;
import com.roch.hzz_baidumap_demo.entity.Photo;
import com.roch.hzz_baidumap_demo.entity.WenTiLeiXin;
import com.roch.hzz_baidumap_demo.entity.WenTiLeiXin_Result;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.SharePreferencesUtil;
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
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.utils.ImageCaptureManager;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 *  巡河时，发现问题进行信息上报的页面
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 11:28
 */
public class XinXiShangBaoActivity extends CommonBaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.gv_home)
    GridView gvHome;
    @BindView(R.id.et_shangbao_neirong)
    EditText etShangbaoNeirong;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.rc_qtype)
    RecyclerView rc_qtype;
//    @BindView(R.id.scrollView)
    ScrollView scrollView;

    /**
     * GridView适配器的数据源
     */
    List<Photo> photos = new ArrayList<>();
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    Gv_Photo_Adapter gv_photo_adapter;

    private QtypeAdapter mAdapter;
    /**
     * 巡视的河道的ID
     */
    private String xunshihedao_id;

    /**
     * 1.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_xinxi_shangbao;
    }

    /**
     * 2.初始化传参数据---设置title标题
     */
    @Override
    public void initBundle() {
        super.initBundle();
        xunshihedao_id = intent.getStringExtra("xunshihedao_id");
        LogUtil.println("信息上报的河道ID为：==="+xunshihedao_id);
    }

    /**
     * 3.初始化Tool控件
     */
    @Override
    public void initToolbar() {
        super.initToolbar();
        tvSure.setText("上报");
        tvSure.setVisibility(View.VISIBLE);
    }

    /**
     * 4.初始化控件的监听
     */
    public void initListener() {
        gvHome.setOnItemClickListener(this);
//        gvHome.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction()==MotionEvent.ACTION_UP) {
//                    scrollView.requestDisallowInterceptTouchEvent(false);
//                }else {
//                    scrollView.requestDisallowInterceptTouchEvent(true);
//                }
//                return false;
//            }
//        });
    }

    /**
     * 5.初始化数据
     */
    @Override
    public void initData() {
        super.initData();
        params.clear();
        params.put("typegroupcode", "qtype");
        LogUtil.println("信息上报时请求问题类型网址为：===" + URLs.XinXi_ShangBao_Qtype);
        MyApplication.getInstance().getHttpUtilsInstance().post(XinXiShangBaoActivity.this,
                URLs.XinXi_ShangBao_Qtype,
                params,
                null,
                MyConstans.FIRST);

        // 初始化问题类型RecyclerView的适配器
        rc_qtype.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new QtypeAdapter(this);
        rc_qtype.setAdapter(mAdapter);

        //拍照或选择照片Item
        Photo photo = new Photo();
        photo.setId("0");
        photo.setUrl(String.valueOf(R.drawable.add_photo_3));
        photos.add(photo);

        gv_photo_adapter = new Gv_Photo_Adapter(this, photos);
        gvHome.setAdapter(gv_photo_adapter);
        gvHome.setOnItemClickListener(this);

        captureManager = new ImageCaptureManager(this);
    }

    @OnClick(R.id.tv_sure)
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_sure: // 信息上报
                String shangBaoNeiRong = etShangbaoNeirong.getText().toString().trim();
                if (StringUtil.isEmpty(shangBaoNeiRong) && photos.size() <= 1) {
                    ShowToast("上报内容和照片不能同时为空");
                    return;
                }
                //提交信息上报的内容和照片到服务器
                commitDataToServer(shangBaoNeiRong);
                break;
        }
    }

    /**
     * 提交信息上报的内容和照片到服务器
     *
     * @param shangBaoNeiRong
     */
    private void commitDataToServer(String shangBaoNeiRong) {
        //提交信息上报的内容和照片到服务器
        commitFileAndContent_New(shangBaoNeiRong);
    }

    /**
     * 网络请求的普通参数---键值对
     */
    private Map<String, String> params = new HashMap<>();
    /**
     * 网络请求的图片参数---键值对
     */
    private Map<String, File> fileParams = new HashMap<>();

    private void commitFileAndContent_New(String shangBaoNeiRong) {
        //一般普通参数
        params.clear();
        // 问题类型
        String qtype = "";
        Set<String> keySet = Common.qtype_select.keySet();
        for (String key : keySet) { // Common.qtype_select.get(key)
            qtype = qtype + key + ",";
        }
        // 上报时间
//        String time = StringUtil.getCurrentlyDate();
        String time = StringUtil.getCurrentlyDateTime();
        // 巡河人员ID
        LoginUser loginUser = SharePreferencesUtil.getLoginUser(this, Common.Login_User_Name);
        String userId = loginUser.getId();

        params.put("rvId", xunshihedao_id); // 河道ID
        params.put("userid", userId); // 巡河人员ID
        params.put("location", Common.mCurrentLon + "," + Common.mCurrentLat);  // 位置
//        params.put("nt", "备注----测试");  // 备注
        params.put("detail", shangBaoNeiRong); // 上报内容
        params.put("ckTime", time);  // 上报时间
        params.put("qtype", qtype);  // 问题类型
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
        LogUtil.println("巡河上报时每一张照片****鲁班压缩之前****的路径为：===" + photos.get(i).getUrl());
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
                        LogUtil.println("巡河上报时每一张照片****鲁班压缩后****的路径为：===" + file.getAbsolutePath());
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
            LogUtil.println("信息上报(带图片)的网址为：===" + URLs.XinXi_ShangBao);
            MyApplication.getInstance().getHttpUtilsInstance().post(XinXiShangBaoActivity.this,
                    URLs.XinXi_ShangBao,
                    params,
                    fileParams,
                    MyConstans.SECOND);
        }else {
            LogUtil.println("信息上报(不带图片)的网址为：===" + URLs.XinXi_ShangBao_No_Photo);
            MyApplication.getInstance().getHttpUtilsInstance().post(XinXiShangBaoActivity.this,
                    URLs.XinXi_ShangBao_No_Photo,
                    params,
                    null,
                    MyConstans.SECOND);
        }
    }

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        switch (flag) {
            case MyConstans.FIRST:
                LogUtil.println("信息上报时请求问题类型成功===" + str);
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    WenTiLeiXin_Result wenTiLeiXin_result = WenTiLeiXin_Result.parseToT(str1, WenTiLeiXin_Result.class);
                    if (null != wenTiLeiXin_result && wenTiLeiXin_result.getSuccess()) {
                        List<WenTiLeiXin> wenTiLeiXins = wenTiLeiXin_result.getJsondata();
                        if (null != wenTiLeiXins) {
                            mAdapter.addAll(wenTiLeiXins);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case MyConstans.SECOND:
                LogUtil.println("信息上报成功===" + str);
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    JSONObject object = new JSONObject(str1);
                    boolean success = object.optBoolean("success");
                    if (success) {
                        ShowToast("信息上报成功");
                        // 问题类型集合清空
                        Common.qtype_select.clear();
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
                LogUtil.println("信息上报时请求问题类型失败===" + str);
                break;

            case MyConstans.SECOND:
                ShowToast("上报失败");
                LogUtil.println("信息上报请求服务器失败===" + str);
                break;
        }
    }

    PopupWindow popupWindow_bottem;
    float alpha = 1f;
    /**
     * 1.底部弹出PopupWindow，2.并设置里面布局view的点击监听，3.初始化屏幕的背景透明度为 1
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
                            .start(XinXiShangBaoActivity.this);
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



    private ImageCaptureManager captureManager;

    /**
     * 打开照片机
     */
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
        } else if (resultCode == RESULT_OK ) { // && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)
            if(requestCode == PhotoPicker.REQUEST_CODE){
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
            if(requestCode == PhotoPreview.REQUEST_CODE){
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
                        photos.add(0,photo);
                    }
                }
                photo = new Photo();
                photo.setId("0");
                photo.setUrl(String.valueOf(R.drawable.add_photo_3));
                photos.add(photo);

                gv_photo_adapter.notifyDataSetChanged();
                gvHome.setSelection(gv_photo_adapter.getCount() - 1);
                // 设置最后一个条目可见
                setLastItemVisible();
            }
        }
    }

    /**
     * 设置最后一个条目可见
     */
    private void setLastItemVisible() {
        gvHome.post(new Runnable() {
            @Override
            public void run() {
                gvHome.setSelection(gv_photo_adapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Photo photo = (Photo) parent.getItemAtPosition(position);
        if ("0".equals(photo.getId())) { // 拍照或选择照片
            bottomwindow(gvHome);
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
                    .start(XinXiShangBaoActivity.this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) { // 隐藏软键盘，说明点击位置不在EditText上
                etShangbaoNeirong.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }else { // 显示软键盘，说明点击位置在EditText上
                if(v.getId() == etShangbaoNeirong.getId()){
                    etShangbaoNeirong.requestFocus();
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
