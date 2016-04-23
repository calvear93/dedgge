package utils;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/** Utility class to show messages GUI and calculate screen position of frames.
 * @author Cristopher Alvear Candia.
 * @version 1.3
 */
public class Screen {
	
	/** Private constructor. */
	private Screen() {}
	
	// GUI dialogs.
	
	/** Shows a message in a dialog window.
	 * @param windows : JFrame - Parent window.
	 * @param title : String - Title of the dialog.
	 * @param message : String - Message to will be displayed.
	 * @param icon : String - Icon name from "resources" folder.
	 */
	public static void displayMessageDialogGUI( JFrame window, String title, String msg, String icon ) {
		JOptionPane.showMessageDialog( window, msg, title,
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon( "resources/" + icon + ".png" ) );
	}
	
	/** Shows a text input in a dialog window.
	 * @param windows : JFrame - Parent window.
	 * @param title : String - Title of the dialog.
	 * @param message : String - Message to will be displayed.
	 * @param icon : String - Icon name from "resources" folder.
	 * @param defaultInput : String - Default input in dialog window.
	 * @return String - Returns the input.
	 */
	public static String displayInputDialogGUI( JFrame window, String title, String msg, String icon, String defaultInput ) {
		return ( String ) JOptionPane.showInputDialog( window, msg, title,
				JOptionPane.OK_CANCEL_OPTION, new ImageIcon( "resources/" + icon + ".png" ), null, defaultInput );
	}
	
	/** Shows a combo box in a dialog window.
	 * @param windows : JFrame - Parent window.
	 * @param title : String - Title of the dialog.
	 * @param message : String - Message to will be displayed.
	 * @param icon : String - Icon name from "resources" folder.
	 * @param options : String[] - Array of options.
	 * @param defaultInput : String - Default option (element) in dialog window.
	 * @return String - Returns the option selected.
	 */
	public static String displayComboBoxDialogGUI( JFrame window, String title, String msg, String icon, String[] options, String defaultInput ) {
		return ( String ) JOptionPane.showInputDialog( window, msg, title,
				JOptionPane.OK_CANCEL_OPTION, new ImageIcon( "resources/" + icon + ".png" ), options, defaultInput );
	}
	
	/** Shows multiple options in a dialog window.
	 * @param windows : JFrame - Parent window.
	 * @param title : String - Title of the dialog.
	 * @param message : String - Message to will be displayed.
	 * @param icon : String - Icon name from "resources" folder.
	 * @param options : String[] - Array of options.
	 * @param defaultInput : String - Default option (element) in dialog window.
	 * @return String - Returns the option selected.
	 */
	public static int displayOptionDialogGUI( JFrame window, String title, String msg, String icon, String[] options, String defaultOption ) {
		return JOptionPane.showOptionDialog( window, msg, title,
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				new ImageIcon( "resources/" + icon + ".png" ), options, defaultOption );
	}
	
	/** Shows a warning message in a dialog window.
	 * @param windows : JFrame - Parent window.
	 * @param message : String - Message to will be displayed.
	 */
	public static void displayWarnGUI( JFrame window, String msg ) {
		displayMessageDialogGUI( window, "Warning!",
				HTMLFormatter( msg, 4, "Arial", "#DF7401" ), "warning" );
	}
	
	/** Shows a error message in a dialog window.
	 * @param windows : JFrame - Parent window.
	 * @param message : String - Message to will be displayed.
	 */
	public static void displayErrorGUI( JFrame window, String msg ) {
		displayMessageDialogGUI( window, "Error!",
				HTMLFormatter( msg, 4, "Arial", "#DF0101" ), "error" );
	}
	
	/** Shows a confirmation dialog window.
	 * @param windows : JFrame - Parent window.
	 * @param message : String - Message to will be displayed.
	 */
	static void displayConfirmationGUI( JFrame window, String msg ) {
		displayMessageDialogGUI( window, "Successful Operation",
				HTMLFormatter( msg, 4, "Arial", "#31B404" ), "check" );
	}
	
	/** Shows a file chooser window and returns the path of the selected.
	 * @param extension : String - Extension filter.
	 * @param extensions : String[] - Varargs for add extensions.
	 * @return String - Path of the file.
	 */
	public static String displayFileChooser( String extension, String... extensions ) {
		JFileChooser fileChooser = new JFileChooser();
		// Set the file filters.
		fileChooser.setFileFilter( new FileNameExtensionFilter( extension, extensions ) );
		// Open the file chooser dialog.
		fileChooser.showOpenDialog( null );
		return fileChooser.getSelectedFile().toPath().toString();
	}
	
	/** Returns a formatted string for display into a GUI.
	 * @param message : String - Message to will be displayed.
	 * @param size : int - Font size.
	 * @param font : String - Font style.
	 * @param color : String - Font color.
	 * @return String - The formatted string.
	 */
	public static String HTMLFormatter( String string, int size, String font, String color ) {
		return "<html><font face = \"" + font + "\" size = " + size + " color = \"" + color + "\">" + string + "</font></html>";
	}
	
	// Screen resolution calculations.
	
	/** Calculates the screen resolution.
	 * @return double[] - Array with width and height measurements. { width, height }.
	 */
	static double[] getScreenResolution() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return new double[] { screenSize.getWidth(), screenSize.getHeight() };
	}
	
	/** Calculates the pixels where the window is centered on the screen.
	 * @param width : int - Width of the screen.
	 * @param height : int - Height of the screen.
	 * @return int[] - Array with x and y coordinates. { x, y }.
	 */
	public static int[] calculateCenterScreenPosition( int width, int height ) {
		return new int[]{ ( int ) ( getScreenResolution()[ 0 ] - width ) / 2, ( int ) ( getScreenResolution()[ 1 ] - height ) / 2 };
	}
	
	/** Normalizes the windows resolution when it's over the maximum screen resolution.
	 * @param width : int - Width of the window.
	 * @param height : int - Height of the window.
	 * @return int[] - Array with width and height measurements of the window. { width, height }.
	 */
	public static int[] normalizeResolution( int width, int height ) {
		double[] screen = getScreenResolution();
		int[] resolution = new int[] {
				( int ) ( width > screen[ 0 ] ? screen[ 0 ] : width ),
				( int ) ( height > screen[ 1 ] ? screen[ 1 ] : height )	
		};
		if( resolution[ 0 ] < 136 )
			return normalizeResolution( 136, ( resolution[ 1 ] * 100 / resolution[ 0 ] ) * 136 );
		if( resolution[ 1 ] >= screen[ 1 ] )
			resolution[ 1 ] = ( int ) screen[ 1 ] - 92;
		return resolution;
	}
	
}
