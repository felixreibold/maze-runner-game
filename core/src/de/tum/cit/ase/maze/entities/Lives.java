package de.tum.cit.ase.maze.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class Lives extends Sprite {
    private Hearts fill;

    private Player player;

    private float stateTime;

    public Lives(Sprite sprite, Player player) {

        super(sprite);
        this.player = player;
        this.fill = Hearts.FULL;

        update(Gdx.graphics.getDeltaTime());
        this.stateTime = 0;



    }

    public void update(float delta) {

        // Update animation state
        stateTime += delta;
    }
}
