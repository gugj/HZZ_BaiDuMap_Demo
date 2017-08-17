package com.roch.hzz_baidumap_demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.StringUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;
import com.roch.hzz_baidumap_demo.view.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改登录密码页面
 */
public class UpdatePassWordActivity extends CommonBaseActivity {

    @BindView(R.id.edt_old_pwd)
    ClearEditText edt_old_pwd;
    @BindView(R.id.edt_new_pwd)
    ClearEditText edt_new_pwd;
    @BindView(R.id.edt_new_pwd2)
    ClearEditText edt_new_pwd2;

    /**
     * 网络请求的参数---键值对
     */
    private Map<String, String> params = new HashMap<>();

    @Override
    public int getContentView() {
        MyApplication.getInstance().addActivity_login_out(this);
        return R.layout.activity_update_password;
    }

    @OnClick({R.id.btn_quxiao, R.id.btn_quding})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                MyApplication.getInstance().finishActivity_login_out(this);
                break;

            case R.id.btn_quxiao:
                MyApplication.getInstance().finishActivity_login_out(this);
                break;

            case R.id.btn_quding:
                //检查输入密码是否为空，并请求服务器修改密码
                checkIfNull();
                break;
        }
    }

    String old_pwd;
    String new_pwd;
    String new_pwd2;

    /**
     * 检查输入密码是否为空
     */
    private void checkIfNull() {
        old_pwd = getEditText(edt_old_pwd);
        new_pwd = getEditText(edt_new_pwd);
        new_pwd2 = getEditText(edt_new_pwd2);

        View focusView = null;
        boolean isNot = false; // 判断输入框是否为空

        if (StringUtil.isEmpty(old_pwd)) {
            focusView = edt_old_pwd;
            isNot = true;
            ShowToast("原始密码不能为空");
            return;
        }
        if (StringUtil.isEmpty(new_pwd)) {
            focusView = edt_new_pwd;
            isNot = true;
            ShowToast("新密码不能为空");
            return;
        }
        if (StringUtil.isEmpty(new_pwd2)) {
            focusView = edt_new_pwd2;
            isNot = true;
            ShowToast("再次输入新密码不能为空");
            return;
        }
        if (!new_pwd.equals(new_pwd2)) {
            ShowToast("两次输入密码不一致");
            return;
        }
        if (old_pwd.equals(new_pwd)) {
            ShowToast("新密码和原始密码不能一样");
            return;
        }
        if (isNot) {
            focusView.requestFocus();
            ShowToast("输入密码不能为空");
        } else {
            SharedPreferences sp = getSharedPreferences("loginactivty", Context.MODE_APPEND);
            String login_pwd = sp.getString("password", "");
            String userName = sp.getString("username", "");
            System.out.println("修改密码时获取登陆密码为：==" + login_pwd);
            if (!old_pwd.equals(login_pwd)) {
                ShowToast("原始密码不正确");
                return;
            }
            params.clear();
            params.put("userName", userName);
            params.put("password", new_pwd);
            MyApplication.getInstance().getHttpUtilsInstance().post(UpdatePassWordActivity.this, URLs.Updata_PassWord, params, null, MyConstans.FIRST);
            System.out.println("修改密码页面的网址为：===" + URLs.Updata_PassWord + "?password=" + new_pwd);
        }
    }

    @Override
    public void onSuccessResult(String str, int flag) {
        super.onSuccessResult(str, flag);
        LogUtil.println("密码修改成功：==" + str);
        //修改成功跳转到登陆页面
        MyApplication.getInstance().finishAllActivity_login_out();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFaileResult(String str, int flag) {
        super.onFaileResult(str, flag);
        ShowToast("密码修改失败");
        LogUtil.println("密码修改失败：==" + str);
    }

}
