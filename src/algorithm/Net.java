package algorithm;

import java.awt.image.BufferedImage;

/** Simulates a One Dimensional Movement Net, encapsulating edges by pixel differences.
 * @author Cristopher Alvear Candia.
 * @version 1.8
 */
public class Net implements Comparable<Net> {

	/** Header of the list. */
	private Node header;
	/** Movement resistance threshold. Value between 0 and 255. */
	private int resistance;
	/** Upper and lower vertical limits of the width of the net dispersion. */
	private int top, bottom;
	/** Maximum variation of thickness allowed. */
	private int dispallow;

	/** Constructor.
	 * @param row : int - Row where the net will be located.
	 * @param quantity : int - Quantity of nodes.
	 * @param hstep : int - Horizontal separation of nodes (pixels).
	 * @param direction : int - Direction of the net movement. Should be 90 or 270 degrees.
	 * @param resistance : int - Resistance of movements of the net through image surface.
	 * 	Values are between 0 and 255. Lower values means more edges recognition capability.
	 * @param dispallow : int - Maximum dispersion/variation of thickness allowed.
	 */
	public Net( int row, int quantity, int hstep, int direction, int resistance, int dispallow ) {
		// Precondition: direction e { 90, 270 }; sensitiveness = ( 0, 255 ).
		// Header of the nodes.
		header = new Node( 0, row, direction );
		this.resistance = resistance;
		this.dispallow = dispallow;
		// Generates the nodes left.
		generate( header, row, quantity, hstep, direction );
		// Calculates the limits (upper and lower limits) for identify the area of the net.
		calculateLimits();
	}

	// Nodes generation method.

	/** Recursive method. Generates the nodes of the list.
	 * @param previous : Node - Header node.
	 * @param quantity : int - Quantity of nodes.
	 * @param hstep : int - Horizontal separation of nodes (pixels).
	 * @param direction : int - Direction of the net movement. Should be 90 or 270 degrees.
	 * @return Node - Current node from current method level.
	 */
	private Node generate( Node previous, int row, int quantity, int hstep, int direction ) {
		// Base case: last node to be will generated.
		if( quantity < 2 ) {
			previous.setNext( new Node( previous.getX() + hstep, row, direction ) );
			return previous.getNext();
		}
		// Current node generation.
		Node current = new Node( previous.getX() + hstep, row, direction );
		previous.setNext( current );
		current.setNext( generate( current, row, quantity - 1, hstep, direction ) );
		return current;
	}

	// Getters.

	/** Returns the header of the net.
	 * @return Node - Header of the net.
	 */
	public Node getHeader() {
		return header;
	}

	/** Returns the upper and lower limit of the net.
	 * @return int[] - Limits of the net. Array { top, bottom }.
	 */
	public int[] getLimits() {
		return new int[] { top, bottom };
	}

	// Setters.

	/** Removes a node from the net.
	 * @param node : Node - Node to will be removed.
	 */
	public void remove( Node node ) {
		// Precondition : node <> null; node exists in net.
		if( node.equals( header ) )
			header = node.getNext();
		else
			for( Node n = header; n != null; n = n.getNext() )
				if( n.getNext() != null && n.getNext().equals( node ) )
					n.setNext( node.getNext() );
	}

	// Fitness and environment calculations.

	/** Returns the current fitness of the net. It considers the current state
	 * of nodes, the vertical length of the net and quantity of blocked nodes.
	 * @return int - Fitness.
	 */
	public int fitness() {
		if( header == null )
			return -255;
		int fitness = 0;
		for( Node node = header; node != null; node = node.getNext() )
			fitness += node.getState();
		return fitness + thicknessScore( Math.abs( bottom - top ) );
	}

	/** Returns an arbitrary score for the vertical length.
	 * @param thickness : int - Current vertical length.
	 * @return int - Score.
	 */
	private int thicknessScore( int thickness ) {
		// Precondition : thickness >= 0.
		if( thickness <= dispallow )
			return dispallow - thickness;
		return thickness * -2;
	}

	/** Calculates the limits (top and bottom limits) for identify the vertical length of the net. */
	private void calculateLimits() {
		if( header == null )
			return;
		top = header.getY();
		bottom = header.getY();
		for( Node node = header.getNext(); node != null; node = node.getNext() ) {
			top = top < node.getY() ? top : node.getY();
			bottom = bottom > node.getY() ? bottom : node.getY();
		}
	}

	// Comparable overridden method.

	@Override
	/** Overridden method for sort net collections.
	 * @param net : Net - Net to compare.
	 * @return int - 1 if this net is better than net to compare, -1 if it's worse, and 0 if are equals.
	 */
	public int compareTo( Net net ) {
		if( fitness() > net.fitness() )
			return 1;
		if( fitness() < net.fitness() )
			return -1;
		return 0;
	}

	// Main execution method.

	/** Run the behavior of the nodes.
	 * @param image : BufferedImage - Image.
	 * @param ranges : int[][] - Array of limits from other nets.
	 */
	public void run( BufferedImage image, int[][] ranges ) {
		for( Node node = header; node != null; node = node.getNext() ) {
			// If node is ready, continues with the next.
			if( node.getState() == Node.READY )
				continue;
			// Executes the logic of the node.
			node.run( image, ranges, resistance );
			// If the node is LOCKED, death will come.
			if( node.getState() == Node.WASTE )
				remove( node );
		}
		calculateLimits();
	}

}
