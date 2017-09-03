package reports.reports.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Simple DTO for Laboratory.
 */
public class LaboratoryDTO {
    public Integer id;
    public String name;
    public String description;
    public Long labDate;
    public Long returnReportDate;
    public Long finalReturnReportDate;
    public Long createdDate;
    public Long lastModifiedDate;
    public String createdBy;
    public String lastModifiedBy;
    public SubjectDTO subject;
    public List<ReportDTO> reports;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }
}
