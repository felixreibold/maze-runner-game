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


/**
 * Represents the protagonist which you control in the game. The class handles the animation for walking, colliding and fight animations.
 */
public class Player extends Entity {

    private float gravity;

    // Animation variables
    private Animation<TextureRegion> upAnimation;
    private Animation<TextureRegion> downAnimation;
    private Animation<TextureRegion> leftAnimation;
    private Animation<TextureRegion> rightAnimation;

    private Animation<TextureRegion> upFightAnimation;
    private Animation<TextureRegion> downFightAnimation;
    private Animation<TextureRegion> leftFightAnimation;
    private Animation<TextureRegion> rightFightAnimation;

    // Still frames for idle state
    private TextureRegion stillframeFacingDown;
    private TextureRegion stillframeFacingRight;
    private TextureRegion stillframeFacingUp;
    private TextureRegion stillframeFacingLeft;

    // Variable to store the last direction faced


    private boolean fight;

    private boolean key;

    private Rectangle swordHitZone;
    private float timeSinceLastSwordStrike = 0f;

    /**
     * Constructs a Player with specified animations, textures, and initial settings.
     *
     * @param sprite The sprite representing the player.
     * @param gameMap The game map the player is part of.
     * @param upAnimation The animation when moving up.
     * @param downAnimation The animation when moving down.
     * @param leftAnimation The animation when moving left.
     * @param rightAnimation The animation when moving right.
     * @param upFightAnimation The fighting animation when facing up.
     * @param downFightAnimation The fighting animation when facing down.
     * @param leftFightAnimation The fighting animation when facing left.
     * @param rightFightAnimation The fighting animation when facing right.
     * @param stillframeFacingDown The still frame when facing down.
     * @param stillframeFacingRight The still frame when facing right.
     * @param stillframeFacingUp The still frame when facing up.
     * @param stillframeFacingLeft The still frame when facing left.
     */

    public Player(Sprite sprite, GameMap2 gameMap,
                  Animation<TextureRegion> upAnimation,
                  Animation<TextureRegion> downAnimation,
                  Animation<TextureRegion> leftAnimation,
                  Animation<TextureRegion> rightAnimation,

                  Animation<TextureRegion> upFightAnimation,
                  Animation<TextureRegion> downFightAnimation,
                  Animation<TextureRegion> leftFightAnimation,
                  Animation<TextureRegion> rightFightAnimation,

                  TextureRegion stillframeFacingDown,
                  TextureRegion stillframeFacingRight,
                  TextureRegion stillframeFacingUp,
                  TextureRegion stillframeFacingLeft) {

        super(sprite, gameMap);

        this.speed = 60 * 2;

        this.damageGiven = 1.0f;
        this.lives = 3;

        this.collisionLayer = gameMap.getTileLayer();

        this.upAnimation = upAnimation;
        this.downAnimation = downAnimation;
        this.leftAnimation = leftAnimation;
        this.rightAnimation = rightAnimation;

        this.upFightAnimation = upFightAnimation;
        this.downFightAnimation = downFightAnimation;
        this.leftFightAnimation = leftFightAnimation;
        this.rightFightAnimation = rightFightAnimation;

        this.stillframeFacingDown = stillframeFacingDown;
        this.stillframeFacingRight = stillframeFacingRight;
        this.stillframeFacingUp = stillframeFacingUp;
        this.stillframeFacingLeft = stillframeFacingLeft;


        update(Gdx.graphics.getDeltaTime());
        this.stateTime = 0;

        this.fight = false;
        this.key = false;

        Gdx.app.log("Player", "State Time: " + stateTime);
        Gdx.app.log("Player", "collisionLayer: " + collisionLayer.getHeight());

        MapUtils.processTiles(collisionLayer);
    }

    /**
     * Updates the player's state, including handling input, updating position, and managing animations.
     *
     * @param delta Time in seconds since the last frame.
     */

    public void update(float delta) {

        this.entityBounds = new Rectangle(getX() + getWidth()*(2.5f/8.0f), getY() + getHeight()*(2.0f/8.0f), getWidth()*(3.0f/8.0f), getHeight()*(1.0f/8.0f));

        // Update the time since last direction change
        timeSinceLastSwordStrike += delta;
        timeForCooldown +=delta;

        // Determine if it's time to change direction or stop
        if (timeSinceLastSwordStrike >= upFightAnimation.getAnimationDuration()) {
            fight = false;
            swordHitZone = null;
            timeSinceLastSwordStrike = 0.0f;
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
        handleInput();

        // Update player position based on velocity
        updatePosition(delta);

        // Update animation state
        stateTime += delta;

        // Set the player's current frame based on the active animation
        setRegion(getCurrentAnimation(fight).getKeyFrame(stateTime, true));
    }

    /**
     * Handles keyboard input to control the player's movement and actions.
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
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            fight = true;
        }


    }

    /**
    * Updates the player's position based on velocity and handles collision detection.
    *
    * @param delta Time in seconds since the last frame.
    */
    private void updatePosition(float delta) {
        // Update player position based on velocity and apply gravity
        //velocity.y -= gravity * delta;

        float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false, collisionMapBoundryX = false, collisionMapBoundryY = false;

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

            // left map boundary detection
            if(!collisionX){
                collisionX = (getX() + getWidth()*(2.5f/8.0f))<=0.0f;
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

            // right map boundary detection
            if(!collisionX){
                collisionX = (getX() + getWidth()*(5.5f/8.0f))>= collisionLayer.getWidth()*collisionLayer.getTileWidth()-5;
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

            // lower map boundary detection
            if(!collisionY){
                collisionY = (getY() + getHeight()*(2.0f/8.0f)) <= 0.0f;
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

            // upper map boundary detection
            if(!collisionY){
                collisionY = (getY() + getHeight()*(2.0f/8.0f)) >= collisionLayer.getHeight()*collisionLayer.getTileHeight()-5;
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

    /**
     * Returns the appropriate animation based on the player's current state and direction.
     *
     * @param strike Indicates whether the player is attacking.
     * @return The current animation for the player.
     */
    private Animation<TextureRegion> getCurrentAnimation(boolean strike) {
        getLastDirection();

        if(strike){
            //this.fight = false;
            if (lastDirection == Direction.DOWN) {
                this.swordHitZone = new Rectangle(getX(), getY() - getHeight()*(4.0f/16.0f), getWidth(), getHeight()*(8.0f/16.0f));
                return downFightAnimation;
            } else if (lastDirection == Direction.RIGHT) {
                this.swordHitZone = new Rectangle(getX()+getWidth(), getY() - getHeight()*(2f/16.0f), getWidth(), getHeight()*(12.0f/16.0f));
                return rightFightAnimation;
            } else if (lastDirection == Direction.UP) {
                this.swordHitZone = new Rectangle(getX(), getY() + getHeight()*(12.0f/16.0f), getWidth(), getHeight()*(8.0f/16.0f));
                return upFightAnimation;
            } else {
                this.swordHitZone = new Rectangle(getX()-getWidth(), getY() - getHeight()*(2.0f/16.0f), getWidth(), getHeight()*(12.0f/16.0f));
                return leftFightAnimation;
            }
        }


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

    public boolean isFight() {
        return fight;
    }

    public void setFight(boolean fight) {
        this.fight = fight;
    }

    public boolean hasKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public Rectangle getSwordHitZone() {
        return swordHitZone;
    }
}
