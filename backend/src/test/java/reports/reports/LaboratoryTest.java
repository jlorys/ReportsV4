package reports.reports;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reports.reports.domain.Laboratory;
import reports.reports.dto.LaboratoryDTO;
import reports.reports.dto.support.LazyLoadEvent;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.admin.LaboratoryService;
import reports.reports.service.admin.ReportService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LaboratoryTest {

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

        List<Laboratory> laboratories = new ArrayList<>();
        laboratories.add(new Laboratory("Laboratorium1", "Pomiary napięcia",
                LocalDateTime.of(2017, Month.of(9), 12, 12, 30),
                LocalDateTime.of(2017, Month.of(9), 15, 12, 30),
                LocalDateTime.of(2017, Month.of(9), 16, 12, 30),
                null));

        laboratories.add(new Laboratory("Laboratorium2", "Pomiary mocy",
                LocalDateTime.of(2017, Month.of(9), 13, 12, 15),
                LocalDateTime.of(2017, Month.of(9), 16, 12, 15),
                LocalDateTime.of(2017, Month.of(9), 17, 12, 15),
                null));

        laboratories.stream().forEach(laboratory -> laboratoryService.save(LaboratoryService.toDTO(laboratory)));
    }

    @Test
    public void findAllPageableByExample_ShouldReturnOneEntry() {

        PageRequestByExample<LaboratoryDTO> laboratoryDTOPageRequestByExample = new PageRequestByExample<>();
        LaboratoryDTO laboratoryDTO = new LaboratoryDTO();
        laboratoryDTO.name = "L";
        laboratoryDTOPageRequestByExample.example = laboratoryDTO;

        PageResponse<LaboratoryDTO> laboratoryDTOPageResponse = laboratoryService.findAll(laboratoryDTOPageRequestByExample);
        assertEquals(laboratoryDTOPageResponse.totalElements, 2);

    }

    @Test
    public void findAllPageableByPageRows_ShouldReturnTwoPages() {

        PageRequestByExample<LaboratoryDTO> laboratoryDTOPageRequestByExample = new PageRequestByExample<>();
        LazyLoadEvent lazyLoadEvent = new LazyLoadEvent();
        lazyLoadEvent.rows = 1;
        laboratoryDTOPageRequestByExample.lazyLoadEvent = lazyLoadEvent;

        PageResponse<LaboratoryDTO> laboratoryDTOPageResponse = laboratoryService.findAll(laboratoryDTOPageRequestByExample);

        assertEquals(laboratoryDTOPageResponse.totalPages, 2);

    }

    @Test
    public void saveLaboratory_ShouldCreateNewLaboratory() {

        LaboratoryDTO laboratoryDTO = new LaboratoryDTO();
        laboratoryDTO.id = 1;
        laboratoryDTO.name = "Laboratorium3";
        laboratoryDTO.description = "Laboratorium3 polegające na pomiarach mocy";

        assertEquals(laboratoryService.save(laboratoryDTO).name, "Laboratorium3");

    }

    @After
    public void after() throws Exception {
        laboratoryService.deleteAll();
    }
}

