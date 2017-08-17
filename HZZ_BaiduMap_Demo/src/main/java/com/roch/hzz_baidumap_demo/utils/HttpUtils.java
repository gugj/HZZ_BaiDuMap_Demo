package com.roch.hzz_baidumap_demo.utils;

import com.roch.hzz_baidumap_demo.activity.BaseActivity;
import com.roch.hzz_baidumap_demo.fragment.DefaultBaseFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/3/003 15:43
 */
public class HttpUtils {

    private SuccessResult successResult;

    /**
     * Activity的Post请求
     * @param baseActivity
     * @param url
     * @param params
     * @param flag
     */
    public void post(final BaseActivity baseActivity,String url,Map<String,String> params,Map<String,File> fileParams, final int flag){
        baseActivity.showProgressDialog("正在加载中......", true);
        successResult= (SuccessResult) baseActivity;
        // 获取OkHttpUtils的请求参数的Builder，并添加请求参数----PostFormBuilder
        PostFormBuilder pfb= addBodyParams(url, params, fileParams);
                pfb
                  .tag(baseActivity)
                  .build()
                  .connTimeOut(10000)
                  .readTimeOut(30000)
                  .writeTimeOut(30000)
                  .execute(new StringCallback() {
                      @Override
                      public void onError(Call call, Exception e, int id) {
                          baseActivity.cancelProgressDialog();
                          successResult.onFaileResult(e.toString(), flag);
                      }

                      @Override
                      public void onResponse(String response, int id) {
                          baseActivity.cancelProgressDialog();
                          successResult.onSuccessResult(response, flag);
                      }
                  });
    }

    /**
     * Activity的Post请求
     * @param baseActivity
     * @param url
     * @param params
     * @param flag
     */
    public void post(final BaseActivity baseActivity,String url,Map<String,String> params,Map<String,File> fileParams, final int flag, final boolean isShowProgress){
        if(isShowProgress){
            baseActivity.showProgressDialog("正在加载中......", true);
        }
        successResult= (SuccessResult) baseActivity;
        // 获取OkHttpUtils的请求参数的Builder，并添加请求参数----PostFormBuilder
        PostFormBuilder pfb= addBodyParams(url, params, fileParams);
                pfb
                  .tag(baseActivity)
                  .build()
                  .connTimeOut(10000)
                  .readTimeOut(30000)
                  .writeTimeOut(30000)
                  .execute(new StringCallback() {
                      @Override
                      public void onError(Call call, Exception e, int id) {
                          if(isShowProgress){
                              baseActivity.cancelProgressDialog();
                          }
                          successResult.onFaileResult(e.toString(), flag);
                      }

                      @Override
                      public void onResponse(String response, int id) {
                          if(isShowProgress){
                              baseActivity.cancelProgressDialog();
                          }
                          successResult.onSuccessResult(response, flag);
                      }
                  });
    }

    /**
     * Fragment的Post请求
     * @param baseFragment
     * @param url
     * @param params
     * @param flag
     */
    public void post(final DefaultBaseFragment baseFragment,String url,Map<String,String> params,Map<String,File> fileParams, final int flag){
        ((BaseActivity)(baseFragment.getActivity())).showProgressDialog("正在加载中......", true);
        successResult= (SuccessResult) baseFragment;
        // 获取OkHttpUtils的请求参数的Builder，并添加请求参数----PostFormBuilder
        PostFormBuilder pfb= addBodyParams(url, params, fileParams);
                pfb
                  .tag(baseFragment)
                  .build()
                  .connTimeOut(10000)
                  .readTimeOut(30000)
                  .writeTimeOut(30000)
                  .execute(new StringCallback() {
                      @Override
                      public void onError(Call call, Exception e, int id) {
                          ((BaseActivity)(baseFragment.getActivity())).cancelProgressDialog();
                          successResult.onFaileResult(e.toString(), flag);
                      }

                      @Override
                      public void onResponse(String response, int id) {
                          ((BaseActivity)(baseFragment.getActivity())).cancelProgressDialog();
                          successResult.onSuccessResult(response, flag);
                      }
                  });
    }

    /**
     * 获取OkHttpUtils的请求参数的Builder，并添加请求参数----PostFormBuilder
     * @param url
     * @param params
     * @param fileParams
     * @return
     */
    public PostFormBuilder addBodyParams(String url, Map<String, String> params, Map<String, File> fileParams){
        // 打印请求参数
//        printParams(params);
        // 获取OkHttpUtils的请求参数的Builder----PostFormBuilder
        PostFormBuilder pfb=getPostFormBuilder(url);
        if(StringUtil.isNotEmpty(params) && params.size()>0){
            Set<String> strings = params.keySet();
            for (String key : strings){
                pfb.addParams(key,params.get(key));
                LogUtil.println("OkHttpUtils请求服务器时普通参数："+key+"==="+params.get(key));
            }
        }
        if (StringUtil.isNotEmpty(fileParams) && fileParams.size()>0){
            Set<String> strings = fileParams.keySet();
            for (String key: strings) {
                pfb.addFile("file",key,fileParams.get(key));
                LogUtil.println("OkHttpUtils上传图片时参数：" + key + "===" + fileParams.get(key));
            }
//              pfb.files("flie",fileParams);
        }
        return pfb;
    }

    /**
     * 打印请求参数
     * @param params
     */
    private void printParams(Map<String, String> params) {
        if(StringUtil.isNotEmpty(params) && params.size()>0){
            Set<String> strings = params.keySet();
            for (String key : strings){
                LogUtil.println("OkHttpUtils请求参数："+key+"=="+params.get(key));
            }
        }
    }

    public void canclePost(BaseActivity baseActivity){
        OkHttpUtils.getInstance().cancelTag(baseActivity);
        LogUtil.println("HttpUtils中的baseActivity取消请求网络");
    }

    public void canclePost(DefaultBaseFragment baseFragment){
        OkHttpUtils.getInstance().cancelTag(baseFragment);
        LogUtil.println("HttpUtils中的baseFragment取消请求网络");
    }

    /**
     * 获取OkHttpUtils的请求参数的Builder----PostFormBuilder
     * @param url
     * @return
     */
    public PostFormBuilder getPostFormBuilder(String url){
        return OkHttpUtils.getInstance().post().url(url);
    }

    /**
     * 自定义的接口----请求网络后的回调（请求成功或请求失败）
     */
   public interface SuccessResult {
        void onSuccessResult(String str, int flag);
        void onFaileResult(String str, int flag);
    }

}
