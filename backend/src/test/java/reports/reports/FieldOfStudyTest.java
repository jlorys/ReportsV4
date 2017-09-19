package reports.reports;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reports.reports.domain.FieldOfStudy;
import reports.reports.dto.FieldOfStudyDTO;
import reports.reports.dto.support.LazyLoadEvent;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.admin.FieldOfStudyService;
import reports.reports.service.admin.LaboratoryService;
import reports.reports.service.admin.ReportService;
import reports.reports.service.admin.SubjectService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FieldOfStudyTest {

    @Autowired
    private FieldOfStudyService fieldOfStudyService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private LaboratoryService laboratoryService;

    @Autowired
    private ReportService reportService;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        reportService.deleteAll();
        laboratoryService.deleteAll();
        subjectService.deleteAll();
        fieldOfStudyService.deleteAll();

        List<FieldOfStudy> fieldsOfStudies = new ArrayList<>();
        fieldsOfStudies.add(new FieldOfStudy("Elektronika", "studia elektroniczne"));
        fieldsOfStudies.add(new FieldOfStudy("Informatyka", "studia informatyczne"));
        fieldsOfStudies.add(new FieldOfStudy("Chemia", "studia chemiczne"));
        fieldsOfStudies.add(new FieldOfStudy("Biologia", "studia biologiczne"));
        fieldsOfStudies.add(new FieldOfStudy("Matematyka", "studia matematyczne"));

        fieldsOfStudies.stream().forEach(fieldOfStudy -> fieldOfStudyService.save(FieldOfStudyService.toDTO(fieldOfStudy)));
    }

    @Test
    public void findAllPageableByExample_ShouldReturnOneEntry() {

        PageRequestByExample<FieldOfStudyDTO> fieldOfStudyDTOPageRequestByExample = new PageRequestByExample<>();
        FieldOfStudyDTO fieldOfStudyDTO = new FieldOfStudyDTO();
        fieldOfStudyDTO.name = "E";
        fieldOfStudyDTOPageRequestByExample.example = fieldOfStudyDTO;

        PageResponse<FieldOfStudyDTO> fieldOfStudyDTOPageResponse = fieldOfStudyService.findAll(fieldOfStudyDTOPageRequestByExample);
        assertEquals(fieldOfStudyDTOPageResponse.totalElements, 1);

    }

    @Test
    public void findAllPageableByPageRows_ShouldReturnThreePages() {

        PageRequestByExample<FieldOfStudyDTO> fieldOfStudyDTOPageRequestByExample = new PageRequestByExample<>();
        LazyLoadEvent lazyLoadEvent = new LazyLoadEvent();
        lazyLoadEvent.rows = 2;
        fieldOfStudyDTOPageRequestByExample.lazyLoadEvent = lazyLoadEvent;

        PageResponse<FieldOfStudyDTO> fieldOfStudyDTOPageResponse = fieldOfStudyService.findAll(fieldOfStudyDTOPageRequestByExample);

        assertEquals(fieldOfStudyDTOPageResponse.totalPages, 3);

    }

    @Test
    public void saveFieldOfStudy_ShouldCreateNewFieldOfStudy() {

        FieldOfStudyDTO fieldOfStudyDTO = new FieldOfStudyDTO();
        fieldOfStudyDTO.id = 1;
        fieldOfStudyDTO.name = "Historia";

        assertEquals(fieldOfStudyService.save(fieldOfStudyDTO).name, "Historia");

    }

}

