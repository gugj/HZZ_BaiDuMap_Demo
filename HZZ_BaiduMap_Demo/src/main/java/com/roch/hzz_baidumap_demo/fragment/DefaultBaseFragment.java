package com.roch.hzz_baidumap_demo.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.MyApplication;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.HttpUtils;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.ToastUtils;

/**
 * Fragment基类
 * 
 * @author ZhaoDongShao
 * 2016年9月5日
 */
public class DefaultBaseFragment extends Fragment implements HttpUtils.SuccessResult{

	ProgressDialog mProgressDialog;
	Activity mActivity;
	Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.mActivity = getActivity();
		this.mContext = getContext();

		// 获取手机的屏幕密度DPI、屏幕的宽度和高度
		initDensityDpi();

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 获取手机的屏幕密度DPI、屏幕的宽度和高度
	 */
	private void initDensityDpi() {
		DisplayMetrics metrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Common.densityDpi = metrics.densityDpi;
		Common.Width = metrics.widthPixels;
		Common.Hight = metrics.heightPixels;
	}

	/*
	 * 显示进度条对话框--不可取消
	 */
	public void showProgressDialog(String msg) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setCancelable(false);
			mProgressDialog.setMessage(msg);
		}
		if (!mProgressDialog.isShowing())
			mProgressDialog.show();
	}

	/*
	 * 显示进度条对话框--可取消
	 */
	public void showProgressDialog(String msg, boolean cancleble) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setCancelable(cancleble);
			mProgressDialog.setMessage(msg);
		}
		if (!mProgressDialog.isShowing())
			mProgressDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void cancelProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.cancel();
	}

	/**
	 * 显示一般时间的吐司
	 */
	public void ShowToast(String msg) {
		ToastUtils.showNormalToast(getActivity(), msg);
	}

	/**
	 * 返回EditText所编辑的内容
	 * @param ed
	 * @return
	 */
	public String getEditText(EditText ed) {
		return ed.getText().toString().trim();
	}

	/**
	 * 返回TextView所显示的内容
	 * @param tv
	 * @return
	 */
	public String getTextView(TextView tv) {
		return tv.getText().toString().trim();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.println("DefaltBaseFragment取消请求网络");
		MyApplication.getInstance().getHttpUtilsInstance().canclePost(this);
	}

	@Override
	public void onSuccessResult(String str, int flag) {
	}

	@Override
	public void onFaileResult(String str, int flag) {
	}
}
