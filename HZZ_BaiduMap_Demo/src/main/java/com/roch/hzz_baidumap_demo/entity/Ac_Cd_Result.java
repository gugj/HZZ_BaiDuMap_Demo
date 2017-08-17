package com.roch.hzz_baidumap_demo.entity;

import java.util.List;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/18/018 15:37
 */
public class Ac_Cd_Result extends BaseResult {

    private List<Ad_Cd> jsondata;

    public List<Ad_Cd> getJsondata() {
        return jsondata;
    }

    public void setJsondata(List<Ad_Cd> jsondata) {
        this.jsondata = jsondata;
    }

}
