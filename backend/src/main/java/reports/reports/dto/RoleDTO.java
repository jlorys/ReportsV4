package reports.reports.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Simple DTO for Role.
 */
public class RoleDTO {
    public Integer id;
    public String roleName;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }
}