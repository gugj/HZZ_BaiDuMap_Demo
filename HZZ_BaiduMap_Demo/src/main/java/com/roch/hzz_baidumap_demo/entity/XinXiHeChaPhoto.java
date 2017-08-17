package com.roch.hzz_baidumap_demo.entity;

/**
 * 作者：GuGaoJie
 * 时间：2017/7/13/013 14:58
 */
public class XinXiHeChaPhoto extends BaseEntity {

    /**
     * id : 402881185d2ba2af015d2ba86b990003
     * fileType : 1
     * height : 0
     * ts : {"nanos":183000000,"time":1499676044183,"minutes":40,"seconds":44,"hours":16,"month":6,"timezoneOffset":-480,"year":117,"day":1,"date":10}
     * filePath : jeecg/upload/20170710/1499676044183.jpg
     * width : 0
     * fileName : 1499676044183.jpg
     * itemid : 4028818f5cec738c015cecacb1bd0032
     */
    private String id;
    private String fileType;
    private int height;
    private String filePath;
    private int width;
    private String fileName;
    private String itemid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }


}
