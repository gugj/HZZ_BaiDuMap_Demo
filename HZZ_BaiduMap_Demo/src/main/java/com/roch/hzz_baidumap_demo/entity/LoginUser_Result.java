package com.roch.hzz_baidumap_demo.entity;

import java.util.List;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/14/014 16:06
 */
public class LoginUser_Result extends BaseResult {

    private List<LoginUser> jsondata;

    public List<LoginUser> getJsondata() {
        return jsondata;
    }

    public void setJsondata(List<LoginUser> jsondata) {
        this.jsondata = jsondata;
    }

}
