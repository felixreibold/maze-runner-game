package de.tum.cit.ase.maze.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import de.tum.cit.ase.maze.GameMap2;
import de.tum.cit.ase.maze.helpers.MapUtils;

public class Poof extends Entity{

    private Texture objectsSheet = new Texture(Gdx.files.internal("objects.png"));

    private TextureRegion poof = new TextureRegion(objectsSheet, 32, 32, 32, 32);
    protected Animation<TextureRegion> ghostPoofAnimation;
    private boolean animationStarted = false;
    private boolean animationFinished = false;

    public Poof(Sprite sprite, GameMap2 gameMap,
                Animation<TextureRegion> ghostPoofAnimation) {

        super(sprite, gameMap);

        this.ghostPoofAnimation = ghostPoofAnimation;

        update(Gdx.graphics.getDeltaTime());
        this.stateTime = 0;
    }

    /**
     * Updates the state of the entity which is a small explosion animation, including its animation and position.
     * This method should be called in the game loop to ensure the entity is updated with the correct timing.
     *
     * @param delta The time in seconds since the last update.
     */
    public void update(float delta) {

        timeForCooldown +=delta;

        this.entityBounds = new Rectangle(getX() + getWidth()*(0.0f/16.0f), getY() + getHeight()*(0.0f/16.0f), getWidth()*(16.0f/16.0f), getHeight()*(16f/16.0f));

        // Update animation state only if it hasn't started yet
        if (!animationStarted) {
            // Start the animation
            animationStarted = true;
            stateTime = 0;
        }

        // Set the player's current frame based on the active animation
        setRegion(ghostPoofAnimation.getKeyFrame(stateTime, true));

        // Check if the animation has finished
        animationFinished = ghostPoofAnimation.isAnimationFinished(stateTime);

        if (!animationFinished) {
            // Update animation state
            stateTime += delta;
        }
    }

    /**
     * Sets the flag indicating whether the animation has started.
     *
     * @param animationStarted {@code true} if the animation has started; otherwise, {@code false}.
     */
    public void setAnimationStarted(boolean animationStarted) {
        this.animationStarted = animationStarted;
    }

    /**
     * Checks if the animation has finished playing.
     *
     * @return {@code true} if the animation is finished; otherwise, {@code false}.
     */
    public boolean isAnimationFinished() {
        return animationFinished;
    }



}
