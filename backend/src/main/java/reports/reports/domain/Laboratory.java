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

    public Laboratory(String name, String description, LocalDateTime labDate, LocalDateTime returnReportDate, LocalDateTime finalReturnReportDate) {
        this.name = name;
        this.description = description;
        this.labDate = labDate;
        this.returnReportDate = returnReportDate;
        this.finalReturnReportDate = finalReturnReportDate;
    }
}
