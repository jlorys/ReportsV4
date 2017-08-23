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
import reports.reports.service.RoleService;

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
    private RoleService roleService;

    /**
    * Find by id Role.
    */
    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> findById(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Find by id Role : {}", id);

        return Optional.ofNullable(roleService.findOne(id)).map(roleDTO -> new ResponseEntity<>(roleDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Find a Page of Role using query by example.
     */
    @RequestMapping(value = "/page", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<RoleDTO>> findAll(@RequestBody PageRequestByExample<RoleDTO> prbe) throws URISyntaxException {
        PageResponse<RoleDTO> pageResponse = roleService.findAll(prbe);
        return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/findAllRolesWhichDoNotHaveAppUserWithThisId/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> findAllRolesWhichDoNotHaveAppUserWithThisId(@PathVariable Integer id) throws URISyntaxException {

        List<RoleDTO> results = roleService.findAllRolesWhichDoNotHaveAppUserWithThisId(id);

        return new ResponseEntity<>(results, new HttpHeaders(), HttpStatus.OK);
    }
}