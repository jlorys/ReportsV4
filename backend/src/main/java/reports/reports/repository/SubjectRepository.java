package reports.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    Subject getByName(String name);

}
