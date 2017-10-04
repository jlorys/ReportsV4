package reports.reports.service.reviewer.support;

import org.springframework.context.ApplicationListener;
import reports.reports.dto.AppUserDTO;
import reports.reports.service.user.support.OnRegistrationCompleteEvent;

public class GradeListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {


    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.sendMailWithGrade(event);
    }

    private void sendMailWithGrade(OnRegistrationCompleteEvent event) {
        AppUserDTO appUserDTO = event.getAppUserDTO();

    }
}
