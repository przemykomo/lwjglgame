package xyz.przemyk.lwjglgame.game;

import org.joml.Matrix4fStack;

public class Camera extends GameObject {
    public float moveForward = 0;
    public float moveRight = 0;
    public float moveUp = 0;
    public static final float speed = 0.2f;

    @Override
    public void update() {
        super.update();
        moveRelative(moveRight * speed, moveForward * speed, moveUp * speed);
    }

    @Override
    public void render(BatchRenderer renderer, Matrix4fStack matrix4fStack, float partialTicks) {}
}
