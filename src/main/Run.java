package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import algorithm.DifferentialEvolution;
import utils.ImageUtils;

/** Executes the algorithm.
 * @author Cristopher Alvear Candia.
 */
public class Run {
	
	/* Main execution method. */
	public static void main( String[] args ) {
		// Display a image selector
		//String path = Screen.displayFileChooser( "bmp", "jpg", "jpeg", "png" );
		String path = "images/2.jpeg";
		
		// Loads the image for the digital processing.
		BufferedImage image = ImageUtils.loadImage( path );
		// Sets the random seed.
		//Randomizer.seed( 0 );
		
		// Differential Evolution execution.
		execute( image, 9999, 0.1, 0.3, 0.1, 0.4, 0.6, 2 );
		
		// Render the image to the screen.
		new ImageRenderer( image );
		//new ImageRenderer( ImageUtils.loadImage( path ) );
	}
	
	/** Executes the algorithm.
	 * @param image : BufferedImage - Image to will be processed.
	 * @param generations : int - How many generations will be generated.
	 * @param popdensity : double - Percentage of vertical length where nets will be inserted.
	 * @param ndensity : double - Percentage of horizontal length where nodes will be inserted.
	 * @param mutation : double - Percentage of generating new mutated children from population.
	 * @param selection : double - Percentage of best nets selected from population.
	 * @param sensitiveness : double - Percentage of sensitiveness of the movement of the net.
	 * @param dispallow : int - Maximum dispersion/variation of thickness allowed.
	 */
	private static void execute( BufferedImage image, int generations, double popdensity, double ndensity,
			double mutation, double selection, double sensitiveness, int dispallow ) {
		DifferentialEvolution de = new DifferentialEvolution( image, popdensity, ndensity, mutation, selection, sensitiveness, dispallow );
		// Runs the algorithm.
		de.run( generations );
		// Draws the last generation in the net.
		de.print( image, Color.WHITE, 0, -1 );
	}

}
