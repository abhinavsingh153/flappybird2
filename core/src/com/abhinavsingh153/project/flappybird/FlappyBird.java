package com.abhinavsingh153.project.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    SpriteBatch batch;
    Texture background;

    // in order to get the bird flapping we need to put the birds images (Texture) in an array

    Texture[] birds;
    int flapState = 1;
    float birdY = 0;
    float velocity = 0;
    int gameState = 0;

    Texture toptube;
    Texture bottomtube;

    int score;
    int scoringTube = 0;
    BitmapFont scoreFont;
    Texture gameOver;
    float gap = 350;

    // max off set of the tubes from the centre up and down

    float maxTubeOffset;
    Random randomGenerator;
    float tubeVelocity = 4;
    int numberOfTubes = 4;
    float[] tubeOffset = new float[numberOfTubes];
    float[] tubeX = new float[numberOfTubes];
    float distanceBetweenTubes;

    Circle birdCircle;

    //ShapeRenderer shapeRenderer ;

    Rectangle[] toptubeRectangles;
    Rectangle[] bottomtubeRectangles;

    @Override
    public void create() {

        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.WHITE);
        scoreFont.getData().setScale(5);

        gameOver = new Texture("gameover.png");
        birdCircle = new Circle();
        //shapeRenderer = new ShapeRenderer();

        toptubeRectangles = new Rectangle[numberOfTubes];
        bottomtubeRectangles = new Rectangle[numberOfTubes];

        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        toptube = new Texture("toptube.png");
        bottomtube = new Texture("bottomtube.png");


        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
        startGame();
    }

    public void startGame() {

        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
            toptubeRectangles[i] = new Rectangle();
            bottomtubeRectangles[i] = new Rectangle();

        }
    }

    @Override
    public void render() {

        batch.begin();

        // displaying the background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (gameState == 1) {
            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth()) {
                score++;
                Gdx.app.log("Score : ", String.valueOf(score));
                if (scoringTube < numberOfTubes - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }


            if (Gdx.input.justTouched()) {
                velocity = -20;
                // it creates a random no. between 0 & 1
            }

            for (int i = 0; i < numberOfTubes; i++) {
                if (tubeX[i] < -toptube.getWidth()) {
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                } else {
                    tubeX[i] = tubeX[i] - tubeVelocity;
                }

                batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i]);
                toptubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], toptube.getWidth(), toptube.getHeight());
                bottomtubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i], bottomtube.getWidth(), bottomtube.getHeight());
            }


            // stopping the bird from disappearing from the screen.

            if (birdY > 0) {
                velocity++;
                birdY -= velocity;
            }

            // if the bird is not above the boottm pf the screen
            else {
                gameState = 2;
            }

        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }
        // the sate where the collisio is detected andwe need to show game over along with
        //the option of restarting game.
        else if (gameState == 2) {
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                gameState = 0;
                startGame();
                score = 0;
                scoringTube = 0;
                velocity = 0;
            }
        }


        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }

        // to add our background we need to begin the batch.
        //It just tells the render rmethod that we are going to start diplayimg the sprites now


        // positioning flappy to the centre of the screen.

        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        scoreFont.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2 - 30, Gdx.graphics.getHeight() * 3 / 4);
        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth(), birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);
        //shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for (int i = 0; i < numberOfTubes; i++) {

            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i] , toptube.getWidth() , toptube.getHeight());
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i] , bottomtube.getWidth(), bottomtube.getHeight());
            if (Intersector.overlaps(birdCircle, toptubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomtubeRectangles[i])) {
                gameState = 2;
            }
        }


        //  shapeRenderer.end();

    }

}
