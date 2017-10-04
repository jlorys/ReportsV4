package reports.reports.web.user;

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
import reports.reports.service.user.UserReportService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/userReports")
public class UserReportRestController {

    private final Logger log = LoggerFactory.getLogger(UserReportRestController.class);

    private UserReportService userReportService;

    @Autowired
    public UserReportRestController(UserReportService userReportService) {
        this.userReportService = userReportService;
    }

    /**
     * Find a Page of Reports using query by example.
     */
    @PostMapping(value = "/page", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PageResponse<ReportDTO>> findAll(@RequestBody PageRequestByExample<ReportDTO> prbe) throws URISyntaxException {
        PageResponse<ReportDTO> pageResponse = userReportService.findAll(prbe);
        return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Find by id Report.
     */
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ReportDTO> findById(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Find by id Report : {}", id);

        return Optional.ofNullable(userReportService.findOne(id)).map(reportDTO -> new ResponseEntity<>(reportDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }

    /**
     * Delete by id Report.
     */
    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Delete by id Report : {}", id);

        try {
            userReportService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception x) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Update Report.
     */
    @PutMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ReportDTO> update(@RequestBody ReportDTO reportDTO) throws URISyntaxException {

        log.debug("Update ReportDTO : {}", reportDTO);

        if (!reportDTO.isIdSet()) {
            return create(reportDTO);
        }

        ReportDTO result = userReportService.save(reportDTO);

        return ResponseEntity.ok().body(result);
    }

    /**
     * Create a new Report.
     */
    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ReportDTO> create(@RequestBody ReportDTO reportDTO) throws URISyntaxException {

        log.debug("Create ReportDTO : {}", reportDTO);

        if (reportDTO.isIdSet()) {
            return ResponseEntity.badRequest().header("Failure", "Cannot create Report with existing ID").body(null);
        }

        ReportDTO result = userReportService.save(reportDTO);

        return ResponseEntity.created(new URI("/api/users/" + result.id)).body(result);
    }

    @GetMapping("/file/{reportId}")
    @PreAuthorize("hasAuthority('USER')")
    public void downloadFile (@PathVariable Integer reportId, HttpServletResponse response) throws IOException {
        userReportService.downloadFile(reportId, response);
    }
}