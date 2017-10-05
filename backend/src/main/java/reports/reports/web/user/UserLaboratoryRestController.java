package reports.reports.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reports.reports.dto.LaboratoryDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;

import reports.reports.service.user.UserLaboratoryService;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/userLaboratory")
public class UserLaboratoryRestController {

    private final Logger log = LoggerFactory.getLogger(UserLaboratoryRestController.class);

    private UserLaboratoryService laboratoryService;

    @Autowired
    public UserLaboratoryRestController(UserLaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
    }

    /**
     * Find a Page of Laboratory using query by example.
     */
    @PostMapping(value = "/page", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PageResponse<LaboratoryDTO>> findAll(@RequestBody PageRequestByExample<LaboratoryDTO> prbe) throws URISyntaxException {
        PageResponse<LaboratoryDTO> pageResponse = laboratoryService.findAll(prbe);
        return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<LaboratoryDTO> findById(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Find by id Laboratory : {}", id);

        return Optional.ofNullable(laboratoryService.findOne(id)).map(laboratoryDTO -> new ResponseEntity<>(laboratoryDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/findAll", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<LaboratoryDTO>> findAll() throws URISyntaxException {

        List<LaboratoryDTO> results = laboratoryService.findAll();

        return new ResponseEntity<>(results, new HttpHeaders(), HttpStatus.OK);
    }
}
