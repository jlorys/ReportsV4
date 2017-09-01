package reports.reports.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reports.reports.dto.SubjectDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.SubjectService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/subject")
public class SubjectRestController {

    private final Logger log = LoggerFactory.getLogger(AppUserRestController.class);

    @Autowired
    private SubjectService subjectService;

    /**
     * Find a Page of Subject using query by example.
     */
    @PostMapping(value = "/page", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<SubjectDTO>> findAll(@RequestBody PageRequestByExample<SubjectDTO> prbe) throws URISyntaxException {
        PageResponse<SubjectDTO> pageResponse = subjectService.findAll(prbe);
        return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Delete by id Subject.
     */
    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Delete by id Subject : {}", id);

        try {
            subjectService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception x) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Find by id Subject.
     */
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectDTO> findById(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Find by id Subject : {}", id);

        return Optional.ofNullable(subjectService.findOne(id)).map(subjectDTO -> new ResponseEntity<>(subjectDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Update Subject.
     */
    @PutMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectDTO> update(@RequestBody SubjectDTO subjectDTO) throws URISyntaxException {

        log.debug("Update Subject : {}", subjectDTO);

        if (!subjectDTO.isIdSet()) {
            return create(subjectDTO);
        }

        SubjectDTO result = subjectService.save(subjectDTO);

        return ResponseEntity.ok().body(result);
    }

    /**
     * Create a new Subject.
     */
    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectDTO> create(@RequestBody SubjectDTO subjectDTO) throws URISyntaxException {

        log.debug("Create SubjectDTO : {}", subjectDTO);

        if (subjectDTO.isIdSet()) {
            return ResponseEntity.badRequest().header("Failure", "Cannot create Subject with existing ID").body(null);
        }

        SubjectDTO result = subjectService.save(subjectDTO);

        return ResponseEntity.created(new URI("/api/subject" + result.id)).body(result);
    }
}