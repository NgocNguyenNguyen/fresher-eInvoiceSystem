package csc.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name = "parameter")
public class Parameter implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;

	@Basic(optional = false)
	@Column(name = "time_email")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeEmail;

	@Basic(optional = false)
	@Column(name = "email")
	private String email;
	
	@Column(name = "pwd_email")
	private String pwdEmail;
	
	public Parameter() {
	}
		
	public Parameter(Integer id, Date timeEmail, String email, String pwdEmail) {
		super();
		this.id = id;
		this.timeEmail = timeEmail;
		this.email = email;
		this.pwdEmail = pwdEmail;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getTimeEmail() {
		return timeEmail;
	}

	public void setTimeEmail(Date timeEmail) {
		this.timeEmail = timeEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwdEmail() {
		return pwdEmail;
	}

	public void setPwdEmail(String pwdEmail) {
		this.pwdEmail = pwdEmail;
	}
			
	
	
}
