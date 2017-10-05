package reports.reports.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Laboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime labDate;
    private LocalDateTime returnReportDate;
    private LocalDateTime finalReturnReportDate;
    @CreatedDate
    private Long createdDate;
    @LastModifiedDate
    private Long lastModifiedDate;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;

    @ManyToOne()
    @JoinColumn(name = "SUBJECT_ID")
    private Subject subject;

    @OneToMany(mappedBy = "laboratory")
    private List<Report> reports;

    public Laboratory(){}

    public Laboratory(String name, String description, LocalDateTime labDate, LocalDateTime returnReportDate, LocalDateTime finalReturnReportDate, Subject subject) {
        this.name = name;
        this.description = description;
        this.labDate = labDate;
        this.returnReportDate = returnReportDate;
        this.finalReturnReportDate = finalReturnReportDate;
        this.subject = subject;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLabDate() {
        if(Optional.ofNullable(labDate).isPresent())
         return labDate.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
        return null;
    }

    public void setLabDate(Long labDate) {
        if(Optional.ofNullable(labDate).isPresent()){
            this.labDate = Instant.ofEpochMilli(labDate).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }else {
            this.labDate = null;
        }
    }

    public Long getReturnReportDate() {
        if(Optional.ofNullable(returnReportDate).isPresent())
         return returnReportDate.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
        return null;
    }

    public void setReturnReportDate(Long returnReportDate) {
        if(Optional.ofNullable(returnReportDate).isPresent()){
            this.returnReportDate = Instant.ofEpochMilli(returnReportDate).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }else {
            this.returnReportDate = null;
        }
    }

    public Long getFinalReturnReportDate() {
        if(Optional.ofNullable(finalReturnReportDate).isPresent())
         return finalReturnReportDate.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
        return null;
    }

    public void setFinalReturnReportDate(Long finalReturnReportDate) {
        if(Optional.ofNullable(finalReturnReportDate).isPresent()){
            this.finalReturnReportDate = Instant.ofEpochMilli(finalReturnReportDate).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }else{
            this.finalReturnReportDate = null;
        }
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Long lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Report> getReports() {
        if(Optional.ofNullable(reports).isPresent()) return reports;
        return Collections.EMPTY_LIST;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public boolean addReport(Report report) {
        return getReports().add(report);
    }
}
