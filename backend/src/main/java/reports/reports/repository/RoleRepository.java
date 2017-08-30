package reports.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.Role;

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
