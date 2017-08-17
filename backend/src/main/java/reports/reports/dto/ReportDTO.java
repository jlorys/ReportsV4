package reports.reports.dto;

import java.util.List;

/**
 * Simple DTO for Report.
 */
public class ReportDTO {
    public Integer id;
    public String description;
    public String filePath;
    public String fileName;
    public String fileExtension;
    public String grade;
    public Long createdDate;
    public Long lastModifiedDate;
    public String createdBy;
    public String lastModifiedBy;
    public Boolean isSendInTime;

    public List<AppUserDTO> users;
}
