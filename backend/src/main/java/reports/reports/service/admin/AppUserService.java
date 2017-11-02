package reports.reports.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.domain.AppUser;
import reports.reports.domain.VerificationToken;
import reports.reports.dto.AppUserDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.AppUserRepository;
import reports.reports.repository.ReportRepository;
import reports.reports.repository.RoleRepository;
import reports.reports.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    private AppUserRepository appUserRepository;
    private RoleRepository roleRepository;
    private ReportRepository reportRepository;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository,
                          RoleRepository roleRepository,
                          ReportRepository reportRepository,
                          PasswordEncoder passwordEncoder,
                          VerificationTokenRepository verificationTokenRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.reportRepository = reportRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    private static AppUser toEntity(AppUserDTO dto) {
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
        user.setEnabled(dto.enabled);

        return user;
    }

    public static AppUserDTO toDTO(AppUser report) {
        return toDTO(report, 0);
    }

    public static AppUserDTO toDTO(AppUser user, int depth) {
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
        if (depth < 1) {
            dto.roles = user.getRoles().stream().map(role -> RoleService.toDTO(role)).collect(Collectors.toList());
            dto.reports = user.getReports().stream().map(report -> ReportService.toDTO(report, 1)).collect(Collectors.toList());
        }
        dto.enabled = user.isEnabled();

        return dto;
    }

    private static AppUserDTO toDTOWithoutPersonalData(AppUser user) {
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

    @Transactional(readOnly = true)
    public PageResponse<AppUserDTO> findAll(PageRequestByExample<AppUserDTO> req) {
        Example<AppUser> example = null;
        AppUser user = toEntity(req.example);

        if (user != null) {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("userName", match -> match.ignoreCase().startsWith())
                    .withMatcher("password", match -> match.ignoreCase().startsWith())
                    .withMatcher("firstName", match -> match.ignoreCase().startsWith())
                    .withMatcher("lastName", match -> match.ignoreCase().startsWith())
                    .withMatcher("email", match -> match.ignoreCase().startsWith())
                    .withMatcher("createdDate", match -> match.ignoreCase().startsWith())
                    .withMatcher("lastModifiedDate", match -> match.ignoreCase().startsWith())
                    .withMatcher("createdBy", match -> match.ignoreCase().startsWith())
                    .withMatcher("lastModifiedBy", match -> match.ignoreCase().startsWith())
                    .withMatcher("enabled", match -> match.ignoreCase().startsWith());

            example = Example.of(user, matcher);
        }

        Page<AppUser> page;
        if (example != null) {
            page = appUserRepository.findAll(example, req.toPageable());
        } else {
            page = appUserRepository.findAll(req.toPageable());
        }

        List<AppUserDTO> content = page.getContent().stream().map(appUser -> toDTO(appUser)).collect(Collectors.toList());
        return new PageResponse<>(page.getTotalPages(), page.getTotalElements(), content);
    }

    public void delete(Integer id) {
        appUserRepository.delete(id);
    }

    public void deleteOneDayUnconfirmedUsers() {

        List<VerificationToken> verificationTokens;

        verificationTokens = verificationTokenRepository.findAll().stream()
                .filter(verificationToken ->
                        verificationToken.getCreatedDate() <
                                LocalDateTime.now().minusDays(1).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli())
                .collect(Collectors.toList());

        verificationTokenRepository.delete(verificationTokens);
        verificationTokens.forEach(verificationToken -> appUserRepository.delete(verificationToken.getAppUser().getId()));

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

        if (dto.isIdSet()) {
            user.setPassword(dto.password);
        } else {
            user.setPassword(passwordEncoder.encode(dto.password));
        }

        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setEmail(dto.email);
        user.setCreatedDate(dto.createdDate);
        user.setLastModifiedDate(dto.lastModifiedDate);
        user.setCreatedBy(dto.createdBy);
        user.setLastModifiedBy(dto.lastModifiedBy);

        user.getRoles().clear();
        if (dto.roles != null) {
            dto.roles.forEach(role -> user.addRole(roleRepository.findOne(role.id)));
        }

        user.getReports().clear();
        if (dto.reports != null) {
            dto.reports.forEach(report -> user.addReport(reportRepository.findOne(report.id)));
        }

        user.setEnabled(true);

        return toDTO(appUserRepository.save(user));
    }

    @Transactional
    public AppUserDTO changePassword(AppUserDTO dto, String oldPassword, String newPassword, String newPasswordRepeat) {
        if (dto == null) {
            return null;
        }

        AppUser appUser = appUserRepository.findOne(dto.id);

        if (newPassword.matches(newPasswordRepeat)) {
            if (passwordEncoder.matches(oldPassword, appUser.getPassword())) {

                appUser.setPassword(passwordEncoder.encode(newPassword));
                return toDTO(appUserRepository.save(appUser));
            }
        }

        return null;
    }

    public void deleteAll() {
        appUserRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<AppUserDTO> findAllAppUsersWhichDoNotHaveReportWithThisId(Integer reportId) {
        List<AppUser> results = appUserRepository.findAll();
        List<AppUser> filteredResults = results.stream().filter(appUser -> appUser.getReports().stream().noneMatch(report -> report.getId().equals(reportId)))
                .collect(Collectors.toList());
        return filteredResults.stream().map(appUser -> toDTOWithoutPersonalData(appUser)).collect(Collectors.toList());
    }
}
