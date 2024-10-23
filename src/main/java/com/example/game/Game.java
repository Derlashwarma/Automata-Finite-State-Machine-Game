package com.example.game;

import com.example.game.Entity.Enemy;
import com.example.game.Entity.Player;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Game implements Runnable {
    public AnchorPane main_container;
    public static boolean game_running;
    private double minimumHealth = 100;
    private double minimumSpeed = 50;
    private int interval = 2000;
    public static ArrayList<Runnable> enemies;
    private ImageView character;
    public static int score;
    public static Player player;

    // Enum for Game States
    public enum GameState {
        START,
        PLAYING,
        GAME_OVER
    }

    private GameState currentState;

    public Game(AnchorPane pane, ImageView character) {
        this.main_container = pane;
        game_running = true;
        enemies = new ArrayList<>();
        this.character = character;
        this.currentState = GameState.START; // Initial state
    }

    public static void addScore(int sc) {
        score += sc;
    }

    public static void endGame() {
        game_running = false;
    }

    @Override
    public void run() {
        while (game_running) {
            switch (currentState) {
                case START:
                    initializeGame();
                    break;
                case PLAYING:
                    gameLoop();
                    break;
                case GAME_OVER:
                    handleGameOver();
                    break;
            }
        }
    }

    private void initializeGame() {
        player = new Player(50, Color.GREEN, "Ardel");
        player.setAnchorPane(main_container);
        Thread playerThread = new Thread(player);
        playerThread.start();

        main_container.setOnMouseMoved(event -> {
            character.setLayoutX(event.getX() - 50);
            character.setLayoutY(event.getY() - 40);
            player.setLayoutX(event.getX());
            player.setLayoutY(event.getY());
            player.setCurrentX(event.getX());
            player.setCurrentY(event.getY());
        });

        // Transition to PLAYING state
        currentState = GameState.PLAYING;
    }

    private void gameLoop() {
        Random random = new Random();
        while (game_running) {
            if (enemies.size() < 10) {
                spawnEnemy(random);
            }

            try {
                Thread.sleep(interval);
                if (interval > 50) {
                    interval -= 10;
                }
                minimumHealth += 10;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Check game over condition
            if (player.getHealth() <= 0) {
                currentState = GameState.GAME_OVER;
            }
        }
    }

    private void spawnEnemy(Random random) {
        int minRange = 5;
        int maxRange = 495;
        int range = maxRange - minRange + 1;
        int randomX = random.nextInt(range) + minRange;

        long speed = (long) (random.nextDouble() * (50 - minimumSpeed) + minimumSpeed);
        double health = random.nextInt((int) minimumHealth);
        Enemy enemy = new Enemy(speed, 50, randomX, 0, Color.BLUE, "Enemy");
        enemy.setAnchorPane(main_container);
        enemy.setHealth(health);

        Thread enemyThread = new Thread(enemy);
        Platform.runLater(() -> {
            main_container.getChildren().add(enemy);
        });
        enemyThread.start();
    }

    private void handleGameOver() {
        System.out.println("GAME OVER");
        System.out.println("TOTAL SCORE: " + score);
        Platform.runLater(() -> {
            main_container.getChildren().clear();
        });
        endGame();
    }
}
