package com.roch.hzz_baidumap_demo.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.roch.hzz_baidumap_demo.R;
import com.roch.hzz_baidumap_demo.utils.SharePreferencesUtil;

/**
 * 自定义的View---LinearLayout
 */
public class AutoClearSearchView extends LinearLayout implements OnClickListener {

	/**
	 * 返回键
	 */
	private TextView tv_back;

	/**
	 * 搜索图标
	 */
	private ImageView iv_search;

	/**
	 * 输入框---EditText
	 */
	private EditText input;

	/**
	 * 删除图标
	 */
	private ImageView ivDelete;

	/**
	 * 上下文对象
	 */
	private Context mContext;

	/**
	 * 历史搜索记录和自动补全记录---ListView
	 */
	private ListView lvTips;

	/**
	 * 历史搜索记录ListView的适配器
	 */
	private ArrayAdapter<String> mHintAdapter;

	/**
	 * 自动补全的ListView的适配器
	 */
	private ArrayAdapter<String> mAutoCompleteAdapter;

	/**
	 * 整个自定义SearchView的监听接口
	 */
	private SearchViewListener mListener;

	/**
	 * 设置整个自定义SearchView的监听接口
	 */
	public void setSearchViewListener(SearchViewListener listener) {
		mListener = listener;
	}

	public AutoClearSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.custom_auto_complete_view2, this);
		//初始化查找控件---设置历史搜索记录的条目点击监听；设置EditText的内容改变监听和软键盘的确认搜索监听
		initViews();
	}

	/**
	 * 初始化查找控件---设置历史搜索记录的条目点击监听；设置EditText的内容改变监听和软键盘的确认搜索监听
	 */
	private void initViews() {
		input = (EditText) findViewById(R.id.custom_auto_complete_tv);
		lvTips = (ListView) findViewById(R.id.custom_auto_complete_listview);
		ivDelete = (ImageView) findViewById(R.id.custom_auto_complete_clear_text);
		iv_search = (ImageView) findViewById(R.id.iv_search);
		tv_back = (TextView) findViewById(R.id.tv_back);
		ivDelete.setVisibility(GONE);
		lvTips.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String item = lvTips.getAdapter().getItem(position).toString();
				input.setText(item);
				input.setSelection(item.length());
				lvTips.setVisibility(View.GONE);
				//通知监听者---进行搜索操作(已经选中了历史搜索记录中的某一条)
				notifyStartSearching(item);
			}
		});
		ivDelete.setOnClickListener(this);
		iv_search.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		//给EditText设置内容改变的监听
		input.addTextChangedListener(new EditChangedListener());
		input.setOnClickListener(this);
		//当点击软键盘上的回车键---即确认搜索键时
		input.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					lvTips.setVisibility(View.GONE);
					//通知监听者---进行搜索操作(已经选中了历史搜索记录中的某一条)
					notifyStartSearching(input.getText().toString());
				}
				return true;
			}
		});
	}

	public class EditChangedListener implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (!"".equals(s.toString())) { //输入框EditText有内容----显示自动补全搜索记录
				ivDelete.setVisibility(VISIBLE);
				lvTips.setVisibility(VISIBLE);
				if (mAutoCompleteAdapter != null && lvTips.getAdapter() != mAutoCompleteAdapter) {
					lvTips.setAdapter(mAutoCompleteAdapter);
				}
				// 更新autoComplete数据
				if (mListener != null) {
					mListener.onRefreshAutoComplete(s + "");
				}
			} else { //输入框EditText没有内容-----显示历史搜索记录
				ivDelete.setVisibility(GONE);
				if (mHintAdapter != null) {
					lvTips.setAdapter(mHintAdapter);
				}
				lvTips.setVisibility(GONE);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	}

	/**
	 * 获取输入框中的内容
	 * @return
	 */
	public String getEtInput(){
		return input.getText().toString().trim();
	}

	/**
	 * 获取输入框是否有焦点
	 * @return
	 */
	public boolean getHasFoucs(){
		return input.hasFocus();
	}

	/**
	 * 通知监听者---进行搜索操作(已经选中了历史搜索记录中的某一条)
	 * @param item
	 */
	protected void notifyStartSearching(String item) {
		if (mListener != null) {
			mListener.onSearch(input.getText().toString().trim());
		}
		// 隐藏软键盘
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 整个自定义SearchView的监听接口
	 */
	public interface SearchViewListener {

		/**
		 * 输入框内容改变时自动追加输入的内容
		 * @param text 传入补全后的文本
		 */
		void onRefreshAutoComplete(String text);

		/**
		 * 开始搜索
		 * @param text 传入输入框的文本
		 */
		void onSearch(String text);

		/**
		 * 关闭当前activity
		 */
		void CloseActivity();

		/**
		 * 输入框有焦点
		 */
		void hasFoucs();
	}

	/**
	 * 设置历史搜索记录ListView的适配器
	 */
	public void setTipsHintAdapter(ArrayAdapter<String> adapter) {
		this.mHintAdapter = adapter;
		if (lvTips.getAdapter() == null) {
			lvTips.setAdapter(mHintAdapter);
		}
	}

	/**
	 * 设置自动补全的ListView的适配器
	 */
	public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {
		this.mAutoCompleteAdapter = adapter;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_auto_complete_listview:
			lvTips.setVisibility(VISIBLE);
			break;
			
		case R.id.custom_auto_complete_clear_text: //点击清除输入内容的图标
			input.setText("");
			ivDelete.setVisibility(GONE);
			break;
			
		case R.id.iv_search: //点击搜索按钮
			mListener.onSearch(input.getText().toString().trim());
			lvTips.setVisibility(GONE);
			//保存历史搜索记录
			saveHistory();
			break;
			
		case R.id.tv_back: //点击返回按钮
			mListener.CloseActivity();
			break;
		}
	}

	/**
	 * 获取历史搜索记录和自动补全记录---ListView
	 * @return
	 */
	public ListView getLvTips(){
		return lvTips;
	}

	/**
	 * 保存历史搜索记录
	 */
	private void saveHistory() {
		String text = input.getText().toString().trim();
		String long_history = SharePreferencesUtil.getHistory(mContext);
		if (!long_history.contains(text + ",")) {
			StringBuilder sb = new StringBuilder(long_history);
			sb.insert(0, text + ",");
			SharePreferencesUtil.saveHistory(sb.toString(), mContext);
		}
	}

	/**
	 * 隐藏返回按钮
	 */
	public void getVisibleisGone() {
		tv_back.setVisibility(View.GONE);
	}
}
