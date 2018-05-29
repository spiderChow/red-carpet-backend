package cn.edu.fudan.yst.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gzdaijie
 */
@Entity
@Table(name = "votes")
public class Vote implements Serializable {
    private static final long serialVersionUID = -7792597282750540598L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String ipAddress;

    @ManyToOne(optional = false)
    @JoinColumn(name = "nomination_id")
    @NotNull
    private Nomination nomination;

    @NotNull
    private String date;

    @NotNull
    private String dateDetail;

    @NotNull
    private int weight = 1;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Nomination getNomination() {
        return nomination;
    }

    public void setNomination(Nomination nomination) {
        this.nomination = nomination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setDate(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        this.date = ft.format(date);
    }

    public String getDateDetail() {
        return dateDetail;
    }

    public void setDateDetail(String dateDetail) {
        this.dateDetail = dateDetail;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setDateDetail(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd_HH-mm-ss");
        this.dateDetail = ft.format(date);
    }

}
