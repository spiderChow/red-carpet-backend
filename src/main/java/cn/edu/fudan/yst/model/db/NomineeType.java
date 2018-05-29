package cn.edu.fudan.yst.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author gzdaijie
 */
@Entity
@Table(name = "nominee_types")
public class NomineeType implements Serializable {

    private static final long serialVersionUID = -7792597282750540598L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int displayOrder;

    @JsonIgnore
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    private List<Nomination> nominationList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public List<Nomination> getNominationList() {
        return nominationList;
    }

    public void setNominationList(List<Nomination> nominationList) {
        this.nominationList = nominationList;
    }
}
