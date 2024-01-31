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
 * Represents a key chest in the game. Handles the animation for opening the chest and revealing the key.
 */
public class KeyChest extends Entity{

    protected Animation<TextureRegion> openKeyChestAnimation;

    private boolean animationStarted = false;
    private boolean animationFinished = false;

    private boolean animationInProgress = false;

    /**
     * Constructs a KeyChest with specified sprite, game map, and animation.
     *
     * @param sprite               The sprite representing the key chest.
     * @param gameMap              The game map the key chest is part of.
     * @param openKeyChestAnimation The animation for opening the key chest.
     */
    public KeyChest(Sprite sprite, GameMap2 gameMap,
                Animation<TextureRegion> openKeyChestAnimation) {


        super(sprite, gameMap);

        this.openKeyChestAnimation = openKeyChestAnimation;

        this.speed = 0;

        this.damageGiven = 0.0f;
        this.lives = 100;

        this.collisionLayer = gameMap.getTileLayer();


        update(Gdx.graphics.getDeltaTime());
        this.stateTime = 0;

        Gdx.app.log("KeyChest", "State Time: " + stateTime);

        MapUtils.processTiles(collisionLayer);
    }

    /**
     * Updates the state of the key chest, including managing animations.
     *
     * @param delta Time in seconds since the last frame.
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

        // Update animation state only if it hasn't started yet
        if (animationStarted) {
            // Start the animation
            animationStarted = false;
            stateTime = 0;
        }

        // Set the player's current frame based on the active animation


        setRegion(openKeyChestAnimation.getKeyFrame(stateTime, true));



        // Check if the animation has finished
        animationFinished = openKeyChestAnimation.isAnimationFinished(stateTime);

        if (!animationFinished && animationInProgress) {
            // Update animation state
            stateTime += delta;
        }else if (animationFinished){
            TextureRegion[] frames = openKeyChestAnimation.getKeyFrames();
            TextureRegion lastFrame = frames[frames.length - 1];
            setRegion(lastFrame);
        }
    }

    /**
     * Initiates the opening animation for the key chest.
     */
    public void setAnimationStarted(boolean animationStarted) {
        this.animationStarted = animationStarted;
    }

    /**
     * Checks if the opening animation has finished.
     *
     * @return True if the animation has finished, false otherwise.
     */
    public boolean isAnimationFinished() {
        return animationFinished;
    }

    public boolean isAnimationInProgress() {
        return animationInProgress;
    }

    public void setAnimationInProgress(boolean animationInProgress) {
        this.animationInProgress = animationInProgress;
    }

    private Animation<TextureRegion> getCurrentAnimation() {

        return openKeyChestAnimation;
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
