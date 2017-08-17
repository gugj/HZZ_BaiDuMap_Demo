package com.roch.hzz_baidumap_demo.entity;

/**
 * 作者：GuGaoJie
 * 时间：2017/8/3/003 17:25
 */
public class ImgList extends BaseEntity {

    private String id;
    private String fileType;
    private String filePath;
    private String fileName;
    private String itemid;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public String getFileType() {
        return fileType;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileName() {
        return fileName;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }
    public String getItemid() {
        return itemid;
    }

}
