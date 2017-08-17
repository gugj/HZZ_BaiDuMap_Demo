package com.roch.hzz_baidumap_demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.MyAdapter;
import com.roch.hzz_baidumap_demo.entity.Photo;
import com.roch.hzz_baidumap_demo.photo.Bimp;
import com.roch.hzz_baidumap_demo.photo.PhotoView;
import com.roch.hzz_baidumap_demo.photo.ViewPagerFixed;
import com.roch.hzz_baidumap_demo.utils.Common;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 这个是用于进行图片浏览时的界面
 *
 * @author king
 * @version 2014年10月18日  下午11:47:53
 * @QQ:595163260
 */
public class GalleryActivity extends CommonBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.send_button)
    /**
     * 预览照片完成按钮
     */
    Button send_bt;
    @BindView(R.id.tv_title)
    TextView tv_title;
    /**
     * 自定义的ViewPager
     */
    @BindView(R.id.gallery01)
    ViewPagerFixed pager;

    /**
     * 预览照片的索引位置---如果是从选择照片页面传进来，初始化为0，如果是从帮扶记录页面传进来，初始化为 position
     */
    private int position;

    private Intent intent;
    /**
     * 贫困户householderid或贫困村id
     */
    private String id = "";
    /**
     * 预览照片页面中ViewPager的索引位置
     */
    private int location = 0;

    /**
     * 预览照片页面中把本地照片路径转换为imageView后的照片集合
     */
    private ArrayList<View> listViews = null;
    private MyPageAdapter adapter;

    /**
     * 帮扶记录activity页面传进来的预览照片的集合
     */
    List<Photo> lists_bfjl;
    /**
     * 标志位---当前预览照片的数据源是帮扶记录activity还是选择照片activity传进来的,默认不是false
     */
    boolean type_bfjl;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.plugin_camera_gallery);
        ButterKnife.bind(this);
//		ViewUtils.inject(this);

        initToolbar();
