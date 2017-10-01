package reports.reports.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.config.security.UserContext;
import reports.reports.domain.AppUser;
import reports.reports.dto.AppUserDTO;
import reports.reports.repository.AppUserRepository;
import reports.reports.repository.RoleRepository;
import reports.reports.service.admin.AppUserService;

@Service
public class UserAccountService {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserAccountService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public AppUserDTO findLoggedUser() {
        return AppUserService.toDTO(appUserRepository.findOne(UserContext.getId()));
    }

    @Transactional
    public AppUserDTO changePassword(AppUserDTO dto, String oldPassword, String newPassword, String newPasswordRepeat) {
        if (dto == null) {
            return null;
        }

        AppUser appUser = appUserRepository.findOne(dto.id);

        if(newPassword.matches(newPasswordRepeat)){
            if(passwordEncoder.matches(oldPassword, appUser.getPassword())){

                appUser.setPassword(passwordEncoder.encode(newPassword));
                return AppUserService.toDTO(appUserRepository.save(appUser));
            }
        }

        return null;
    }

    /**
     * register non enabled user.
     */
    @Transactional
    public AppUserDTO register(AppUserDTO dto) {
        if (dto == null) {
            return null;
        }

        final AppUser user;

        user = new AppUser();

        user.setUserName(dto.userName);

        if (dto.isIdSet()) {user.setPassword(dto.password); }
        else{ user.setPassword(passwordEncoder.encode(dto.password)); }

        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setEmail(dto.email);
        user.setCreatedDate(dto.createdDate);
        user.setLastModifiedDate(dto.lastModifiedDate);
        user.setCreatedBy(dto.createdBy);
        user.setLastModifiedBy(dto.lastModifiedBy);

        user.getRoles().clear();
        if (dto.roles != null) {
            dto.roles.stream().forEach(role -> user.addRole(roleRepository.findOne(role.id)));
        }

        user.setEnabled(false);

        user.getReports().clear();

        return AppUserService.toDTO(appUserRepository.save(user));
    }
}
