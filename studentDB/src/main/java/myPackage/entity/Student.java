package myPackage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Student {
	
	private @Id @GeneratedValue Long id;
	private String nome;
	private String cognome;
	private @Enumerated(EnumType.STRING) Corso corso;
	private String email;
	private String password;
	
	private String role;
	
	private boolean enabled;
	@Column(name = "verification_code", length = 64)
    private String verificationCode;
    
    @Column(name = "reset_password_token", length = 64)
    private String resetPasswordToken;



	public Student() {};
	
	public Student(String nome, String cognome, Corso corso, String email, String password,String role) {
		this.nome = nome;
		this.cognome = cognome;
		this.corso = corso;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	public Student(Student initStudent) {
		this.nome = initStudent.getNome();
		this.cognome = initStudent.getCognome();
		this.corso = initStudent.getCorso();
		this.email = initStudent.getEmail();
		this.password = initStudent.getPassword();
		this.role = initStudent.getRole();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Corso getCorso() {
		return corso;
	}
	public void setCorso(Corso corso) {
		this.corso = corso;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}
}
