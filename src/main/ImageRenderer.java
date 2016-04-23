package main;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import utils.Screen;

/** GUI Render class that displays an image in a window.
 * @author Cristopher Alvear Candia.
 * @version 1.2
 */
class ImageRenderer extends JFrame {
	
	/** Serialization ID. */
	private static final long serialVersionUID = 1L;
	
	/** Constructor.
	 * @param image : BufferedImage - Image loaded in memory.
	 */
	ImageRenderer( BufferedImage image ) {
		// Resolution and position coordinates of the window calculation.
		int[] resolution = Screen.normalizeResolution( image.getWidth(), image.getHeight() );
		int[] windowPos = Screen.calculateCenterScreenPosition( resolution[0], resolution[1] ); // On down could be w +17 h +38
		// Sets the window properties.
		setTitle( "DIP ED Algorithm - Image Render" );
		setLocation( windowPos[0], windowPos[1] );
		setSize( resolution[0], resolution[1] );
		setIconImage( new ImageIcon( "resources/icon.png" ).getImage() );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        // Draws the image over a JLabel.
		Picture picture = new Picture( image );
		add( picture );
		// Shows the window.
        setVisible( true );
	}

}
