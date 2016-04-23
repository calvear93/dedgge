package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/** Draws a BufferedImage into a JLabel for display it.
 * @author Cristopher Alvear Candia.
 * @version 1.0
 */
class Picture extends JLabel {
	
	/** Serialization ID. */
	private static final long serialVersionUID = 1L;
	/** Image. */
	private ImageIcon image;
	
	/** Constructor.
	 * @param raster : BufferedImage - Image loaded in memory.
	 */
	Picture( BufferedImage raster ) {
		image = new ImageIcon( raster );
	}
	
	/** Inherited (and overridden) method that draws the image into the JLabel. */
	protected void paintComponent( Graphics graphic ) {
		graphic.drawImage( image.getImage(), 0, 0, getWidth(), getHeight(), this );
	}
}
