package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import de.tum.cit.ase.maze.helpers.MapUtils;
import de.tum.cit.ase.maze.helpers.PropertiesFileParser;
import de.tum.cit.ase.maze.helpers.TileSetUtils;
import de.tum.cit.ase.maze.helpers.Tuple;

import java.io.IOException;
import java.util.*;


public class GameMap2 {
    private TiledMap tiledMap;
    private TiledMapTileLayer tileLayer;

    private SpriteBatch spriteBatch;
    private int mapWidth;
    private int mapHeight;
    private Texture basicTileSheet;
    private TextureRegion[][] tiles;

    private TiledMapTileSet basicTilesSet;

    private Texture objectsTileSheet;
    private TextureRegion[][] objects;

    private Texture mobsTileSheet;
    private TextureRegion[][] mobs;

    private Texture thingsTileSheet;
    private TextureRegion[][] things;

    private Tuple spawn;
    private List<Tuple> trapSpawns = new ArrayList<>();
    private List<Tuple> enemySpawns = new ArrayList<>();

    private Map<Integer, TiledMapTile> mapTextures = new HashMap<>();

    private Map<Tuple, Integer> gameMapMap;

    public GameMap2(String filePath) {
        this.tiledMap = new TiledMap();

        this.basicTileSheet = new Texture(Gdx.files.internal("assets/basictiles.png"));
        this.tiles = TextureRegion.split(basicTileSheet, 16, 16);
        this.basicTilesSet = TileSetUtils.createTileSet(tiles);

        this.objectsTileSheet = new Texture(Gdx.files.internal("assets/objects.png"));
        this.objects = TextureRegion.split(objectsTileSheet, 16, 16);

        this.mobsTileSheet = new Texture(Gdx.files.internal("assets/mobs.png"));
        this.mobs = TextureRegion.split(mobsTileSheet, 16, 16);

        this.thingsTileSheet = new Texture(Gdx.files.internal("assets/things.png"));
        this.things = TextureRegion.split(thingsTileSheet, 16, 16);

        // Populate the map with the texture regions
        mapTextures.put(0, basicTilesSet.getTile(0)); // Wall (horizontal)
        mapTextures.put(1, basicTilesSet.getTile(17)); // Entry point
        mapTextures.put(2, basicTilesSet.getTile(48)); // Exit
        mapTextures.put(3, new StaticTiledMapTile(things[4][6])); // Trap (static obstacle)
        mapTextures.put(4, new StaticTiledMapTile(mobs[4][6])); // Enemy (dynamic obstacle)
        mapTextures.put(5, new StaticTiledMapTile(things[0][6])); // Key
        mapTextures.put(6, basicTilesSet.getTile(1)); // Wall (vertical)
        mapTextures.put(7, basicTilesSet.getTile(2)); // Wall (L-section)
        mapTextures.put(8, basicTilesSet.getTile(3)); // Wall (T-section)
        mapTextures.put(9, basicTilesSet.getTile(14)); // Floor
        mapTextures.put(10, basicTilesSet.getTile(65)); // Outside


        this.spriteBatch = new SpriteBatch();

        try {
            this.gameMapMap = PropertiesFileParser.parsePropertiesFile(filePath);
        } catch (Exception e) {
            Gdx.app.log("GameMap", "Map File Not Found: " + e.getMessage());
        }

        mapWidth = 0;
        mapHeight = 0;

        for (Tuple coordinates : gameMapMap.keySet()) {
            // Calculate map dimensions based on the largest x and y coordinates
            if(coordinates.getX() > mapWidth){
                mapWidth = coordinates.getX();
            }
            if(coordinates.getY() > mapHeight){
                mapHeight = coordinates.getY();
            }
            if(gameMapMap.get(coordinates) == 1){
                spawn = coordinates;
            }
            if(gameMapMap.get(coordinates) == 3){
                trapSpawns.add(coordinates);
            }
            if(gameMapMap.get(coordinates) == 4){
                enemySpawns.add(coordinates);
            }
        }

        mapWidth += 1;
        mapHeight+= 1;

        // Create a TiledMap with a TiledMapTileLayer
        this.tileLayer = new TiledMapTileLayer(mapWidth, mapHeight, 16, 16); // Assuming tiles are 16x16

        // Set tiles in a loop
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {

                TextureRegion currentTile;
                Tuple currentCoordinateTuple = new Tuple(x, y);
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                //cell.setTile(new StaticTiledMapTile(tiles[0][0]));

                // For the better display of walls we need to know what the surrounding tiles are
                Tuple northTile = null;
                Tuple eastTile = null;
                Tuple southTile = null;
                Tuple westTile = null;

                boolean northWall = false;
                boolean eastWall = false;
                boolean southWall = false;
                boolean westWall = false;

                int wall_connections = 0;

                if(x>0 && y>0 && x<mapWidth-1 && y<mapHeight-1){
                    northTile = new Tuple(x, y+1);
                    eastTile = new Tuple(+1, y);
                    southTile = new Tuple(x, y-1);
                    westTile = new Tuple(x-1, y);
                } else if(x==0 && y==0){
                    northTile = new Tuple(x, y+1);
                    eastTile = new Tuple(+1, y);
                } else if(x==0 && y==mapHeight-1){
                    eastTile = new Tuple(+1, y);
                    southTile = new Tuple(x, y-1);
                } else if(x==mapWidth-1 && y==mapHeight-1){
                    southTile = new Tuple(x, y-1);
                    westTile = new Tuple(x-1, y);
                } else if(x==mapWidth-1 && y==0){
                    northTile = new Tuple(x, y+1);
                    westTile = new Tuple(x-1, y);
                } else if(x==0){
                    northTile = new Tuple(x, y+1);
                    eastTile = new Tuple(+1, y);
                    southTile = new Tuple(x, y-1);
                } else if(y==mapHeight-1){
                    eastTile = new Tuple(+1, y);
                    southTile = new Tuple(x, y-1);
                    westTile = new Tuple(x-1, y);
                } else if(x==mapWidth-1){
                    northTile = new Tuple(x, y+1);
                    southTile = new Tuple(x, y-1);
                    westTile = new Tuple(x-1, y);
                } else if(y==0){
                    northTile = new Tuple(x, y+1);
                    eastTile = new Tuple(+1, y);
                    westTile = new Tuple(x-1, y);
                }

                if(northTile != null && gameMapMap.containsKey(northTile)) {
                    northWall = gameMapMap.get(northTile) == 0;
                }
                if(eastTile != null && gameMapMap.containsKey(eastTile)) {
                    eastWall = gameMapMap.get(eastTile) == 0;
                }
                if(southTile != null && gameMapMap.containsKey(southTile)) {
                    southWall = gameMapMap.get(southTile) == 0;
                }
                if(westTile != null && gameMapMap.containsKey(westTile)) {
                    westWall = gameMapMap.get(westTile) == 0;
                }

                wall_connections += northWall ? 1 : 0;
                wall_connections += eastWall ? 1 : 0;
                wall_connections += southWall ? 1 : 0;
                wall_connections += westWall ? 1 : 0;


                if (!gameMapMap.containsKey(currentCoordinateTuple) || gameMapMap.get(currentCoordinateTuple) == 3 || gameMapMap.get(currentCoordinateTuple) == 4) {
                    cell.setTile(basicTilesSet.getTile(13)); // Floor
                } else {
                    cell.setTile(mapTextures.get(gameMapMap.get(currentCoordinateTuple))); // Set the specified tile image

                    // by default all walls are set to be horizontal wall segments
                    // next we will make the walls more beautiful by selecting the appropriate tile (vertical-segment, T-segment, L-segment)
                    if (gameMapMap.get(currentCoordinateTuple) == 0) {

                        // For all wall tiles assign a new "blocked" property and set it to true.
                        // This will be used in the Player Class to check for collisions
                        //cell.getTile().getProperties().put("blocked", true);

                        if (!southWall) {
                            if(northWall){
                                if(eastWall){
                                    cell.setTile(mapTextures.get(7)); // Set wall as L-segment
                                } else {
                                    // TODO: Flip this tile texture, since it represents an L-segment of a wall we need |_ and _|
                                    cell.setTile(mapTextures.get(7));
                                }
                            }
                        } else {
                            cell.setTile(mapTextures.get(6)); // Set wall as a T-segment
                            if (northWall) {
                                cell.setTile(mapTextures.get(6)); // Set wall as vertical-segment
                            } else {
                                cell.setTile(mapTextures.get(8)); // Set wall as a T-segment
                            }
                        }
                        if(x==0 && y==mapHeight-1){
                            cell.setTile(mapTextures.get(8)); // Set wall as a T-segment
                        } else if(x==mapWidth-1 && y==mapHeight-1){
                            cell.setTile(mapTextures.get(8)); // Set wall as a T-segment
                        }

                    }

                }



                tileLayer.setCell(x, y, cell);
            }
        }

