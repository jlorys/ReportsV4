package reports.reports.web.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reports.reports.config.security.UserRoleService;
import reports.reports.dto.AppUserDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.admin.AppUserService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/users")
public class AppUserRestController {

    private final Logger log = LoggerFactory.getLogger(AppUserRestController.class);

    private AppUserService appUserService;
    private UserRoleService userRoleService;

    @Autowired
    public AppUserRestController(AppUserService appUserService, UserRoleService userRoleService) {
        this.appUserService = appUserService;
        this.userRoleService = userRoleService;
    }

    /**
     * Find a Page of users using query by example.
     */
    @PostMapping(value = "/page", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponse<AppUserDTO>> findAll(@RequestBody PageRequestByExample<AppUserDTO> prbe) throws URISyntaxException {
        PageResponse<AppUserDTO> pageResponse = appUserService.findAll(prbe);
        return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws URISyntaxException {

            log.debug("Delete by id User : {}", id);

            try {
                appUserService.delete(id);
                return ResponseEntity.ok().build();
            } catch (Exception x) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

    }

    @DeleteMapping(value = "/deleteOneDayUnconfirmedUsers", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteOneDayUnconfirmedUsers() throws URISyntaxException {

            try {
                appUserService.deleteOneDayUnconfirmedUsers();
                return ResponseEntity.ok().build();
            } catch (Exception x) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppUserDTO> findById(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Find by id User : {}", id);

        return Optional.ofNullable(appUserService.findOne(id)).map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/name/{userName}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppUserDTO> findByUserName(@PathVariable String userName) throws URISyntaxException {

        log.debug("Find by userName : {}", userName);

        return Optional.ofNullable(appUserService.findByUserName(userName)).map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppUserDTO> update(@RequestBody AppUserDTO userDTO) throws URISyntaxException {

        log.debug("Update UserDTO : {}", userDTO);

        if (!userDTO.isIdSet()) {
            return create(userDTO);
        }

        AppUserDTO result = appUserService.save(userDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping(value = "/changePassword/{userId}/{oldPassword}/{newPassword}/{newPasswordRepeat}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('REVIEWER') or hasAuthority('USER')")
    public ResponseEntity<?> updatePassword(@PathVariable Integer userId,
                                            @PathVariable String oldPassword,
                                            @PathVariable String newPassword,
                                            @PathVariable String newPasswordRepeat) throws URISyntaxException {

        AppUserDTO appUserDTO = appUserService.findOne(userId);

        if (Optional.ofNullable(appUserDTO).isPresent()) {
            AppUserDTO result = appUserService.changePassword(appUserDTO, oldPassword, newPassword, newPasswordRepeat);
            return ResponseEntity.ok().body(result);
        }

        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppUserDTO> create(@RequestBody AppUserDTO userDTO) throws URISyntaxException {

        log.debug("Create UserDTO : {}", userDTO);

        if (userDTO.isIdSet()) {
            return ResponseEntity.badRequest().header("Failure", "Cannot create User with existing ID").body(null);
        }

        AppUserDTO result = appUserService.save(userDTO);

        return ResponseEntity.created(new URI("/api/users/" + result.id)).body(result);
    }

    @GetMapping(value = "/findAllAppUsersWhichDoNotHaveReportWithThisId/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<AppUserDTO>> findAllAppUsersWhichDoNotHaveReportWithThisId(@PathVariable Integer id) throws URISyntaxException {

        List<AppUserDTO> results = appUserService.findAllAppUsersWhichDoNotHaveReportWithThisId(id);

        return new ResponseEntity<>(results, new HttpHeaders(), HttpStatus.OK);
    }

}
