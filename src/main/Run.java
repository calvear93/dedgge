package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import algorithm.DifferentialEvolution;
import utils.ImageUtils;
import utils.Randomizer;

/** Executes the algorithm.
 * @author Cristopher Alvear Candia.
 */
public class Run {

	public static void main( String[] args ) {
		String path = "images/2.jpeg";
		// Loads the image for the digital processing.
		BufferedImage image = ImageUtils.loadImage( path );
		
		Randomizer.seed( 0 );
		
		/**
		 * popdensity
		 * ndensity
		 * mutation
		 * selection
		 * sensitiveness
		 * dispallow
		 */
		// Differential Evolution algorithm.
		//DifferentialEvolution de = new DifferentialEvolution( image, 0.2, 0.4, 0.1, 0.2, 0.2, 2 );
		runDE( image );
		
		// Render the image to the screen.
		new ImageRenderer( image );
		//new ImageRenderer( ImageUtils.loadImage( path ) );
	}
	
	public static void runDE( BufferedImage image ) {
		DifferentialEvolution de = new DifferentialEvolution( image, 0.2, 0.4, 0.1, 0.3, 0.2, 3 );
		// 100 generations.
		de.run( 9999 );
		// Draws the last generation in the net.
		de.print( image, Color.WHITE, 0, -1 );
	}
	
	

}
