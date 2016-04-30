package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import algorithm.Coordinate;
import algorithm.Node;

/** Utility class to processing images.
 * @author Cristopher Alvear Candia.
 * @version 1.9.3
 */
public class ImageUtils {
	
	/** Private constructor. */
	private ImageUtils() {}
	
	/** Loads an image as BufferedImage.
	 * @param path : String - Path of the image to will be load.
	 * @return BufferedImage - Image loaded.
	 */
	public static BufferedImage loadImage( String path ) {
		try {
            return ImageIO.read( new File( path ) );
        } catch( IOException exception ) {
            Console.displayError( "Image loading failed!" );
        }
		return null;
	}
	
	/** Generates a scalegray histogram.
	 * @param image : BufferedImage.
	 * @return int[] - Histogram array.
	 */
	public static int[] histogram( BufferedImage image ) {
		int[] histogram = new int[ 255 ];
		for( int x = 0; x < image.getWidth(); x++ )
			for( int y = 0; y < image.getHeight(); y++ )
				histogram[ averagePixel( new Color( image.getRGB( x, y ) ) ) ]++;
		
		return histogram;
	}
	
	// Image processing methods.
	
	/** Turns a image into a grayscale one.
	 * @param image : BufferedImage - Image to will be turned into a grayscale one.
	 */
	public static void grayScale( BufferedImage image ) {
		for( int x = 0; x < image.getWidth(); x++ )
			for( int y = 0; y < image.getHeight(); y++ )
				image.setRGB( x, y, pixelToGrayScale( new Color( image.getRGB( x, y ) ) ).getRGB() );
	}
	
	/** Contrast an image truncating the pixels to linear channels.
	 * @param image : BufferedImage - Image to will be contrast.
	 * @param channels : int - Quantity of channels (divisions). More channels means less contrast.
	 * @param alignment : int - Alignment of the channels respect to middle. (+ close to BLACK, 0 centered, - close to WHITE).
	 * 	It means positive values sets more pixels BLACK, and negative values sets more pixels WHITE.
	 */
	public static void contrast( BufferedImage image, int channels, int alignment ) {
		// Precondition : image should be in grayscale.
		for( int x = 0; x < image.getWidth(); x++ )
			for( int y = 0; y < image.getHeight(); y++ )
				image.setRGB( x, y, pixelChannelFilter( new Color( image.getRGB( x, y ) ), channelsLimit( channels, alignment ) ).getRGB() );
	}
	
	/** Sets Laplacian filter at the input image.
	 * @param image : BufferedImage - Image to will be processed.
	 * @return BufferedImage - Image with Laplacian filter.
	 */
	public static BufferedImage laplace( BufferedImage image ) {
		// Clones the image.
		BufferedImage imageFiltered = cloneImage( image );
		for( int x = 0; x < image.getWidth(); x++ )
			for( int y = 0; y < image.getHeight(); y++ )
				imageFiltered.setRGB( x, y, new Color( laplaceRed( image, x, y ), laplaceGreen( image, x, y ), laplaceBlue( image, x, y ) ).getRGB() );
		return imageFiltered;
	}
	
	// Image processing utilities.
	
	/** Turns a pixel into a grayscale pixel calculating the average of the three color channels.
	 * @param pixel : Color - Pixel to will be turned into a grayscale one.
	 * @return Color - Grayscale pixel.
	 */
	private static int averagePixel( Color pixel ) {
		return ( pixel.getRed() + pixel.getGreen() + pixel.getBlue() ) / 3;
	}
	
	/** Turns a pixel into a grayscale pixel calculating the average of the three color channels.
	 * @param pixel : Color - Pixel to will be turned into a grayscale one.
	 * @return Color - Grayscale pixel.
	 */
	private static Color pixelToGrayScale( Color pixel ) {
		int media = averagePixel( pixel );
		return new Color( media, media, media );
	}
	
