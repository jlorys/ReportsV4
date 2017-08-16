package reports.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
