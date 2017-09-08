package reports.reports.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reports.reports.service.AppUserService;

@Service
public class UserRoleService {

    @Autowired
    private AppUserService appUserService;

    public Boolean isLoggedUserHasRoleAdmin() {
        return appUserService.findOne(UserContext.getId()).roles.stream().anyMatch(roleDTO -> roleDTO.roleName.matches("Admin"));
    }

    public Boolean isLoggedUserHasRoleUser() {
        return appUserService.findOne(UserContext.getId()).roles.stream().anyMatch(roleDTO -> roleDTO.roleName.matches("User"));
    }
}
