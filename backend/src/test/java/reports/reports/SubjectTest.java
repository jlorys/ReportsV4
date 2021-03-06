package reports.reports;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reports.reports.domain.Subject;
import reports.reports.dto.SubjectDTO;
import reports.reports.dto.support.LazyLoadEvent;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.admin.LaboratoryService;
import reports.reports.service.admin.ReportService;
import reports.reports.service.admin.SubjectService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubjectTest {

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

        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Pomiary", "przedmiot który uczy mierzenia wartości elektrycznych", null));

        subjects.stream().forEach(subject -> subjectService.save(SubjectService.toDTO(subject)));
    }

    @Test
    public void findAllPageableByExample_ShouldReturnOneEntry() {

        PageRequestByExample<SubjectDTO> subjectDTOPageRequestByExample = new PageRequestByExample<>();
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.name = "P";
        subjectDTOPageRequestByExample.example = subjectDTO;

        PageResponse<SubjectDTO> subjectDTOPageResponse = subjectService.findAll(subjectDTOPageRequestByExample);
        assertEquals(1, subjectDTOPageResponse.totalElements);

    }

    @Test
    public void findAllPageableByPageRows_ShouldReturnOnePage() {

        PageRequestByExample<SubjectDTO> subjectDTOPageRequestByExample = new PageRequestByExample<>();
        LazyLoadEvent lazyLoadEvent = new LazyLoadEvent();
        lazyLoadEvent.rows = 1;
        subjectDTOPageRequestByExample.lazyLoadEvent = lazyLoadEvent;

        PageResponse<SubjectDTO> subjectDTOPageResponse = subjectService.findAll(subjectDTOPageRequestByExample);

        assertEquals(1, subjectDTOPageResponse.totalPages);

    }

    @Test
    public void saveFieldOfStudy_ShouldCreateNewFieldOfStudy() {

        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.id = 1;
        subjectDTO.name = "Materiałoznawstwo";
        subjectDTO.description = "przedmiot o materiałoznawstwie";

        assertEquals("Materiałoznawstwo", subjectService.save(subjectDTO).name);

    }

    @After
    public void after() throws Exception {
        subjectService.deleteAll();
    }

}

