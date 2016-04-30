package utils;

import java.util.Random;

/** Utility class to get random structures.
 * @author Cristopher Alvear Candia.
 * @version 2.4
 */
public class Randomizer {
	
	/** Random generator object. */
	private static Random randomizer = new Random();
	
	/** Private constructor. */
	private Randomizer() {}
	
	/** Sets the seed of the pseudo-random sequence. 
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
	public static int getInt( int lower, int upper ) {
		return ( int ) randomizer.nextDouble() * ( upper - lower + 1 ) + lower;
	}
	
	/** Returns a normally distributed random value. 
	 * @param mean : int - Mean of the normal distribution.
	 * @param sdeviation : int - Standard deviation of the normal distribution.
	 * @return int - Normally distributed random value.
	 */
	public static double getGaussian( float mean, float sdeviation ) {
		return randomizer.nextGaussian() * sdeviation + mean;
	}
	
	/** Returns a normally distributed random integer value. 
	 * Approximately 70% of values will be between (mean - sdeviation, mean + sdeviation).
	 * @param mean : int - Mean of the normal distribution.
	 * @param sdeviation : int - Standard deviation of the normal distribution.
	 * @return int - Normally distributed random value.
	 */
	public static int getGaussianInt( float mean, float sdeviation ) {
		return ( int ) Math.round( getGaussian( mean, sdeviation ) );
	}
	
	/** Returns a random value from an array. 
	 * @param values : int[] - Array of values.
	 * @return int - Random value from values array.
	 */
	public static int getFrom( int... values ) {
		return values[ getInt( 0, values.length - 1 ) ];
	}
	
	/** Returns a random value excluding the ranges. 
	 * @param lower : int - Left/Lower inclusive limit.
	 * @param upper : int - Right/Upper inclusive limit.
	 * @param ranges : int[][] - Matrix of limits.
	 * @return int - Random value.
	 */
	public static int getIntExceptTheRanges( int lower, int upper, int[][] ranges ) {
		int chosen = getInt( lower, upper );
		return ImageUtils.withinRanges( chosen, ranges ) ? getIntExceptTheRanges( lower, upper, ranges ) : chosen;
	}
	
	/** Returns a normally distributed random value excluding the ranges. 
	 * @param mean : int - Mean of the normal distribution.
	 * @param sdeviation : int - Standard deviation of the normal distribution.
	 * @param lower : int - Left/Lower inclusive limit.
	 * @param upper : int - Right/Upper inclusive limit.
	 * @param ranges : int[][] - Matrix of limits.
	 * @return int - Random Gaussian value.
	 */
	public static int getGaussianExceptTheRanges( float mean, float sdeviation, int lower, int upper, int[][] ranges ) {
		int chosen = getGaussianInt( mean, sdeviation );
		return ImageUtils.withinRanges( chosen, ranges ) || chosen < lower || chosen > upper ? getGaussianExceptTheRanges( mean, sdeviation, lower, upper, ranges ) : chosen;
	}
	
	/** Returns a random standard angle. 
	 * @return int - Random angle [0,360).
	 */
	public static int getAngle() {
		return getInt( 0, 359 );
	}
	
	/** Returns a random vertical standard angle (direction). 
	 * @return int - Random angle. Maybe 90 (up) or 270 (down) degrees.
	 */
	public static int getVerticalDirection() {
		return getFrom( 90, 207 );
	}
	
	/** Returns a random horizontal standard angle (direction). 
	 * @return int - Maybe 0 (left) or 180 (right) degrees.
	 */
	public static int getHorizontalDirection() {
		return getFrom( 0, 180 );
	}

}
