package de.tum.cit.ase.maze.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.MazeRunnerGame;

public class Hud {

    private MazeRunnerGame game;


    public Stage stage;
    private Viewport viewport;
    private Integer worldTimer;
    private float timer = 0; // Timer to keep track of time
    private float timeCount;

    private boolean keyAcquired;
    private Integer score;
    Label countdowLabel;
    Label scoreLabel;
    Label timeLabel;

    Label timeLabelText;
    Label levelLabel;
    Label worldLabel;

    Label keyLabelText;

    Label keyLabel;

    Label livesLabel;

    Label livesLabelText;

    Label scoreLabelText;
    private float lives;

    public Hud(SpriteBatch sb, MazeRunnerGame game){
        this.game = game;
        worldTimer = 300;
        timeCount = 0.0f;
        keyAcquired = false;
        score = 0;
        lives = 3.0f;
        viewport = new FitViewport(MazeRunnerGame.V_WIDTH, MazeRunnerGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        update(0.0f);

    }

    /**
     * Updates the game HUD each frame, including the countdown timer, player lives, key status, and score.
     * This method manages the timing logic for decrementing the in-game world timer and updates the display
     * of various HUD elements such as player lives, key acquisition status, score, and remaining time.
     *
     * @param delta The time in seconds since the last update, used for tracking and updating the countdown timer.
     */

    public void update(float delta) {


        timer += delta;

        // Check if one second has passed
        if (timer >= 1.0f) {
            // Decrement the worldTimer by 1 second
            worldTimer--;

            // Reset the timer for the next second
            timer -= 1.0f;

            // Ensure the timer doesn't go below 0
            if (worldTimer < 0) {
                worldTimer = 0;
            }
        }



        stage.clear();

        Table table = new Table();
        table.top();
        table.setFillParent(true);




        levelLabel = new Label("1-1", game.getSkin());
        worldLabel = new Label("WORLD", game.getSkin());

        livesLabelText = new Label("LIVES", game.getSkin());
        livesLabel = new Label(String.format("%.1f", lives), game.getSkin());

        keyLabelText = new Label("KEY", game.getSkin());
        if(keyAcquired){
            keyLabel = new Label("Yes", game.getSkin());
        } else {
            keyLabel = new Label("No", game.getSkin());
        }

        scoreLabelText = new Label("SCORE", game.getSkin());
        scoreLabel = new Label(String.format("%06d", score), game.getSkin());

        timeLabelText = new Label("TIME", game.getSkin());
        countdowLabel = new Label(String.format("%03d", worldTimer), game.getSkin()); //, new Label.LabelStyle(new BitmapFont(), Color.WHITE));



        table.add(livesLabelText).expandX().padTop(10);
        table.add(keyLabelText).expandX().padTop(10);
        table.add(scoreLabelText).expandX().padTop(10);
        table.add(timeLabelText).expandX().padTop(10);
        table.row();
        table.add(livesLabel).expandX().padTop(10);
        table.add(keyLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.add(countdowLabel).expandX().padTop(10);

        stage.addActor(table);
    }

    public MazeRunnerGame getGame() {
        return game;
    }

    public void setGame(MazeRunnerGame game) {
        this.game = game;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public Integer getWorldTimer() {
        return worldTimer;
    }

    public void setWorldTimer(Integer worldTimer) {
        this.worldTimer = worldTimer;
    }

    public float getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(float timeCount) {
        this.timeCount = timeCount;
    }

    public boolean isKeyAcquired() {
        return keyAcquired;
    }

    public void setKeyAcquired(boolean keyAcquired) {
        this.keyAcquired = keyAcquired;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Label getCountdowLabel() {
        return countdowLabel;
    }

    public void setCountdowLabel(Label countdowLabel) {
        this.countdowLabel = countdowLabel;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public void setScoreLabel(Label scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(Label timeLabel) {
        this.timeLabel = timeLabel;
    }

    public Label getLevelLabel() {
        return levelLabel;
    }

    public void setLevelLabel(Label levelLabel) {
        this.levelLabel = levelLabel;
    }

    public Label getWorldLabel() {
        return worldLabel;
    }

    public void setWorldLabel(Label worldLabel) {
        this.worldLabel = worldLabel;
    }

    public Label getLivesLabel() {
        return livesLabel;
    }

    public void setLivesLabel(Label livesLabel) {
        this.livesLabel = livesLabel;
    }

    public float getLives() {
        return lives;
    }

    public void setLives(float lives) {
        this.lives = lives;
    }
}
