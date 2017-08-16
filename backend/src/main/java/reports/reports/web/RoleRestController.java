package reports.reports.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reports.reports.dto.RoleDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.RoleRepository;
import reports.reports.service.RoleService;
import reports.reports.web.support.AutoCompleteQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/roles")
public class RoleRestController {

    private final Logger log = LoggerFactory.getLogger(RoleRestController.class);

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleDTOService;

    /**
     * Create a new Role.
     */
    @RequestMapping(value = "/", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> create(@RequestBody RoleDTO roleDTO) throws URISyntaxException {

        log.debug("Create RoleDTO : {}", roleDTO);

        if (roleDTO.isIdSet()) {
            return ResponseEntity.badRequest().header("Failure", "Cannot create Role with existing ID").body(null);
        }

        RoleDTO result = roleDTOService.save(roleDTO);

        return ResponseEntity.created(new URI("/api/roles/" + result.id)).body(result);
    }

    /**
    * Find by id Role.
    */
    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> findById(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Find by id Role : {}", id);

        return Optional.ofNullable(roleDTOService.findOne(id)).map(roleDTO -> new ResponseEntity<>(roleDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Update Role.
     */
    @RequestMapping(value = "/", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> update(@RequestBody RoleDTO roleDTO) throws URISyntaxException {

        log.debug("Update RoleDTO : {}", roleDTO);

        if (!roleDTO.isIdSet()) {
            return create(roleDTO);
        }

        RoleDTO result = roleDTOService.save(roleDTO);

        return ResponseEntity.ok().body(result);
    }

    /**
     * Find a Page of Role using query by example.
     */
    @RequestMapping(value = "/page", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<RoleDTO>> findAll(@RequestBody PageRequestByExample<RoleDTO> prbe) throws URISyntaxException {
        PageResponse<RoleDTO> pageResponse = roleDTOService.findAll(prbe);
        return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
    }

    /**
    * Auto complete support.
    */
    @RequestMapping(value = "/complete", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> complete(@RequestBody AutoCompleteQuery acq) throws URISyntaxException {

        List<RoleDTO> results = roleDTOService.complete(acq.query, acq.maxResults);

        return new ResponseEntity<>(results, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Delete by id Role.
     */
    @RequestMapping(value = "/{id}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Delete by id Role : {}", id);

        try {
            roleRepository.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception x) {
            // todo: dig exception, most likely org.hibernate.exception.ConstraintViolationException
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}