//        MyApplication.getInstance().addActivity(GalleryActivity.this);

        //预览照片完成按钮
        send_bt.setOnClickListener(new GallerySendListener());
        //获取intent
        intent = getIntent();
        //预览照片的索引位置---如果是从选择照片页面传进来，初始化为0，如果是从帮扶记录页面传进来，初始化为 position
        position = Integer.parseInt(intent.getStringExtra("position"));
        //贫困户householderid或贫困村id
        id = intent.getStringExtra(Common.INTENT_KEY);
        //标志位---当前预览照片的数据源是否是帮扶记录传进来的
        type_bfjl = intent.getBooleanExtra("type_bfjl", false);
        //帮扶记录activity页面传进来的预览照片的集合
        lists_bfjl = (List<Photo>) intent.getSerializableExtra("photos_bfjl");

        if (null != lists_bfjl && lists_bfjl.size() > 0) { //这时候是帮扶记activity传进来的预览照片
            for (int i = 0; i < lists_bfjl.size(); i++) {
                try {
                    initListViews(Bimp.revitionImageSize(lists_bfjl.get(i).getUrl()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else { //这时候是选择照片activity传进来的预览照片
            for (int i = 0; i < MyAdapter.mSelectedImage.size(); i++) {
                try {
                    initListViews(Bimp.revitionImageSize(MyAdapter.mSelectedImage.get(i).getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //设置预览照片完成按钮能否被点击---设置文字
        isShowOkBt();
        pager.setOnPageChangeListener(pageChangeListener);
        location = position;

        adapter = new MyPageAdapter(listViews);
        pager.setAdapter(adapter);
        pager.setPageMargin((int) getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
        pager.setCurrentItem(location);
    }

    @Override
    public int getContentView() {
        return R.layout.plugin_camera_gallery;
    }

    /**
     * 初始化toolbar
     * 2016年8月17日
     * ZhaoDongShao
     */
    public void initToolbar() {
        tv_title.setVisibility(View.GONE);
        toolbar.setTitle("图片预览");
        tvBack.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 创建toolbar上的删除按钮
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_delete_menu, menu);
        return true;
    }

    /**
     * 当点击toolbar上的返回、删除按钮时
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //返回按钮
//                MyApplication.getInstance().finishActivity(GalleryActivity.this);
                finish();
                break;

            case R.id.delete: //删除按钮
                if (listViews.size() == 1) { // 如果点击一次删除后，图片数量为 1
                    if (type_bfjl) { //这时候是帮扶记activity传进来的预览照片
                        lists_bfjl.clear();
                        send_bt.setText("完成(" + (location + 1) + "/" + lists_bfjl.size() + ")");
                        //发送广播---通知帮扶记录activity页面，照片已经删除完
                        Intent intent = new Intent("bfjl_yulan_photo_complete");
                        intent.putExtra("lists_bfjl", (Serializable) lists_bfjl);
                        sendBroadcast(intent);
                    } else { //这时候是选择照片activity传进来的预览照片
                        MyAdapter.mSelectedImage.clear();
                        Bimp.max = 0;
                        send_bt.setText("完成(" + (location + 1) + "/" + MyAdapter.mSelectedImage.size() + ")");
                        Intent intent = new Intent("data.broadcast.action");
                        sendBroadcast(intent);
                    }
                    //设置预览照片完成按钮能否被点击
                    isShowOkBt();
                    finish();
                } else { // 如果点击一次删除后，图片数量还大于 1
                    if (type_bfjl) { //这时候是帮扶记activity传进来的预览照片
                        lists_bfjl.remove(location);
                        send_bt.setText("完成(" + (location + 1) + "/" + lists_bfjl.size() + ")");
                    } else { //这时候是选择照片activity传进来的预览照片
                        MyAdapter.mSelectedImage.remove(location);
                        Bimp.max--;
                        send_bt.setText("完成(" + (location + 1) + "/" + MyAdapter.mSelectedImage.size() + ")");
                    }
                    //设置预览照片完成按钮能否被点击
                    isShowOkBt();
                    pager.removeAllViews();
                    listViews.remove(location);
                    adapter.setListViews(listViews);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
        return true;
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
            int currentPostion = location + 1;
            if (type_bfjl) {
                send_bt.setText("完成(" + currentPostion + "/" + lists_bfjl.size() + ")");
            } else {
                send_bt.setText("完成(" + currentPostion + "/" + MyAdapter.mSelectedImage.size() + ")");
            }
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private void initListViews(Bitmap bm) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        PhotoView img = new PhotoView(this);
        img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    // 完成按钮的监听
    private class GallerySendListener implements OnClickListener {
        public void onClick(View v) {
            if (type_bfjl) { //如果是帮扶记录activity页面，预览照片完成
                //发送广播---通知帮扶记录activity页面，预览照片已经完成---把帮扶记录页面传过来的照片集合传回去
                Intent intent = new Intent("bfjl_yulan_photo_complete");
                intent.putExtra("lists_bfjl", (Serializable) lists_bfjl);
                sendBroadcast(intent);
            } else { //如果是选择照片activity页面，预览照片完成
                List<String> photos = new ArrayList<String>();
                for (int i = 0; i < MyAdapter.mSelectedImage.size(); i++) {
                    photos.add(MyAdapter.mSelectedImage.get(i).getPath());
                }
                //发送广播---通知帮扶记录activity页面，预览照片已经完成---把选择照片页面传过来的照片集合传回去
                Intent intent1 = new Intent("xzzp_yulan_photo_complete");
                intent1.putExtra("select_photo_paths", (ArrayList<String>) photos);
                sendBroadcast(intent1);
            }
            finish();
        }
    }

    /**
     * 设置预览照片完成按钮能否被点击
     */
    public void isShowOkBt() {
        if (type_bfjl) { //这时候是帮扶记activity传进来的预览照片
            if (null != lists_bfjl && lists_bfjl.size() > 0) {
                send_bt.setText("完成(" + (location + 1) + "/" + lists_bfjl.size() + ")");
                send_bt.setPressed(true);
                send_bt.setClickable(true);
                send_bt.setTextColor(Color.WHITE);
            } else {
                send_bt.setPressed(false);
                send_bt.setClickable(false);
                send_bt.setTextColor(Color.parseColor("#E1E0DE"));
            }
        } else { //这时候是选择照片activity传进来的预览照片
            if (MyAdapter.mSelectedImage.size() > 0) {
                send_bt.setText("完成(" + (location + 1) + "/" + MyAdapter.mSelectedImage.size() + ")");
                send_bt.setPressed(true);
                send_bt.setClickable(true);
                send_bt.setTextColor(Color.WHITE);
            } else {
                send_bt.setPressed(false);
                send_bt.setClickable(false);
                send_bt.setTextColor(Color.parseColor("#E1E0DE"));
            }
        }
    }

    /**
     * 监听返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            intent.setClass(GalleryActivity.this, SelectPhotoActivity.class);
            intent.putExtra(Common.INTENT_KEY, id);
            startActivity(intent);
        }
        return true;
    }

    class MyPageAdapter extends PagerAdapter {

        /**
         * 预览照片页面中把本地照片路径转换为imageView后的照片集合
         */
        private ArrayList<View> listViews;
        /**
         * ViewPager的数量
         */
        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);
            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

}
