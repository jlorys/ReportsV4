package reports.reports.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reports.reports.config.security.UserContext;
import reports.reports.dto.AppUserDTO;
import reports.reports.service.AppUserService;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class SecurityResource {

    @Autowired
    private AppUserService appUserService;

    @GetMapping(value = "/authenticated", produces = APPLICATION_JSON_VALUE)
    public boolean isAuthenticated() {
        return UserContext.getId() != null;
    }

    /**
     * Find logged User.
     */
    @GetMapping(value = "/loggedUser", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUserDTO> findLoggedUser() throws URISyntaxException {

        return Optional.ofNullable(appUserService.findOne(UserContext.getId())).map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
