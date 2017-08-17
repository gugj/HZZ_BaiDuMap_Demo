package com.roch.hzz_baidumap_demo.entity;

import java.util.List;

/**
 * 河道Result
 * 作者：GuGaoJie
 * 时间：2017/7/5/005 14:03
 */
public class HeDao_Result extends BaseResult{

    private List<HeDao> jsondata;

    public List<HeDao> getJsondata() {
        return jsondata;
    }

    public void setJsondata(List<HeDao> jsondata) {
        this.jsondata = jsondata;
    }

}
