package me.iwf.photopicker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.event.OnItemCheckListener;
import me.iwf.photopicker.fragment.ImagePagerFragment;
import me.iwf.photopicker.fragment.PhotoPickerFragment;

import static android.widget.Toast.LENGTH_LONG;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_COLUMN_NUMBER;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_MAX_COUNT;
import static me.iwf.photopicker.PhotoPicker.EXTRA_GRID_COLUMN;
import static me.iwf.photopicker.PhotoPicker.EXTRA_MAX_COUNT;
import static me.iwf.photopicker.PhotoPicker.EXTRA_ORIGINAL_PHOTOS;
import static me.iwf.photopicker.PhotoPicker.EXTRA_PREVIEW_ENABLED;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_CAMERA;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;
import static me.iwf.photopicker.PhotoPicker.KEY_SELECTED_PHOTOS;

/**
 * 选择图片Activity
 */
public class PhotoPickerActivity extends BaseActivity {

  private PhotoPickerFragment pickerFragment;
  private ImagePagerFragment imagePagerFragment;
  private MenuItem menuDoneItem;

  private int maxCount = DEFAULT_MAX_COUNT;

  /** to prevent multiple calls to inflate menu */
  private boolean menuIsInflated = false;

  private boolean showGif = false;
  private int columnNumber = DEFAULT_COLUMN_NUMBER;
  private ArrayList<String> originalPhotos = null;
  private LinearLayout ll_top_toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    boolean showCamera      = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, true);
    boolean showGif         = getIntent().getBooleanExtra(EXTRA_SHOW_GIF, false);
    boolean previewEnabled  = getIntent().getBooleanExtra(EXTRA_PREVIEW_ENABLED, true);

    setShowGif(showGif);

    setContentView(R.layout.__picker_activity_photo_picker);

    ll_top_toolbar=(LinearLayout)findViewById(R.id.ll_top_toolbar);
    //设置顶部状态栏的颜色
    setStatusBarColor();

    Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);
    setTitle(R.string.__picker_title);

    ActionBar actionBar = getSupportActionBar();

    assert actionBar != null;
    actionBar.setDisplayHomeAsUpEnabled(true);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      actionBar.setElevation(25);
    }

    maxCount = getIntent().getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
    columnNumber = getIntent().getIntExtra(EXTRA_GRID_COLUMN, DEFAULT_COLUMN_NUMBER);
    originalPhotos = getIntent().getStringArrayListExtra(EXTRA_ORIGINAL_PHOTOS);

    pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
    if (pickerFragment == null) {
      pickerFragment = PhotoPickerFragment
          .newInstance(showCamera, showGif, previewEnabled, columnNumber, maxCount, originalPhotos);
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.container, pickerFragment, "tag")
          .commit();
      getSupportFragmentManager().executePendingTransactions();
    }

    pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
      @Override public boolean onItemCheck(int position, Photo photo, final int selectedItemCount) {

        menuDoneItem.setEnabled(selectedItemCount > 0);

        if (maxCount <= 1) {
          List<String> photos = pickerFragment.getPhotoGridAdapter().getSelectedPhotos();
          if (!photos.contains(photo.getPath())) {
            photos.clear();
            pickerFragment.getPhotoGridAdapter().notifyDataSetChanged();
          }
          return true;
        }

        if (selectedItemCount > maxCount) {
          Toast.makeText(getActivity(), getString(R.string.__picker_over_max_count_tips, maxCount),
              LENGTH_LONG).show();
          return false;
        }
        menuDoneItem.setTitle(getString(R.string.__picker_done_with_count, selectedItemCount, maxCount));
        return true;
      }
    });

  }

  /**
   * 设置顶部状态栏的颜色
   * 2016年11月3日
   */
  private void setStatusBarColor() {
    // 如果版本 >=21即Android 5.0
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

      ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
      View statusBarView = mContentView.getChildAt(0);
      // 移除假的 View
      if (statusBarView != null && statusBarView.getLayoutParams() != null
              && statusBarView.getLayoutParams().height == getStatusBarHeight()) {
        mContentView.removeView(statusBarView);
      }
      // 不预留空间
      if (mContentView.getChildAt(0) != null) {
        ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
      }
      LinearLayout.LayoutParams lParams = (android.widget.LinearLayout.LayoutParams) ll_top_toolbar.getLayoutParams();
      int height = getStatusBarHeight();
      lParams.height = height;

      ll_top_toolbar.setLayoutParams(lParams);
      ll_top_toolbar.setBackgroundColor(getResources().getColor(R.color.__picker_color_145bba)); // ResourceUtil.getInstance().getColorById(R.color.color_145bba)
    }

    // 如果版本 >=19即Android 4.4，<21即Android 5.0
    if (Build.VERSION_CODES.LOLLIPOP > Build.VERSION.SDK_INT
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      Window window = getWindow();
      ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
      // 首先使 ChildView 不预留空间
      View mChildView = mContentView.getChildAt(0);
      if (mChildView != null) {
        ViewCompat.setFitsSystemWindows(mChildView, false);
      }

      int statusBarHeight = getStatusBarHeight();
      // 需要设置这个 flag 才能设置状态栏
      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      // 避免多次调用该方法时,多次移除了 View
      if (mChildView != null && mChildView.getLayoutParams() != null
              && mChildView.getLayoutParams().height == statusBarHeight) {
        // 移除假的 View.
        mContentView.removeView(mChildView);
        mChildView = mContentView.getChildAt(0);
      }
      if (mChildView != null) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
        // 清除 ChildView 的 marginTop 属性
        if (lp != null && lp.topMargin >= statusBarHeight) {
          lp.topMargin -= statusBarHeight;
          mChildView.setLayoutParams(lp);
        }
      }
      LinearLayout.LayoutParams lParams = (android.widget.LinearLayout.LayoutParams) ll_top_toolbar.getLayoutParams();
      int height = getStatusBarHeight();
      lParams.height = height;
      ll_top_toolbar.setLayoutParams(lParams);
      ll_top_toolbar.setBackgroundColor(getResources().getColor(R.color.__picker_color_145bba)); // ResourceUtil.getInstance().getColorById(R.color.color_145bba)
    }

    // 如果版本小于19，即小于Android 4.4
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      ll_top_toolbar.setVisibility(View.GONE);
    }
  }

  /**
   * Overriding this method allows us to run our exit animation first, then exiting
   * the activity when it complete.
   */
  @Override public void onBackPressed() {
    if (imagePagerFragment != null && imagePagerFragment.isVisible()) {
      imagePagerFragment.runExitAnimation(new Runnable() {
        public void run() {
          if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
          }
        }
      });
    } else {
      super.onBackPressed();
    }
  }


  public void addImagePagerFragment(ImagePagerFragment imagePagerFragment) {
    this.imagePagerFragment = imagePagerFragment;
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.container, this.imagePagerFragment)
        .addToBackStack(null)
        .commit();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    if (!menuIsInflated) {
      getMenuInflater().inflate(R.menu.__picker_menu_picker, menu);
      menuDoneItem = menu.findItem(R.id.done);
      if (originalPhotos != null && originalPhotos.size() > 0) {
        menuDoneItem.setEnabled(true);
        menuDoneItem.setTitle(
                getString(R.string.__picker_done_with_count, originalPhotos.size(), maxCount));
      } else {
        menuDoneItem.setEnabled(false);
      }
      menuIsInflated = true;
      return true;
    }
    return false;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      super.onBackPressed();
      return true;
    }

    if (item.getItemId() == R.id.done) {
      Intent intent = new Intent();
      ArrayList<String> selectedPhotos = pickerFragment.getPhotoGridAdapter().getSelectedPhotoPaths();
      intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectedPhotos);
      setResult(RESULT_OK, intent);
      finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public PhotoPickerActivity getActivity() {
    return this;
  }

  public boolean isShowGif() {
    return showGif;
  }

  public void setShowGif(boolean showGif) {
    this.showGif = showGif;
  }
}
