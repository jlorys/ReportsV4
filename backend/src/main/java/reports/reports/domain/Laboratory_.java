package reports.reports.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

@StaticMetamodel(Laboratory.class)
public abstract class Laboratory_ {

    public static volatile SingularAttribute<Laboratory, Integer> id;
    public static volatile SingularAttribute<Laboratory, String> name;
    public static volatile SingularAttribute<Laboratory, String> description;
    public static volatile SingularAttribute<Laboratory, Long> labDate;
    public static volatile SingularAttribute<Laboratory, Long> returnReportDate;
    public static volatile SingularAttribute<Laboratory, Long> finalReturnReportDate;
    public static volatile SingularAttribute<Laboratory, Long> createdDate;
    public static volatile SingularAttribute<Laboratory, Long> lastModifiedDate;
    public static volatile SingularAttribute<Laboratory, String> createdBy;
    public static volatile SingularAttribute<Laboratory, String> lastModifiedBy;

    public static volatile SingularAttribute<Laboratory, Subject> subject;

    public static volatile ListAttribute<Laboratory, Report> reports;
}
