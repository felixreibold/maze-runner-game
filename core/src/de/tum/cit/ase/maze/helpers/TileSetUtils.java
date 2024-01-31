package de.tum.cit.ase.maze.helpers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

/**
 * Utility class for creating a {@link TiledMapTileSet} from a 2D array of {@link TextureRegion} objects.
 * This class allows for the dynamic generation of tilesets for use in a tiled game map.
 */
public class TileSetUtils {

    private static final String BLOCKED_PROPERTY = "blocked";

    /**
     * Creates and returns a {@link TiledMapTileSet} from the provided 2D array of {@link TextureRegion} tiles.
     * Each tile can be assigned properties, such as a "blocked" property to indicate impassable terrain.
     *
     * @param tiles A 2D array of {@link TextureRegion} objects representing the tiles.
     * @return A {@link TiledMapTileSet} composed of the provided tiles.
     */
    public static TiledMapTileSet createTileSet(TextureRegion[][] tiles) {
        TiledMapTileSet tileSet = new TiledMapTileSet();

        int tileId = 0; // Start with an initial tile ID

        // Iterate through your tiles array and create tiles
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                TextureRegion textureRegion = tiles[y][x];

                // Create a new tile
                StaticTiledMapTile tile = new StaticTiledMapTile(textureRegion);

                // Set properties for specific tiles (e.g., blocked property)
                if (shouldSetBlockedProperty(x, y)) {
                    tile.getProperties().put(BLOCKED_PROPERTY, true);
                }

                // Add the tile to the tile set
                tileSet.putTile(tileId++, tile);
            }
        }

        return tileSet;
    }

    /**
     * Determines whether the "blocked" property should be set for a tile at the given coordinates.
     * This is a placeholder for whatever logic is needed to identify which tiles should be impassable.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @return {@code true} if the tile at the given coordinates should be marked as blocked, otherwise {@code false}.
     */
    private static boolean shouldSetBlockedProperty(int x, int y) {
        // Example condition: set "blocked" property for specific tiles
        return (x == 0 && y == 0) || (x == 1 && y == 0) || (x == 2 && y == 0) || (x == 3 && y == 0) || (x == 0 && y == 12);
    }

}