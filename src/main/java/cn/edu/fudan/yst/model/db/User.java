package cn.edu.fudan.yst.model.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 管理员的实体类
 * @author gzdaijie
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @NotNull
    private String id;

    @NotNull
    private String password;

    @NotNull
    private String roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public void clearPassword() {
        this.password = null;
    }
}
