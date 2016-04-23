package algorithm;

/** Represents a 2D coordinate.
 * @author Cristopher Alvear Candia.
 * @version 1.0
 */
public class Coordinate {
	
	/** Horizontal (x) and vertical (y) components. */
	protected int x, y;
	
	/** Constructor.
	 * @param x : int - Horizontal component.
	 * @param y : int - Vertical component.
	 */
	public Coordinate( int x, int y ) {
		this.x = x;
		this.y = y;
	}
	
	// Getters.
	
	/** Returns the x value.
	 * @return int - Horizontal component.
	 */
	public int getX() {
		return x;
	}
	/** Returns the y value.
	 * @return int - Vertical component.
	 */
	public int getY() {
		return y;
	}
	
	// Setters.
	
	/** Sets the x value.
	 * @param x : int - Horizontal component.
	 */
	public void setX( int x ) {
		this.x = x;
	}
	
	/** Sets the y value.
	 * @param y : int - Vertical component.
	 */
	public void setY( int y ) {
		this.x = y;
	}
	
}
