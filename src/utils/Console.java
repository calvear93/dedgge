package utils;

/** Utility class to show messages by console.
 * @author Cristopher Alvear Candia.
 * @version 1.1
 */
public class Console {
	
	/** Private constructor. */
	private Console() {}
	
	/** Shows a information message by console.
	 * @param message : String - Message to will be displayed.
	 */
	public static void displayInfo( String msg ) {
		System.out.println( " [i] " + msg );
	}
	
	/** Shows a warning message by console.
	 * @param message : String - Message to will be displayed.
	 */
	public static void displayWarning( String msg ) {
		System.out.println( " [!] " + msg );
	}
	
	/** Shows a qustion message by console.
	 * @param message : String - Message to will be displayed.
	 */
	public static void displayQuestion( String msg ) {
		System.out.println( " [?] " + msg );
	}
	
	/** Shows an exception message by console.
	 * @param message : String - Message to will be displayed.
	 */
	public static void displayException( String msg ) {
		System.err.println( " [EXCEPTION] " + msg );
	}
	
	/** Shows a error message by console.
	 * @param message : String - Message to will be displayed.
	 */
	public static void displayError( String msg ) {
		System.err.println( " [ERROR] " + msg );
	}
	
}
