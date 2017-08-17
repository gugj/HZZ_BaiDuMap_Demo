package com.roch.hzz_baidumap_demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roch.hzz_baidumap_demo.activity.CommonBaseActivity;
import com.roch.hzz_baidumap_demo.adapter.HeDaoXuanZeAdapter;
import com.roch.hzz_baidumap_demo.adapter.XinXiHeChaDetailAdapter;
import com.roch.hzz_baidumap_demo.dialog.NormalDailog;
import com.roch.hzz_baidumap_demo.entity.HeDao;
import com.roch.hzz_baidumap_demo.entity.HeDao_Result;
import com.roch.hzz_baidumap_demo.entity.LoginUser;
import com.roch.hzz_baidumap_demo.entity.MapEntity;
import com.roch.hzz_baidumap_demo.utils.Common;
import com.roch.hzz_baidumap_demo.utils.DataCleanManager;
import com.roch.hzz_baidumap_demo.utils.LogUtil;
import com.roch.hzz_baidumap_demo.utils.MyConstans;
import com.roch.hzz_baidumap_demo.utils.SharePreferencesUtil;
import com.roch.hzz_baidumap_demo.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/27/027 11:28
 */
public class SettingActivity extends CommonBaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_geren_ziliao)
    ListView lvGerenZiliao;
    @BindView(R.id.rl_guanxia_hedao)
    RelativeLayout rlGuanxiaHedao;
    @BindView(R.id.lv_guanxia_hedao)
    ListView lvGuanxiaHedao;
    @BindView(R.id.rl_clear)
    RelativeLayout rlClear;
    @BindView(R.id.rl_update_pwd)
    RelativeLayout rlUpdatePwd;
    @BindView(R.id.tv_size)
    TextView tvSize;
    private XinXiHeChaDetailAdapter adapter;

    /**
     * 网络请求的参数---键值对
     */
    private Map<String, String> params = new HashMap<>();

    /**
     * 0.获取该Activity中的布局View
     */
    @Override
    public int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    public void initListener() {
        super.initListener();
        MyApplication.getInstance().addActivity_login_out(this);
        lvGuanxiaHedao.setOnItemClickListener(this);
    }

    /**
     * 5.初始化数据
     */
    @Override
    public void initData() {
        initCacheSize();

        LoginUser loginUser = SharePreferencesUtil.getLoginUser(this, Common.Login_User_Name);
        if (null != loginUser) {
            String loginUserId = loginUser.getId();
            params.clear();
            // 获取请求服务器的基础查询条件参数：1.page;2.typeAdcd
//            params.put("page", String.valueOf(current_page));
            params.put("division", loginUserId);
            MyApplication.getInstance().getHttpUtilsInstance().post(SettingActivity.this, URLs.HeZhang_GuanXia_HeDao_List, params, null, MyConstans.FIRST);
            System.out.println("个人资料页面时请求管辖河道数据的网址为：==" + URLs.HeZhang_GuanXia_HeDao_List);

            List<MapEntity> mapEntities = new ArrayList<MapEntity>();
            MapEntity mapEntity = null;

            mapEntity = new MapEntity("登陆名", loginUser.getUserName());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("用户名", loginUser.getRealName());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("部门", loginUser.getDepartname());
            mapEntities.add(mapEntity);
            mapEntity = new MapEntity("职位", loginUser.getEmail());
            mapEntities.add(mapEntity);
            switch (loginUser.getOrgType()) {
                case "1":
                    mapEntity = new MapEntity("河长级别", "市级河长");
                    mapEntities.add(mapEntity);
                    break;

                case "2":
                    mapEntity = new MapEntity("河长级别", "县级河长");
                    mapEntities.add(mapEntity);
                    break;

                case "3":
                    mapEntity = new MapEntity("河长级别", "乡级河长");
                    mapEntities.add(mapEntity);
                    break;

                case "4":
                    mapEntity = new MapEntity("河长级别", "村级河长");
                    mapEntities.add(mapEntity);
                    break;

                case "11":
                    mapEntity = new MapEntity("河长级别", "巡河人员");
                    mapEntities.add(mapEntity);
                    break;

                case "21":
                    mapEntity = new MapEntity("河长级别", "一般用户");
                    mapEntities.add(mapEntity);
                    break;
            }
            mapEntity = new MapEntity("联系方式", loginUser.getMobilePhone());
            mapEntities.add(mapEntity);

            adapter = new XinXiHeChaDetailAdapter(this, mapEntities);
            lvGerenZiliao.setAdapter(adapter);
        }
    }

    /**
     * 清除缓存
     */
    private void initCacheSize() {
        String path = Environment.getExternalStorageDirectory().getPath() + Common.CACHE_PATHE;
        File file = new File(path);
        try {
            String size = DataCleanManager.getCacheSize(file);
            tvSize.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 河长对应的河道列表
     */
    List<HeDao> heDaos;
    HeDaoXuanZeAdapter heDaoXuanZeAdapter;

    @Override
    public void onSuccessResult(String str, int flag) {
        switch (flag) {
            case MyConstans.FIRST:
                LogUtil.println("请求管辖河道成功===" + str);
                try {
                    JSONArray array = new JSONArray(str);
                    JSONObject jsonObject = array.getJSONObject(0);
                    String str1 = jsonObject.toString();

                    HeDao_Result heDao_result = HeDao_Result.parseToT(str1, HeDao_Result.class);
                    if (null != heDao_result && heDao_result.getSuccess()) {
                        heDaos = heDao_result.getJsondata();
                        if (null != heDaos) {
                            heDaoXuanZeAdapter = new HeDaoXuanZeAdapter(SettingActivity.this, heDaos,2);
                            lvGuanxiaHedao.setAdapter(heDaoXuanZeAdapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFaileResult(String str, int flag) {
        switch (flag) {
            case MyConstans.FIRST:
                ShowToast("请求管辖河道失败");
                LogUtil.println("请求管辖河道失败===" + str);
                break;
        }
    }

    @OnClick({R.id.rl_clear, R.id.rl_update_pwd, R.id.btn_login_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                MyApplication.getInstance().finishActivity_login_out(this);
                break;

            case R.id.rl_clear:
                // 显示清除缓存的对话框
                showClearCachePopupWindow();
                break;

            case R.id.rl_update_pwd:
                // 进入修改密码界面
                Intent intent=new Intent(this, UpdatePassWordActivity.class);
                intent.putExtra(Common.TITLE_KEY,"修改密码");
                startActivity(intent);
                break;

            case R.id.btn_login_out:
                // 显示退出登录的对话框
                showLoginOutPopupWindow();
                break;
        }
    }

    /**
     * 显示退出登录的对话框
     */
    private void showLoginOutPopupWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("是否退出当前登录用户？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplication.getInstance().finishAllActivity_login_out();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.create().show();
    }

    /**
     * 显示清除缓存的对话框
     */
    private void showClearCachePopupWindow() {
        final NormalDailog dailog = new NormalDailog(this,R.style.popup_dialog_style);
        dailog.show();
        dailog.setContentText("确定清除缓存数据吗？");
        dailog.setOnClickLinener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.normal_dialog_done:
                        showProgressDialog("正在清除...");
                        DataCleanManager.cleanExternalCache(SettingActivity.this);
                        DataCleanManager.cleanSharedPreference(SettingActivity.this);
                        DataCleanManager.cleanInternalCache(SettingActivity.this);
                        DataCleanManager.cleanFiles(SettingActivity.this);
                        initCacheSize();
                        cancelProgressDialog();
                        dailog.dismiss();
                        ShowToast("清除缓存成功");
                        break;

                    case R.id.normal_dialog_cancel:
                        dailog.dismiss();
                        break;
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, HeDaoZiLiaoDetailActivity.class);
        intent.putExtra("hedao", heDaos.get(position));
        intent.putExtra(Common.TITLE_KEY, "管辖河道详情");
        startActivity(intent);
    }

}
