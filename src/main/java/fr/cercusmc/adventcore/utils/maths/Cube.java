package fr.cercusmc.adventcore.utils.maths;

import fr.cercusmc.adventcore.utils.Location;

/**
 * Represents a cube in 3D space.
 */
public class Cube {

    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;
    private final double zMin;
    private final double zMax;

    /**
     * Creates a new cube with the given boundaries.
     * @param xMin Xmin
     * @param xMax XMax
     * @param yMin YMin
     * @param yMax YMax
     * @param zMin ZMin
     * @param zMax ZMax
     */
    public Cube(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    /**
     * Creates a new cube with the given center and radius.
     * @param centerX CenterX
     * @param centerY CenterY
     * @param centerZ CenterZ
     * @param radius Radius
     */
    public Cube(double centerX, double centerY, double centerZ, double radius) {
        this(centerX - radius, centerX + radius, centerY - radius, centerY + radius, centerZ - radius, centerZ + radius);
    }

    /**
     * Create a new cube with the given location, radius, and height range.
     * @param location The location of the cube's center
     * @param radius The radius of the cube
     * @param minHeight The minimum height of the cube
     * @param maxHeight The maximum height of the cube
     */
    public Cube(Location location, int radius, int minHeight, int maxHeight) {
        this(location.x() - radius, location.x() + radius, minHeight, maxHeight, location.z() - radius, location.z() + radius);
    }

    /**
     * Checks if the given point is inside the cube.
     * @param x X-coords
     * @param y Y-coords
     * @param z Z-coords
     */
    public boolean contains(double x, double y, double z) {
        return x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax;
    }


    /**
     * Checks if the given location is inside this cube.
     * @param location The cube to check
     */
    public boolean contains(Location location) {
        return location.x() >= xMin && location.x() <= xMax && location.y() >= yMin && location.y() <= yMax && location.z() >= zMin && location.z() <= zMax;
    }
}
