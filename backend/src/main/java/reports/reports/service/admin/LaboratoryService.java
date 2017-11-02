package reports.reports.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.domain.Laboratory;
import reports.reports.dto.LaboratoryDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.LaboratoryRepository;
import reports.reports.repository.ReportRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LaboratoryService {

    private LaboratoryRepository laboratoryRepository;
    private ReportRepository reportRepository;

    @Autowired
    public LaboratoryService(LaboratoryRepository laboratoryRepository, ReportRepository reportRepository) {
        this.laboratoryRepository = laboratoryRepository;
        this.reportRepository = reportRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<LaboratoryDTO> findAll(PageRequestByExample<LaboratoryDTO> req) {
        Example<Laboratory> example = null;
        Laboratory laboratory = toEntity(req.example);

        if (laboratory != null) {
            ExampleMatcher matcher = ExampleMatcher.matching() //
                    .withMatcher("name", match -> match.ignoreCase().startsWith())
                    .withMatcher("description", match -> match.ignoreCase().startsWith())
                    .withMatcher("labDate", match -> match.ignoreCase().startsWith())
                    .withMatcher("returnReportDate", match -> match.ignoreCase().startsWith())
                    .withMatcher("finalReturnReportDate", match -> match.ignoreCase().startsWith())
                    .withMatcher("createdDate", match -> match.ignoreCase().startsWith())
                    .withMatcher("lastModifiedDate", match -> match.ignoreCase().startsWith())
                    .withMatcher("createdBy", match -> match.ignoreCase().startsWith())
                    .withMatcher("lastModifiedBy", match -> match.ignoreCase().startsWith());

            example = Example.of(laboratory, matcher);
        }

        Page<Laboratory> page;
        if (example != null) {
            page = laboratoryRepository.findAll(example, req.toPageable());
        } else {
            page = laboratoryRepository.findAll(req.toPageable());
        }

        List<LaboratoryDTO> content = page.getContent().stream().map(laboratory1 -> toDTO(laboratory1)).collect(Collectors.toList());
        return new PageResponse<>(page.getTotalPages(), page.getTotalElements(), content);
    }

    public void delete(Integer id) {
        laboratoryRepository.delete(id);
    }

    public void deleteAll() {
        laboratoryRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public LaboratoryDTO findOne(Integer id) {
        Laboratory laboratory = laboratoryRepository.findOne(id);
        return toDTO(laboratory);
    }

    @Transactional
    public LaboratoryDTO save(LaboratoryDTO dto) {
        if (dto == null) {
            return null;
        }

        final Laboratory laboratory;

        if (dto.isIdSet()) {
            Laboratory labTmp = laboratoryRepository.findOne(dto.id);
            if (labTmp != null) {
                laboratory = labTmp;
            } else {
                laboratory = new Laboratory();
                laboratory.setId(dto.id);
            }
        } else {
            laboratory = new Laboratory();
        }

        laboratory.setName(dto.name);
        laboratory.setDescription(dto.description);
        laboratory.setLabDate(dto.labDate);
        laboratory.setReturnReportDate(dto.returnReportDate);
        laboratory.setFinalReturnReportDate(dto.finalReturnReportDate);
        laboratory.setCreatedDate(dto.createdDate);
        laboratory.setLastModifiedDate(dto.lastModifiedDate);
        laboratory.setCreatedBy(dto.createdBy);
        laboratory.setLastModifiedBy(dto.lastModifiedBy);

        laboratory.getReports().clear();
        if (dto.reports != null) {
            dto.reports.forEach(report -> laboratory.addReport(reportRepository.findOne(report.id)));
        }
        laboratory.setSubject(SubjectService.toEntity(dto.subject));

        return toDTO(laboratoryRepository.save(laboratory));
    }

    static Laboratory toEntity(LaboratoryDTO dto) {
        if (dto == null) {
            return null;
        }

        Laboratory laboratory = new Laboratory();

        laboratory.setId(dto.id);
        laboratory.setDescription(dto.description);
        laboratory.setName(dto.name);
        laboratory.setCreatedDate(dto.createdDate);
        laboratory.setLastModifiedDate(dto.lastModifiedDate);
        laboratory.setCreatedBy(dto.createdBy);
        laboratory.setLastModifiedBy(dto.lastModifiedBy);
        laboratory.setLabDate(dto.labDate);
        laboratory.setReturnReportDate(dto.returnReportDate);
        laboratory.setFinalReturnReportDate(dto.finalReturnReportDate);

        return laboratory;
    }

    public static LaboratoryDTO toDTO(Laboratory laboratory) {
        return toDTO(laboratory, 0);
    }

    public static LaboratoryDTO toDTO(Laboratory laboratory, int depth) {
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
        if(depth<1){
            dto.reports = laboratory.getReports().stream().map(report -> ReportService.toDTO(report, 1)).collect(Collectors.toList());
        }
        dto.subject = SubjectService.toDTO(laboratory.getSubject(), 1);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<LaboratoryDTO> findAll() {
        List<Laboratory> results = laboratoryRepository.findAll();
        return results.stream().map(laboratory -> toDTO(laboratory)).collect(Collectors.toList());
    }

}
