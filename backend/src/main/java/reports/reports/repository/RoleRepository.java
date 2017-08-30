package reports.reports.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.Role;
import reports.reports.domain.Role_;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Return the persistent instance of {@link Role} with the given unique property value roleName,
     * or null if there is no such persistent instance.
     *
     * @param roleName the unique value
     * @return the corresponding {@link Role} persistent instance or null
     */
    Role getByRoleName(String roleName);
}
