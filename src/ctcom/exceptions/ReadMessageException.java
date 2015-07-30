package ctcom.exceptions;

public class ReadMessageException extends Exception {
	/**
	 * Satisfy Serializable interface
	 */
	private static final long serialVersionUID = 1L;

	public ReadMessageException(String reason) {
		super(reason);
	}
}
