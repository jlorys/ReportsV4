package reports.reports.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import reports.reports.domain.Laboratory;

import java.util.List;

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

    public LaboratoryDTO laboratory;

    public List<AppUserDTO> users;

    @JsonIgnore
    public boolean isIdSet() {
        return id != null;
    }
}