        // Add the tile layer to the map
        MapLayers layers = tiledMap.getLayers();
        layers.add(tileLayer);

        int layerIndex = MapUtils.getLayerIndex(tiledMap, tileLayer);

        Gdx.app.log("GameMap", "tileLayer index : " + layerIndex);

        //MapUtils.processTiles(tileLayer);


    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public Tuple getSpawn() {
        return spawn;
    }

    public List<Tuple> getTrapSpawns() {
        return trapSpawns;
    }

    public List<Tuple> getEnemySpawns() {
        return enemySpawns;
    }

    public TiledMapTileLayer getTileLayer() {
        return tileLayer;
    }

    public void loadMapFromProperties(MazeRunnerGame game, String propertiesFilePath) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("your_layer_name");

        Properties properties = new Properties();
        try {
            properties.load(Gdx.files.internal(propertiesFilePath).reader());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String[] coordinates = entry.getKey().toString().split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);

            int tileCode = Integer.parseInt(entry.getValue().toString());
            TextureRegion tileRegion = getTileRegion(tileCode);

            // Render the tile at (x, y)
            game.getSpriteBatch().draw(tileRegion, x * 16, y * 16);
        }
    }

    public void render(Batch batch) {
        // Render any additional map-related logic
        // (e.g., handling objects, entities, etc.)
    }

    private TextureRegion getTileRegion(int tileCode) {
        // Assuming there are 6 tiles in the first row (tile codes 0 to 5)
        if (tileCode >= 0 && tileCode < 6) {
            return tiles[0][tileCode];
        } else {
            // Handle other cases or return a default texture region
            return null;
        }
    }
}