	/** Generates an array of the limits of the channels for the contrast algorithm.
	 * @param channels : int - Quantity of channels.
	 * @param alignment : int - Alignment of the channels respect to middle. (+ close to BLACK, 0 centered, - close to WHITE).
	 * 	It means positive values sets more pixels BLACK, and negative values sets more pixels WHITE.
	 * @return int[] - Array of the limits of the channels.
	 */
	private static int[] channelsLimit( int channels, int alignment ) {
		// Sets the array that will contain limits.
		int[] limits = new int[ channels + 1 ];
		// Sets lowest and highest limit.
		limits[ 0 ] = 0;
		limits[ channels ] = 255;
		// Calculates the width of the channels.
		int width = 255 / channels;
		// Normalizes the value.
		width = width > alignment ? width - alignment : width + ( width - alignment );
		// Generates the array.
		for( int i = 1; i < channels; i++ )
			limits[ i ] = limits[ i - 1 ] + width;
		return limits;
	}
	
	/** Filters the pixel for the contrast algorithm.
	 * @param pixel : Color - Pixel to will be contrast.
	 * @param limits : int[] - Array of the limits of the channels.
	 * @return Color - Contrast pixel.
	 */
	private static Color pixelChannelFilter( Color pixel, int[] limits ) {
		// Calculates the average pixel value (grayscale).
		int media = averagePixel( pixel );
		// Gets the highest index of the channel found.
		int channel = binaryRangeSearch( limits, media, 0, limits.length - 1 );
		// Filter the color of the pixel.
		if( channel == 1 )
			return Color.BLACK;
		if( channel == limits.length - 1 )
			return Color.WHITE;
		media = ( limits[ channel - 1 ] + limits[ channel ] ) / 2;
		return new Color( media, media, media );
	}
	
	/** Returns red pixel value with Laplacian operator applied.
	 * @param image : BufferedImage - Image.
	 * @param x : int - Horizontal component (row).
	 * @param y : int - Vertical component (column).
	 * @return int - Red pixel value.
	 */
	private static int laplaceRed( BufferedImage image, int x, int y ) {
		Coordinate[] adjacent = adjacentCoordinates( 1, new Coordinate( x, y ), image.getWidth(), image.getHeight() );
		int red = new Color( image.getRGB( x, y ) ).getRed() * 10;
		for( Coordinate coordinate : adjacent )
			red -= new Color( image.getRGB( coordinate.getX(), coordinate.getY() ) ).getRed();
		return Math.min( 255, Math.max( 0, red ) );
	}
	
	/** Returns green pixel value with Laplacian operator applied.
	 * @param image : BufferedImage - Image.
	 * @param x : int - Horizontal component (row).
	 * @param y : int - Vertical component (column).
	 * @return int - Green pixel value.
	 */
	private static int laplaceGreen( BufferedImage image, int x, int y ) {
		Coordinate[] adjacent = adjacentCoordinates( 1, new Coordinate( x, y ), image.getWidth(), image.getHeight() );
		int green = new Color( image.getRGB( x, y ) ).getGreen() * 10;
		for( Coordinate coordinate : adjacent )
			green -= new Color( image.getRGB( coordinate.getX(), coordinate.getY() ) ).getGreen();
		return Math.min( 255, Math.max( 0, green ) );
	}
	
	/** Returns blue pixel value with Laplacian operator applied.
	 * @param image : BufferedImage - Image.
	 * @param x : int - Horizontal component (row).
	 * @param y : int - Vertical component (column).
	 * @return int - Blue pixel value.
	 */
	private static int laplaceBlue( BufferedImage image, int x, int y ) {
		Coordinate[] adjacent = adjacentCoordinates( 1, new Coordinate( x, y ), image.getWidth(), image.getHeight() );
		int blue = new Color( image.getRGB( x, y ) ).getBlue() * 10;
		for( Coordinate coordinate : adjacent )
			blue -= new Color( image.getRGB( coordinate.getX(), coordinate.getY() ) ).getBlue();
		return Math.min( 255, Math.max( 0, blue ) );
	}
	
	// Surrounding coordinates.
	
