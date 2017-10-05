package reports.reports.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class SubjectDTO {
    public Integer id;
    public String name;
    public String description;
    public Long createdDate;
    public Long lastModifiedDate;
    public String createdBy;
    public String lastModifiedBy;
    public List<LaboratoryDTO> laboratories;
    public FieldOfStudyDTO fieldOfStudy;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }
}
