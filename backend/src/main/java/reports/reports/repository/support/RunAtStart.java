package reports.reports.repository.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reports.reports.domain.*;
import reports.reports.repository.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class RunAtStart {

    private final AppUserRepository appUserRepository;
    private final FieldOfStudyRepository fieldOfStudyRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final ReportRepository reportRepository;
    private final RoleRepository roleRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RunAtStart(AppUserRepository appUserRepository,
                      FieldOfStudyRepository fieldOfStudyRepository,
                      LaboratoryRepository laboratoryRepository,
                      ReportRepository reportRepository,
                      RoleRepository roleRepository,
                      SubjectRepository subjectRepository,
                      PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.fieldOfStudyRepository = fieldOfStudyRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.reportRepository = reportRepository;
        this.roleRepository = roleRepository;
        this.subjectRepository = subjectRepository;
        this.passwordEncoder = passwordEncoder;
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
        appUsers.add(new AppUser("admin", passwordEncoder.encode("admin"), "Jakub", "Loryś", "kuba35@vp.pl"));
        appUsers.add(new AppUser("user1", passwordEncoder.encode("user1"), "Dylon", "Sanawabicz", "1@x.pl"));
        appUsers.add(new AppUser("user2", passwordEncoder.encode("user2"), "Arkadiusz", "Głowa", "2@x.pl"));
        appUsers.add(new AppUser("user3", passwordEncoder.encode("user3"), "Mateusz", "Morfeusz", "3@x.pl"));
        appUsers.add(new AppUser("user4", passwordEncoder.encode("user4"), "Rafał", "Śmigacz", "4@x.pl"));
        appUsers.add(new AppUser("user5", passwordEncoder.encode("user5"), "Alojzy", "Wichajster", "5@x.pl"));
        appUsers.add(new AppUser("user6", passwordEncoder.encode("user6"), "Bob", "Malajski", "6@x.pl"));
        appUsers.add(new AppUser("user7", passwordEncoder.encode("user7"), "James", "Łatana", "7@x.pl"));
        appUsers.add(new AppUser("user8", passwordEncoder.encode("user8"), "Czesław", "Korcipa", "8@x.pl"));
        appUsers.add(new AppUser("user9", passwordEncoder.encode("user9"), "Jan", "Łasica", "9@x.pl"));
        appUsers.add(new AppUser("user10", passwordEncoder.encode("user10"), "Dominik", "Jahaś", "10@x.pl"));
        appUsers.add(new AppUser("user11", passwordEncoder.encode("user11"), "Bogusław", "Łęcina", "11@x.pl"));
        appUsers.add(new AppUser("user12", passwordEncoder.encode("user12"), "Mariusz", "Tracz", "12@x.pl"));
        appUsers.add(new AppUser("user13", passwordEncoder.encode("user13"), "Sylwester", "Kasztan", "13@x.pl"));

        appUsers.stream().forEach(appUser -> {
            if (!Optional.ofNullable(appUserRepository.getByUserName(appUser.getUserName())).isPresent()) {
                appUserRepository.save(appUser);
            }
        });
    }

    private void generateRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("USER", "Użytkownik może dodawać sprawozdania i przeglądać kierunki studiów, przedmioty, laboratoria i swoje sprawozdania. " +
                "Może usuwać i modyfikować swoje sprawozdania pod warunkiem że nie mają one oceny"));
        roles.add(new Role("REVIEWER", "Recenzent może oceniać sprawozdania i tworzyć kierunki studiów, przedmioty, laboratoria"));
        roles.add(new Role("ADMIN", "Admin zarządza użytkownikami, nie może dodawać sprawozdania lecz może modyfikować wszystkie sprawozdania"));

        roles.stream().forEach(role -> {
            if (!Optional.ofNullable(roleRepository.getByRoleName(role.getRoleName())).isPresent()) {
                roleRepository.save(role);
            }
        });
    }

    private void generateFieldsOfStudies() {
        List<FieldOfStudy> fieldsOfStudies = new ArrayList<>();
        fieldsOfStudies.add(new FieldOfStudy("Elektronika", "studia elektroniczne"));
        fieldsOfStudies.add(new FieldOfStudy("Informatyka", "studia informatyczne"));
        fieldsOfStudies.add(new FieldOfStudy("Chemia", "studia chemiczne"));
        fieldsOfStudies.add(new FieldOfStudy("Biologia", "studia biologiczne"));
        fieldsOfStudies.add(new FieldOfStudy("Matematyka", "studia matematyczne"));

        fieldsOfStudies.stream().forEach(fieldOfStudy -> {
            if (!Optional.ofNullable(fieldOfStudyRepository.getByName(fieldOfStudy.getName())).isPresent()) {
                fieldOfStudyRepository.save(fieldOfStudy);
            }
        });
    }

    private void generateSubjects() {
        FieldOfStudy fieldOfStudy = fieldOfStudyRepository.findOne(1);

        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Pomiary", "przedmiot który uczy mierzenia wartości elektrycznych", fieldOfStudy));

        subjects.stream().forEach(subject -> {
            if (!Optional.ofNullable(subjectRepository.getByName(subject.getName())).isPresent()) {
                subjectRepository.save(subject);
            }
        });
    }

    private void generateLaboratories() {
        Subject subject = subjectRepository.findOne(1);

        List<Laboratory> laboratories = new ArrayList<>();
        laboratories.add(new Laboratory("Laboratorium1", "Pomiary napięcia",
                LocalDateTime.of(2017, Month.of(9), 12, 12, 30),
                LocalDateTime.of(2017, Month.of(9), 15, 12, 30),
                LocalDateTime.of(2017, Month.of(9), 16, 12, 30),
                subject));

        laboratories.add(new Laboratory("Laboratorium2", "Pomiary mocy",
                LocalDateTime.of(2017, Month.of(9), 13, 12, 15),
                LocalDateTime.of(2017, Month.of(9), 16, 12, 15),
                LocalDateTime.of(2017, Month.of(9), 17, 12, 15),
                subject));

        laboratories.stream().forEach(lab -> {
            if (!Optional.ofNullable(laboratoryRepository.getByName(lab.getName())).isPresent()) {
                laboratoryRepository.save(lab);
            }
        });
    }

    private void generateReports() {
        Laboratory laboratory = laboratoryRepository.findOne(1);

        List<Report> reports = new ArrayList<>();
        reports.add(new Report("Opis1", "files/", "sprawozdanie1", ".pdf", null, null, laboratory));
        reports.add(new Report("Opis2", "files/", "sprawozdanie2", ".pdf", null, null, laboratory));
        reports.add(new Report("Opis3", "files/", "sprawozdanie3", ".pdf", null, null, laboratory));
        reports.add(new Report("Opis4", "files/", "sprawozdanie4", ".pdf", "4.0", true, laboratory));
        reports.add(new Report("Opis5", "files/", "sprawozdanie5", ".pdf", "2.5", true, laboratory));
        reports.add(new Report("Opis6", "files/", "sprawozdanie6", ".pdf", "5.0", true, laboratory));
        reports.add(new Report("Opis7", "files/", "sprawozdanie7", ".pdf", "3.0", true, laboratory));
        reports.add(new Report("Opis8", "files/", "sprawozdanie8", ".pdf", "2.0", true, laboratory));
        reports.add(new Report("Opis9", "files/", "sprawozdanie9", ".pdf", "3.0", true, laboratory));
        reports.add(new Report("Opis10", "files/", "sprawozdanie10", ".pdf", "3.5", true, laboratory));
        reports.add(new Report("Opis11", "files/", "sprawozdanie11", ".pdf", "3.5", true, laboratory));
        reports.add(new Report("Opis12", "files/", "sprawozdanie12", ".pdf", "3.5", true, laboratory));

        reports.stream().forEach(report -> {
            if (!Optional.ofNullable(reportRepository.getByDescription(report.getDescription())).isPresent()) {
                reportRepository.save(report);
            }
        });
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