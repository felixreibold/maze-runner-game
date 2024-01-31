package de.tum.cit.ase.maze.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.GameMap2;
import de.tum.cit.ase.maze.helpers.MapUtils;

public class PlayerBackup extends Sprite {

    // the movement velocity
    private Vector2 velocity = new Vector2();

    private float speed = 60 * 2, gravity = 60 * 1.8f;

    // Animation variables
    private Animation<TextureRegion> upAnimation;
    private Animation<TextureRegion> downAnimation;
    private Animation<TextureRegion> leftAnimation;
    private Animation<TextureRegion> rightAnimation;

    // Still frames for idle state
    private TextureRegion stillframeFacingDown;
    private TextureRegion stillframeFacingRight;
    private TextureRegion stillframeFacingUp;
    private TextureRegion stillframeFacingLeft;

    // Variable to store the last direction faced
    private Direction lastDirection;
    private int ld = 2;

    private float stateTime;

    private TiledMapTileLayer collisionLayer;

    public PlayerBackup(Sprite sprite, GameMap2 gameMap,
                  Animation<TextureRegion> upAnimation,
                  Animation<TextureRegion> downAnimation,
                  Animation<TextureRegion> leftAnimation,
                  Animation<TextureRegion> rightAnimation,
                  TextureRegion stillframeFacingDown,
                  TextureRegion stillframeFacingRight,
                  TextureRegion stillframeFacingUp,
                  TextureRegion stillframeFacingLeft) {

        super(sprite);
        this.collisionLayer = gameMap.getTileLayer();

        this.upAnimation = upAnimation;
        this.downAnimation = downAnimation;
        this.leftAnimation = leftAnimation;
        this.rightAnimation = rightAnimation;
        this.stillframeFacingDown = stillframeFacingDown;
        this.stillframeFacingRight = stillframeFacingRight;
        this.stillframeFacingUp = stillframeFacingUp;
        this.stillframeFacingLeft = stillframeFacingLeft;
        update(Gdx.graphics.getDeltaTime());
        this.stateTime = 0;

        Gdx.app.log("Player", "State Time: " + stateTime);
        Gdx.app.log("Player", "collisionLayer: " + collisionLayer.getHeight());

        MapUtils.processTiles(collisionLayer);
    }

    public void update(float delta) {
        // Handle arrow key inputs
        handleInput();

        // Update player position based on velocity
        updatePosition(delta);

        // Update animation state
        stateTime += delta;

        // Set the player's current frame based on the active animation
        setRegion(getCurrentAnimation().getKeyFrame(stateTime, true));
    }

    private void handleInput() {
        velocity.x = 0; // Reset x-velocity before processing input
        velocity.y = 0; // if you do not "reset" the velocity, the player will keep going, like sliding on ice. this could be a feature "not a bug"

        // Check arrow key inputs
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x = -speed; // Move left
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x = speed; // Move right
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocity.y = speed; // Move left
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.y = -speed; // Move right
        }

    }

    private void updatePosition(float delta) {
        // Update player position based on velocity and apply gravity
        //velocity.y -= gravity * delta;

        float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false;

        //Previous movement method
        //setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta));

        // move on x
        setX(getX() + velocity.x * delta);

        if(velocity.x < 0) {
            // top left
            collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");

            // middle left
            if(!collisionX){
                collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()/2) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            // bottom left
            if(!collisionX){
                collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

        } else if(velocity.x > 0) {
            // top right
            collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");

            // middle right
            if(!collisionX){
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()/2) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            // bottom right
            if(!collisionX){
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }


        }

        // react to X collision
        if(collisionX){
            setX(oldX);
            velocity.x = 0;
        }


        // move on y
        setY(getY() + velocity.y * delta);

        if(velocity.y < 0) {
            // bottom left
            collisionY = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight))
                    .getTile().getProperties().containsKey("blocked");

            // bottom middle
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()/2) / tileWidth), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            // bottom right
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }


        } else if(velocity.y > 0) {
            // top left
            collisionY = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");

            // top middle
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()/2) / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            // top right
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

        }

        // react to Y collision
        if(collisionY){
            setY(oldY);
            velocity.y = 0;
        }




    }

    public void updateSprite() {
        // Set the player's current frame based on the active animation

    }

    private Animation<TextureRegion> getCurrentAnimation() {
        getLastDirection();
        if (velocity.y > 0) {
            return upAnimation;
        } else if (velocity.y < 0) {
            return downAnimation;
        } else if (velocity.x < 0) {
            return leftAnimation;
        } else if (velocity.x > 0) {
            return rightAnimation;
        } else {
            // Default to the still frame based on the last direction faced
            if (lastDirection == Direction.DOWN) {
                return new Animation<>(0, stillframeFacingDown);
            } else if (lastDirection == Direction.RIGHT) {
                return new Animation<>(0, stillframeFacingRight);
            } else if (lastDirection == Direction.UP) {
                return new Animation<>(0, stillframeFacingUp);
            } else {
                return new Animation<>(0, stillframeFacingLeft);
            }
        }
    }

    // Enum to represent player directions
    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }



    private Direction getLastDirection() {
        // Determine the last direction based on the current velocity
        if (velocity.y > 0) {
            lastDirection = Direction.UP;
            ld = 1;
        } else if (velocity.y < 0) {
            lastDirection = Direction.DOWN;
            ld = 2;
        } else if (velocity.x < 0) {
            lastDirection = Direction.LEFT;
            ld = 3;
        } else if (velocity.x > 0) {
            lastDirection = Direction.RIGHT;
            ld = 4;
        }

        return lastDirection;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getSpeed() {
        return speed;
    }

    public float getGravity() {
        return gravity;
    }

    public Animation<TextureRegion> getUpAnimation() {
        return upAnimation;
    }

    public Animation<TextureRegion> getDownAnimation() {
        return downAnimation;
    }

    public Animation<TextureRegion> getLeftAnimation() {
        return leftAnimation;
    }

    public Animation<TextureRegion> getRightAnimation() {
        return rightAnimation;
    }

    public TextureRegion getStillframeFacingDown() {
        return stillframeFacingDown;
    }

    public TextureRegion getStillframeFacingRight() {
        return stillframeFacingRight;
    }

    public TextureRegion getStillframeFacingUp() {
        return stillframeFacingUp;
    }

    public TextureRegion getStillframeFacingLeft() {
        return stillframeFacingLeft;
    }

    public int getLd() {
        return ld;
    }

    public float getStateTime() {
        return stateTime;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }
}
