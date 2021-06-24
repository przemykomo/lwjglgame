package xyz.przemyk.lwjglgame.game;

import org.joml.Vector3f;

public class Camera {
    public Vector3f position = new Vector3f();
    public Vector3f previousPosition = new Vector3f();
    public float moveForward = 0;
    public float moveRight = 0;
    public float moveUp = 0;
    public static final float speed = 0.2f;
    public float angleXDegrees;
    public float angleYDegrees;

    public void update() {
        previousPosition.set(position);
        moveRelative(moveRight * speed, moveForward * speed, moveUp * speed);
    }

    public void moveRelative(float right, float forward, float up) {
        position.x += Math.sin(Math.toRadians(angleYDegrees)) * forward;
        position.z += Math.cos(Math.toRadians(angleYDegrees)) * -forward;

        position.x += Math.cos(Math.toRadians(angleYDegrees)) * right;
        position.z += Math.sin(Math.toRadians(angleYDegrees)) * right;

        position.y += up;
    }
}
