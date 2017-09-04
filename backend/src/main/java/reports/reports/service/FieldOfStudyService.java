package reports.reports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.domain.FieldOfStudy;
import reports.reports.domain.FieldOfStudy_;
import reports.reports.dto.FieldOfStudyDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.FieldOfStudyRepository;
import reports.reports.repository.SubjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldOfStudyService {

    @Autowired
    FieldOfStudyRepository fieldOfStudyRepository;

    @Autowired
    SubjectService subjectService;

    @Autowired
    SubjectRepository subjectRepository;

    @Transactional(readOnly = true)
    public PageResponse<FieldOfStudyDTO> findAll(PageRequestByExample<FieldOfStudyDTO> req) {
        Example<FieldOfStudy> example = null;
        FieldOfStudy fieldOfStudy = toEntity(req.example);

        if (fieldOfStudy != null) {
            ExampleMatcher matcher = ExampleMatcher.matching() //
                    .withMatcher(FieldOfStudy_.name.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(FieldOfStudy_.description.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(FieldOfStudy_.createdDate.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(FieldOfStudy_.lastModifiedDate.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(FieldOfStudy_.createdBy.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(FieldOfStudy_.lastModifiedBy.getName(), match -> match.ignoreCase().startsWith());

            example = Example.of(fieldOfStudy, matcher);
        }

        Page<FieldOfStudy> page;
        if (example != null) {
            page = fieldOfStudyRepository.findAll(example, req.toPageable());
        } else {
            page = fieldOfStudyRepository.findAll(req.toPageable());
        }

        List<FieldOfStudyDTO> content = page.getContent().stream().map(this::toDTO).collect(Collectors.toList());
        return new PageResponse<>(page.getTotalPages(), page.getTotalElements(), content);
    }

    public void delete(Integer id) {
        fieldOfStudyRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public FieldOfStudyDTO findOne(Integer id) {
        FieldOfStudy fieldOfStudy = fieldOfStudyRepository.findOne(id);
        return toDTO(fieldOfStudy);
    }

    /**
     * Save the passed dto as a new entity or update the corresponding entity if any.
     */
    @Transactional
    public FieldOfStudyDTO save(FieldOfStudyDTO dto) {
        if (dto == null) {
            return null;
        }

        final FieldOfStudy fieldOfStudy;

        if (dto.isIdSet()) {
            FieldOfStudy userTmp = fieldOfStudyRepository.findOne(dto.id);
            if (userTmp != null) {
                fieldOfStudy = userTmp;
            } else {
                fieldOfStudy = new FieldOfStudy();
                fieldOfStudy.setId(dto.id);
            }
        } else {
            fieldOfStudy = new FieldOfStudy();
        }

        fieldOfStudy.setName(dto.name);
        fieldOfStudy.setDescription(dto.description);
        fieldOfStudy.setCreatedDate(dto.createdDate);
        fieldOfStudy.setLastModifiedDate(dto.lastModifiedDate);
        fieldOfStudy.setCreatedBy(dto.createdBy);
        fieldOfStudy.setLastModifiedBy(dto.lastModifiedBy);

        fieldOfStudy.getSubjects().clear();
        if (dto.subjects != null) {
            dto.subjects.stream().forEach(subject -> fieldOfStudy.addSubject(subjectRepository.findOne(subject.id)));
        }

        return toDTO(fieldOfStudyRepository.save(fieldOfStudy));
    }

    /**
     * Converts the passed dto to a FieldOfStudy.
     * Convenient for query by example.
     */
    public FieldOfStudy toEntity(FieldOfStudyDTO dto) {
        if (dto == null) {
            return null;
        }

        FieldOfStudy fieldOfStudy = new FieldOfStudy();

        fieldOfStudy.setId(dto.id);
        fieldOfStudy.setDescription(dto.description);
        fieldOfStudy.setName(dto.name);
        fieldOfStudy.setCreatedDate(dto.createdDate);
        fieldOfStudy.setLastModifiedDate(dto.lastModifiedDate);
        fieldOfStudy.setCreatedBy(dto.createdBy);
        fieldOfStudy.setLastModifiedBy(dto.lastModifiedBy);

        return fieldOfStudy;
    }

    public FieldOfStudyDTO toDTO(FieldOfStudy fieldOfStudy) {
        return toDTO(fieldOfStudy, 0);
    }

    /**
     * Converts the passed fieldOfStudy to a DTO. The depth is used to control the
     * amount of association you want.
     *
     * @param fieldOfStudy
     */
    public FieldOfStudyDTO toDTO(FieldOfStudy fieldOfStudy, int depth) {
        if (fieldOfStudy == null) {
            return null;
        }

        FieldOfStudyDTO dto = new FieldOfStudyDTO();

        dto.id = fieldOfStudy.getId();
        dto.name = fieldOfStudy.getName();
        dto.description = fieldOfStudy.getDescription();
        dto.createdDate = fieldOfStudy.getCreatedDate();
        dto.lastModifiedDate = fieldOfStudy.getLastModifiedDate();
        dto.createdBy = fieldOfStudy.getCreatedBy();
        dto.lastModifiedBy = fieldOfStudy.getLastModifiedBy();
        if(depth<1){
            dto.subjects = fieldOfStudy.getSubjects().stream().map(subject -> subjectService.toDTO(subject, 1)).collect(Collectors.toList());
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public List<FieldOfStudyDTO> findAll() {
        List<FieldOfStudy> results = fieldOfStudyRepository.findAll();
        return results.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
