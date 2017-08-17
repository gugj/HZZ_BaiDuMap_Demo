package com.roch.hzz_baidumap_demo.entity;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/12/012 14:48
 */
public class HeDao extends BaseEntity {

    /**
     * id : 4028813c5d1229b8015d122e5f73000b
     * superiorRiver : B114CA9C-9B8D-43FA-B076-2258F171DA8D
     * riverStart : 七点
     * adCd : 410202002000
     * riverEnd : 重点
     * riverAlias : 乡级河道测试数据
     * riverLength : 45
     * riverName : 乡级河道流域
     */
    private String id;
    private String superiorRiver;
    private String riverStart;
    private String adCd;
    private String riverEnd;
    private String riverAlias;
    private String riverLength;
    private String riverName;
    private String adNM;
    private String fatherRiver;//上级河段

    public String getFatherRiver() {
        return fatherRiver;
    }

    public void setFatherRiver(String fatherRiver) {
        this.fatherRiver = fatherRiver;
    }

    public String getAdNM() {
        return adNM;
    }

    public void setAdNM(String adNM) {
        this.adNM = adNM;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuperiorRiver() {
        return superiorRiver;
    }

    public void setSuperiorRiver(String superiorRiver) {
        this.superiorRiver = superiorRiver;
    }

    public String getRiverStart() {
        return riverStart;
    }

    public void setRiverStart(String riverStart) {
        this.riverStart = riverStart;
    }

    public String getAdCd() {
        return adCd;
    }

    public void setAdCd(String adCd) {
        this.adCd = adCd;
    }

    public String getRiverEnd() {
        return riverEnd;
    }

    public void setRiverEnd(String riverEnd) {
        this.riverEnd = riverEnd;
    }

    public String getRiverAlias() {
        return riverAlias;
    }

    public void setRiverAlias(String riverAlias) {
        this.riverAlias = riverAlias;
    }

    public String getRiverLength() {
        return riverLength;
    }

    public void setRiverLength(String riverLength) {
        this.riverLength = riverLength;
    }

    public String getRiverName() {
        return riverName;
    }

    public void setRiverName(String riverName) {
        this.riverName = riverName;
    }

}
