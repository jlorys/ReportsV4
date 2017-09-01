package reports.reports.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reports.reports.dto.LaboratoryDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.LaboratoryService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/laboratory")
public class LaboratoryRestController {

    private final Logger log = LoggerFactory.getLogger(AppUserRestController.class);

    @Autowired
    private LaboratoryService laboratoryService;

    /**
     * Find a Page of Laboratory using query by example.
     */
    @PostMapping(value = "/page", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<LaboratoryDTO>> findAll(@RequestBody PageRequestByExample<LaboratoryDTO> prbe) throws URISyntaxException {
        PageResponse<LaboratoryDTO> pageResponse = laboratoryService.findAll(prbe);
        return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Delete by id Laboratory.
     */
    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Delete by id Laboratory : {}", id);

        try {
            laboratoryService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception x) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Find by id Laboratory.
     */
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LaboratoryDTO> findById(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Find by id Laboratory : {}", id);

        return Optional.ofNullable(laboratoryService.findOne(id)).map(laboratoryDTO -> new ResponseEntity<>(laboratoryDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Update Laboratory.
     */
    @PutMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LaboratoryDTO> update(@RequestBody LaboratoryDTO laboratoryDTO) throws URISyntaxException {

        log.debug("Update Laboratory : {}", laboratoryDTO);

        if (!laboratoryDTO.isIdSet()) {
            return create(laboratoryDTO);
        }

        LaboratoryDTO result = laboratoryService.save(laboratoryDTO);

        return ResponseEntity.ok().body(result);
    }

    /**
     * Create a new Laboratory.
     */
    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LaboratoryDTO> create(@RequestBody LaboratoryDTO laboratoryDTO) throws URISyntaxException {

        log.debug("Create LaboratoryDTO : {}", laboratoryDTO);

        if (laboratoryDTO.isIdSet()) {
            return ResponseEntity.badRequest().header("Failure", "Cannot create Laboratory with existing ID").body(null);
        }

        LaboratoryDTO result = laboratoryService.save(laboratoryDTO);

        return ResponseEntity.created(new URI("/api/laboratory" + result.id)).body(result);
    }
}
