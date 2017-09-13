package reports.reports.service.reviewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.domain.Laboratory;
import reports.reports.domain.Report;
import reports.reports.dto.ReportDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.LaboratoryRepository;
import reports.reports.repository.ReportRepository;
import reports.reports.service.admin.ReportService;

import java.util.Optional;

@Service
public class ReviewerReportService {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private LaboratoryRepository laboratoryRepository;

    @Transactional
    public ReportDTO save(ReportDTO dto) {
        if (Optional.ofNullable(dto.id).isPresent()) {

            Report report = reportRepository.findOne(dto.id);
            Laboratory laboratory = laboratoryRepository.findOne(report.getLaboratory().getId());

            if(report.getLastModifiedDate() < laboratory.getFinalReturnReportDate() &&
                    report.getLastModifiedDate() > laboratory.getReturnReportDate()){
                Float gradeReducedHalf = Float.valueOf(dto.grade) - 0.5F;
                report.setGrade(gradeReducedHalf.toString());
                report.setSendInTime(false);
            }
            else if(report.getLastModifiedDate() > laboratory.getFinalReturnReportDate()){
                report.setGrade(String.valueOf("2"));
                report.setSendInTime(false);
            }
            else{
                report.setSendInTime(true);
            }
            return reportService.save(reportService.toDTO(report));

        } else {
            return null;
        }
    }
}