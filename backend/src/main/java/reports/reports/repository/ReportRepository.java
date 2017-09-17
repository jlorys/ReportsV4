package reports.reports.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.AppUser;
import reports.reports.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    Page<Report> findByUsers(AppUser appUser, Pageable pageable);

    Report getByDescription(String description);

}
