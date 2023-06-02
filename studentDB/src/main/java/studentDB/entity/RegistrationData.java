package studentDB.entity;

public class RegistrationData {
	
	private String nome;
	private String cognome;
	private String email;
	private String password;
	private String confirmPassword;
	private String corso;
	
	public RegistrationData(String nome, String cognome, String email, String password, String confirmPassword,
			String corso) {
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.corso = corso;
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
	public String getCorso() {
		return corso;
	}
	public void setCorso(String corso) {
		this.corso = corso;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
