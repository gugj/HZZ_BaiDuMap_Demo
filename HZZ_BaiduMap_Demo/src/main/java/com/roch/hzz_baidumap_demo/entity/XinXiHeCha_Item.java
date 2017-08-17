package com.roch.hzz_baidumap_demo.entity;

import java.util.List;

/**
 * 作者：GuGaoJie
 * 时间：2017/8/7/007 16:03
 */
public class XinXiHeCha_Item extends BaseEntity {

    /**
     * id : 4028818f5cec738c015cecacb1bd0032
     * detail : dsdsdsddsd
     * stime : {"nanos":0,"time":1499054411000,"minutes":0,"seconds":11,"hours":12,"month":6,"timezoneOffset":-480,"year":117,"day":1,"date":3}
     * imglist : [{"id":"402881185d3f0573015d3f09301c0004","fileType":"1","ts":{"nanos":50000000,"time":1500001153050,"minutes":59,"seconds":13,"hours":10,"month":6,"timezoneOffset":-480,"year":117,"day":5,"date":14},"filePath":"jeecg/upload/20170803/1501749202691.jpg","fileName":"1500001153050.jpg","itemid":"4028818f5cec738c015cecacb1bd0032"}]
     * location : 113.698637,34.786283
     * status : 0
     * uname :  张三
     * rvName : 贾鲁河
     */
    private String id;
    private String detail;
    private String location;
    private String status;
    private String uname;
    private String rvName;
    private String stimestr;
    private List<ImgList> imglist;

    public String getStimestr() {
        return stimestr;
    }

    public void setStimestr(String stimestr) {
        this.stimestr = stimestr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getRvName() {
        return rvName;
    }

    public void setRvName(String rvName) {
        this.rvName = rvName;
    }

    public List<ImgList> getImglist() {
        return imglist;
    }

    public void setImglist(List<ImgList> imglist) {
        this.imglist = imglist;
    }

}
