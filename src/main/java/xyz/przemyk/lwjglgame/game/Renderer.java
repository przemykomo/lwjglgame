package xyz.przemyk.lwjglgame.game;

import org.joml.Matrix4f;
import xyz.przemyk.lwjglgame.engine.Utils;
import xyz.przemyk.lwjglgame.engine.Window;
import xyz.przemyk.lwjglgame.engine.graph.Mesh;
import xyz.przemyk.lwjglgame.engine.graph.ShaderProgram;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private ShaderProgram shaderProgram;
    private Matrix4f projectionMatrix;

    public void init(Window window) throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("fragment.fs"));
        shaderProgram.link();

        float aspectRatio = (float) window.getWidth() / window.getHeight();
        projectionMatrix = new Matrix4f();
        projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
        shaderProgram.createUniform("cameraMatrix");
        shaderProgram.bind();
        shaderProgram.setUniform("cameraMatrix", projectionMatrix);
        shaderProgram.unbind();
    }

    public void render(Window window, Mesh mesh, Camera camera) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);

            float aspectRatio = (float) window.getWidth() / window.getHeight();
            projectionMatrix.identity().setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
        }

        shaderProgram.bind();
        Matrix4f cameraMatrix = new Matrix4f().rotateXYZ((float) Math.toRadians(camera.angleXDegrees), (float) Math.toRadians(camera.angleYDegrees), 0)
                .translate(-camera.position.x, -camera.position.y, -camera.position.z);
        projectionMatrix.mul(cameraMatrix, cameraMatrix);
        shaderProgram.setUniform("cameraMatrix", cameraMatrix);

        glBindVertexArray(mesh.getVaoId());
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
