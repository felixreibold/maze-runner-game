package de.tum.cit.ase.maze.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.GameMap2;
import de.tum.cit.ase.maze.helpers.MapUtils;

/**
 * Represents a trap entity in the game. Traps can deal damage to the player or enemies and have an animation for activation.
 */
public class Trap extends Entity{

    protected Animation<TextureRegion> trapAnimation;

    /**
     * Constructs a Trap entity with specified sprite, game map, and animation.
     *
     * @param sprite           The sprite representing the trap.
     * @param gameMap          The game map the trap is part of.
     * @param trapAnimation    The animation to play when the trap is activated.
     */
    public Trap(Sprite sprite, GameMap2 gameMap,
                 Animation<TextureRegion> trapAnimation) {


        super(sprite, gameMap);

        this.trapAnimation = trapAnimation;

        this.speed = 0;

        this.damageGiven = 0.25f;
        this.lives = 10;

        this.collisionLayer = gameMap.getTileLayer();


        update(Gdx.graphics.getDeltaTime());
        this.stateTime = 0;

        Gdx.app.log("Ghost", "State Time: " + stateTime);

        MapUtils.processTiles(collisionLayer);
    }

    /**
     * Updates the trap's state. This includes handling animations and checking for damage.
     *
     * @param delta Time since last frame in seconds.
     */

    public void update(float delta) {

        timeForCooldown +=delta;

        this.entityBounds = new Rectangle(getX() + getWidth()*(3.0f/16.0f), getY() + getHeight()*(1.0f/16.0f), getWidth()*(10.0f/16.0f), getHeight()*(10f/16.0f));

        if (timeForCooldown >= COOLDOWN_TIME) {
            checkDamage();
            //Gdx.app.log("Player", "Lives: " + lives);
            if(tookDamage){
                //Gdx.app.log("Player", "Lives: " + lives);
                timeForCooldown = 0.0f;
                tookDamage = false;
                damageTaken = 0.0f;

            }

        }

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

    /**
     * Updates the animation state of the trap, playing or finishing the animation as needed.
     *
     * @param delta Time since last frame in seconds.
     */
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
            collisionX = collisionLayer.getCell((int) ((getX() + getWidth()*(2.5f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");

            // middle left
            if(!collisionX){
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()*(4.0f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            // bottom left
            if(!collisionX){
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()*(5.5f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

        } else if(velocity.x > 0) {
            // top right
            collisionX = collisionLayer.getCell((int) ((getX() + getWidth()*(2.5f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");

            // middle right
            if(!collisionX){
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()*(4.0f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            // bottom right
            if(!collisionX){
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()*(5.5f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
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
            collisionY = collisionLayer.getCell((int) ((getX() + getWidth()*(2.5f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");

            // bottom middle
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()*(4.0f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            // bottom right
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()*(5.5f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }


        } else if(velocity.y > 0) {
            // top left
            collisionY = collisionLayer.getCell((int) ((getX() + getWidth()*(2.5f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");

            // top middle
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()*(4.0f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            // top right
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()*(5.5f/8.0f)) / tileWidth), (int) ((getY() + getHeight()*(2.0f/8.0f)) / tileHeight))
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
        /*getLastDirection();


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
        }*/
        return trapAnimation;
    }



    public Vector2 getVelocity() {
        return velocity;
    }

    public float getSpeed() {
        return speed;
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
