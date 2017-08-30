package reports.reports.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.AppUser;
import reports.reports.domain.AppUser_;

import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
	/**
	 * Return the persistent instance of {@link AppUser} with the given unique property value userName,
	 * or null if there is no such persistent instance.
	 *
	 * @param userName the unique value
	 * @return the corresponding {@link AppUser} persistent instance or null
	 */
	AppUser getByUserName(String userName);
}
