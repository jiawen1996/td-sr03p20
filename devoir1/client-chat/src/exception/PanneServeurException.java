package exception;

public class PanneServeurException extends Exception {
	private String message;

	public PanneServeurException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
