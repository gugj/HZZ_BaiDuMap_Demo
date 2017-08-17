package com.roch.hzz_baidumap_demo.entity;

import java.util.List;

/**
 * 巡河记录Result
 * 作者：GuGaoJie
 * 时间：2017/7/5/005 14:03
 */
public class XunHeJiLu_Result extends BaseResult{

    private List<XunHeJiLu> jsondata;

    public List<XunHeJiLu> getJsondata() {
        return jsondata;
    }

    public void setJsondata(List<XunHeJiLu> jsondata) {
        this.jsondata = jsondata;
    }

}
