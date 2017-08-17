package com.roch.hzz_baidumap_demo.entity;

import java.util.List;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/18/018 16:46
 */
public class Ad_Cd extends BaseEntity {

    /**
     * id : 410200000000
     * TSysAdBEntitys : []
     * adNm : 开封市
     * TSysAdBEntity : null
     */
    private String id;
    private String adNm;
    private Object TSysAdBEntity;
    private List<?> TSysAdBEntitys;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdNm() {
        return adNm;
    }

    public void setAdNm(String adNm) {
        this.adNm = adNm;
    }

    public Object getTSysAdBEntity() {
        return TSysAdBEntity;
    }

    public void setTSysAdBEntity(Object TSysAdBEntity) {
        this.TSysAdBEntity = TSysAdBEntity;
    }

    public List<?> getTSysAdBEntitys() {
        return TSysAdBEntitys;
    }

    public void setTSysAdBEntitys(List<?> TSysAdBEntitys) {
        this.TSysAdBEntitys = TSysAdBEntitys;
    }

}
