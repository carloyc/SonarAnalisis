package mp.sitili.modules.user.entities;

import javax.persistence.*;
import mp.sitili.modules.role.entities.Role;

import java.util.Set;


@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(100)")
    private String password;


    @Column(name="status", columnDefinition = "TINYINT(1)")
    private Boolean status;

    @javax.persistence.ManyToMany(fetch = javax.persistence.FetchType.EAGER, cascade = javax.persistence.CascadeType.ALL)
    @javax.persistence.JoinTable(name = "USER_ROLE",
            joinColumns = {
                    @javax.persistence.JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @javax.persistence.JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> role;

    public User(String email, String password, Boolean status, Set<Role> role) {
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }
}