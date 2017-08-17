package com.roch.hzz_baidumap_demo.entity;

/**
 * @author ZhaoDongShao
 * 2016年5月10日
 */
public class Photo extends BaseEntity{

	private String url;
	private String id;

	public Photo() {
	}

	public Photo(String id, String url) {
		this.id = id;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
