package reports.reports.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reports.reports.dto.ReportDTO;
import reports.reports.dto.support.PageRequestByExample;
import reports.reports.dto.support.PageResponse;
import reports.reports.service.ReportService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/reports")
public class ReportRestController {

    //Save the uploaded file to this folder
    private static String UPLOAD_FOLDER = "uploaded_files//";
    private final Logger log = LoggerFactory.getLogger(AppUserRestController.class);
    @Autowired
    private ReportService reportService;

    /**
     * Find a Page of Reports using query by example.
     */
    @PostMapping(value = "/page", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<ReportDTO>> findAll(@RequestBody PageRequestByExample<ReportDTO> prbe) throws URISyntaxException {
        PageResponse<ReportDTO> pageResponse = reportService.findAll(prbe);
        return new ResponseEntity<>(pageResponse, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Find by id Report.
     */
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> findById(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Find by id Report : {}", id);

        return Optional.ofNullable(reportService.findOne(id)).map(reportDTO -> new ResponseEntity<>(reportDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Delete by id Report.
     */
    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws URISyntaxException {

        log.debug("Delete by id Report : {}", id);

        try {
            reportService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception x) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Update Report.
     */
    @PutMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> update(@RequestBody ReportDTO reportDTO) throws URISyntaxException {

        log.debug("Update ReportDTO : {}", reportDTO);

        if (!reportDTO.isIdSet()) {
            return create(reportDTO);
        }

        ReportDTO result = reportService.save(reportDTO);

        return ResponseEntity.ok().body(result);
    }

    /**
     * Create a new Report.
     */
    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDTO> create(@RequestBody ReportDTO reportDTO) throws URISyntaxException {

        log.debug("Create ReportDTO : {}", reportDTO);

        if (reportDTO.isIdSet()) {
            return ResponseEntity.badRequest().header("Failure", "Cannot create Report with existing ID").body(null);
        }

        ReportDTO result = reportService.save(reportDTO);

        return ResponseEntity.created(new URI("/api/users/" + result.id)).body(result);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String addReport(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return "File is empty";
        }

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
}