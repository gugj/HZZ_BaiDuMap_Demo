package com.roch.hzz_baidumap_demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类
 * @author ZhaoDongShao
 * 2016年9月5日
 */
public abstract class BaseFragment extends DefaultBaseFragment {

	Activity mActivity;
	Context mContext;
	Unbinder unbinder;
	Bundle bundle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		super.onCreateView(inflater,container,savedInstanceState);
		this.mActivity = getActivity();
		this.mContext = getContext();

		// 1.获取该Fragment的布局View
		View contentView = getContenView(mActivity);
		//在contentView中查找View控件
		unbinder = ButterKnife.bind(this, contentView);
		// 2.初始化Bundle信息
		initBundle();
		// 3.初始化控件的监听
		initListener();
		// 4.初始化数据
		initData();
		return contentView;
	}

	/**
	 * 1.获取该Fragment的布局View
	 */
	protected abstract View getContenView(Activity mActivity);

	/**
	 * 2.初始化Bundle信息
	 */
	private void initBundle() {
		bundle = getArguments();
	}

	/**
	 * 3.初始化控件的监听
	 */
	private void initListener() {
	}

	/**
	 * 4.初始化数据
	 */
	protected abstract void initData();

}
