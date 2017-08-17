package com.roch.hzz_baidumap_demo.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 极光推送的消息实体类
 * 作者：GuGaoJie
 * 时间：2017/7/25/025 15:14
 */
@Entity
public class Message extends BaseEntity{

    @Id(autoincrement = true)
    private Long id;
    private String title;
    private String content;

    @Generated(hash = 977969778)
    public Message(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    @Generated(hash = 637306882)
    public Message() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
