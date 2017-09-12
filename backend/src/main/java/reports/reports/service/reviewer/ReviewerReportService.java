package reports.reports.service.reviewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.dto.ReportDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.admin.ReportService;

@Service
public class ReviewerReportService {

    @Autowired
    private ReportService reportService;

    @Transactional(readOnly = true)
    public ReportDTO findOne(Integer id) {
        return reportService.findOne(id);
    }

    @Transactional(readOnly = true)
    public PageResponse<ReportDTO> findAll(PageRequestByExample<ReportDTO> req) {
        return reportService.findAll(req);
    }

    /**
     * Save the passed dto as a new entity or update the corresponding entity if any.
     */
    @Transactional
    public ReportDTO save(ReportDTO dto) {
        return reportService.save(dto);
    }
}