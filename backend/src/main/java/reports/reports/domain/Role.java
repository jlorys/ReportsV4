package reports.reports.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty
    @Size(max = 100)
    @Column(name = "ROLE_NAME", nullable = false, unique = true, length = 100)
    private String roleName;

    @ManyToMany
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "ROLE_ID") ,
            inverseJoinColumns = @JoinColumn(name = "USER_ID") )
    List<AppUser> users;

    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {return roleName;}

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<AppUser> getUsers() {
        if(Optional.ofNullable(users).isPresent()) return users;
        return Collections.EMPTY_LIST;
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }
}
