package br.com.hubfintech.authorization.entities;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@MappedSuperclass
public abstract class BaseEntity {
	
	@Version
	private Long version;

	@Column(insertable = true, updatable = false, nullable = false)
	private Timestamp createDate;

	@Column(insertable = false, updatable = true, nullable = true)
	private Timestamp alterDate;

	@PrePersist
	public void onCreate() {
		this.setCreateDate(new Timestamp((new Date()).getTime()));
	}

	@PreUpdate
	public void onPersist() {
		this.setAlterDate(new Timestamp((new Date()).getTime()));
	}
}