/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;

/**
 * 
 */
public class BookNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BookNotFoundException(String customMessage) {
		super(customMessage);
	}
	
}
