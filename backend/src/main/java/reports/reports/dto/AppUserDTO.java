package reports.reports.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import reports.reports.config.security.UserWithId;
import reports.reports.domain.Report;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Simple DTO for AppUser.
 */
public class AppUserDTO {
    public Integer id;
    public String userName;
    public String password;
    public String firstName;
    public String lastName;
    public String email;
    public Long createdDate;
    public Long lastModifiedDate;
    public String createdBy;
    public String lastModifiedBy;
    public List<RoleDTO> roles;
    public List<Report> reports;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }
}