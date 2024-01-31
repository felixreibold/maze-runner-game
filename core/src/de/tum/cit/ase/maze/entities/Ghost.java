package de.tum.cit.ase.maze.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.GameMap2;
import de.tum.cit.ase.maze.helpers.MapUtils;

import java.util.Random;

/**
 * Ghosts are the main enemies the game. The class handles the animation for floating randomly and colliding.
 */

public class Ghost extends Enemy{

    private float timeSinceLastDirectionChange = 0f;
    private float stationaryTime = 0f;
    private boolean isMoving = true;
    private final float minMoveTime = 2f; // minimum time to move in seconds
    private final float maxMoveTime = 3f; // maximum time to move in seconds
    private final float stopTime = 0.5f; // time to stop in seconds
    private Random random = new Random();

    /**
     * Represents a Ghost enemy in the game, extending the Enemy class. Ghosts have unique movement
     * patterns, capable of stopping and changing directions randomly. This class handles the ghost's
     * animations, movements, and interactions within the game world.
     *
     * @param sprite The sprite representing the ghost.
     * @param gameMap The game map the ghost is part of, used for collision detection and movement.
     * @param upAnimation The animation used when the ghost moves up.
     * @param downAnimation The animation used when the ghost moves down.
     * @param leftAnimation The animation used when the ghost moves left.
     * @param rightAnimation The animation used when the ghost moves right.
     * @param upAttackAnimation The attack animation used when the ghost attacks upwards.
     * @param downAttackAnimation The attack animation used when the ghost attacks downwards.
     * @param leftAttackAnimation The attack animation used when the ghost attacks to the left.
     * @param rightAttackAnimation The attack animation used when the ghost attacks to the right.
     * @param stillframeFacingUp The still frame used when the ghost is facing up and stationary.
     * @param stillframeFacingDown The still frame used when the ghost is facing down and stationary.
     * @param stillframeFacingLeft The still frame used when the ghost is facing left and stationary.
     * @param stillframeFacingRight The still frame used when the ghost is facing right and stationary.
     */
    public Ghost(Sprite sprite, GameMap2 gameMap,
                 Animation<TextureRegion> upAnimation,
                 Animation<TextureRegion> downAnimation,
                 Animation<TextureRegion> leftAnimation,
                 Animation<TextureRegion> rightAnimation,

                 Animation<TextureRegion> upAttackAnimation,
                 Animation<TextureRegion> downAttackAnimation,
                 Animation<TextureRegion> leftAttackAnimation,
                 Animation<TextureRegion> rightAttackAnimation,

                 TextureRegion stillframeFacingUp,
                 TextureRegion stillframeFacingDown,
                 TextureRegion stillframeFacingLeft,
                 TextureRegion stillframeFacingRight) {


        super(sprite, gameMap,
                upAnimation, downAnimation, leftAnimation, rightAnimation,
                upAttackAnimation, downAttackAnimation, leftAttackAnimation, rightAttackAnimation,
                stillframeFacingUp, stillframeFacingDown, stillframeFacingLeft, stillframeFacingRight);

        this.speed = 60 * 1;

        this.damageGiven = 0.5f;
        this.lives = 1;

        this.collisionLayer = gameMap.getTileLayer();

        this.upAnimation = upAnimation;
        this.downAnimation = downAnimation;
        this.leftAnimation = leftAnimation;
        this.rightAnimation = rightAnimation;

        this.upAttackAnimation = upAttackAnimation;
        this.downAttackAnimation = downAttackAnimation;
        this.leftAttackAnimation = leftAttackAnimation;
        this.rightAttackAnimation = rightAttackAnimation;

        this.stillframeFacingDown = stillframeFacingDown;
        this.stillframeFacingRight = stillframeFacingRight;
        this.stillframeFacingUp = stillframeFacingUp;
        this.stillframeFacingLeft = stillframeFacingLeft;


        update(Gdx.graphics.getDeltaTime());
        this.stateTime = 0;

        Gdx.app.log("Ghost", "State Time: " + stateTime);

        MapUtils.processTiles(collisionLayer);
    }

    /**
     * Updates the ghost's state each frame, including movement and checking for interactions with
     * the player or environment. Handles changing directions and stopping based on randomized behavior.
     *
     * @param delta The time in seconds since the last frame. Used for frame-independent movement and timing.
     */

    public void update(float delta) {

        timeForCooldown +=delta;

        this.entityBounds = new Rectangle(getX() + getWidth()*(4.0f/16.0f), getY() + getHeight()*(3.0f/16.0f), getWidth()*(8.0f/16.0f), getHeight()*(4.0f/16.0f));

        // Update the time since last direction change
        timeSinceLastDirectionChange += delta;

        // Determine if it's time to change direction or stop
        if (isMoving && timeSinceLastDirectionChange >= minMoveTime + random.nextFloat() * (maxMoveTime - minMoveTime)) {
            // Stop moving and reset the timer
            velocity.set(0, 0);
            isMoving = false;
            timeSinceLastDirectionChange = 0;
            stationaryTime = 0;
        } else if (!isMoving) {
            stationaryTime += delta;
            if (stationaryTime >= stopTime) {
                // Change direction and start moving
                float newAngle = random.nextFloat() * 360; // Random angle in degrees
                //float speed = 1; // Set your desired speed
                velocity.set((float) (speed * Math.cos(Math.toRadians(newAngle))), (float) (speed * Math.sin(Math.toRadians(newAngle))));
                isMoving = true;
                timeSinceLastDirectionChange = 0;
            }
        }

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
        //handleInput();

        // Update player position based on velocity
        updatePosition(delta);

        // Update animation state
        stateTime += delta;

        // Set the player's current frame based on the active animation
        setRegion(getCurrentAnimation().getKeyFrame(stateTime, true));
    }

    /**
     * Handles user input for the ghost, used for debugging or controlled movement.
     * Adjusts the ghost's velocity based on keyboard inputs.
     */

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
     * Determines the current animation for the ghost based on its velocity and last direction faced.
     * Defaults to still frames when the ghost is not moving.
     *
     * @return The current animation to be displayed for the ghost.
     */
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





    public Vector2 getVelocity() {
        return velocity;
    }

    public float getSpeed() {
        return speed;
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
