package reports.reports;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reports.reports.domain.Report;
import reports.reports.dto.ReportDTO;
import reports.reports.dto.support.LazyLoadEvent;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.admin.ReportService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportTest {

    @Autowired
    private ReportService reportService;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        reportService.deleteAll();

        List<Report> reports = new ArrayList<>();
        reports.add(new Report("Opis1", "files/", "sprawozdanie1", ".pdf", null, null, null));
        reports.add(new Report("Opis2", "files/", "sprawozdanie2", ".pdf", null, null, null));
        reports.add(new Report("Opis3", "files/", "sprawozdanie3", ".pdf", null, null, null));
        reports.add(new Report("Opis4", "files/", "sprawozdanie4", ".pdf", "4.0", true, null));
        reports.add(new Report("Opis5", "files/", "sprawozdanie5", ".pdf", "2.5", true, null));
        reports.add(new Report("Opis6", "files/", "sprawozdanie6", ".pdf", "5.0", true, null));
        reports.add(new Report("Opis7", "files/", "sprawozdanie7", ".pdf", "3.0", true, null));
        reports.add(new Report("Opis8", "files/", "sprawozdanie8", ".pdf", "2.0", true, null));
        reports.add(new Report("Opis9", "files/", "sprawozdanie9", ".pdf", "3.0", true, null));
        reports.add(new Report("Opis10", "files/", "sprawozdanie10", ".pdf", "3.5", true, null));
        reports.add(new Report("Opis11", "files/", "sprawozdanie11", ".pdf", "3.5", true, null));
        reports.add(new Report("Opis12", "files/", "sprawozdanie12", ".pdf", "3.5", true, null));

        reports.stream().forEach(report -> reportService.save(ReportService.toDTO(report)));
    }

    @Test
    public void findAllPageableByExample_ShouldReturnOneEntry() {

        PageRequestByExample<ReportDTO> reportDTOPageRequestByExample = new PageRequestByExample<>();
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.description = "O";
        reportDTOPageRequestByExample.example = reportDTO;

        PageResponse<ReportDTO> reportDTOPageResponse = reportService.findAll(reportDTOPageRequestByExample);
        assertEquals(reportDTOPageResponse.totalElements, 12);

    }

    @Test
    public void findAllPageableByPageRows_ShouldReturnOnePage() {

        PageRequestByExample<ReportDTO> reportDTOPageRequestByExample = new PageRequestByExample<>();
        LazyLoadEvent lazyLoadEvent = new LazyLoadEvent();
        lazyLoadEvent.rows = 3;
        reportDTOPageRequestByExample.lazyLoadEvent = lazyLoadEvent;

        PageResponse<ReportDTO> reportDTOPageResponse = reportService.findAll(reportDTOPageRequestByExample);

        assertEquals(reportDTOPageResponse.totalPages, 4);

    }

    @Test
    public void saveReport_ShouldCreateNewReport() {

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.id = 1;
        reportDTO.description = "Opis13";

        assertEquals(reportService.save(reportDTO).description, "Opis13");

    }

}

