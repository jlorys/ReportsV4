package reports.reports.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reports.reports.domain.Report;
import reports.reports.dto.ReportDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.repository.AppUserRepository;
import reports.reports.repository.ReportRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {

    //Save the uploaded file to this folder
    private static final String UPLOAD_FOLDER = "uploaded_files//";

    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    private ReportRepository reportRepository;
    private AppUserRepository appUserRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, AppUserRepository appUserRepository) {
        this.reportRepository = reportRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional(readOnly = true)
    public ReportDTO findOne(Integer id) {
        Report report = reportRepository.findOne(id);
        return toDTO(report);
    }

    @Transactional(readOnly = true)
    public List<Long> findAllGrades() {
        List<Report> reports = reportRepository.findAll();
        List<String> grades = new ArrayList<>();
        reports.forEach(report -> grades.add(report.getGrade()));
        List<String> gradesFilteredNull = grades.stream().filter(s -> Optional.ofNullable(s).isPresent()).collect(Collectors.toList());

        List<Long> gradesAmount = new ArrayList<>();

        gradesAmount.add(gradesFilteredNull.stream().filter(s -> s.matches("2.0")).count());
        gradesAmount.add(gradesFilteredNull.stream().filter(s -> s.matches("2.5")).count());
        gradesAmount.add(gradesFilteredNull.stream().filter(s -> s.matches("3.0")).count());
        gradesAmount.add(gradesFilteredNull.stream().filter(s -> s.matches("3.5")).count());
        gradesAmount.add(gradesFilteredNull.stream().filter(s -> s.matches("4.0")).count());
        gradesAmount.add(gradesFilteredNull.stream().filter(s -> s.matches("4.5")).count());
        gradesAmount.add(gradesFilteredNull.stream().filter(s -> s.matches("5.0")).count());

        return gradesAmount;
    }

    @Transactional(readOnly = true)
    public PageResponse<ReportDTO> findAll(PageRequestByExample<ReportDTO> req) {
        Example<Report> example = null;
        Report report = toEntity(req.example);

        if (report != null) {
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("description", match -> match.ignoreCase().contains())
                    .withMatcher("filePath", match -> match.ignoreCase().contains())
                    .withMatcher("fileName", match -> match.ignoreCase().contains())
                    .withMatcher("fileExtension", match -> match.ignoreCase().contains())
                    .withMatcher("grade", match -> match.ignoreCase().contains())
                    .withMatcher("createdDate", match -> match.ignoreCase().contains())
                    .withMatcher("lastModifiedDate", match -> match.ignoreCase().contains())
                    .withMatcher("createdBy", match -> match.ignoreCase().contains())
                    .withMatcher("lastModifiedBy", match -> match.ignoreCase().contains())
                    .withMatcher("isSendInTime", match -> match.ignoreCase().contains());

            example = Example.of(report, matcher);
        }

        Page<Report> page;
        if (example != null) {
            page = reportRepository.findAll(example, req.toPageable());
        } else {
            page = reportRepository.findAll(req.toPageable());
        }


        List<ReportDTO> content = page.getContent().stream().map(report1 -> toDTO(report1)).collect(Collectors.toList());
        return new PageResponse<>(page.getTotalPages(), page.getTotalElements(), content);
    }

    public void delete(Integer id) {
        reportRepository.delete(id);
    }

    public void deleteAll() {
        reportRepository.deleteAll();
    }

    @Transactional
    public ReportDTO save(ReportDTO dto) {
        if (dto == null) {
            return null;
        }

        final Report report;

        if (dto.isIdSet()) {
            Report reportTmp = reportRepository.findOne(dto.id);
            if (reportTmp != null) {
                report = reportTmp;
            } else {
                report = new Report();
                report.setId(dto.id);
            }
        } else {
            report = new Report();
        }

        report.setDescription(dto.description);
        report.setFilePath(dto.filePath);
        report.setFileName(dto.fileName);
        report.setFileExtension(dto.fileExtension);
        report.setGrade(dto.grade);
        report.setCreatedDate(dto.createdDate);
        report.setLastModifiedDate(dto.lastModifiedDate);
        report.setCreatedBy(dto.createdBy);
        report.setLastModifiedBy(dto.lastModifiedBy);
        report.setSendInTime(dto.isSendInTime);

        report.getUsers().clear();
        if (dto.users != null) {
            dto.users.forEach(user -> report.addUser(appUserRepository.findOne(user.id)));
        }
        report.setLaboratory(LaboratoryService.toEntity(dto.laboratory));

        return toDTO(reportRepository.save(report));
    }

    private static Report toEntity(ReportDTO dto) {
        if (dto == null) {
            return null;
        }

        Report report = new Report();

        report.setId(dto.id);
        report.setDescription(dto.description);
        report.setFilePath(dto.filePath);
        report.setFileName(dto.fileName);
        report.setFileExtension(dto.fileExtension);
        report.setGrade(dto.grade);
        report.setCreatedDate(dto.createdDate);
        report.setLastModifiedDate(dto.lastModifiedDate);
        report.setCreatedBy(dto.createdBy);
        report.setLastModifiedBy(dto.lastModifiedBy);

        return report;
    }

    public static ReportDTO toDTO(Report report) {
        return toDTO(report, 0);
    }

    public static ReportDTO toDTO(Report report, int depth) {
        if (report == null) {
            return null;
        }

        ReportDTO dto = new ReportDTO();

        dto.id = report.getId();
        dto.description = report.getDescription();
        dto.filePath = report.getFilePath();
        dto.fileName = report.getFileName();
        dto.fileExtension = report.getFileExtension();
        dto.grade = report.getGrade();
        dto.createdDate = report.getCreatedDate();
        dto.lastModifiedDate = report.getLastModifiedDate();
        dto.createdBy = report.getCreatedBy();
        dto.lastModifiedBy = report.getLastModifiedBy();
        dto.isSendInTime = report.isSendInTime();
        if (depth < 1) {
            dto.users = report.getUsers().stream().map(user -> AppUserService.toDTO(user, 1)).collect(Collectors.toList());
        }
        dto.laboratory = LaboratoryService.toDTO(report.getLaboratory(), 1);

        return dto;
    }

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) return "File is empty";

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "uploadAccepted";
    }

    public void downloadFile(Integer reportId, HttpServletResponse response) throws IOException {
        boolean success = false;
        try {

            Report report = reportRepository.getOne(reportId);
            Path path = Paths.get("uploaded_files//" + report.getId() + "_r_" + report.getFileName() + report.getFileExtension());
            byte[] data = Files.readAllBytes(path);
            success = true;

        } catch (NoSuchFileException e) {
            log.error("NoSuchFileException");
        }
        if (success) {

            Report report = reportRepository.getOne(reportId);
            File someFile = new File(report.getFileName());
            Path path = Paths.get("uploaded_files//" + report.getId() + "_r_" + report.getFileName() + report.getFileExtension());
            byte[] data = Files.readAllBytes(path);

            FileOutputStream fos = new FileOutputStream(someFile);
            fos.write(data);
            fos.flush();
            fos.close();

            FileInputStream inputStream = new FileInputStream(someFile);

            String fileName = URLEncoder.encode(someFile.getName(), "UTF-8");
            fileName = URLDecoder.decode(fileName, "ISO8859_1");
            response.setContentType("application/x-msdownload");

            String headerValue = String.format("attachment; filename=\"%s\"", fileName + report.getFileExtension());
            response.setHeader("Content-Disposition", headerValue);

            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            // reads parts in loop, when next we giving to output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outStream.close();
            someFile.delete();
        }
    }
}
