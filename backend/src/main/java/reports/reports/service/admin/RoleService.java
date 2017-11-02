package reports.reports.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.domain.Role;
import reports.reports.dto.RoleDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.RoleRepository;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public RoleDTO findOne(Integer id) {
        return toDTO(roleRepository.findOne(id));
    }

    @Transactional(readOnly = true)
    public List<RoleDTO> findAllRolesWhichDoNotHaveAppUserWithThisId(Integer userId) {
        List<Role> results = roleRepository.findAll();
        List<Role> filteredResults = results.stream().filter(role -> role.getUsers().stream().noneMatch(user -> user.getId().equals(userId)))
                .collect(Collectors.toList());
        return filteredResults.stream().map(role -> toDTO(role)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<RoleDTO> findAll(PageRequestByExample<RoleDTO> req) {
        Example<Role> example = null;
        Role role = toEntity(req.example);

        if (role != null) {
            ExampleMatcher matcher = ExampleMatcher.matching() //
                    .withMatcher("roleName", match -> match.ignoreCase().startsWith());

            example = Example.of(role, matcher);
        }

        Page<Role> page;
        if (example != null) {
            page = roleRepository.findAll(example, req.toPageable());
        } else {
            page = roleRepository.findAll(req.toPageable());
        }

        List<RoleDTO> content = page.getContent().stream().map(role1 -> toDTO(role1)).collect(Collectors.toList());
        return new PageResponse<>(page.getTotalPages(), page.getTotalElements(), content);
    }

    public static RoleDTO toDTO(Role role) {
        return toDTO(role, 1);
    }


    public static RoleDTO toDTO(Role role, int depth) {
        if (role == null) {
            return null;
        }

        RoleDTO dto = new RoleDTO();

        dto.id = role.getId();
        dto.roleName = role.getRoleName();
        dto.description = role.getDescription();

        return dto;
    }

    private static Role toEntity(RoleDTO dto) {
        if (dto == null) {
            return null;
        }

        Role role = new Role();

        role.setId(dto.id);
        role.setRoleName(dto.roleName);
        role.setDescription(dto.description);

        return role;
    }
}