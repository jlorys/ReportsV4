package reports.reports.service.reviewer.support;

import org.springframework.context.ApplicationEvent;
import reports.reports.dto.AppUserDTO;

public class OnGradeEvent extends ApplicationEvent {

    private AppUserDTO appUserDTO;

    public OnGradeEvent(AppUserDTO appUserDTO) {
        super(appUserDTO);
        this.appUserDTO = appUserDTO;
    }

    public AppUserDTO getAppUserDTO() {
        return appUserDTO;
    }

    public void setAppUserDTO(AppUserDTO appUserDTO) {
        this.appUserDTO = appUserDTO;
    }
}
