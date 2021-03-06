package reports.reports.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

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
    public List<ReportDTO> reports;
    public Boolean enabled;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }
}