/*
public class GameMap {


    private TiledMap tiledMap;
    private TextureRegion[][] tiles;  // Assuming you'll load the tileset as a TextureRegion

    public GameMap(TiledMap tiledMap, TextureAtlas textureAtlas) {
        this.tiledMap = tiledMap;
        this.tiles = TextureRegion.split(textureAtlas.findRegion("basictiles"), 16, 16);  // Assuming you're using TextureAtlas
    }

    public void interpretMap() {
        Texture basicTileSheet = new Texture(Gdx.files.internal("basictiles.png"));

        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("your_layer_name");

        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                if (cell != null) {
                    int tileCode = Integer.parseInt(cell.getTile().getProperties().get("type", String.class));
                    TextureRegion tileRegion = getTileRegion(tileCode);

                    // Now you can use tileRegion for rendering or other purposes
                    // Example: batch.draw(tileRegion, x * 16, y * 16);
                }
            }
        }
    }

    private TextureRegion getTileRegion(int tileCode) {
        switch (tileCode) {
            case 0:
                return tiles[1][6];  // col 6, row 1
            case 1:
                return tiles[7][1];  // col 1, row 7
            case 2:
                return tiles[6][2];  // col 2, row 6
            case 3:
                return tiles[3][7];  // col 7, row 3
            case 4:
                return tiles[8][4];  // col 4, row 8
            case 5:
                return tiles[9][6];  // col 6, row 9
            default:
                return null;  // Handle other cases as needed
        }
    }



}*/
