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

    default List<Role> complete(String query, int maxResults) {
        Role probe = new Role();
        probe.setRoleName(query);

        ExampleMatcher matcher = ExampleMatcher.matching() //
                .withMatcher(Role_.roleName.getName(), match -> match.ignoreCase().startsWith());

        Page<Role> page = findAll(Example.of(probe, matcher), new PageRequest(0, maxResults));
        return page.getContent();
    }
}
