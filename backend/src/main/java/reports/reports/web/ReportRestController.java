package reports.reports.web;

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
import reports.reports.service.ReportService;
import reports.reports.web.support.AutoCompleteQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/reports")
public class ReportRestController {

    private final Logger log = LoggerFactory.getLogger(AppUserRestController.class);

    @Autowired
    private ReportService reportService;

    /**
     * Find a Page of Reports using query by example.
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<ReportDTO>> findAll(@RequestBody PageRequestByExample<ReportDTO> prbe) throws URISyntaxException {
        PageResponse<ReportDTO> pageResponse = reportService.findAll(prbe);
        return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Find by id Report.
     */
    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> findById(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Find by id Report : {}", id);

        return Optional.ofNullable(reportService.findOne(id)).map(reportDTO -> new ResponseEntity<>(reportDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Delete by id Report.
     */
    @RequestMapping(value = "/{id}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Delete by id Report : {}", id);

        try {
            reportService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception x) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Update Report.
     */
    @RequestMapping(value = "/", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> update(@RequestBody ReportDTO reportDTO) throws URISyntaxException {

        log.debug("Update ReportDTO : {}", reportDTO);

        if (!reportDTO.isIdSet()) {
            return create(reportDTO);
        }

        ReportDTO result = reportService.save(reportDTO);

        return ResponseEntity.ok().body(result);
    }

    /**
     * Create a new User.
     */
    @RequestMapping(value = "/", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> create(@RequestBody ReportDTO reportDTO) throws URISyntaxException {

        log.debug("Create ReportDTO : {}", reportDTO);

        if (reportDTO.isIdSet()) {
            return ResponseEntity.badRequest().header("Failure", "Cannot create User with existing ID").body(null);
        }

        ReportDTO result = reportService.save(reportDTO);

        return ResponseEntity.created(new URI("/api/users/" + result.id)).body(result);
    }

    /**
     * Auto complete support.
     */
    @RequestMapping(value = "/complete", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportDTO>> complete(@RequestBody AutoCompleteQuery acq) throws URISyntaxException {

        List<ReportDTO> results = reportService.complete(acq.query, acq.maxResults);

        return new ResponseEntity<>(results, new HttpHeaders(), HttpStatus.OK);
    }
}