package com.roch.hzz_baidumap_demo.entity;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/3/003 13:58
 */
public class ShiJiHeDao extends BaseEntity{

        private static final long serialVersionUID = 1L;

        private String id;
        private String superiorRiver;
        private String riverStart;
        private String adCd;
        private String adnm;
        private String riverEnd;
        private String riverAlias;
        private String riverLength;
        private String riverName;
        private String location;
        private String mk;

    public String getAdnm() {
        return adnm;
    }

    public void setAdnm(String adnm) {
        this.adnm = adnm;
    }

    public String getMk() {
        return mk;
    }

    public void setMk(String mk) {
        this.mk = mk;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setSuperiorRiver(String superiorRiver) {
            this.superiorRiver = superiorRiver;
        }
        public String getSuperiorRiver() {
            return superiorRiver;
        }

        public void setRiverStart(String riverStart) {
            this.riverStart = riverStart;
        }
        public String getRiverStart() {
            return riverStart;
        }

        public void setAdCd(String adCd) {
            this.adCd = adCd;
        }
        public String getAdCd() {
            return adCd;
        }

        public void setRiverEnd(String riverEnd) {
            this.riverEnd = riverEnd;
        }
        public String getRiverEnd() {
            return riverEnd;
        }

        public void setRiverAlias(String riverAlias) {
            this.riverAlias = riverAlias;
        }
        public String getRiverAlias() {
            return riverAlias;
        }

        public void setRiverLength(String riverLength) {
            this.riverLength = riverLength;
        }
        public String getRiverLength() {
            return riverLength;
        }

        public void setRiverName(String riverName) {
            this.riverName = riverName;
        }
        public String getRiverName() {
            return riverName;
        }

}
