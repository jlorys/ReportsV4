package reports.reports.service.reviewer.support;

import org.springframework.context.ApplicationEvent;
import reports.reports.dto.ReportDTO;

public class OnGradeEvent extends ApplicationEvent {

    private ReportDTO reportDTO;

    public OnGradeEvent(ReportDTO reportDTO) {
        super(reportDTO);
        this.reportDTO = reportDTO;
    }

    ReportDTO getReportDTO() {
        return reportDTO;
    }
}
