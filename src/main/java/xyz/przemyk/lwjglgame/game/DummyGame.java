package xyz.przemyk.lwjglgame.game;

import org.joml.Vector2f;
import xyz.przemyk.lwjglgame.engine.IGameLogic;
import xyz.przemyk.lwjglgame.engine.MouseInput;
import xyz.przemyk.lwjglgame.engine.Window;
import xyz.przemyk.lwjglgame.engine.graph.Mesh;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class DummyGame implements IGameLogic {

    private int direction = 0;
    private float color = 0.0f;
    private final Camera camera;
    private BatchRenderer batchRenderer;

    public DummyGame() {
        camera = new Camera();
    }

    @Override
    public void init(Window window) throws Exception {
        batchRenderer = new BatchRenderer(window);
    }

    @Override
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

    @Override
    public void update(MouseInput mouseInput) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if ( color < 0 ) {
            color = 0.0f;
        }

        camera.update();
    }

    @Override
    public void render(Window window, float partialTicks) {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        window.setClearColor(color, color, color, 0.0f);

        batchRenderer.reset();
        batchRenderer.render(window, camera, partialTicks);
        batchRenderer.flush();
    }

    @Override
    public void cleanup() {
        batchRenderer.cleanup();
    }
}
