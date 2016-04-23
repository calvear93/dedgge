package utils;

import java.util.Random;

/** Utility class to get random structures.
 * @author Cristopher Alvear Candia.
 * @version 2.2
 */
public class Randomizer {
	
	/** Random generator. */
	private static Random randomizer = new Random();
	
	/** Private constructor. */
	private Randomizer() {}
	
	/** Sets the sequence of the pseudo-random generation. 
	 * @param seed : int - Seed of the pseudo-random sequence.
	 */
	public static void seed( int seed ) {
		randomizer = new Random( seed );
	}
	
	/** Returns a random integer between two given limits. 
	 * @param lower : int - Left/Lower inclusive limit.
	 * @param upper : int - Right/Upper inclusive limit.
	 * @return int - Random integer [a,b].
	 */
	public static int randomInt( int lower, int upper ) {
		return ( int ) ( Math.random() * ( upper - lower + 1 ) + lower );
	}
	
	/** Returns a random value from an array. 
	 * @param values : int[] - Array of values.
	 * @return int - Random value from values array.
	 */
	public static int randomFrom( int... values ) {
		return values[ randomInt( 0, values.length - 1 ) ];
	}
	
	/** Returns a random value from an array excluding the ranges. 
	 * @param lower : int - Left/Lower inclusive limit.
	 * @param upper : int - Right/Upper inclusive limit.
	 * @param ranges : int[][] - Matrix of limits.
	 * @return int - Random value from values array.
	 */
	public static int randomFromExcept( int lower, int upper, int[][] ranges ) {
		int chosen = randomInt( lower, upper );
		return ImageUtils.withinRanges( chosen, ranges ) ? randomFromExcept( lower, upper, ranges ) : chosen;
	}
	
	/** Returns a random standard angle. 
	 * @return int - Random angle [0,360).
	 */
	public static int randomAngle() {
		return randomInt( 0, 359 );
	}

}
