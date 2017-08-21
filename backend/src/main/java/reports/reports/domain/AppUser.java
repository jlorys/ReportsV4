package reports.reports.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class AppUser {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(unique = true)
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	@CreatedDate
	private Long createdDate;
	@LastModifiedDate
	private Long lastModifiedDate;
	@CreatedBy
	private String createdBy;
    @LastModifiedBy
	private String lastModifiedBy;

	// Many to many
	@ManyToMany
	@JoinTable(name = "USER_ROLE",
			joinColumns = @JoinColumn(name = "USER_ID") ,
			inverseJoinColumns = @JoinColumn(name = "ROLE_ID") )
	private List<Role> roles;

	// Many to many
	@ManyToMany
	@JoinTable(name = "USER_REPORT",
			joinColumns = @JoinColumn(name = "USER_ID") ,
			inverseJoinColumns = @JoinColumn(name = "REPORT_ID") )
	private List<Report> reports;

	public AppUser() {}

	public AppUser(String userName, String password, String firstName, String lastName, String email) {
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Report> getReports() {
		if(Optional.ofNullable(reports).isPresent()) return reports;
		return Collections.EMPTY_LIST;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	/**
	 * Returns the granted authorities for this user. You may override
	 * this method to provide your own custom authorities.
	 */
	@Transient
	public List<String> getRoleNames() {
		List<String> roleNames = new ArrayList<>();

		for (Role role : getRoles()) {
			roleNames.add(role.getRoleName());
		}
		return roleNames;
	}

	/**
	 * Returns the {@link #roles} list.
	 */
	public List<Role> getRoles() {
		if(Optional.ofNullable(roles).isPresent()) return roles;
			return Collections.EMPTY_LIST;
	}

	/**
	 * Helper method to add the passed {@link Role} to the {@link #roles} list.
	 */
	public boolean addRole(Role role) {
		return getRoles().add(role);
	}

	/**
	 * Helper method to add the passed {@link Role} to the {@link #roles} list.
	 */
	public boolean addReport(Report report) {
		return getReports().add(report);
	}
}
