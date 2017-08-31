package reports.reports.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Simple DTO for Subject.
 */
public class SubjectDTO {
    private Integer id;
    private String name;
    private String description;
    private Long createdDate;
    private Long lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
    private List<LaboratoryDTO> laboratories;
    private FieldOfStudyDTO fieldOfStudy;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }
}
