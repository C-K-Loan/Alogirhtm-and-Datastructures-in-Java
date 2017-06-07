/**
 * The class Point describes points in three-dimensional
 * Cartesian coordinates.
 *
 * @author MPGI2 Tutoren
 * @version 1.3
 */
public class Point {

	/**
	 * The x-coordinate of a point.
	 */
	private int x;

	/**
	 * The y-coordinate of a point.
	 */
	private int y;

	/**
	 * The z-coordinate of a point.
	 */
	private int z;
	
	/**
	 * Empty constructor, creates point at coordinates (0,0,0).
	 */
	public Point() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	/**
	 * Constructor that creates specified point.
	 * 
	 * @param x  the distance from origin in x direction
	 * @param y  the distance from origin in y direction
	 * @param z  the distance from origin in z direction
	 */
	public Point(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns the x-coordinate of the point.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Returns the y-coordinate of the point.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Returns the z-coordinate of the point.
	 */
	public int getZ() {
		return this.z;
	}
	
	/**
	 * Moves this point to a new position.
	 * 
	 * @param dx  the distance from current position in x direction
	 * @param dy  the distance from current position in y direction
	 * @param dz  the distance from current position in z direction
	 */
	public void shift(int dx, int dy, int dz) {
		this.x = this.x + dx;
		this.y = this.y + dy;
		this.z = this.z + dz;
	}
	
	@Override
	public String toString() {
		return "Point(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}


