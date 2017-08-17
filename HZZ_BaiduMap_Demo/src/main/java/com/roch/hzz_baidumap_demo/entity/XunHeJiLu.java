package com.roch.hzz_baidumap_demo.entity;

import java.util.List;

public class XunHeJiLu extends BaseEntity{

    private String nt;
    private String detail;
    private String handle;
    private Object stime;
    private String status;
    private String locations;
    private String location;
    private String userid;
    private String ckTime_end;
    private String ctype;
    private String qtype;
    private String sname;
    private String riverName;
    private String ctypetext;
    private String ad_cd;
    private String telnumb;
    private String id;
    private String flag;
    private String ckTime_begin;
    private String name;
    private String adnm;
    private CkTimeBean ckTime;

    private List<ImgList> imglist; // 图片路径集合

    public List<ImgList> getImglist() {
        return imglist;
    }

    public void setImglist(List<ImgList> imglist) {
        this.imglist = imglist;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNt() {
        return nt;
    }

    public void setNt(String nt) {
        this.nt = nt;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Object getStime() {
        return stime;
    }

    public void setStime(Object stime) {
        this.stime = stime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCkTime_end() {
        return ckTime_end;
    }

    public void setCkTime_end(String ckTime_end) {
        this.ckTime_end = ckTime_end;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getRiverName() {
        return riverName;
    }

    public void setRiverName(String riverName) {
        this.riverName = riverName;
    }

    public String getCtypetext() {
        return ctypetext;
    }

    public void setCtypetext(String ctypetext) {
        this.ctypetext = ctypetext;
    }

    public String getAd_cd() {
        return ad_cd;
    }

    public void setAd_cd(String ad_cd) {
        this.ad_cd = ad_cd;
    }

    public String getTelnumb() {
        return telnumb;
    }

    public void setTelnumb(String telnumb) {
        this.telnumb = telnumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCkTime_begin() {
        return ckTime_begin;
    }

    public void setCkTime_begin(String ckTime_begin) {
        this.ckTime_begin = ckTime_begin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdnm() {
        return adnm;
    }

    public void setAdnm(String adnm) {
        this.adnm = adnm;
    }

    public CkTimeBean getCkTime() {
        return ckTime;
    }

    public void setCkTime(CkTimeBean ckTime) {
        this.ckTime = ckTime;
    }
}