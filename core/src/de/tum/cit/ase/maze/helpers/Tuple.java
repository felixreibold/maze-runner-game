package de.tum.cit.ase.maze.helpers;

import java.util.Comparator;
import java.util.Objects;

/**
 * Represents a simple 2D coordinate (tuple) with integer values for x and y.
 * It includes methods for getting the x and y values, checking equality, and computing the hash code.
 * This class is useful for representing positions or dimensions in a 2D space.
 */
public class Tuple{

    int x;
    int y;

    /**
     * Constructs a Tuple with specified x and y coordinates.
     *
     * @param x The x-coordinate of the Tuple.
     * @param y The y-coordinate of the Tuple.
     */
    public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a string representation of the Tuple in the format "(x, y)".
     *
     * @return A string representation of the Tuple.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Checks if this Tuple is equal to another object. Equality is based on both the x and y coordinates.
     *
     * @param obj The object to compare with this Tuple.
     * @return {@code true} if the object is a Tuple with the same x and y coordinates; otherwise, {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tuple otherTuple = (Tuple) obj;
        return x == otherTuple.x && y == otherTuple.y;
    }

    /**
     * Computes the hash code for this Tuple based on its x and y coordinates.
     *
     * @return The hash code of this Tuple.
     */

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
