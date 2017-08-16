package reports.reports.dto.support;

import org.springframework.data.domain.Pageable;

public class PageRequestByExample<DTO> {
    public DTO example;
    public LazyLoadEvent lazyLoadEvent;

    public Pageable toPageable() {
        return lazyLoadEvent != null ? lazyLoadEvent.toPageable() : null;
    }
}