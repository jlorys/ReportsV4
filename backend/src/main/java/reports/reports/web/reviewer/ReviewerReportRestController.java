package reports.reports.web.reviewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private ReviewerReportService reviewerReportService;

    /**
     * Find a Page of Reports using query by example.
     */
    @PostMapping(value = "/page", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<ReportDTO>> findAll(@RequestBody PageRequestByExample<ReportDTO> prbe) throws URISyntaxException {
        PageResponse<ReportDTO> pageResponse = reviewerReportService.findAll(prbe);
        return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Find by id Report.
     */
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> findById(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Find by id Report : {}", id);

        return Optional.ofNullable(reviewerReportService.findOne(id)).map(reportDTO -> new ResponseEntity<>(reportDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Update Report.
     */
    @PutMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> update(@RequestBody ReportDTO reportDTO) throws URISyntaxException {

        log.debug("Update ReportDTO : {}", reportDTO);

        if (!reportDTO.isIdSet()) {
            return null;
        }

        ReportDTO result = reviewerReportService.save(reportDTO);

        return ResponseEntity.ok().body(result);
    }

}