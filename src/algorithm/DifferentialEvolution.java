package algorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import utils.ImageUtils;
import utils.Randomizer;

/** Generates and evolves multiple nodes as RNA into nets. 
 * @author Cristopher Alvear Candia.
 * @version 1.4
 */
public class DifferentialEvolution {
	
	/** Image loaded in memory. */
	private BufferedImage image;
	/** List of nets. The population. */
	public ArrayList<Net> nets;
	/** Current net positions. */
	public int[][] unavailableRanges;
	
	// Differential Evolution variables.
	
	/** Population density. Percentage of vertical length where nets will be inserted. (0,1). */
	private double popdensity;
	/** Nodes density. Percentage of horizontal length where nodes will be inserted. (0,1) */
	private double ndensity;
	/** Mutation probability. Percentage of generating new mutated children from population. [0,1] */
	private double mutation;
	/** Selection percentage. Percentage of best nets selected from population. [0,1] */
	private double selection;
	
	// Net variables.
	
	/** Quantity of nets. How many nets will be inserted depending of vertical length. */
	private int qnet;
	/** Quantity of nodes. How many nodes will be inserted depending of horizontal length. */
	private int qnode;
	/** Horizontal Step. How much distance there is between nodes in a net. */
	private int hstep;
	/** Maximum dispersion/variation of thickness allowed. */
	private int dispallow;
	
	/** Heuristic. Minimum and maximum vertical pixel value difference of the image. { min, max }*/
	private int[] pixeldiff;
	
	/** Constructor. Double parameters values are between 0 and 1.
	 * @param image : BufferedImage - Image to will be processed.
	 * @param popdensity : double - Percentage of vertical length where nets will be inserted.
	 * @param ndensity : double - Percentage of horizontal length where nodes will be inserted.
	 * @param mutation : double - Percentage of generating new mutated children from population.
	 * @param selection : double - Percentage of best nets selected from population.
	 * @param sensitiveness : double - Percentage of sensitiveness of the movement of the net.
	 * @param dispallow : int - Maximum dispersion/variation of thickness allowed.
	 */
	public DifferentialEvolution( BufferedImage image, double popdensity, double ndensity,
			double mutation, double selection, double sensitiveness, int dispallow ) {
		// Assigns variable values.
		this.image = image;
		this.popdensity = popdensity;
		this.ndensity = ndensity;
		this.mutation = mutation;
		this.selection = selection;
		this.dispallow = dispallow;
		// Initializes list of nets.
		nets = new ArrayList<>();
		// Calculates minimum and maximum pixel value differences of the image.
		pixeldiff = ImageUtils.verticalDifferenceAnalysis( image );
		// Calculate new maximum minimum depending of sensitiveness.
		pixeldiff[ 1 ]++;
		pixeldiff[ 0 ] += ( pixeldiff[ 1 ] - pixeldiff[ 0 ] ) * ( 1 - sensitiveness );
	}
	
	/** Calculates net features.
	 * @param popdensity : double - Percentage of vertical length where nets will be inserted.
	 * @param ndensity : double - Percentage of generating new children from population.
	 */
	private void calculateNetFeatures( double popdensity, double ndensity ) {
		qnet = ( int ) Math.ceil( image.getHeight() * popdensity ) - nets.size();
		qnode = ( int ) Math.ceil( image.getWidth() * ndensity );
		hstep = ( int ) ( 1 / ndensity ) + 1;
	}
	
	/** Calculates net current positions and occupied ranges. */
	private void calculateUnavailableRanges() {
		// Initializes new ranges array.
		unavailableRanges = new int[ nets.size() ][];
		int i = 0;
		// Saves all net limits into ranges array.
		for( Net net : nets )
			unavailableRanges[ i++ ] = net.getLimits();
	}
	
	/** Generates a new net and adds it to nets list.
	 * @param resistance : int - Resistance of movements of the net through image surface.
	 */
	private void generateNet( int resistance ) {
		float middle = image.getHeight() / 2;
		nets.add( new Net(
					Randomizer.getGaussianExceptTheRanges( middle, middle, 0, image.getHeight(), unavailableRanges ), // Row.
					qnode, // Quantity of nodes.
					hstep, // Horizontal step between nodes.
					Randomizer.getVerticalDirection(), // Direction, maybe 90 (up) or 270 (down).
					resistance, // Resistance.
					dispallow // Dispersion of thickness allowed.
				) );
	}
	
	/** Generates the nets for the current generation.
	 * @param quantity : int - Quantity of nodes.
	 */
	private void generate( int quantity, int resistance ) {
		while( quantity-- > 0 ) {
			calculateUnavailableRanges();
			generateNet( resistance );
		}
	}
	
	/** Generates nets for the current generation.
	 * @param quantity : int - Quantity of nodes.
	 */
	private void generate( int quantity ) {
		generate( quantity, Randomizer.getInt( pixeldiff[ 0 ], pixeldiff[ 1 ] ) );
	}
	
	/** Generates the mutated (randomized) nets for the current generation.
	 * @param quantity : int - Quantity of nodes.
	 */
	private void generateMutations( int quantity ) {
		generate( quantity, Randomizer.getInt( 1, 255 ) );
	}
	
	/** Draws the nets in the image.
	 * @param image : BufferedImage - Image.
	 * @param color : Color - Color of the drawing.
	 * @param nodeLevel : int - Thickness of the nodes drawing.
	 * @param lineLevel : int - Thickness of the lines drawing.
	 */	
	public void print( BufferedImage image, Color color, int nodeLevel, int lineLevel ) {
		for( Net net : nets )
			ImageUtils.drawNet( image, color, net.getHeader(), nodeLevel, lineLevel  );
	}
	
	/** Fits the population selecting the best nets. */
	private void fitPopulation() {
		// List of selected nets.
		ArrayList<Net> bestNets = new ArrayList<>();
		// Sorts (descending) the nets by fitness. (Best fitness are at the end of the list).
		Collections.sort( nets );
		
		// Index for to access to the nets.
		int i;
		// Advances until to find first positive fitness value in the nets.
		for( i = 0; i < nets.size() && nets.get( i ).fitness() < 0; i++ );
		
		// If index is out of bounds, finalizes.
		if( i >= nets.size() )
			return;
		// Calculates the minimum and maximum fitness of the nets.
		int bestmin = nets.get( i ).fitness(), bestmax = nets.get( nets.size() - 1 ).fitness();
		// Calculates the lower fitness depending of selection percentage.
		int lowerfitness =  bestmax - ( int ) ( ( bestmax - bestmin ) * selection );
		
		// Selects the best nets depending of selection percentage.
		for( ; i < nets.size(); i++ )
			if( nets.get( i ).fitness() >= lowerfitness )
				bestNets.add( nets.get( i ) );
		
		// Sets the new list with the best nets.
		nets = bestNets;
	}
	
	/** Draws the nets in the image.
	 * @param generations : int - How many generations will be generated.
	 */
	public void run( int generations ) {
		// Iterations for each generation.
		int lifetime;
		while( generations-- > 0 ) {//|| nets.size() < 20 ) {
			// Calculate net specific features.
			calculateNetFeatures( popdensity , ndensity );
			lifetime = ( int ) ( image.getHeight() * ( 1 - popdensity ) );
			// Generates the new nets generation.
			generate( ( int ) ( qnet * ( 1 - mutation ) ) );
			generateMutations( ( int ) ( qnet * mutation ) );
			// Executes the behaviour of the nets.
			while( --lifetime > 0 )
				for( Net net : nets )
					net.run( image, unavailableRanges );
			// Selects the best nets to survive.
			fitPopulation();
		}
	}
}
