package algorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;
import utils.ImageUtils;

/** Represents the unit of an RNA, that's the net for the IP algorithm. Aims to simulate
 * a 2D coordinate with direction and adjacent next node reference. It behaves as a
 * RNA communicating the current state and will moves depending of the resistance.
 * [info] DESCRIPTION OF STATES:
 * 	[0] FREE : node is free to advance through the image.
 * 	[1] READY : it means the node is located in a breakpoint or edge.
 * 	[-1] BLOCKED : node is blocked by other node, it can be released in next iterations.
 * 	[-2] WASTE : node is blocked by other node or image edge permanently. It's waste and it will be removed from the net.
 * @author Cristopher Alvear Candia.
 * @version 2.2
 */
public class Node extends Coordinate {
	
	/** Direction of the node. Value is between 0 and 359 degrees. */
	private double angle;
	/** Adjacent next node. */
	private Node next;
	/** State of the node. Should be FREE, READY, BLOCKED or WASTE only */
	private byte state;
	
	/** Constants. States of the node. */
	public static final byte FREE = 0;
	public static final byte READY = 1;
	public static final byte BLOCKED = -1;
	public static final byte WASTE = -2;
	
	/** Constants. Direction. */
	public static final byte CLOCKWISE = 1;
	public static final byte ANTICLOCKWISE = -1;
	
	/** Constructor.
	 * @param x : int - Horizontal component.
	 * @param y : int - Vertical component.
	 * @param angle : int - Direction in degrees.
	 */
	public Node( int x, int y, int angle ) {
		super( x, y );
		state = FREE;
		this.angle = angle;
	}
	
	// Getters.
	
	/** Returns the angle.
	 * @return int - Degree between 0 and 359.
	 */	
	public double getAngle() {
		return angle;
	}
	
	/** Returns the next node.
	 * @return Node - Next node.
	 */
	public Node getNext() {
		return next;
	}
	
	/** Returns the state of the node.
	 * @return byte - State static const. FREE, READY, BLOCKED or LOCKED.
	 */
	public byte getState() {
		return state;
	}
	
	// Setters.
	
	/** Sets the angle.
	 * @param angle : int - Degree between 0 and 359 inclusively.
	 */	
	public void setAngle( int angle ) {
		// Precondition: 0 <= angle < 360.
		this.angle = angle;
	}
	
	/** Rotate the node changing his direction.
	 * @param angle : int - Degree between 0 and 359 inclusively.
	 * @param direction : byte - Direction static const. CLOCKWISE or ANTICLOCKWISE.
	 */	
	public void rotate( int angle, byte direction ) {
		// Precondition: 0 <= angle < 360.
		this.angle = ImageUtils.standardAngle( this.angle + angle * direction );
	}
	
	/** Sets the next node.
	 * @param next : Node.
	 */
	public void setNext( Node next ) {
		this.next = next;
	}

	// Behavior methods.
	
	/** Changes the position (coordinate) by distance in pixels from origin guided by the direction.
	 * @param distance : int - Distance in pixels.
	 * @param image : BufferedImage - Image.
	 * @param ranges : int[][] - Array of limits from other nets.
	 * @param resistance : int - Resistance of movements of the net through image surface.
	 * @return byte - State of the node.
	 */
	public byte advance( int distance, BufferedImage image, int[][] ranges, int resistance ) {
		if( !ImageUtils.isAValidImageCoordinate( x, y, image.getWidth(), image.getHeight() ) )
			return WASTE;
		// Calculates grayscale value of futures (next adjacent) coordinates.
		int xf = x - ( int ) Math.round( ImageUtils.moveHorizontalComponent( angle, distance ) );
		int yf = y - ( int ) Math.round( ImageUtils.moveVerticalComponent( angle, distance ) );
		// If the node can't moves, it's blocked.
		if( !ImageUtils.isAValidImageCoordinate( xf, yf, image.getWidth(), image.getHeight() ) || ImageUtils.withinRanges( yf, ranges ) )
			return state == BLOCKED ? WASTE : BLOCKED;
		if( Math.abs( imminentPixelDifference( image, xf, yf ) ) > resistance )//)&& imminentPixelDifference( image, xf, yf ) > 0 )
			return READY;
		// Node will do a movement.
		x = xf;
		y = yf;
		return FREE;
	}
	
	/** Calculates the grayscale pixel difference between current and future position.
	 * @param image : BufferedImage - Image.
	 * @param xf : int - Horizontal future component.
	 * @param yf : int - Vertical future component.
	 * @return int - Scalegray value difference.
	 */
	public int imminentPixelDifference( BufferedImage image, int xf, int yf ) {
		// Difference between current pixel value and future pixel value by direction. 
		//return Math.abs( new Color( image.getRGB( x, y ) ).getBlue() - new Color( image.getRGB( xf, yf ) ).getBlue() );
		return new Color( image.getRGB( x, y ) ).getBlue() - new Color( image.getRGB( xf, yf ) ).getBlue();
	}
	
	// Main execution method.
	
	/** Advances if it isn't over a breakpoint, limit or border.
	 * @param image : BufferedImage - Image.
	 * @param ranges : int[][] - Array of limits from other nets.
	 * @param resistance : int - Movement resistance threshold.
	 * 	Values are between 0 and 255. Lower values means more breakpoint recognition capability.
	 */
	public void run( BufferedImage image, int[][] ranges, int resistance ) {
		// Analyzes and advances one step only, for the maximum effectiveness.
		state = advance( 1, image, ranges, resistance );
	}
	
}
