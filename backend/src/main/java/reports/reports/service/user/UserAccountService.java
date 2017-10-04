package reports.reports.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.config.security.UserContext;
import reports.reports.domain.AppUser;
import reports.reports.domain.VerificationToken;
import reports.reports.dto.AppUserDTO;
import reports.reports.repository.AppUserRepository;
import reports.reports.repository.RoleRepository;
import reports.reports.repository.VerificationTokenRepository;
import reports.reports.service.admin.AppUserService;
import reports.reports.service.user.support.OnRegistrationCompleteEvent;

import java.util.UUID;

@Service
public class UserAccountService {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher eventPublisher;
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public UserAccountService(AppUserRepository appUserRepository,
                              PasswordEncoder passwordEncoder,
                              ApplicationEventPublisher eventPublisher,
                              VerificationTokenRepository verificationTokenRepository) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.verificationTokenRepository = verificationTokenRepository;
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
        user.setEnabled(false);
        user.getReports().clear();

        AppUser appUser = appUserRepository.save(user);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(dto));

        return AppUserService.toDTO(appUser);
    }

    public AppUserDTO enableAccount(String token) {

        VerificationToken verificationToken = verificationTokenRepository.getByToken(token);
        AppUser appUser = appUserRepository.findOne(verificationToken.getAppUser().getId());
        appUser.setEnabled(true);

        return AppUserService.toDTO(appUserRepository.save(appUser));
    }
}
