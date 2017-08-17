package com.roch.hzz_baidumap_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.roch.hzz_baidumap_demo.utils.SharePreferencesUtil;
import com.roch.hzz_baidumap_demo.utils.ToastUtils;
import com.roch.hzz_baidumap_demo.view.AutoClearSearchView;
import com.roch.hzz_baidumap_demo.view.ClearEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/20/020 11:46
 */
public class MainActivity_SearchView_AutoComplete extends AppCompatActivity implements View.OnClickListener, AutoClearSearchView.SearchViewListener {

//    private ClearEditText edt_username;
    private AutoClearSearchView edt_username;
    private ClearEditText edt_password;
    private TextView tv_server;
    private Button btn_login;
    ArrayAdapter<String> arrayAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview_autocomplete);

        initView();
    }

    private void initView() {
        edt_username = (AutoClearSearchView) findViewById(R.id.edt_username);
        edt_password = (ClearEditText) findViewById(R.id.edt_password);
        tv_server = (TextView) findViewById(R.id.tv_server);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
        edt_username.setSearchViewListener(this);
        edt_username.setAutoCompleteAdapter(getArrayAdapter());
    }

    private ArrayAdapter<String> getArrayAdapter(){
        if(null==arrayAdapter){
           return arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,historys_fit);
        }
        return arrayAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String username = edt_username.getEtInput();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "username不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = edt_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "password不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        edt_username.getLvTips().setVisibility(View.GONE);
        //保存历史搜索记录
        saveHistory();

    }

    /**
     * 保存历史搜索记录
     */
    private void saveHistory() {
        String text = edt_username.getEtInput();
        String long_history = SharePreferencesUtil.getHistory(this);
        if (!long_history.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(long_history);
            sb.insert(0, text + ",");
            SharePreferencesUtil.saveHistory(sb.toString(), this);

            ToastUtils.show("保存历史搜索记录成功");
        }
    }

    @Override
    public void onRefreshAutoComplete(String text) {
        String long_history = SharePreferencesUtil.getHistory(this);
        System.out.println("获取到历史搜索记录===="+long_history);
        String[] strings = long_history.split(",");
        historys.clear();
        if(null!=strings && strings.length>0){
            for (int i = 0; i < strings.length; i++) {
                historys.add(strings[i]);
            }
        }
        historys_fit.clear();
        if(historys.size()>0){
            for (int i = 0; i < historys.size(); i++) {
                if(historys.get(i).startsWith(text)){
                    historys_fit.add(historys.get(i));
                }
            }
        }
        getArrayAdapter().notifyDataSetChanged();
//        edt_username.setAutoCompleteAdapter(arrayAdapter);
    }

    @Override
    public void onSearch(String text) {
        ToastUtils.show("开始进行搜索.......");
    }

    @Override
    public void CloseActivity() {
    }

    private List<String> historys=new ArrayList<>();
    private List<String> historys_fit=new ArrayList<>();
    @Override
    public void hasFoucs() {
    }

}
