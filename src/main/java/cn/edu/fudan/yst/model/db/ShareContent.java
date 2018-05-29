package cn.edu.fudan.yst.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author gzdaijie
 */
@Entity
@Table(name = "share_content")
public class ShareContent implements Serializable {
    private static final long serialVersionUID = -7792597282750540598L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Column(name = "description")
    private String desc;

    @NotNull
    private String link = "http://yst.fudan.edu.cn/hongtan/vote";

    @NotNull
    private String imgUrl = "http://yst.fudan.edu.cn/hongtan/vote/wx-share.jpg";

    @NotNull
    private int displayOrder = 9999;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
