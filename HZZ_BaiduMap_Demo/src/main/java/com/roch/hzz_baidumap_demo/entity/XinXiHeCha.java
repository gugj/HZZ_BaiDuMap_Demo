package com.roch.hzz_baidumap_demo.entity;

/**
 * 信息核查实体类
 * 作者：GuGaoJie
 * 时间：2017/7/11/011 11:58
 */
public class XinXiHeCha extends BaseEntity {
    /**
     * id : 2528818f5cec738c015cecacb1bd0031
     * uid : 8a8ab0b246dc81120146dc8181950052
     * infoStatus : 0
     * remStatus : 1
     * stime : {"nanos":307000000,"time":1500976154307,"minutes":49,"seconds":14,"hours":17,"month":6,"timezoneOffset":-480,"year":117,"day":2,"date":25}
     * location : 113.698707,34.78614
     * supInfo : 信息核查上报不带图片
     * reportRvinfoWxAndImgEntity : {"id":"4028818f5cec738c015cecacb1bd0032","detail":"dsdsdsddsd","stime":{"nanos":0,"time":1499054411000,"minutes":0,"seconds":11,"hours":12,"month":6,"timezoneOffset":-480,"year":117,"day":1,"date":3},"imglist":[{"id":"402881185d3f0573015d3f09301c0004","fileType":"1","ts":{"nanos":50000000,"time":1500001153050,"minutes":59,"seconds":13,"hours":10,"month":6,"timezoneOffset":-480,"year":117,"day":5,"date":14},"filePath":"jeecg/upload/20170803/1501749202691.jpg","fileName":"1500001153050.jpg","itemid":"4028818f5cec738c015cecacb1bd0032"}],"location":"113.698637,34.786283","status":"0","uname":" 张三","rvName":"贾鲁河"}
     */
//    private String position;
//    private String remstatustxt;
//    private String detail;
////    private String rctime;
//    private String cbtime;
//    private String stime;
//    private String status;
//    private String location;
//    private String uname;
//    private String ctypetxt;
//    private String ctype;
//    private String rhdetail;
//    private String statustxt;
//    private String rhtime;
//    private String sup_info;
//    private String telnumb;
//    private String id;  // 接报信息ID
//    private String cid; // 确认信息ID
//    private String rem_status;
//    private String orgname;
//    private String infostatustxt;
//    private String rvName;
//    private String dlname;
//    private String infoStatus;

    private String id;
    private String uid;
    private String infoStatus;
    private String remStatus;
    private String remStatusstr;
    private String location;
    private String stimestr;
    private String supInfo;
    private XinXiHeCha_Item reportRvinfoWxAndImgEntity;

    public String getRemStatusstr() {
        return remStatusstr;
    }

    public void setRemStatusstr(String remStatusstr) {
        this.remStatusstr = remStatusstr;
    }

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInfoStatus() {
        return infoStatus;
    }

    public void setInfoStatus(String infoStatus) {
        this.infoStatus = infoStatus;
    }

    public String getRemStatus() {
        return remStatus;
    }

    public void setRemStatus(String remStatus) {
        this.remStatus = remStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSupInfo() {
        return supInfo;
    }

    public void setSupInfo(String supInfo) {
        this.supInfo = supInfo;
    }

    public XinXiHeCha_Item getReportRvinfoWxAndImgEntity() {
        return reportRvinfoWxAndImgEntity;
    }

    public void setReportRvinfoWxAndImgEntity(XinXiHeCha_Item reportRvinfoWxAndImgEntity) {
        this.reportRvinfoWxAndImgEntity = reportRvinfoWxAndImgEntity;
    }
}