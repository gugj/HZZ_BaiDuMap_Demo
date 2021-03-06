package com.roch.hzz_baidumap_demo.entity;

/**
 * 保存键值
 * @author ZhaoDongShao
 * 2016年6月1日
 */
public class MapEntity extends BaseEntity{

	private String key;
	private String value;

	public MapEntity(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
