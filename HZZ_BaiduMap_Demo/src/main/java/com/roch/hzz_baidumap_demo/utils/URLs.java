package com.roch.hzz_baidumap_demo.utils;

public class URLs {

        //阿里云服务器---新
//      public static String IP = "47.93.120.179:9005";
        //王子松服务器
//	    public static String IP = "192.168.1.153:8080";
       //荣聪服务器
//	    public static String IP = "192.168.1.152:8080";
       //梁建立服务器
	    public static String IP = "192.168.1.173:8089";
       //李一鸣服务器
//	    public static String IP = "192.168.1.109:8080";
       //李启豪服务器
//	    public static String IP = "192.168.1.188:8080";

    /**
     * WebService服务器端接口的BaseUrl <br/>
     * 手机端---http://192.168.1.173:8089/jeecg/app/
     */
    public static String WEB_SERVICE_URL = "http://" + IP + "/jeecg/app/";

    /**
     * image——url服务器端图片的BaseUrl <br/>
     * http://101.200.190.254:9100/
     */
    public static String IMAGE_URL = "http://" + IP + "/";

//	public URLs(String address) {
//		if (address.equals("")) {
//			IP = "101.200.190.254:8999";
//		}else {
//			IP = address;
//		}
//	}

    /**
     * 登陆接口 <br/>
     * http://192.168.1.112:8089/jeecg/app/appSystemController.do?appLogin
     */
    public static String LOGIN = WEB_SERVICE_URL + "appSystemController.do?appLogin";
    /**
     * 修改密码接口 <br/>
     * http://192.168.1.112:8089/jeecg/app/userInfoController.do?savenewpwd
     */
    public static String Updata_PassWord = WEB_SERVICE_URL + "userInfoController.do?savenewpwd";

    /**
     * 市、县、乡、村级河道列表 <br/>
     * http://192.168.1.112:8089/jeecg/app/appTchannelInfoController.do?datagrid
     */
    public static String ShiJi_HeDao_List = WEB_SERVICE_URL + "appTchannelInfoController.do?datagrid";

    /**
     * 小微水体河道列表 <br/>
     * http://192.168.1.112:8089/jeecg/app/appTSmallwaterInfoController.do?datagrid
     */
    public static String XiaoWeiShuiTi_HeDao_List = WEB_SERVICE_URL + "appTSmallwaterInfoController.do?datagrid";

    /**
     * 巡河记录列表 <br/>
     * http://192.168.1.112:8089/jeecg/app/reportRvinfoAppController.do?datapage
     */
    public static String XunHe_JiLu_List = WEB_SERVICE_URL + "reportRvinfoAppController.do?datapage";
    /**
     * 根据河长ID获取管辖河道列表 <br/>
     * http://192.168.1.112:8089/jeecg/app/appTchannelInfoController.do?getChannelByDivision
     */
    public static String HeZhang_GuanXia_HeDao_List = WEB_SERVICE_URL + "appTchannelInfoController.do?getChannelByDivision";

    /**
     * 信息核查---污染接报事件列表 <br/>
     * http://192.168.1.112:8089/jeecg/app/sbsjAppController/datagrid.do
     */
    public static String Xinxi_HeCha_List = WEB_SERVICE_URL + "sbsjAppController.do?datagrid";

    /**
     * 结束巡河---提交巡河轨迹到服务器 <br/>
     * http://192.168.1.112:8089/jeecg/app/reportRvinfoAppController.do?saveTrail
     */
    public static String Commit_XunHe_GuiJi = WEB_SERVICE_URL + "reportRvinfoAppController.do?saveTrail";
    /**
     * 信息核查tab页---接报信息图片 <br/>
     * http://192.168.1.112:8089/jeecg/app/sbsjAppController.do/mediaJb
     */
    public static String Xinxi_HeCha_Tab_JieBao = WEB_SERVICE_URL + "sbsjAppController.do?mediaJb";
    /**
     * 根据巡河时信息上报点ID---获取详情 <br/>
     * http://192.168.1.112:8089/jeecg/app/reportRvinfoAppController.do?getReportDetailById
     */
    public static String XunHe_ShangBao_Marker_Detail = WEB_SERVICE_URL + "reportRvinfoAppController.do?getReportDetailById";
    /**
     * 信息核查tab页---确认信息图片 <br/>
     * http://192.168.1.112:8089/jeecg/app/sbsjAppController.do/mediaQr
     */
    public static String Xinxi_HeCha_Tab_QueRen = WEB_SERVICE_URL + "sbsjAppController.do?mediaQr";

    /**
     * 巡河轨迹详情 <br/>
     * http://192.168.1.112:8089/jeecg/app/reportRvinfoAppController.do?loadTrailById
     */
    public static String XunHe_GuiJi_Detail = WEB_SERVICE_URL + "reportRvinfoAppController.do?loadTrailById";

    /**
     * 巡河轨迹中信息上报的点 <br/>
     * http://192.168.1.112:8089/jeecg/app/reportRvinfoAppController.do?loadMarkersById
     */
    public static String XunHe_ShangBao_Marker = WEB_SERVICE_URL + "reportRvinfoAppController.do?loadMarkersById";

    /**
     * 图片上传 <br/>
     *  http://101.200.190.254:9100/jeecg/app/poorfamily/upload.do
     */
    public static String IMAGE_UP_LOAD = WEB_SERVICE_URL + "poorfamily/upload.do";

    /**
     *  巡河时信息上报---内容部分和照片部分 <br/>
     *  http://101.200.190.254:9100/jeecg/app/reportRvinfoAppController.do?reportApp
     */
    public static String XinXi_ShangBao = WEB_SERVICE_URL + "reportRvinfoAppController.do?reportApp";
    /**
     *  巡河时信息上报---内容部分 <br/>
     *  http://101.200.190.254:9100/jeecg/app/reportRvinfoAppController.do?reportNoMapApp
     */
    public static String XinXi_ShangBao_No_Photo = WEB_SERVICE_URL + "reportRvinfoAppController.do?reportNoMapApp";
    /**
     *  信息上报---问题类型----字典表 <br/>
     *  http://101.200.190.254:9100/jeecg/app/appSystemController.do?getTypesByGroupCode
     */
    public static String XinXi_ShangBao_Qtype = WEB_SERVICE_URL + "appSystemController.do?getTypesByGroupCode";
    /**
     *  请求行政区接口 <br/>
     *  http://101.200.190.254:9100/jeecg/app/tSysAdBController.do?getAdcdByFadcd
     */
    public static String XingZhengQu = WEB_SERVICE_URL + "tSysAdBController.do?getAdcdByFadcd";
    /**
     *  信息核查时--上报---内容部分和照片部分 <br/>
     *  http://101.200.190.254:9100/jeecg/app/sbsjAppController.do?uploadApp
     */
    public static String XinXi_HeCha_ShangBao = WEB_SERVICE_URL + "sbsjAppController.do?uploadApp";
    /**
     *  信息核查时--上报---内容部分 <br/>
     *  http://101.200.190.254:9100/jeecg/app/sbsjAppController.do?uploadNomapApp
     */
    public static String XinXi_HeCha_ShangBao_No_Photo = WEB_SERVICE_URL + "sbsjAppController.do?uploadNomapApp";
}
