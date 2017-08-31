package reports.reports.repository.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reports.reports.domain.*;
import reports.reports.repository.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
class RunAtStart {

    private final AppUserRepository appUserRepository;
    private final FieldOfStudyRepository fieldOfStudyRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final ReportRepository reportRepository;
    private final RoleRepository roleRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public RunAtStart(AppUserRepository appUserRepository,
                      FieldOfStudyRepository fieldOfStudyRepository,
                      LaboratoryRepository laboratoryRepository,
                      ReportRepository reportRepository,
                      RoleRepository roleRepository,
                      SubjectRepository subjectRepository) {
        this.appUserRepository = appUserRepository;
        this.fieldOfStudyRepository = fieldOfStudyRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.reportRepository = reportRepository;
        this.roleRepository = roleRepository;
        this.subjectRepository = subjectRepository;
    }

    @PostConstruct
    public void runAtStart() {
        generateManyUsers();
        generateRoles();
        generateFieldsOfStudies();
        generateSubjects();
        generateLaboratories();
        generateReports();
        addRolesToUsers();
        addReportsToUsers();
    }

    private void generateManyUsers() {
        List<AppUser> appUsers = new ArrayList<>();
        appUsers.add(new AppUser("admin", "admin", "Jakub", "Loryś", "kuba35@vp.pl"));
        appUsers.add(new AppUser("user1", "user1", "Dylon", "Sanawabicz", "ds@x.pl"));
        appUsers.add(new AppUser("user2", "user2", "Arkadiusz", "Głowa", "ag@x.pl"));
        appUsers.add(new AppUser("user3", "user3", "Mateusz", "Morfeusz", "mm@x.pl"));
        appUsers.add(new AppUser("user4", "user4", "Rafał", "Śmigacz", "rs@x.pl"));
        appUsers.add(new AppUser("user5", "user5", "Alojzy", "Wichajster", "aw@x.pl"));
        appUsers.add(new AppUser("user6", "user6", "Bob", "Malajski", "bm@x.pl"));
        appUsers.add(new AppUser("user7", "user7", "James", "Łatana", "jl@x.pl"));
        appUsers.add(new AppUser("user8", "user8", "Czesław", "Korcipa", "ck@x.pl"));
        appUsers.add(new AppUser("user9", "user9", "Jan", "Łasica", "jl@x.pl"));
        appUsers.add(new AppUser("user10", "user10", "Dominik", "Jahaś", "dj@x.pl"));
        appUsers.add(new AppUser("user11", "user11", "Bogusław", "Łęcina", "bl@x.pl"));
        appUsers.add(new AppUser("user12", "user12", "Mariusz", "Tracz", "mt@x.pl"));
        appUsers.add(new AppUser("user13", "user13", "Sylwester", "Kasztan", "sk@x.pl"));

        appUsers.stream().forEach(appUser -> appUserRepository.save(appUser));
    }

    private void generateRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("User"));
        roles.add(new Role("Admin"));

        roles.stream().forEach(role -> roleRepository.save(role));
    }

    private void generateFieldsOfStudies() {
        List<FieldOfStudy> fieldsOfStudies = new ArrayList<>();
        fieldsOfStudies.add(new FieldOfStudy("Elektronika", "studia elektroniczne"));

        fieldsOfStudies.stream().forEach(fieldOfStudy -> fieldOfStudyRepository.save(fieldOfStudy));
    }

    private void generateSubjects() {
        FieldOfStudy fieldOfStudy = fieldOfStudyRepository.findOne(1);

        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Pomiary", "przedmiot który uczy mierzenia wartości elektrycznych", fieldOfStudy));
        
        subjects.stream().forEach(subject -> subjectRepository.save(subject));
    }

    private void generateLaboratories() {
        Subject subject = subjectRepository.findOne(1);

        List<Laboratory> laboratories = new ArrayList<>();
        laboratories.add(new Laboratory("Laboratorium1", "Pomiary napięcia",
                LocalDateTime.of(2017, Month.of(9), 12, 12, 0),
                LocalDateTime.of(2017, Month.of(9), 15, 12, 0),
                LocalDateTime.of(2017, Month.of(9), 16, 12, 0),
                subject));

        laboratories.add(new Laboratory("Laboratorium2", "Pomiary mocy",
                LocalDateTime.of(2017, Month.of(9), 13, 12, 0),
                LocalDateTime.of(2017, Month.of(9), 16, 12, 0),
                LocalDateTime.of(2017, Month.of(9), 17, 12, 0),
                subject));

        laboratories.stream().forEach(lab -> laboratoryRepository.save(lab));
    }

    private void generateReports() {
        Laboratory laboratory = laboratoryRepository.findOne(1);

        List<Report> reports = new ArrayList<>();
        reports.add(new Report("Opis1", "files/", "sprawozdanie1", ".pdf", "4.5", true, laboratory));
        reports.add(new Report("Opis2", "files/", "sprawozdanie2", ".pdf", "5.0", true, laboratory));
        reports.add(new Report("Opis3", "files/", "sprawozdanie3", ".pdf", "3.0", true, laboratory));
        reports.add(new Report("Opis4", "files/", "sprawozdanie4", ".pdf", "4.0", true, laboratory));
        reports.add(new Report("Opis5", "files/", "sprawozdanie5", ".pdf", "3.5", true, laboratory));
        reports.add(new Report("Opis6", "files/", "sprawozdanie6", ".pdf", "5.0", true, laboratory));
        reports.add(new Report("Opis7", "files/", "sprawozdanie7", ".pdf", "3.0", true, laboratory));
        reports.add(new Report("Opis8", "files/", "sprawozdanie8", ".pdf", "3.0", true, laboratory));
        reports.add(new Report("Opis9", "files/", "sprawozdanie9", ".pdf", "3.0", true, laboratory));
        reports.add(new Report("Opis10", "files/", "sprawozdanie10", ".pdf", "3.5", true, laboratory));
        reports.add(new Report("Opis11", "files/", "sprawozdanie11", ".pdf", "3.5", true, laboratory));
        reports.add(new Report("Opis12", "files/", "sprawozdanie12", ".pdf", "3.5", true, laboratory));

        reports.stream().forEach(report -> reportRepository.save(report));
    }

    private void addRolesToUsers() {
        AppUser appUser = appUserRepository.findOne(1);
        List<Role> roles = roleRepository.findAll();
        appUser.setRoles(roles);
        appUserRepository.save(appUser);
    }

    private void addReportsToUsers() {
        AppUser appUser = appUserRepository.findOne(1);
        List<Report> reports = reportRepository.findAll();
        appUser.setReports(reports);
        appUserRepository.save(appUser);
    }
}