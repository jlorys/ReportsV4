package reports.reports.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(FieldOfStudy.class)
public abstract class FieldOfStudy_ {

    public static volatile SingularAttribute<FieldOfStudy, Integer> id;
    public static volatile SingularAttribute<FieldOfStudy, String> name;
    public static volatile SingularAttribute<FieldOfStudy, String> description;
    public static volatile SingularAttribute<FieldOfStudy, Long> createdDate;
    public static volatile SingularAttribute<FieldOfStudy, Long> lastModifiedDate;
    public static volatile SingularAttribute<FieldOfStudy, String> createdBy;
    public static volatile SingularAttribute<FieldOfStudy, String> lastModifiedBy;

    public static volatile ListAttribute<FieldOfStudy, Subject> subjects;
}
