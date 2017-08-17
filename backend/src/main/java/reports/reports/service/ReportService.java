package reports.reports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.domain.Report_;
import reports.reports.domain.Report;
import reports.reports.dto.ReportDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.AppUserRepository;
import reports.reports.repository.ReportRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AppUserService userService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    public PageResponse<ReportDTO> findAll(PageRequestByExample<ReportDTO> req) {
        Example<Report> example = null;
        Report report = toEntity(req.example);

        if (report != null) {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher(Report_.description.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(Report_.filePath.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(Report_.fileName.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(Report_.fileExtension.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(Report_.grade.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(Report_.createdDate.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(Report_.lastModifiedDate.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(Report_.createdBy.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(Report_.lastModifiedBy.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(Report_.isSendInTime.getName(), match -> match.ignoreCase().startsWith());

            example = Example.of(report, matcher);
        }

        Page<Report> page;
        if (example != null) {
            page = reportRepository.findAll(example, req.toPageable());
        } else {
            page = reportRepository.findAll(req.toPageable());
        }

        List<ReportDTO> content = page.getContent().stream().map(this::toDTO).collect(Collectors.toList());
        return new PageResponse<>(page.getTotalPages(), page.getTotalElements(), content);
    }

    public void delete(Integer id) {
        reportRepository.delete(id);
    }

    /**
     * Save the passed dto as a new entity or update the corresponding entity if any.
     */
    @Transactional
    public ReportDTO save(ReportDTO dto) {
        if (dto == null) {
            return null;
        }

        final Report report;

        if (dto.isIdSet()) {
            Report userTmp = reportRepository.findOne(dto.id);
            if (userTmp != null) {
                report = userTmp;
            } else {
                report = new Report();
                report.setId(dto.id);
            }
        } else {
            report = new Report();
        }

        report.setDescription(dto.description);
        report.setFilePath(dto.filePath);
        report.setFileName(dto.fileName);
        report.setFileExtension(dto.fileExtension);
        report.setGrade(dto.grade);
        report.setCreatedDate(dto.createdDate);
        report.setLastModifiedDate(dto.lastModifiedDate);
        report.setCreatedBy(dto.createdBy);
        report.setLastModifiedBy(dto.lastModifiedBy);
        report.setSendInTime(dto.isSendInTime);

        report.getUsers().clear();
        if (dto.users != null) {
            dto.users.stream().forEach(role -> report.addUser(appUserRepository.findOne(role.id)));
        }

        return toDTO(reportRepository.save(report));
    }

    /**
     * Converts the passed dto to a Report.
     * Convenient for query by example.
     */
    public Report toEntity(ReportDTO dto) {
        if (dto == null) {
            return null;
        }

        Report report = new Report();

        report.setId(dto.id);
        report.setDescription(dto.description);
        report.setFilePath(dto.filePath);
        report.setFileName(dto.fileName);
        report.setFileExtension(dto.fileExtension);
        report.setGrade(dto.grade);
        report.setCreatedDate(dto.createdDate);
        report.setLastModifiedDate(dto.lastModifiedDate);
        report.setCreatedBy(dto.createdBy);
        report.setLastModifiedBy(dto.lastModifiedBy);
        report.setSendInTime(dto.isSendInTime);

        return report;
    }

    /**
     * Converts the passed report to a DTO. The depth is used to control the
     * amount of association you want.
     *
     * @param report
     */
    public ReportDTO toDTO(Report report) {
        if (report == null) {
            return null;
        }

        ReportDTO dto = new ReportDTO();

        dto.id = report.getId();
        dto.description = report.getDescription();
        dto.filePath = report.getFilePath();
        dto.fileName = report.getFileName();
        dto.fileExtension = report.getFileExtension();
        dto.grade = report.getGrade();
        dto.createdDate = report.getCreatedDate();
        dto.lastModifiedDate = report.getLastModifiedDate();
        dto.createdBy = report.getCreatedBy();
        dto.lastModifiedBy = report.getLastModifiedBy();
        dto.isSendInTime = report.isSendInTime();

        dto.users = report.getUsers().stream().map(role -> userService.toDTO(role)).collect(Collectors.toList());

        return dto;
    }

}
