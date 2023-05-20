package myPackage.exception;

@SuppressWarnings("serial")
public class StudentException extends RuntimeException {

	public StudentException(String message) {
		super("Errore: " + message);
	}
}