	/** Returns an array with the mathematical values for build permutation of coordinates.
	 * This methods allows to calculate adjacent coordinates in a level (in pixels around the coordinate) given.
	 * @param level : int - Pixels around the coordinate, like deepness.
	 * @return int[] - Coordinates' permutation array.
	 */
	private static int[] permutationArray( int level ) {
		// Array consists of a symmetric reflected vector with 0 in the middle, as lowest limit the
		// level as negative value and highest limit the level as positive value.
		int[] array = new int[ level * 2 + 1 ];
		for( int i = 0, value = -( level ); i < array.length; i++, value++ )
			array[ i ] = value;
		return array;
	}
	
	/** Calculates the quantity of the coordinates around a coordinate by a level given.
	 * @param level : int - Pixels around the coordinate, like deepness.
	 * @param quantity : int - Base value. ALWAYS 1!!!. ('Cause is a recursive method).
	 * @return int - How many coordinates should be generated.
	 */
	private static int quantitySurroundingCoordinates( int level, int quantity ) {
		if( level == 0 )
			return quantity;
		// Each level, quantity increases in 8 multiplied by the current level.
		return quantitySurroundingCoordinates( level - 1, quantity + 8 * level );
	}
	
	/** Calculates the adjacent coordinates of one origin coordinate.
	 * @param level : int - Pixels around the coordinate, like deepness. -1 for skip drawing.
	 * @param originCoordinate : Coordinate - Base coordinate.
	 * @return Coordinate[] - Array of coordinates around the origin coordinate.
	 */	
	private static Coordinate[] adjacentCoordinates( int level, Coordinate originCoordinate, int width, int height ) {
		// Level < 0 means don't want to draw it.
		if( level < 0 )
			return new Coordinate[]{};
		ArrayList<Coordinate> coordinates = new ArrayList<>( quantitySurroundingCoordinates( level, 1 ) );
		int[] pArray = permutationArray( level );
		// Generates bounding coordinates.
		for( int x = 0; x < pArray.length; x++ )
			for( int y = 0 ; y < pArray.length; y++ )
				if( isAValidImageCoordinate( pArray[ x ] + originCoordinate.getX(), pArray[ y ] + originCoordinate.getY(), width, height ) )
					coordinates.add( new Coordinate( pArray[ x ] + originCoordinate.getX(), pArray[ y ] + originCoordinate.getY() ) );
		return coordinates.toArray( new Coordinate[ coordinates.size() ] );
	}
	
	/** Calculates the adjacent coordinates from the origin coordinate.
	 * @param level : int - Pixels around the coordinate, like deepness.
	 * @param originCoordinate : Coordinate - Base coordinate.
	 * @return Coordinate[] - Array of coordinates around the origin coordinate.
	 */	
	private static Coordinate[] aimingCoordinates( Coordinate c1, Coordinate c2, int width, int height ) {
		// Quantity of coordinates is determined by the distance between the coordinates plus one.
		// If steps value is low, quantity increases, 'cause more coordinates will generated.
		int size;
		ArrayList<Coordinate> coordinates = new ArrayList<>( size = ( int ) distanceBetweenCoordinates( c1, c2 ) + 1 );
		// Gets origin coordinate and the direction to the other one. (Could be upside down).
		double x = c1.getX(), y = c1.getY(), direction = angleBetweenCoordinates( c1, c2 );
		for( int k = 0; k < size; k++ ) {
			// Calculation of the new coordinates by direction.
			x -= moveHorizontalComponent( direction, 1 );
			y -= moveVerticalComponent( direction, 1 );
			if( isAValidImageCoordinate( ( int ) x, ( int ) y, width, height ) )
				coordinates.add( new Coordinate( ( int ) x, ( int ) y ) );
		}
		return coordinates.toArray( new Coordinate[ coordinates.size() ] );
	}
	
