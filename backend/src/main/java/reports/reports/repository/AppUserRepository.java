package reports.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

	AppUser getByUserName(String userName);

}
