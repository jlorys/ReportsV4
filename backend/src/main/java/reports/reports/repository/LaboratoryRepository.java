package reports.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.Laboratory;

public interface LaboratoryRepository extends JpaRepository<Laboratory, Integer> {

}