	/** Verifies if the coordinate is valid.
	 * @param x : int - Horizontal component.
	 * @param y : int - Vertical component.
	 * @param width : int - Width of the image.
	 * @param height : int - Height of the image.
	 * @return boolean - True if is valid (within the range), otherwise False.
	 */
	public static boolean isAValidImageCoordinate( int x, int y, int width, int height ) {
		return x >= 0 && x < width && y < height && y >= 0;
	}
	
	// Image Analysis.
	
	/** Calculates the minimum and maximum of the vertical pixel value difference.
	 * @param BufferedImage - Image to will be analyzed.
	 * @return int[] - Array of data. Contains { min, max }.
	 */
	public static int[] verticalDifferenceAnalysis( BufferedImage image ) {
		int difference, max = 0, min = 255;
		for( int y = 1; y < image.getHeight(); y++ )
			for( int x = 0; x < image.getWidth(); x++ ) {
				difference = Math.abs( new Color( image.getRGB( x, y - 1 ) ).getBlue() - new Color( image.getRGB( x, y ) ).getBlue() );
				max = max < difference ? difference : max;
				min = min > difference ? difference : min;
			}
		return new int[]{ min, max };
	}
	
	// Drawing utilities.
	
	/** Draws a net in the image.
	 * @param image : BufferedImage - Image.
	 * @param color : Color - Color of the drawing.
	 * @param header : Node - Header node of the net.
	 * @param nodeLevel : int - Thickness of the nodes drawing.
	 * @param lineLevel : int - Thickness of the lines drawing.
	 * @return Node - Next node to will be drawn.
	 */	
	public static Node drawNet( BufferedImage image, Color color, Node current, int nodeLevel, int lineLevel ) {
		// Base case : if current node is null, finalizes the method.
		if( current == null )
			return null;
		// Base case : if current node is the last, draws it and finalizes the method.
		if( current.getNext() == null ) {
			drawCoordinate( image, color, nodeLevel, current );
			return current;
		}
		// Draws the coordinate and draws the line between current and next node.
		drawCoordinate( image, color, nodeLevel, current );
		drawLine( image, color, lineLevel, current, current.getNext() );
		return drawNet( image, color, current.getNext(), nodeLevel, lineLevel );
	}
	
	/** Draws a coordinate in the image.
	 * @param image : BufferedImage - Image.
	 * @param color : Color - Color of the drawing.
	 * @param level : int - Thickness of the coordinate drawing.
	 * @param coordinate : Coordinate - Coordinate to will be drawn.
	 */
	public static void drawCoordinate( BufferedImage image, Color color, int level, Coordinate coordinate ) {
		draw( image, color, adjacentCoordinates( level, coordinate, image.getWidth(), image.getHeight() ) );
	}
	
	/** Draws a line between two coordinates in the image.
	 * @param image : BufferedImage - Image.
	 * @param color : Color - Color of the drawing.
	 * @param level : int - Thickness of the line drawing.
	 * @param c1 : Coordinate - First coordinate.
	 * @param c2 : Coordinate - Second coordinate.
	 */
	public static void drawLine( BufferedImage image, Color color, int level, Coordinate c1, Coordinate c2 ) {
		Coordinate[] coordinates = aimingCoordinates( c1, c2, image.getWidth(), image.getHeight() );
		for( Coordinate coordinate : coordinates )
			drawCoordinate( image, color, level, coordinate );
	}
	
	/** Draws the coordinates of the array in the image.
	 * @param image : BufferedImage - Image.
	 * @param color : Color - Color of the drawing.
	 * @param coordinates : Coordinate[] - Array of coordinates to will be drawn.
	 */
	private static void draw( BufferedImage image, Color color, Coordinate... coordinates ) {
		for( Coordinate coordinate : coordinates )
			image.setRGB( coordinate.getX(), coordinate.getY(), color.getRGB() );
	}
	
	// Image cloning method.
	
	/** Clones a BufferedImage.
	 * @param image : BufferedImage - Image to clone.
	 * @param image : BufferedImage - Cloned image.
	 */
	public static BufferedImage cloneImage( BufferedImage image ) {
		BufferedImage clone = new BufferedImage( image.getWidth(), image.getHeight(), image.getType() );
		clone.getGraphics().drawImage( image, 0, 0, null );
		return clone;
	}
	
