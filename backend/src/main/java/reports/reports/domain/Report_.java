package reports.reports.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Report.class)
public abstract class Report_ {

    public static volatile SingularAttribute<Report, Integer> id;
    public static volatile SingularAttribute<Report, String> description;
    public static volatile SingularAttribute<Report, String> filePath;
    public static volatile SingularAttribute<Report, String> fileName;
    public static volatile SingularAttribute<Report, String> fileExtension;
    public static volatile SingularAttribute<Report, String> grade;
    public static volatile SingularAttribute<Report, Long> createdDate;
    public static volatile SingularAttribute<Report, Long> lastModifiedDate;
    public static volatile SingularAttribute<Report, String> createdBy;
    public static volatile SingularAttribute<Report, String> lastModifiedBy;
    public static volatile SingularAttribute<Report, Boolean> isSendInTime;

    public static volatile ListAttribute<Report, AppUser> users;

    public static volatile SingularAttribute<Report, Laboratory> laboratory;
}
