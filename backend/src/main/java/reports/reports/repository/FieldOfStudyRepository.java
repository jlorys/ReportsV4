package reports.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.FieldOfStudy;

public interface FieldOfStudyRepository  extends JpaRepository<FieldOfStudy, Integer> {

}
