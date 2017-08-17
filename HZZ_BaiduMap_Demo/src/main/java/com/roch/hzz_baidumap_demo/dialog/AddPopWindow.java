package com.roch.hzz_baidumap_demo.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.activity.BaseActivity;
import com.roch.hzz_baidumap_demo.adapter.PopuMenuListAdapter;
import com.roch.hzz_baidumap_demo.entity.ListMenu;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

public class AddPopWindow extends PopupWindow {

	private Activity context;
	private View conentView;
	PopuMenuListAdapter menuListAdapter;
	private ShowMessageListener showMessageListener;

	private int width;
	// 用于保存PopupWindow的高度
	private int height;

	public void setShowMessageListener(ShowMessageListener showMessageListener) {
		this.showMessageListener = showMessageListener;
	}

	/**
	 * @param context 上下文
	 * @param type_selection_conut 类型为1，说明是只有 帮扶记录 1个选项;类型为2，说明是有 市级、县级、乡级、村级、小微水体 5个选项;类型为3，说明是有 照片和拍照 2个选项
	 */
	@SuppressLint("InflateParams")
	public AddPopWindow(final Activity context, int type_selection_conut) {
		super(context);
		this.context=context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.popuwindow_menu, null,false);
		// 设置SelectPicPopupWindow的View  
		this.setContentView(conentView);  
		// 设置SelectPicPopupWindow弹出窗体的宽  
		this.setWidth((int) (Common.Width * 0.3));
		// 设置SelectPicPopupWindow弹出窗体的高  
		this.setHeight(LayoutParams.WRAP_CONTENT);  
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);  
		this.setOutsideTouchable(true);  
		// 刷新状态  
		this.update();  
		// 实例化一个ColorDrawable颜色为半透明  
//		ColorDrawable dw = new ColorDrawable(0000000000);  
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		if(type_selection_conut==2){
			this.setBackgroundDrawable(ResourceUtil.getInstance().getDrawableByID(R.drawable.popup_bg)); //pop_info_window_1
			// 设置SelectPicPopupWindow弹出窗体动画效果
			this.setAnimationStyle(R.style.AnimationPreview);
			// 绘制
			this.mandatoryDraw();
		}else if(type_selection_conut==3){
			this.setBackgroundDrawable(ResourceUtil.getInstance().getDrawableByID(R.drawable.pop_info_window_1)); //pop_info_window_1
			this.setAnimationStyle(R.style.AnimationPreview_2);
		}

		ListView listView = (ListView) conentView.findViewById(R.id.lv_menu);
		List<ListMenu> list = new ArrayList<ListMenu>();
		ListMenu listMenu = null;

		if(type_selection_conut==1){ //类型为1，说明是只有 帮扶记录 1个选项
			listMenu = new ListMenu();
			listMenu.setName("帮扶记录");
			listMenu.setRid(0);
			list.add(listMenu);
		}else if(type_selection_conut==2){ //类型为2，说明是有 市级、县级、乡级、村级、小微水体 5个选项
			listMenu = new ListMenu();
			listMenu.setName("市级河道");
//			listMenu.setRid(R.drawable.ico_photo);
			list.add(listMenu);

			listMenu = new ListMenu();
			listMenu.setName("县级河道");
//			listMenu.setRid(R.drawable.ico_cream);
			list.add(listMenu);

			listMenu = new ListMenu();
			listMenu.setName("乡级河道");
//			listMenu.setRid(R.drawable.ico_cream);
			list.add(listMenu);

			listMenu = new ListMenu();
			listMenu.setName("村级河道");
//			listMenu.setRid(R.drawable.ico_cream);
			list.add(listMenu);

			listMenu = new ListMenu();
			listMenu.setName("小微水体");
//			listMenu.setRid(R.drawable.ico_cream);
			list.add(listMenu);
		}else if(type_selection_conut==3){ //类型为3，说明是有 照片和拍照 2个选项
			listMenu = new ListMenu();
			listMenu.setName("照片");
			listMenu.setRid(R.drawable.ico_photo);
			list.add(listMenu);

			listMenu = new ListMenu();
			listMenu.setName("拍照");
			listMenu.setRid(R.drawable.ico_cream);
			list.add(listMenu);
		}

		menuListAdapter = new PopuMenuListAdapter(list, context);
		listView.setAdapter(menuListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListMenu listMenu = (ListMenu)parent.getItemAtPosition(position);
				AddPopWindow.this.dismiss();
				showMessageListener.Message(listMenu);
			}
		});
	}

	/**
	 * 强制绘制popupWindowView，并且初始化popupWindowView的尺寸
	 */
	private void mandatoryDraw() {
		this.conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		/**
		 * 强制刷新后拿到PopupWindow的宽高
		 */
		this.width = this.conentView.getMeasuredWidth();
		this.height = this.conentView.getMeasuredHeight();
	}

	/** 
	 * 显示popupWindow
	 * @param parent 
	 */  
	public void showPopupWindow(View parent,int pox) {  
		if (!this.isShowing()) {  
			// 以下拉方式显示Popupwindow  
			this.showAsDropDown(parent, pox, 10);
		} else {  
			this.dismiss();  
		}  
	}

	/**
	 * 显示在控件的下中方
	 */
	public void showAtDropDownCenter(View parent,View toolBar) {
		if (parent.getVisibility() == View.GONE) {
			this.showAtLocation(parent, 0, 0, 0);
		} else {

			// x y
			int[] location = new int[2];
			int[] location2 = new int[2];
			//获取在整个屏幕内的绝对坐标
			parent.getLocationOnScreen(location);
			toolBar.getLocationOnScreen(location2);

			LogUtil.println("筛选图标的宽度的一半为：==" + parent.getWidth() / 2);
			LogUtil.println("布局View的宽度的一半为：=="+ (Common.Width * 0.3)/2);

			this.showAtLocation(parent, 0, (int) ((location[0] + parent.getWidth() / 2)-(Common.Width * 0.3)/2), // - this.width / 2
					toolBar.getHeight() + ((BaseActivity) this.showMessageListener).getStatusBarHeight()); //location[1] + parent.getHeight()+20  +((BaseActivity)this.showMessageListener).getStatusBarHeight())
			LogUtil.println("PopupWindow的X轴坐标点为：=="+ ((location[0] + parent.getWidth() / 2)-(Common.Width * 0.3)/2));
		}
	}

	public interface ShowMessageListener {
		/**
		 * 显示信息
		 * @param object
		 */
		void Message(Object object);
	}

}
