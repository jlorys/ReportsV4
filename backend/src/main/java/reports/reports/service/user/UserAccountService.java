package reports.reports.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.config.security.UserContext;
import reports.reports.domain.AppUser;
import reports.reports.dto.AppUserDTO;
import reports.reports.repository.AppUserRepository;
import reports.reports.service.admin.AppUserService;

@Service
public class UserAccountService {

    @Autowired
    AppUserService appUserService;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AppUserDTO findLoggedUser() {
        return appUserService.toDTO(appUserRepository.findOne(UserContext.getId()));
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
                return appUserService.toDTO(appUserRepository.save(appUser));
            }
        }

        return null;
    }
}
