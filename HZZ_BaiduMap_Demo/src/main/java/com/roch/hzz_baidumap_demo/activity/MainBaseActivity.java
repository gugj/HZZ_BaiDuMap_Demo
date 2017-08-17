package com.roch.hzz_baidumap_demo.activity;

import android.os.Bundle;
import android.view.View;

import com.roch.hzz_baidumap_demo.R;

/**
 * 继承自BaseActivity，父类只有一个方法，即设置状态栏颜色：手机版本大于21即Android5.0时，先取消设置透明状态栏，然后设置自定义的状态栏颜色
 */
public class MainBaseActivity extends BaseActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tv_back:
				finish();
				break;
		}
	}

}
