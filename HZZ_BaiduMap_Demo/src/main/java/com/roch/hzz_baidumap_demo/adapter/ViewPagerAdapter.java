package com.roch.hzz_baidumap_demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 自定义的ViewPager的适配器adapter，继承自FragmentStatePagerAdapter
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> list;
	
	/**
	 * 标题
	 */
	private String[] title;

	/**
	 * @param fm
	 */
	public ViewPagerAdapter(List<Fragment> list, FragmentActivity fm) {
		super(fm.getSupportFragmentManager());
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return title[position];
	}

	/**
	 * 设置导航栏的标题
	 */
	public void setTitle(String[] title) {
		this.title = title;
	}
}
