package com.roch.hzz_baidumap_demo.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 作者：GuGaoJie
 * 时间：2017/6/19/019 10:52
 */
@Entity
public class LatLonEntity {

    @Id
    private Long id;

    private double latitude;
    private double lontitude;

    private String time;

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLontitude() {
        return this.lontitude;
    }

    public void setLontitude(double lontitude) {
        this.lontitude = lontitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1804246681)
    public LatLonEntity(Long id, double latitude, double lontitude, String time) {
        this.id = id;
        this.latitude = latitude;
        this.lontitude = lontitude;
        this.time = time;
    }

    @Generated(hash = 510956612)
    public LatLonEntity() {
    }

   
}
