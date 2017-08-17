/**
 * 
 */
package com.roch.hzz_baidumap_demo.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class ImageViewPager extends ViewPager {

	private static final String TAG = "ImageViewPager";

	public ImageViewPager(Context context) {
		super(context);
	}

	public ImageViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			//不理会
			Log.e(TAG,"hacky viewpager error1");
			return false;
		}catch(ArrayIndexOutOfBoundsException e ){
			//不理会
			Log.e(TAG,"hacky viewpager error2");
			return false;
		}
	}

}
