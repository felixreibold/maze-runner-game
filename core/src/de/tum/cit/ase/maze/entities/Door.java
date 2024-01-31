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

public class Door extends Entity{

    protected Animation<TextureRegion> openDoorAnimation;

    private boolean animationStarted = false;
    private boolean animationFinished = false;

    private boolean animationInProgress = false;

    private Rectangle moveToNextLevel;

    public Door(Sprite sprite, GameMap2 gameMap,
                    Animation<TextureRegion> openDoorAnimation) {


        super(sprite, gameMap);

        this.openDoorAnimation = openDoorAnimation;

        this.speed = 0;

        this.damageGiven = 0.0f;
        this.lives = 100;

        this.collisionLayer = gameMap.getTileLayer();



        update(Gdx.graphics.getDeltaTime());
        this.stateTime = 0;

        Gdx.app.log("Door", "State Time: " + stateTime);

        MapUtils.processTiles(collisionLayer);
    }

    /**
     * Updates the state of the entity each frame, handling cooldowns, damage checks, and animation updates.
     * This method should be called in the game loop to ensure the entity responds to game events appropriately.
     *
     * @param delta The time in seconds since the last frame. This is used to ensure smooth and consistent updates
     *              across different frame rates.
     */

    public void update(float delta) {

        timeForCooldown +=delta;

        this.entityBounds = new Rectangle(getX() + getWidth()*(0.0f/16.0f), getY() + getHeight()*(0.0f/16.0f), getWidth()*(16.0f/16.0f), getHeight()*(16f/16.0f));
        this.moveToNextLevel = new Rectangle(getX() + getWidth()*(0.0f/16.0f), getY() + getHeight()*(0.0f/16.0f), getWidth()*(16.0f/16.0f), getHeight()*(2.0f/16.0f));


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


        setRegion(openDoorAnimation.getKeyFrame(stateTime, true));



        // Check if the animation has finished
        animationFinished = openDoorAnimation.isAnimationFinished(stateTime);

        if (!animationFinished && animationInProgress) {
            // Update animation state
            stateTime += delta;
        }else if (animationFinished){
            TextureRegion[] frames = openDoorAnimation.getKeyFrames();
            TextureRegion lastFrame = frames[frames.length - 1];
            setRegion(lastFrame);
        }
    }

    public void setAnimationStarted(boolean animationStarted) {
        this.animationStarted = animationStarted;
    }

    public boolean isAnimationFinished() {
        return animationFinished;
    }

    public boolean isAnimationInProgress() {
        return animationInProgress;
    }

    public void setAnimationInProgress(boolean animationInProgress) {
        this.animationInProgress = animationInProgress;
    }

    public Rectangle getMoveToNextLevel() {
        return moveToNextLevel;
    }



    private Animation<TextureRegion> getCurrentAnimation() {

        return openDoorAnimation;
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
