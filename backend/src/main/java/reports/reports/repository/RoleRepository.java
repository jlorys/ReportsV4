package reports.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reports.reports.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role getByRoleName(String roleName);

}

