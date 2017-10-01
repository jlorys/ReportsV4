package reports.reports.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(AppUser.class)
public abstract class AppUser_ {

    public static volatile SingularAttribute<AppUser, Integer> id;
    public static volatile SingularAttribute<AppUser, String> userName;
    public static volatile SingularAttribute<AppUser, String> password;
    public static volatile SingularAttribute<AppUser, String> firstName;
    public static volatile SingularAttribute<AppUser, String> lastName;
    public static volatile SingularAttribute<AppUser, String> email;
    public static volatile SingularAttribute<AppUser, Long> createdDate;
    public static volatile SingularAttribute<AppUser, Long> lastModifiedDate;
    public static volatile SingularAttribute<AppUser, String> createdBy;
    public static volatile SingularAttribute<AppUser, String> lastModifiedBy;
    public static volatile SingularAttribute<AppUser, Boolean> enabled;

    public static volatile ListAttribute<AppUser, Role> roles;

    public static volatile ListAttribute<AppUser, Report> report;
}