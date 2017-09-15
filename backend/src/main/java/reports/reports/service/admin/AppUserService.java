package reports.reports.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.domain.AppUser;
import reports.reports.domain.AppUser_;
import reports.reports.dto.AppUserDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.AppUserRepository;
import reports.reports.repository.ReportRepository;
import reports.reports.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public PageResponse<AppUserDTO> findAll(PageRequestByExample<AppUserDTO> req) {
        Example<AppUser> example = null;
        AppUser user = toEntity(req.example);

        if (user != null) {
            ExampleMatcher matcher = ExampleMatcher.matching() //
                    .withMatcher(AppUser_.userName.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(AppUser_.password.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(AppUser_.firstName.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(AppUser_.lastName.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(AppUser_.email.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(AppUser_.createdDate.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(AppUser_.lastModifiedDate.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(AppUser_.createdBy.getName(), match -> match.ignoreCase().startsWith())
                    .withMatcher(AppUser_.lastModifiedBy.getName(), match -> match.ignoreCase().startsWith());

            example = Example.of(user, matcher);
        }

        Page<AppUser> page;
        if (example != null) {
            page = appUserRepository.findAll(example, req.toPageable());
        } else {
            page = appUserRepository.findAll(req.toPageable());
        }

        List<AppUserDTO> content = page.getContent().stream().map(this::toDTO).collect(Collectors.toList());
        return new PageResponse<>(page.getTotalPages(), page.getTotalElements(), content);
    }

    public void delete(Integer id) {
        appUserRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public AppUserDTO findOne(Integer id) {
        AppUser appUser = appUserRepository.findOne(id);
        return toDTO(appUser);
    }

    @Transactional(readOnly = true)
    public AppUserDTO findByUserName(String username) {
        return toDTO(appUserRepository.getByUserName(username));
    }

    /**
     * Save the passed dto as a new entity or update the corresponding entity if any.
     */
    @Transactional
    public AppUserDTO save(AppUserDTO dto) {
        if (dto == null) {
            return null;
        }

        final AppUser user;

        if (dto.isIdSet()) {
            AppUser userTmp = appUserRepository.findOne(dto.id);
            if (userTmp != null) {
                user = userTmp;
            } else {
                user = new AppUser();
                user.setId(dto.id);
            }
        } else {
            user = new AppUser();
        }

        user.setUserName(dto.userName);

        if (dto.isIdSet()) {user.setPassword(dto.password); }
        else{ user.setPassword(passwordEncoder.encode(dto.password)); }

        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setEmail(dto.email);
        user.setCreatedDate(dto.createdDate);
        user.setLastModifiedDate(dto.lastModifiedDate);
        user.setCreatedBy(dto.createdBy);
        user.setLastModifiedBy(dto.lastModifiedBy);

        user.getRoles().clear();
        if (dto.roles != null) {
            dto.roles.stream().forEach(role -> user.addRole(roleRepository.findOne(role.id)));
        }

        user.getReports().clear();
        if (dto.reports != null) {
            dto.reports.stream().forEach(report -> user.addReport(reportRepository.findOne(report.id)));
        }

        return toDTO(appUserRepository.save(user));
    }


    @Transactional
    public AppUserDTO changePassword(AppUserDTO dto, String oldPassword, String newPassword, String newPasswordRepeat) {
        if (dto == null) {
            return null;
        }

        AppUser appUser = appUserRepository.findOne(dto.id);

        if(newPassword.matches(newPasswordRepeat)){
            if(passwordEncoder.matches(oldPassword, appUser.getPassword())){

                appUser.setPassword(passwordEncoder.encode(newPassword));
                return toDTO(appUserRepository.save(appUser));
            }
        }

        return null;
    }

    /**
     * Converts the passed dto to a User.
     * Convenient for query by example.
     */
    public AppUser toEntity(AppUserDTO dto) {
        if (dto == null) {
            return null;
        }

        AppUser user = new AppUser();

        user.setId(dto.id);
        user.setUserName(dto.userName);
        user.setPassword(dto.password);
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setEmail(dto.email);
        user.setCreatedDate(dto.createdDate);
        user.setLastModifiedDate(dto.lastModifiedDate);
        user.setCreatedBy(dto.createdBy);
        user.setLastModifiedBy(dto.lastModifiedBy);

        return user;
    }

    public AppUserDTO toDTO(AppUser report) {
        return toDTO(report, 0);
    }

    /**
     * Converts the passed user to a DTO. The depth is used to control the
     * amount of association you want.
     *
     * @param user
     */
    public AppUserDTO toDTO(AppUser user, int depth) {
        if (user == null) {
            return null;
        }

        AppUserDTO dto = new AppUserDTO();

        dto.id = user.getId();
        dto.userName = user.getUserName();
        dto.password = user.getPassword();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.email = user.getEmail();
        dto.createdDate = user.getCreatedDate();
        dto.lastModifiedDate = user.getLastModifiedDate();
        dto.createdBy = user.getCreatedBy();
        dto.lastModifiedBy = user.getLastModifiedBy();
        if(depth<1){
            dto.roles = user.getRoles().stream().map(role -> roleService.toDTO(role)).collect(Collectors.toList());
            dto.reports = user.getReports().stream().map(report -> reportService.toDTO(report, 1)).collect(Collectors.toList());
        }

        return dto;
    }

    public void deleteAll() {
        appUserRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<AppUserDTO> findAllAppUsersWhichDoNotHaveReportWithThisId(Integer reportId) {
        List<AppUser> results = appUserRepository.findAll();
        List<AppUser> filteredResults = results.stream().filter(appUser -> appUser.getReports().stream().noneMatch(report -> report.getId().equals(reportId)))
                .collect(Collectors.toList());
        return filteredResults.stream().map(this::toDTOWithoutPersonalData).collect(Collectors.toList());
    }

    public AppUserDTO toDTOWithoutPersonalData(AppUser user) {
        if (user == null) {
            return null;
        }

        AppUserDTO dto = new AppUserDTO();

        dto.id = user.getId();
        dto.userName = user.getUserName();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();

        return dto;
    }
}
