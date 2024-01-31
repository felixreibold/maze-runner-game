package de.tum.cit.ase.maze.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.maze.GameMap2;

/**
 * Represents a generic enemy entity in the game.
 */
public class Enemy extends Entity{
    protected Animation<TextureRegion> upAnimation;
    protected Animation<TextureRegion> downAnimation;
    protected Animation<TextureRegion> leftAnimation;
    protected Animation<TextureRegion> rightAnimation;

    protected Animation<TextureRegion> upAttackAnimation;
    protected Animation<TextureRegion> downAttackAnimation;
    protected Animation<TextureRegion> leftAttackAnimation;
    protected Animation<TextureRegion> rightAttackAnimation;

    protected TextureRegion stillframeFacingUp;
    protected TextureRegion stillframeFacingDown;
    protected TextureRegion stillframeFacingLeft;
    protected TextureRegion stillframeFacingRight;

    /**
     * Constructs an Enemy with animations and static facing textures.
     *
     * @param sprite                The sprite representing the enemy.
     * @param gameMap               The game map the enemy is part of.
     * @param upAnimation           The animation for moving up.
     * @param downAnimation         The animation for moving down.
     * @param leftAnimation         The animation for moving left.
     * @param rightAnimation        The animation for moving right.
     * @param upAttackAnimation     The animation for attacking up.
     * @param downAttackAnimation   The animation for attacking down.
     * @param leftAttackAnimation   The animation for attacking left.
     * @param rightAttackAnimation  The animation for attacking right.
     * @param stillframeFacingUp    The static texture for facing up.
     * @param stillframeFacingDown  The static texture for facing down.
     * @param stillframeFacingLeft  The static texture for facing left.
     * @param stillframeFacingRight The static texture for facing right.
     */
    public Enemy(Sprite sprite, GameMap2 gameMap,
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

        super(sprite, gameMap);


    }


    /**
     * Updates the position of the enemy based on its velocity.
     * This method handles collision detection with the map's tile layer.
     *
     * @param delta Time in seconds since the last frame.
     */
    protected void updatePosition(float delta) {
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


}
