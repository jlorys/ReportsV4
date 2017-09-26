package reports.reports.web.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reports.reports.dto.FieldOfStudyDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.admin.FieldOfStudyService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/fieldOfStudy")
public class FieldOfStudyRestController {

    private final Logger log = LoggerFactory.getLogger(FieldOfStudyRestController.class);

    private FieldOfStudyService fieldOfStudyService;

    @Autowired
    public FieldOfStudyRestController(FieldOfStudyService fieldOfStudyService) {
        this.fieldOfStudyService = fieldOfStudyService;
    }

    /**
     * Find a Page of FieldOfStudy using query by example.
     */
    @PostMapping(value = "/page", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REVIEWER') or hasAuthority('USER')")
    public ResponseEntity<PageResponse<FieldOfStudyDTO>> findAll(@RequestBody PageRequestByExample<FieldOfStudyDTO> prbe) throws URISyntaxException {
        PageResponse<FieldOfStudyDTO> pageResponse = fieldOfStudyService.findAll(prbe);
        return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Delete by id FieldOfStudy.
     */
    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REVIEWER')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Delete by id FieldOfStudy : {}", id);

        try {
            fieldOfStudyService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception x) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Find by id FieldOfStudy.
     */
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REVIEWER') or hasAuthority('USER')")
    public ResponseEntity<FieldOfStudyDTO> findById(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Find by id FieldOfStudy : {}", id);

        return Optional.ofNullable(fieldOfStudyService.findOne(id)).map(fieldOfStudyDTO -> new ResponseEntity<>(fieldOfStudyDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Update FieldOfStudy.
     */
    @PutMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REVIEWER')")
    public ResponseEntity<FieldOfStudyDTO> update(@RequestBody FieldOfStudyDTO fieldOfStudyDTO) throws URISyntaxException {

        log.debug("Update FieldOfStudyDTO : {}", fieldOfStudyDTO);

        if (!fieldOfStudyDTO.isIdSet()) {
            return create(fieldOfStudyDTO);
        }

        FieldOfStudyDTO result = fieldOfStudyService.save(fieldOfStudyDTO);

        return ResponseEntity.ok().body(result);
    }

    /**
     * Create a new FieldOfStudy.
     */
    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REVIEWER')")
    public ResponseEntity<FieldOfStudyDTO> create(@RequestBody FieldOfStudyDTO fieldOfStudyDTO) throws URISyntaxException {

        log.debug("Create FieldOfStudyDTO : {}", fieldOfStudyDTO);

        if (fieldOfStudyDTO.isIdSet()) {
            return ResponseEntity.badRequest().header("Failure", "Cannot create FieldOfStudy with existing ID").body(null);
        }

        FieldOfStudyDTO result = fieldOfStudyService.save(fieldOfStudyDTO);

        return ResponseEntity.created(new URI("/api/fieldOfStudy" + result.id)).body(result);
    }

    @GetMapping(value = "/findAll", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REVIEWER') or hasAuthority('USER')")
    public ResponseEntity<List<FieldOfStudyDTO>> findAll() throws URISyntaxException {

        List<FieldOfStudyDTO> results = fieldOfStudyService.findAll();

        return new ResponseEntity<>(results, new HttpHeaders(), HttpStatus.OK);
    }
}
