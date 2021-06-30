package exceptions;

public class StartNegativeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StartNegativeException() {
		super("insert error: start time must be nonnegative");
	}
}
