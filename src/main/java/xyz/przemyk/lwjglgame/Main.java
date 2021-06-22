package xyz.przemyk.lwjglgame;

import xyz.przemyk.lwjglgame.engine.GameEngine;
import xyz.przemyk.lwjglgame.engine.IGameLogic;
import xyz.przemyk.lwjglgame.game.DummyGame;

public class Main {

    public static void main(String[] args) {
        try {
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEngine = new GameEngine("DevWindow", 600, 480, true, gameLogic);
            gameEngine.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
