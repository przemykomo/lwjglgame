package xyz.przemyk.lwjglgame.game;

public class RotatingObject extends GameObject {

    @Override
    public void update() {
        super.update();
        angleYDegrees++;
    }
}
