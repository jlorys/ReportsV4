package reports.reports.service.reviewer.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import reports.reports.domain.Report;
import reports.reports.dto.AppUserDTO;
import reports.reports.dto.ReportDTO;
import reports.reports.repository.ReportRepository;

import java.util.List;

@Component
public class GradeListener implements
        ApplicationListener<OnGradeEvent> {

    private final Logger log = LoggerFactory.getLogger(GradeListener.class);

    private JavaMailSender mailSender;
    private ReportRepository reportRepository;

    @Autowired
    public GradeListener(JavaMailSender mailSender, ReportRepository reportRepository) {
        this.mailSender = mailSender;
        this.reportRepository = reportRepository;
    }

    @Override
    public void onApplicationEvent(OnGradeEvent event) {
        this.sendMailWithGrade(event);
    }

    private void sendMailWithGrade(OnGradeEvent event) {
        ReportDTO reportDTO = event.getReportDTO();

        try {
            sendMailWithGradeToReportAuthors(reportDTO.users, reportDTO);
        } catch (MailException me) {
            log.error("Mail sending error: " + me);
        }
    }

    public void sendMailWithGradeToReportAuthors(List<AppUserDTO> authors, ReportDTO reportDTO) throws MailException {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        Report report = reportRepository.getOne(reportDTO.id);

        authors.stream().forEach(appUser -> {
                    mailMessage.setTo(appUser.email);
                    mailMessage.setFrom("reportswebapplication@gmail.com");
                    mailMessage.setSubject("Ocena ze sprawozdania " + report.getFileName() + report.getFileExtension());
                    mailMessage.setText("Recenzent wystawił ocenę " + report.getGrade());
                    mailSender.send(mailMessage);
                }
        );
    }

}
