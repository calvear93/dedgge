package utils;

/** Helps to measure how fast the programs runs.
 * @author Cristopher Alvear Candia.
 * @version 2.1
 */
public class Timer {

	/** Timer start time in nanoseconds. */
	private long startTime;
	/** Timer end time in nanoseconds. */
	private long endTime;

	/** Constructor. */
	public Timer() {}
	
	// Functional methods.
	
	/** Starts the timer. */
	public void start() {
		startTime = System.nanoTime();
	}
	
	// Getters.
	
	/** Ends the timer. */
	public void stop() {
		endTime = System.nanoTime();
	}
	
	/** Returns the time in nanoseconds */
	public long getTimeInNanos() {
		return endTime - startTime;
	}
	
	/** Returns the time in miliseconds */
	public double getTimeInMillis() {
		return ( double ) getTimeInNanos() / 1000000;
	}
	
	/** Returns the time in seconds */
	public double getTimeInSeconds() {
		return ( double ) getTimeInMillis() / 1000;
	}
	
	/** Returns the time in minutes */
	public double getTimeInMinutes() {
		return ( double ) getTimeInSeconds() / 60;
	}
	
}