	// Binary searching.
	
	/** Returns the higher limit index from array of a value, within a array of limits.
	 * @param array : int[] - Array of limits where to search.
	 * @param value : int - Value to search.
	 * @param lowest : int - Lowest index to search.
	 * @param highest : int - Highest index to search.
	 */
	private static int binaryRangeSearch( int[] array, int value, int lowest, int highest ) {
		// Base case, if the range is minimum, returns the higher current limit.
		if( highest - lowest == 1 )
			return highest;
		// Calculates middle index.
		int middle = ( lowest + highest ) / 2;
		// If value is higher than middle value, search range will be bounded to start from the middle.
		if( value > array[ middle ] )
			return binaryRangeSearch( array, value, middle, highest );
		if( value == array[ middle ] )
			return middle + 1;
		// If value is lower than middle value, search range will be bounded to ending in the middle.
		return binaryRangeSearch( array, value, lowest, middle );
	}
	
	// Ranges calculation.
	
	/** Verifies if the number is within some range in the array.
	 * @param value : int - Value.
	 * @param ranges : int[][] - Matrix of limits.
	 * @return boolean - True if value is in the range, False otherwise.
	 */
	public static boolean withinRanges( int value, int[][] ranges ) {
		for( int[] limits : ranges )
			if( limits != null && !outOfRange( value, limits ) )
				return true;
		return false;
	}
	
	/** Verifies if the number is in the range.
	 * @param value : int - Value.
	 * @param array : int[] - Array of limits { top, bottom } in the image.
	 * @return boolean - True if value is in the range, False otherwise.
	 */
	private static boolean outOfRange( int value, int[] limits ) {
		return value < limits[ 0 ] || value > limits[ 1 ];
	}
	
	// Mathematical calculations.
	
	/** Calculates the distance between two coordinates.
	 * @param c1 : Coordinate - First coordinate.
	 * @param c2 : Coordinate - Second coordinate.
	 * @return double - Distance between the two coordinates.
	 */
	private static double distanceBetweenCoordinates( Coordinate c1, Coordinate c2 ) {
		return Math.sqrt( Math.pow( c1.getX() - c2.getX(), 2 ) + Math.pow( c1.getY() - c2.getY(), 2 ) );
	}
	
	/** Calculates the angle (direction) from one coordinate to another.
	 * @param c1 : Coordinate - First coordinate.
	 * @param c2 : Coordinate - Second coordinate.
	 * @return double - Angle or direction from the first coordinate to the second [0, 360).
	 */
	private static double angleBetweenCoordinates( Coordinate c1, Coordinate c2 ) {
	    return standardAngle( Math.toDegrees( Math.atan2( c1.getY() - c2.getY(), c1.getX() - c2.getX() ) ) );
	}
	
	/** Normalizes the angle to a range from 0 to 359.
	 * @param angle : double - Angle to be will normalized.
	 * @return double - Angle normalized.
	 */
	public static double standardAngle( double angle ) {
		if( angle < 0 )
			return standardAngle( angle + 360 );
		if( angle > 359 )
			return standardAngle( angle - 360 );
	    return angle;
	}
	
	/** Moves the horizontal component (x) a distance by direction.
	 * @param angle : double - Angle or direction. Range from 0 to 359.
	 * @param distance : double - Units to move.
	 * @return double - x.
	 */
	public static double moveHorizontalComponent( double angle, double distance ) {
	    return Math.cos( Math.toRadians( angle ) ) * distance;
	}
	
	/** Moves the vertical component (y) a distance by direction.
	 * @param angle : double - Angle or direction. Range from 0 to 359.
	 * @param distance : double - Units to move.
	 * @return double - y.
	 */
	public static double moveVerticalComponent( double angle, double distance ) {
	    return Math.sin( Math.toRadians( angle ) ) * distance;
	}
	
}
