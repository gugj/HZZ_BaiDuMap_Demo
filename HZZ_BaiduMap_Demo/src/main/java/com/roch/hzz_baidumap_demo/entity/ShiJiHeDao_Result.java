package com.roch.hzz_baidumap_demo.entity;

import java.util.List;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/3/003 13:54
 */
public class ShiJiHeDao_Result extends BaseResult {

        private static final long serialVersionUID = 1L;
        private List<ShiJiHeDao> jsondata;

        public void setJsondata(List<ShiJiHeDao> jsondata) {
            this.jsondata = jsondata;
        }
        public List<ShiJiHeDao> getJsondata() {
            return jsondata;
        }

}
