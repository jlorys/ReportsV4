package reports.reports.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser appUser;

    @CreatedDate
    private Long createdDate;

    public VerificationToken() {}

    public VerificationToken(String token, AppUser appUser) {
        this.token = token;
        this.appUser = appUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
