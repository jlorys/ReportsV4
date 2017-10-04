package reports.reports.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reports.reports.dto.AppUserDTO;
import reports.reports.service.user.UserAccountService;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/userAccount")
public class UserAccountRestController {

    private final Logger log = LoggerFactory.getLogger(UserAccountRestController.class);

    private UserAccountService userAccountService;

    @Autowired
    public UserAccountRestController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * Update User password.
     */
    @PutMapping(value = "/changePassword/{oldPassword}/{newPassword}/{newPasswordRepeat}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePassword(@PathVariable String oldPassword,
                                            @PathVariable String newPassword,
                                            @PathVariable String newPasswordRepeat) throws URISyntaxException {

        AppUserDTO appUserDTO = userAccountService.findLoggedUser();

        if (Optional.ofNullable(appUserDTO).isPresent()) {
            AppUserDTO result = userAccountService.changePassword(appUserDTO, oldPassword, newPassword, newPasswordRepeat);
            return ResponseEntity.ok().body(result);
        }

        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    /**
     * Register User.
     */
    @PutMapping(value = "/register", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUserDTO> register(@RequestBody AppUserDTO userDTO) throws URISyntaxException {

        log.debug("Register UserDTO : {}", userDTO);

        AppUserDTO result = userAccountService.register(userDTO);

        return ResponseEntity.ok().body(result);
    }

    /**
     * Enable User.
     */
    @GetMapping(value = "/enable/{token}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUserDTO> enable(@PathVariable String token) throws URISyntaxException {

        AppUserDTO result = userAccountService.enableAccount(token);

        return ResponseEntity.ok().body(result);
    }
}
