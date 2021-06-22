package xyz.przemyk.lwjglgame.game;

import org.joml.Vector3f;

public class Camera {
    public Vector3f position = new Vector3f();
    public float moveForward = 0;
    public static final float speed = 0.2f;

    public void update() {
        position.z -= moveForward * speed;
    }
}
