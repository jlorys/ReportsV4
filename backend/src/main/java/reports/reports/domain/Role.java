package reports.reports.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    List<AppUser> users;

    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    @Column(name = "ID", precision = 10)
    @GeneratedValue(strategy = IDENTITY)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotEmpty
    @Size(max = 100)
    @Column(name = "ROLE_NAME", nullable = false, unique = true, length = 100)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
