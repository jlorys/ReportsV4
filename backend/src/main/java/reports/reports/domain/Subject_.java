package reports.reports.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Subject.class)
public abstract class Subject_ {

    public static volatile SingularAttribute<Subject, Integer> id;
    public static volatile SingularAttribute<Subject, String> name;
    public static volatile SingularAttribute<Subject, String> description;
    public static volatile SingularAttribute<Subject, Long> createdDate;
    public static volatile SingularAttribute<Subject, Long> lastModifiedDate;
    public static volatile SingularAttribute<Subject, String> createdBy;
    public static volatile SingularAttribute<Subject, String> lastModifiedBy;

    public static volatile ListAttribute<Subject, Laboratory> laboratories;

    public static volatile SingularAttribute<Subject, FieldOfStudy> fieldOfStudy;
}
