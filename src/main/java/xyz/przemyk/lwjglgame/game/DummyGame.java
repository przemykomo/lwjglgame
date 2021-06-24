package xyz.przemyk.lwjglgame.game;

import org.joml.Matrix4fStack;
import org.joml.Vector2f;
import xyz.przemyk.lwjglgame.engine.MouseInput;
import xyz.przemyk.lwjglgame.engine.Window;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame {

    private int direction = 0;
    private float color = 0.0f;
    private final Camera camera;
    private BatchRenderer batchRenderer;
    private final ArrayList<GameObject> gameObjects;

    public DummyGame() {
        camera = new Camera();
        gameObjects = new ArrayList<>();
    }

    public void init(Window window) throws Exception {
        batchRenderer = new BatchRenderer(window);
        gameObjects.add(new GameObject());
        GameObject second = new RotatingObject();
        second.position.set(2, 0.4, -0.3);
        gameObjects.add(second);
        gameObjects.add(camera);
    }

    public void input(Window window, MouseInput mouseInput) {
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }

        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward = 1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveForward = -1;
        } else {
            camera.moveForward = 0;
        }

        if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight = 1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveRight = -1;
        } else {
            camera.moveRight = 0;
        }

        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.moveUp = 1;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            camera.moveUp = -1;
        } else {
            camera.moveUp = 0;
        }

        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotation = mouseInput.getDisplVec();
            camera.angleXDegrees += rotation.x;
            camera.angleYDegrees += rotation.y;
        }
    }

    public void tick() {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if ( color < 0 ) {
            color = 0.0f;
        }

        for (GameObject gameObject : gameObjects) {
            gameObject.update();
        }
    }

    private final Matrix4fStack matrix4fStack = new Matrix4fStack(32);

    public void render(Window window, float partialTicks) {
        window.setClearColor(color, color, color, 0.0f);

        batchRenderer.preRender(window, camera, partialTicks);

        for (GameObject gameObject : gameObjects) {
            gameObject.render(batchRenderer, matrix4fStack, partialTicks);
        }

        batchRenderer.flush();
    }

    public void cleanup() {
        batchRenderer.cleanup();
    }
}
