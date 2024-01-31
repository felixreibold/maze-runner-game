package de.tum.cit.ase.maze.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.GameMap2;

public class Entity extends Sprite {

    protected Vector2 velocity = new Vector2();
    protected float speed;

    protected boolean tookDamage;
    protected float damageTaken;

    public float damageGiven;
    protected float lives;

    protected boolean dead;




    protected Direction lastDirection;
    protected int ld = 2;

    protected float stateTime;


    protected TiledMapTileLayer collisionLayer;

    protected Rectangle entityBounds;

    protected float timeForCooldown = 0f;
    protected static float COOLDOWN_TIME = 0.5f;

    public Entity(Sprite sprite, GameMap2 gameMap) {

        super(sprite);

        this.dead = false;
        this.tookDamage = false;
        this.damageTaken = 0.0f;



    }

    public void takeDamage(float damage){

        if(lives-damage>0){
            setLives(this.lives-damage);
        } else {
            setLives(0);
            setDead(true);
        }

    }

    public void checkDamage(){
        if (tookDamage){
            this.takeDamage(damageTaken);
        }
    }

    public float getLives() {
        return lives;
    }

    public void setLives(float lives) {
        this.lives = lives;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isTookDamage() {
        return tookDamage;
    }

    public void setTookDamage(boolean tookDamage) {
        this.tookDamage = tookDamage;
    }

    public float getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(float damageTaken) {
        this.damageTaken = damageTaken;
    }

    public float getDamageGiven() {
        return damageGiven;
    }

    public void setDamageGiven(float damageGiven) {
        this.damageGiven = damageGiven;
    }

    public Direction getLastDirection() {
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

    public Rectangle getEntityBounds() {
        return entityBounds;
    }

    public void setEntityBounds(Rectangle entityBounds) {
        this.entityBounds = entityBounds;
    }
}
