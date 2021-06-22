package xyz.przemyk.lwjglgame.game;

import xyz.przemyk.lwjglgame.engine.IGameLogic;
import xyz.przemyk.lwjglgame.engine.Window;
import xyz.przemyk.lwjglgame.engine.graph.Mesh;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class DummyGame implements IGameLogic {

    private int direction = 0;

    private float color = 0.0f;

    private final Renderer renderer;

    private Mesh mesh;

    private Camera camera;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        float[] vertices = new float[]{
                -0.5f,  0.5f, -1.05f, 0.0f, 1.0f,
                -0.5f, -0.5f, -1.05f, 0.0f, 0.0f,
                0.5f, -0.5f, -1.05f, 1.0f, 0.0f,
                0.5f,  0.5f, -1.05f, 1.0f, 1.0f
        };
//        float[] colours = new float[]{
//                0.5f, 0.0f, 0.0f,
//                0.0f, 0.5f, 0.0f,
//                0.0f, 0.0f, 0.5f,
//                0.0f, 0.5f, 0.5f,
//        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };
        mesh = new Mesh(vertices, indices);
    }

    @Override
    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_UP) ) {
            direction = 1;
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {
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
    }

    @Override
    public void update(float interval) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if ( color < 0 ) {
            color = 0.0f;
        }

        camera.update();
    }

    @Override
    public void render(Window window) {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, mesh, camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        mesh.cleanup();
    }
}
