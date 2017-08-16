package reports.reports.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reports.reports.config.security.UserContext;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
public class SecurityResource {

    @RequestMapping(value = "/authenticated", method = GET, produces = APPLICATION_JSON_VALUE)
    public boolean isAuthenticated() {
        return UserContext.getId() != null;
    }

}
