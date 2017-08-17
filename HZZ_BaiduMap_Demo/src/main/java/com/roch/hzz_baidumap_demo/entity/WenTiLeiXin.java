package com.roch.hzz_baidumap_demo.entity;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/14/014 9:20
 */
public class WenTiLeiXin extends BaseEntity {

    /**
     * typecode : 1
     * typename : 垃圾漂浮
     */
    private String typecode;
    private String typename;

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

}
