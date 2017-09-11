package reports.reports.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.config.security.UserContext;
import reports.reports.domain.Report;
import reports.reports.dto.ReportDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.AppUserRepository;
import reports.reports.repository.ReportRepository;
import reports.reports.service.admin.ReportService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserReportService {

    private final Logger log = LoggerFactory.getLogger(UserReportService.class);

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportService reportService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    public ReportDTO findOne(Integer id) {
        Report report = reportRepository.findOne(id);
        if(report.getUsers().stream().noneMatch(appUser -> appUser.getId().equals(UserContext.getId()))){
            return null;
        }
        return reportService.toDTO(report);
    }

    @Transactional(readOnly = true)
    public PageResponse<ReportDTO> findAll(PageRequestByExample<ReportDTO> req) {

        Page<Report> page;
        page = reportRepository.findByUsers(appUserRepository.findOne(UserContext.getId()), req.toPageable());
        List<ReportDTO> content = page.getContent().stream().map(report1 -> reportService.toDTO(report1)).collect(Collectors.toList());

        return new PageResponse<>(page.getTotalPages(), page.getTotalElements(), content);
    }

    public void delete(Integer id) {
        Report report = reportRepository.findOne(id);
        if(report.getUsers().stream().noneMatch(appUser -> appUser.getId().equals(UserContext.getId()))){
            log.error("Cannot delete, this is not your report");
        }
        else if(!report.getGrade().isEmpty()){
            log.error("Cannot delete report with grade");
        }
        else {
            reportRepository.delete(id);
        }
    }

    /**
     * Save the passed dto as a new entity or update the corresponding entity if any.
     */
    @Transactional
    public ReportDTO save(ReportDTO dto) {
        Report report = reportRepository.findOne(dto.id);
        if(!report.getGrade().isEmpty()){
            log.error("Cannot update report with grade");
            return null;
        }else if(!dto.grade.isEmpty()){
            log.error("User cannot set grade");
            return null;
        }
        return reportService.save(dto);
    }
}
