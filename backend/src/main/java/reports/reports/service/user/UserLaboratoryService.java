package reports.reports.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.config.security.UserContext;
import reports.reports.domain.Laboratory;
import reports.reports.dto.LaboratoryDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;

import reports.reports.repository.AppUserRepository;
import reports.reports.repository.LaboratoryRepository;
import reports.reports.service.admin.LaboratoryService;
import reports.reports.service.admin.ReportService;
import reports.reports.service.admin.SubjectService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserLaboratoryService {

    private LaboratoryRepository laboratoryRepository;
    private LaboratoryService laboratoryService;
    private AppUserRepository appUserRepository;
    private ReportService reportService;

    @Autowired
    public UserLaboratoryService(LaboratoryRepository laboratoryRepository, LaboratoryService laboratoryService, AppUserRepository appUserRepository, ReportService reportService) {
        this.laboratoryRepository = laboratoryRepository;
        this.laboratoryService = laboratoryService;
        this.appUserRepository = appUserRepository;
        this.reportService = reportService;
    }

    @Transactional(readOnly = true)
    public PageResponse<LaboratoryDTO> findAll(PageRequestByExample<LaboratoryDTO> req) {
        return laboratoryService.findAll(req);
    }

    @Transactional(readOnly = true)
    public LaboratoryDTO findOne(Integer id) {
        Laboratory laboratory = laboratoryRepository.findOne(id);

        if(!Optional.ofNullable(laboratory).isPresent()) {
            return null;
        }

        laboratory.setReports(appUserRepository.findOne(UserContext.getId()).getReports().stream()
                .filter(report -> {
                            if(Optional.ofNullable(report.getLaboratory().getId()).isPresent()){
                                return report.getLaboratory().getId().equals(laboratory.getId());
                            } else{ return true; }
                        }
                )
                .collect(Collectors.toList()));

        return toDTO(laboratory);
    }

    public static LaboratoryDTO toDTO(Laboratory laboratory) {
        if (laboratory == null) {
            return null;
        }

        LaboratoryDTO dto = new LaboratoryDTO();

        dto.id = laboratory.getId();
        dto.name = laboratory.getName();
        dto.description = laboratory.getDescription();
        dto.createdDate = laboratory.getCreatedDate();
        dto.lastModifiedDate = laboratory.getLastModifiedDate();
        dto.createdBy = laboratory.getCreatedBy();
        dto.lastModifiedBy = laboratory.getLastModifiedBy();
        dto.labDate = laboratory.getLabDate();
        dto.returnReportDate = laboratory.getReturnReportDate();
        dto.finalReturnReportDate = laboratory.getFinalReturnReportDate();
        dto.reports = laboratory.getReports().stream().map(report -> ReportService.toDTO(report, 1)).collect(Collectors.toList());
        dto.subject = SubjectService.toDTO(laboratory.getSubject(), 1);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<LaboratoryDTO> findAll() {
        List<Laboratory> results = laboratoryRepository.findAll();
        return results.stream().map(laboratory -> toDTO(laboratory)).collect(Collectors.toList());
    }
}
