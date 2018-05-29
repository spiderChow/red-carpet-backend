package cn.edu.fudan.yst.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author gzdaijie
 */
@Entity
@Table(name = "nominations")
public class Nomination implements Serializable {

    private static final long serialVersionUID = -2023615367287856478L;

    Nomination() {
    }

    public Nomination(String id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true)
    private String id;

    @NotNull
    private String name;

    /**
     * ?
     */
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "school")
    private School school;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "type")
    private NomineeType type;



    @NotNull
    private String introduction;

    private String story;

    /**
     * 一个被提名者可以包含多个投票类Vote
     */
    @JsonIgnore
    @OneToMany(mappedBy = "nomination", cascade = CascadeType.ALL)
    private List<Vote> voteList;

    /**
     * 提名人
     */
    @NotNull
    private String referee;

    @NotNull
    private String refereeId;

    /**
     * 提名人的联系方式
     */
    @NotNull
    private String refereeContactInfo;

    /**
     * ?
     */
    private String remark;

    /**
     *  photo uri on the server
     */
    private String photo;

    /**
     * ?
     */
    private String originId = null;

    private boolean isPassed = false;

    /**
     * ?
     */
    private boolean isDirty = false;

    private String lastModifiedTime;
    private String createdTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public NomineeType getType() {
        return type;
    }

    public void setType(NomineeType type) {
        this.type = type;
    }


    public String getIntroduction() {
        return introduction == null ? "" : introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getRefereeContactInfo() {
        return refereeContactInfo;
    }

    public void setRefereeContactInfo(String refereeContactInfo) {
        this.refereeContactInfo = refereeContactInfo;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        this.isPassed = passed;
    }

    public List<Vote> getVoteList() {
        return voteList;
    }

    public void setVoteList(List<Vote> voteList) {
        this.voteList = voteList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public void setLastModifiedTime(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd_HH-mm-ss");
        this.lastModifiedTime = ft.format(date);
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setCreatedTime(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd_HH-mm-ss");
        this.createdTime = ft.format(date);
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    /**
     * 清除提名人信息
     */
    public void clearRefereeInfo() {
        this.setReferee(null);
        this.setRefereeId(null);
        this.setRefereeContactInfo(null);
    }

    public String getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(String refereeId) {
        this.refereeId = refereeId;
    }
}
