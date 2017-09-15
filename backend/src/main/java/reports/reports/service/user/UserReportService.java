package reports.reports.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.config.security.UserContext;
import reports.reports.domain.AppUser;
import reports.reports.domain.Report;
import reports.reports.dto.AppUserDTO;
import reports.reports.dto.ReportDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.AppUserRepository;
import reports.reports.repository.ReportRepository;
import reports.reports.service.admin.AppUserService;
import reports.reports.service.admin.LaboratoryService;
import reports.reports.service.admin.ReportService;
import reports.reports.web.SecurityRestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private  AppUserService appUserService;

    @Transactional(readOnly = true)
    public ReportDTO findOne(Integer id) {
        Report report = reportRepository.findOne(id);
        if (report.getUsers().stream().noneMatch(appUser -> appUser.getId().equals(UserContext.getId()))) {
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
        if (report.getUsers().stream().noneMatch(appUser -> appUser.getId().equals(UserContext.getId()))) {
            log.error("Cannot delete, this is not your report");
        }
        if (Optional.ofNullable(report.getGrade()).isPresent()) {
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

        if (!Optional.ofNullable(dto.id).isPresent()) {
            if (Optional.ofNullable(dto.grade).isPresent()) {
                log.error("User cannot set grade");
                return null;
            }
            if (!Optional.ofNullable(dto.users).isPresent()){
                dto.users = new ArrayList<>(Arrays.asList(appUserService.findOne(UserContext.getId())));
            }
            return reportService.save(dto);
        }

        Report report = reportRepository.findOne(dto.id);

        if (Optional.ofNullable(report.getGrade()).isPresent()) {
            log.error("Cannot update report with grade");
            return null;
        } else if (Optional.ofNullable(dto.grade).isPresent()) {
            log.error("User cannot set grade");
            return null;
        } else if (!Optional.ofNullable(dto.users).isPresent()){
            dto.users = new ArrayList<>(Arrays.asList(appUserService.findOne(UserContext.getId())));
        }

        return reportService.save(dto);
    }

    public void downloadFile(Integer reportId, HttpServletResponse response) throws IOException {

        Report report = reportRepository.findOne(reportId);
        if (report.getUsers().stream().noneMatch(appUser -> appUser.getId().equals(UserContext.getId()))) {
            log.error("This is not logged user report");
        } else {
            reportService.downloadFile(reportId, response);
        }
    }
}
