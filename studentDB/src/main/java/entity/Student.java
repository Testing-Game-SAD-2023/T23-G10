package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Student {
	
	private @Id @GeneratedValue Long id;
	private String nome;
	private String cognome;
	private Corso corso;
	private String email;
	private String password;
	


	public Student() {};
	
	public Student(String nome, String cognome, Corso corso, String email, String password) {
		this.nome = nome;
		this.cognome = cognome;
		this.corso = corso;
		this.email = email;
		this.password = password;
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
	
	@Override
	public String toString() {
		return "\n"+nome+" "+cognome+"\n"+corso+"\n"+email+"\n"+password;
	}
}
