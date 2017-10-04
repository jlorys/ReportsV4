package reports.reports.service.user.support;

import org.springframework.context.ApplicationEvent;
import reports.reports.dto.AppUserDTO;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private AppUserDTO appUserDTO;

    public OnRegistrationCompleteEvent(AppUserDTO appUserDTO) {
        super(appUserDTO);
        this.appUserDTO = appUserDTO;
    }

    AppUserDTO getAppUserDTO() {
        return appUserDTO;
    }
}
