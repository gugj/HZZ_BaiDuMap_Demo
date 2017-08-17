package com.roch.hzz_baidumap_demo.entity;

import java.util.List;

/**
 * 巡河记录Result
 * 作者：GuGaoJie
 * 时间：2017/7/5/005 14:03
 */
public class XunHeShangBao_Marker_Result extends BaseResult{

    private List<XunHeShangBao_Marker> jsondata;

    public List<XunHeShangBao_Marker> getJsondata() {
        return jsondata;
    }

    public void setJsondata(List<XunHeShangBao_Marker> jsondata) {
        this.jsondata = jsondata;
    }

}
