package com.roch.hzz_baidumap_demo.entity;

import java.util.List;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/14/014 16:55
 */
public class LoginUser extends BaseEntity{

    private String realName;     // 用户名 : 管理员
    private String userName;     // 登陆名 : admin
    private String departname;   // 部门 : 开封市水利局
    private String email;        // 职位 : 市长
    private String orgType;      // 河长级别 : 1

    private String id; // id : 8a8ab0b246dc81120146dc8181950052
    private String ad_cd; // 用户的行政区划

    private String updateBy; // updateBy : admin
    private String departid; // departid : 8a8ab0b246dc81120146dc8180a20016
    private int status; // status : 1
    private CurrentDepartBean currentDepart;
    private int activitiSync; // activitiSync : 0
    private String password; // password : c44b01947c9e6e3f
    private String createBy;
    private String createName;
    private String officePhone; // officePhone : 1
    private UpdateDateBean updateDate;
    private String signatureFile; // signatureFile : images/renfang/qm/licf.gif
    private String updateName; // updateName : 管理员
    private String mobilePhone; // mobilePhone : 158000000
    private String userKey; // userKey : 管理员
    private String browser;
    private int deleteFlag;
    private CreateDateBean createDate;
    private List<UserOrgListBean> userOrgList;
    private List<?> signature;

    public String getAd_cd() {
        return ad_cd;
    }

    public void setAd_cd(String ad_cd) {
        this.ad_cd = ad_cd;
    }

    public String getDepartname() {
        return departname;
    }

    public void setDepartname(String departname) {
        this.departname = departname;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getDepartid() {
        return departid;
    }

    public void setDepartid(String departid) {
        this.departid = departid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CurrentDepartBean getCurrentDepart() {
        return currentDepart;
    }

    public void setCurrentDepart(CurrentDepartBean currentDepart) {
        this.currentDepart = currentDepart;
    }

    public int getActivitiSync() {
        return activitiSync;
    }

    public void setActivitiSync(int activitiSync) {
        this.activitiSync = activitiSync;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public UpdateDateBean getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(UpdateDateBean updateDate) {
        this.updateDate = updateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSignatureFile() {
        return signatureFile;
    }

    public void setSignatureFile(String signatureFile) {
        this.signatureFile = signatureFile;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public CreateDateBean getCreateDate() {
        return createDate;
    }

    public void setCreateDate(CreateDateBean createDate) {
        this.createDate = createDate;
    }

    public List<UserOrgListBean> getUserOrgList() {
        return userOrgList;
    }

    public void setUserOrgList(List<UserOrgListBean> userOrgList) {
        this.userOrgList = userOrgList;
    }

    public List<?> getSignature() {
        return signature;
    }

    public void setSignature(List<?> signature) {
        this.signature = signature;
    }

    public class CurrentDepartBean {

        private Object TSPDepart;
        private String id;
        private String orgType;
        private String departOrder;
        private String departname;
        private String fax;
        private String address;
        private String description;
        private String orgCode;
        private String mobile;
        private List<?> TSDeparts;

        public Object getTSPDepart() {
            return TSPDepart;
        }

        public void setTSPDepart(Object TSPDepart) {
            this.TSPDepart = TSPDepart;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrgType() {
            return orgType;
        }

        public void setOrgType(String orgType) {
            this.orgType = orgType;
        }

        public String getDepartOrder() {
            return departOrder;
        }

        public void setDepartOrder(String departOrder) {
            this.departOrder = departOrder;
        }

        public String getDepartname() {
            return departname;
        }

        public void setDepartname(String departname) {
            this.departname = departname;
        }

        public String getFax() {
            return fax;
        }

        public void setFax(String fax) {
            this.fax = fax;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public List<?> getTSDeparts() {
            return TSDeparts;
        }

        public void setTSDeparts(List<?> TSDeparts) {
            this.TSDeparts = TSDeparts;
        }
    }

    public class UpdateDateBean {

        private int nanos;
        private long time;
        private int minutes;
        private int seconds;
        private int hours;
        private int month;
        private int timezoneOffset;
        private int year;
        private int day;
        private int date;

        public int getNanos() {
            return nanos;
        }

        public void setNanos(int nanos) {
            this.nanos = nanos;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(int timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }
    }

    public class CreateDateBean {

        private int nanos;
        private long time;
        private int minutes;
        private int seconds;
        private int hours;
        private int month;
        private int timezoneOffset;
        private int year;
        private int day;
        private int date;

        public int getNanos() {
            return nanos;
        }

        public void setNanos(int nanos) {
            this.nanos = nanos;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(int timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }
    }

    public class UserOrgListBean {

        private String id;
        private TsDepartBean tsDepart;
        private Object tsUser;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public TsDepartBean getTsDepart() {
            return tsDepart;
        }

        public void setTsDepart(TsDepartBean tsDepart) {
            this.tsDepart = tsDepart;
        }

        public Object getTsUser() {
            return tsUser;
        }

        public void setTsUser(Object tsUser) {
            this.tsUser = tsUser;
        }

        public class TsDepartBean {

            private Object TSPDepart;
            private String id; // id : 402881a45ce36e36015ce38930fa0023
            private String orgType;
            private String departOrder;
            private String departname; // departname : 开封市水利局
            private String fax;
            private String address;
            private String description; // description : 410200000000
            private String orgCode;
            private String mobile;
            private List<?> TSDeparts;

            public Object getTSPDepart() {
                return TSPDepart;
            }

            public void setTSPDepart(Object TSPDepart) {
                this.TSPDepart = TSPDepart;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrgType() {
                return orgType;
            }

            public void setOrgType(String orgType) {
                this.orgType = orgType;
            }

            public String getDepartOrder() {
                return departOrder;
            }

            public void setDepartOrder(String departOrder) {
                this.departOrder = departOrder;
            }

            public String getDepartname() {
                return departname;
            }

            public void setDepartname(String departname) {
                this.departname = departname;
            }

            public String getFax() {
                return fax;
            }

            public void setFax(String fax) {
                this.fax = fax;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getOrgCode() {
                return orgCode;
            }

            public void setOrgCode(String orgCode) {
                this.orgCode = orgCode;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public List<?> getTSDeparts() {
                return TSDeparts;
            }

            public void setTSDeparts(List<?> TSDeparts) {
                this.TSDeparts = TSDeparts;
            }
        }
    }
}
