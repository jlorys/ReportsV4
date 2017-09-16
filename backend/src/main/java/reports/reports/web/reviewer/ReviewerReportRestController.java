package reports.reports.web.reviewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reports.reports.dto.ReportDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.reviewer.ReviewerReportService;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/reviewerReports")
public class ReviewerReportRestController {

    private final Logger log = LoggerFactory.getLogger(ReviewerReportRestController.class);

    private ReviewerReportService reviewerReportService;

    @Autowired
    public ReviewerReportRestController(ReviewerReportService reviewerReportService) {
        this.reviewerReportService = reviewerReportService;
    }

    /**
     * Update Report.
     */
    @PutMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('REVIEWER')")
    public ResponseEntity<ReportDTO> update(@RequestBody ReportDTO reportDTO) throws URISyntaxException {

        log.debug("Update ReportDTO : {}", reportDTO);

        if (!reportDTO.isIdSet()) {
            return null;
        }

        ReportDTO result = reviewerReportService.save(reportDTO);

        return ResponseEntity.ok().body(result);
    }

}