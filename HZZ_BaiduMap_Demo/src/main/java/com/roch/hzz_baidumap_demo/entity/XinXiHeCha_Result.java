package com.roch.hzz_baidumap_demo.entity;

import java.util.List;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/5/005 14:03
 */
public class XinXiHeCha_Result extends BaseResult{

    private List<XinXiHeCha> jsondata;

    public List<XinXiHeCha> getJsondata() {
        return jsondata;
    }

    public void setJsondata(List<XinXiHeCha> jsondata) {
        this.jsondata = jsondata;
    }

}
