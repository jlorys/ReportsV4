package reports.reports.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Report {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String description;
	private String filePath;
	private String fileName;
	private String fileExtension;
	private String grade;
	@CreatedDate
	private Long createdDate;
	@LastModifiedDate
	private Long lastModifiedDate;
	@CreatedBy
	private String createdBy;
	@LastModifiedBy
	private String lastModifiedBy;
	private Boolean isSendInTime;

	@ManyToMany
	@JoinTable(name = "USER_REPORT",
			joinColumns = @JoinColumn(name = "REPORT_ID") ,
			inverseJoinColumns = @JoinColumn(name = "USER_ID") )
	List<AppUser> users;

	@ManyToOne()
	@JoinColumn(name = "LABORATORY_ID")
	private Laboratory laboratory;

	public Report() {}

    public Report(String description, String filePath, String fileName, String fileExtension, String grade, Boolean isSendInTime, Laboratory laboratory) {
        this.description = description;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.grade = grade;
        this.isSendInTime = isSendInTime;
        this.laboratory = laboratory;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public List<AppUser> getUsers() {

		if(Optional.ofNullable(users).isPresent()) return users;
		return new ArrayList<>(Collections.EMPTY_LIST);

	}

	public void setUsers(List<AppUser> users) {
		this.users = users;
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

	public Boolean isSendInTime() {
		return isSendInTime;
	}

	public void setSendInTime(Boolean sendInTime) {
		isSendInTime = sendInTime;
	}

	public boolean addUser(AppUser appUser) {

		this.users = getUsers();
		this.users.add(appUser);
		return true;

	}

	public Laboratory getLaboratory() {return laboratory;}

	public void setLaboratory(Laboratory laboratory) {this.laboratory = laboratory;}

}
