package reports.reports.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Simple DTO for FieldOfStudy.
 */
public class FieldOfStudyDTO {
    public Integer id;
    public String name;
    public String description;
    public Long createdDate;
    public Long lastModifiedDate;
    public String createdBy;
    public String lastModifiedBy;
    public List<SubjectDTO> subjects;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }
}
