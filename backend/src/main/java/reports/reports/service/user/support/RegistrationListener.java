package reports.reports.service.user.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import reports.reports.domain.AppUser;
import reports.reports.domain.VerificationToken;
import reports.reports.dto.AppUserDTO;
import reports.reports.repository.AppUserRepository;
import reports.reports.repository.VerificationTokenRepository;

import java.util.UUID;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    private final Logger log = LoggerFactory.getLogger(RegistrationListener.class);

    private JavaMailSender mailSender;
    private AppUserRepository appUserRepository;
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public RegistrationListener(JavaMailSender mailSender,
                                AppUserRepository appUserRepository,
                                VerificationTokenRepository verificationTokenRepository) {
        this.mailSender = mailSender;
        this.appUserRepository = appUserRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.sendMailWithToken(event.getAppUserDTO());
    }

    private void sendMailWithToken(AppUserDTO dto) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(dto.email);
        mailMessage.setFrom("reportswebapplication@gmail.com");
        mailMessage.setSubject("Aktywacja konta w aplikacji Sprawozdania");
        mailMessage.setText("Aby aktywować swoje konto kliknij w link: localhost:8080/api/userAccount/enable/" + createAppUserToken(dto));

        try{
            mailSender.send(mailMessage);
        }catch (MailException me){
            log.error("Mail sending error: " + me);
        }

    }

    private String createAppUserToken(AppUserDTO dto) {

        AppUser appUser = appUserRepository.getByUserName(dto.userName);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, appUser);

        verificationTokenRepository.save(verificationToken);

        return token;
    }
}
