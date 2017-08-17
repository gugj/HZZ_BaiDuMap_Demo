package me.iwf.photopicker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import me.iwf.photopicker.fragment.ImagePagerFragment;

import static me.iwf.photopicker.PhotoPicker.KEY_SELECTED_PHOTOS;
import static me.iwf.photopicker.PhotoPreview.EXTRA_CURRENT_ITEM;
import static me.iwf.photopicker.PhotoPreview.EXTRA_PHOTOS;
import static me.iwf.photopicker.PhotoPreview.EXTRA_SHOW_DELETE;

/**
 * 图片预览界面Activity
 * Created by donglua on 15/6/24.
 */
public class PhotoPagerActivity extends BaseActivity { // AppCompatActivity

  private ImagePagerFragment pagerFragment;

  private ActionBar actionBar;
  private boolean showDelete;
  private LinearLayout ll_top_toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.__picker_activity_photo_pager);

    ll_top_toolbar=(LinearLayout)findViewById(R.id.ll_top_toolbar);
    //设置顶部状态栏的颜色
    setStatusBarColor();

    int currentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);
    List<String> paths = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
    showDelete = getIntent().getBooleanExtra(EXTRA_SHOW_DELETE, true);

    if (pagerFragment == null) {
      pagerFragment =
          (ImagePagerFragment) getSupportFragmentManager().findFragmentById(R.id.photoPagerFragment);
    }
    pagerFragment.setPhotos(paths, currentItem);


    Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);

//    ActionBar actionBar = getSupportActionBar();
//    if (actionBar != null) {
//      actionBar.setDisplayHomeAsUpEnabled(true);
//      actionBar.setDisplayShowTitleEnabled(false);
//    }
//    initToolbar();

    actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      updateActionBarTitle();
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        actionBar.setElevation(25);
      }
    }

    pagerFragment.getViewPager().addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        updateActionBarTitle();
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

//  TextView tv_back;
//  private void initToolbar() {
//    tv_back = (TextView) findViewById(R.id.tv_back);
//    TextView tv_delete = (TextView) findViewById(R.id.tv_delete);
//
//    tv_back.setVisibility(View.VISIBLE);
//    tv_back.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        if(v.getId()==R.id.tv_back){
////          onBackPressed();
//          Intent intent = new Intent();
//          intent.putExtra(KEY_SELECTED_PHOTOS, pagerFragment.getPaths());
//          setResult(RESULT_OK, intent);
//          finish();
//        }
//      }
//    });
//
//    tv_delete.setVisibility(View.VISIBLE);
//    tv_delete.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        if(v.getId()==R.id.tv_delete){
//          final int index = pagerFragment.getCurrentItem();
//
//          final String deletedPath =  pagerFragment.getPaths().get(index);
//
//          Snackbar snackbar = Snackbar.make(pagerFragment.getView(), R.string.__picker_deleted_a_photo,
//                  Snackbar.LENGTH_LONG);
//
//          if (pagerFragment.getPaths().size() <= 1) {
//
//            // show confirm dialog
//            new AlertDialog.Builder(PhotoPagerActivity.this)
//                    .setTitle(R.string.__picker_confirm_to_delete)
//                    .setPositiveButton(R.string.__picker_yes, new DialogInterface.OnClickListener() {
//                      @Override public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                        pagerFragment.getPaths().remove(index);
//                        pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
//                        onBackPressed();
//                      }
//                    })
//                    .setNegativeButton(R.string.__picker_cancel, new DialogInterface.OnClickListener() {
//                      @Override public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                      }
//                    })
//                    .show();
//
//          } else {
//
//            snackbar.show();
//
//            pagerFragment.getPaths().remove(index);
//            pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
//          }
//
//          snackbar.setAction(R.string.__picker_undo, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              if (pagerFragment.getPaths().size() > 0) {
//                pagerFragment.getPaths().add(index, deletedPath);
//              } else {
//                pagerFragment.getPaths().add(deletedPath);
//              }
//              pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
//              pagerFragment.getViewPager().setCurrentItem(index, true);
//            }
//          });
//        }
//      }
//    });
//  }


  @Override public boolean onCreateOptionsMenu(Menu menu) {
    if (showDelete){
      getMenuInflater().inflate(R.menu.__picker_menu_preview, menu);
    }
    return true;
  }


  @Override public void onBackPressed() {

    Intent intent = new Intent();
    intent.putExtra(KEY_SELECTED_PHOTOS, pagerFragment.getPaths());
    setResult(RESULT_OK, intent);
    finish();

    super.onBackPressed();
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    }

    if (item.getItemId() == R.id.delete) {
      final int index = pagerFragment.getCurrentItem();

      final String deletedPath =  pagerFragment.getPaths().get(index);

      Snackbar snackbar = Snackbar.make(pagerFragment.getView(), R.string.__picker_deleted_a_photo,
          Snackbar.LENGTH_LONG);

      if (pagerFragment.getPaths().size() <= 1) {

        // show confirm dialog
        new AlertDialog.Builder(this)
            .setTitle(R.string.__picker_confirm_to_delete)
            .setPositiveButton(R.string.__picker_yes, new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                pagerFragment.getPaths().remove(index);
                pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
                onBackPressed();
              }
            })
            .setNegativeButton(R.string.__picker_cancel, new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            })
            .show();

      } else {

        snackbar.show();

        pagerFragment.getPaths().remove(index);
        pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
      }

      snackbar.setAction(R.string.__picker_undo, new View.OnClickListener() {
        @Override public void onClick(View view) {
          if (pagerFragment.getPaths().size() > 0) {
            pagerFragment.getPaths().add(index, deletedPath);
          } else {
            pagerFragment.getPaths().add(deletedPath);
          }
          pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
          pagerFragment.getViewPager().setCurrentItem(index, true);
        }
      });

      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public void updateActionBarTitle() {
    if (actionBar != null) actionBar.setTitle(
        getString(R.string.__picker_image_index, pagerFragment.getViewPager().getCurrentItem() + 1,
            pagerFragment.getPaths().size()));

    System.out.println("图片索引为==="+getString(R.string.__picker_image_index, pagerFragment.getViewPager().getCurrentItem() + 1,
            pagerFragment.getPaths().size()));
//    tv_back.setText(
//            getString(R.string.__picker_image_index, pagerFragment.getViewPager().getCurrentItem() + 1,
//                    pagerFragment.getPaths().size()));
  }
}
