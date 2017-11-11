package reports.reports.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.domain.Subject;
import reports.reports.dto.SubjectDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.LaboratoryRepository;
import reports.reports.repository.SubjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private SubjectRepository subjectRepository;
    private LaboratoryRepository laboratoryRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, LaboratoryRepository laboratoryRepository) {
        this.subjectRepository = subjectRepository;
        this.laboratoryRepository = laboratoryRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<SubjectDTO> findAll(PageRequestByExample<SubjectDTO> req) {
        Example<Subject> example = null;
        Subject subject = toEntity(req.example);

        if (subject != null) {
            ExampleMatcher matcher = ExampleMatcher.matching() //
                    .withMatcher("name", match -> match.ignoreCase().contains())
                    .withMatcher("description", match -> match.ignoreCase().contains())
                    .withMatcher("createdDate", match -> match.ignoreCase().contains())
                    .withMatcher("lastModifiedDate", match -> match.ignoreCase().contains())
                    .withMatcher("createdBy", match -> match.ignoreCase().contains())
                    .withMatcher("lastModifiedBy", match -> match.ignoreCase().contains());

            example = Example.of(subject, matcher);
        }

        Page<Subject> page;
        if (example != null) {
            page = subjectRepository.findAll(example, req.toPageable());
        } else {
            page = subjectRepository.findAll(req.toPageable());
        }

        List<SubjectDTO> content = page.getContent().stream().map(subject1 -> toDTO(subject1)).collect(Collectors.toList());
        return new PageResponse<>(page.getTotalPages(), page.getTotalElements(), content);
    }


    public void delete(Integer id) {
        subjectRepository.delete(id);
    }

    public void deleteAll() {
        subjectRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public SubjectDTO findOne(Integer id) {
        Subject subject = subjectRepository.findOne(id);
        return toDTO(subject);
    }

    @Transactional
    public SubjectDTO save(SubjectDTO dto) {
        if (dto == null) {
            return null;
        }

        final Subject subject;

        if (dto.isIdSet()) {
            Subject userTmp = subjectRepository.findOne(dto.id);
            if (userTmp != null) {
                subject = userTmp;
            } else {
                subject = new Subject();
                subject.setId(dto.id);
            }
        } else {
            subject = new Subject();
        }

        subject.setName(dto.name);
        subject.setDescription(dto.description);
        subject.setCreatedDate(dto.createdDate);
        subject.setLastModifiedDate(dto.lastModifiedDate);
        subject.setCreatedBy(dto.createdBy);
        subject.setLastModifiedBy(dto.lastModifiedBy);

        subject.getLaboratories().clear();
        if (dto.laboratories != null) {
            dto.laboratories.forEach(laboratory -> subject.addLaboratory(laboratoryRepository.findOne(laboratory.id)));
        }
        subject.setFieldOfStudy(FieldOfStudyService.toEntity(dto.fieldOfStudy));

        return toDTO(subjectRepository.save(subject));
    }

    static Subject toEntity(SubjectDTO dto) {
        if (dto == null) {
            return null;
        }

        Subject subject = new Subject();

        subject.setId(dto.id);
        subject.setDescription(dto.description);
        subject.setName(dto.name);
        subject.setCreatedDate(dto.createdDate);
        subject.setLastModifiedDate(dto.lastModifiedDate);
        subject.setCreatedBy(dto.createdBy);
        subject.setLastModifiedBy(dto.lastModifiedBy);

        return subject;
    }

    public static SubjectDTO toDTO(Subject subject) {
        return toDTO(subject, 0);
    }

    public static SubjectDTO toDTO(Subject subject, int depth) {
        if (subject == null) {
            return null;
        }

        SubjectDTO dto = new SubjectDTO();

        dto.id = subject.getId();
        dto.name = subject.getName();
        dto.description = subject.getDescription();
        dto.createdDate = subject.getCreatedDate();
        dto.lastModifiedDate = subject.getLastModifiedDate();
        dto.createdBy = subject.getCreatedBy();
        dto.lastModifiedBy = subject.getLastModifiedBy();
        if(depth<1){
            dto.laboratories = subject.getLaboratories().stream().map(laboratory -> LaboratoryService.toDTO(laboratory, 1)).collect(Collectors.toList());
        }
        dto.fieldOfStudy = FieldOfStudyService.toDTO(subject.getFieldOfStudy(), 1);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<SubjectDTO> findAll() {
        List<Subject> results = subjectRepository.findAll();
        return results.stream().map(subject -> toDTO(subject)).collect(Collectors.toList());
    }

}
