package br.repinel;

/**
 * Application Exception. 
 * 
 * @author Roque Pinel
 *
 */
public class MainException extends Exception {
	private static final long serialVersionUID = -2312138943929906420L;

	/**
	 * Creates an exception with a message.
	 * 
	 * @param message The message.
	 */
	public MainException(CharSequence message) {
		super(message.toString());
	}
}
