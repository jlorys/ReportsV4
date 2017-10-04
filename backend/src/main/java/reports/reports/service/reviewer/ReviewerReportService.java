package reports.reports.service.reviewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.domain.AppUser;
import reports.reports.domain.Laboratory;
import reports.reports.domain.Report;
import reports.reports.dto.ReportDTO;
import reports.reports.repository.LaboratoryRepository;
import reports.reports.repository.ReportRepository;
import reports.reports.service.admin.ReportService;
import reports.reports.service.reviewer.support.OnGradeEvent;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewerReportService {

    private ReportService reportService;
    private ReportRepository reportRepository;
    private LaboratoryRepository laboratoryRepository;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public ReviewerReportService(ReportService reportService,
                                 ReportRepository reportRepository,
                                 LaboratoryRepository laboratoryRepository,
                                 ApplicationEventPublisher eventPublisher) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ReportDTO save(ReportDTO dto) {
        if (Optional.ofNullable(dto.id).isPresent()) {

            Report report = reportRepository.findOne(dto.id);
            Laboratory laboratory = laboratoryRepository.findOne(report.getLaboratory().getId());

            if (report.getLastModifiedDate() < laboratory.getFinalReturnReportDate() &&
                    report.getLastModifiedDate() > laboratory.getReturnReportDate()) {
                Float gradeReducedHalf = Float.valueOf(dto.grade) - 0.5F;
                report.setGrade(gradeReducedHalf.toString());
                report.setSendInTime(false);
            } else if (report.getLastModifiedDate() > laboratory.getFinalReturnReportDate()) {
                report.setGrade(String.valueOf("2.0"));
                report.setSendInTime(false);
            } else {
                report.setSendInTime(true);
            }
            ReportDTO reportDTO = reportService.save(ReportService.toDTO(report));

            eventPublisher.publishEvent(new OnGradeEvent(dto));

            return reportDTO;

        } else {
            return null;
        }
    }
}