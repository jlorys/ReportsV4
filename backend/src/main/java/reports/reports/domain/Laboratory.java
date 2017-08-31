package reports.reports.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    public LocalDateTime getLabDate() {
        return labDate;
    }

    public void setLabDate(LocalDateTime labDate) {
        this.labDate = labDate;
    }

    public LocalDateTime getReturnReportDate() {
        return returnReportDate;
    }

    public void setReturnReportDate(LocalDateTime returnReportDate) {
        this.returnReportDate = returnReportDate;
    }

    public LocalDateTime getFinalReturnReportDate() {
        return finalReturnReportDate;
    }

    public void setFinalReturnReportDate(LocalDateTime finalReturnReportDate) {
        this.finalReturnReportDate = finalReturnReportDate;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}
