package de.tum.cit.ase.maze.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for parsing properties files that define the layout of game levels. These files
 * map specific coordinates to tile types or game elements, allowing for dynamic level creation
 * based on simple text files.
 */
public class PropertiesFileParser {

    //String filePath = "maps/level-1.properties";


    /**
     * Parses a properties file at the given file path, translating each line into a mapping
     * between a {@link Tuple} representing coordinates (x, y) and an {@code Integer} representing
     * the tile or element type at those coordinates.
     *
     * @param filePath The path to the properties file to be parsed.
     * @return A map of {@link Tuple} coordinates to {@code Integer} tile or element types.
     * @throws IOException If there is an error reading the file.
     */
    public static Map<Tuple, Integer> parsePropertiesFile(String filePath) throws IOException {
        Map<Tuple, Integer> resultMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    Tuple key = parseTuple(parts[0]);
                    int value = Integer.parseInt(parts[1]);
                    resultMap.put(key, value);
                }
            }
        }

        return resultMap;
    }

    /**
     * Parses a string representation of a tuple, expected to be in the format "x,y", into
     * a {@link Tuple} object.
     *
     * @param input The string representation of the tuple.
     * @return A {@link Tuple} object representing the coordinates, or {@code null} if the input
     *         format is invalid.
     */
    public static Tuple parseTuple(String input) {
        String[] coordinates = input.split(",");
        if (coordinates.length == 2) {
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            return new Tuple(x, y);
        }
        return null;
    }


}