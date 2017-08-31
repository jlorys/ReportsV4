package reports.reports.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Simple DTO for Laboratory.
 */
public class LaboratoryDTO {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime labDate;
    private LocalDateTime returnReportDate;
    private LocalDateTime finalReturnReportDate;
    private Long createdDate;
    private Long lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
    private SubjectDTO subject;
    private List<ReportDTO> reports;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }
}
