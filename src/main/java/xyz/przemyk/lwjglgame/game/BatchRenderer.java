package xyz.przemyk.lwjglgame.game;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import xyz.przemyk.lwjglgame.engine.Utils;
import xyz.przemyk.lwjglgame.engine.Window;
import xyz.przemyk.lwjglgame.engine.graph.ShaderProgram;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

// idea: maybe implement batch rendering natively?
// std::vector is a lot easier to work with than what I'm doing here...
// Maybe it would even be faster?
public class BatchRenderer {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private final int maxBufferCount = 512;
    private final int floatPerVertex = 8;
    private final float[] vertices = new float[maxBufferCount * floatPerVertex];
    private int verticesSize = 0;
    private final int[] indices = new int[maxBufferCount];
    private int indicesSize = 0;
    public final ShaderProgram shaderProgram;
    public final Matrix4f modelViewMatrix;
    public final Matrix4f projectionMatrix;
    private final int vaoId;
    private final int vboId;
    private final int indexVboId;
    private final Vector3f lerpedCameraPos = new Vector3f();

    public BatchRenderer(Window window) throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("fragment.fs"));
        shaderProgram.link();
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("projectionMatrix");
        modelViewMatrix = new Matrix4f();
        float aspectRatio = (float) window.getWidth() / window.getHeight();
        projectionMatrix = new Matrix4f();
        projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
        shaderProgram.bind();
        shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        shaderProgram.unbind();

        vaoId = glGenVertexArrays();
        vboId = glGenBuffers();
        indexVboId = glGenBuffers();

        glBindVertexArray(vaoId);

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_STREAM_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, (long) indices.length * Integer.BYTES, GL_STREAM_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, floatPerVertex * Float.BYTES, 0);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, floatPerVertex * Float.BYTES, 3 * Float.BYTES);

        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, floatPerVertex * Float.BYTES, 5 * Float.BYTES);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        int texture = glGenTextures(); //TODO: dynamic texture atlas
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer byteBuffer = stack.bytes(Utils.loadImageResource("block.png"));
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 16, 16, 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        verticesSize = 0;
        indicesSize = 0;
    }

    public void addModel(float[] modelVertices, int[] modelIndices) {
        if ((verticesSize + modelVertices.length) / floatPerVertex > maxBufferCount || indicesSize + modelIndices.length > maxBufferCount) {
            flush();
        }

        int pos = verticesSize / floatPerVertex;
        System.arraycopy(modelVertices, 0, vertices, verticesSize, modelVertices.length);
        verticesSize += modelVertices.length;

        for (int modelIndex : modelIndices) {
            indices[indicesSize++] = modelIndex + pos;
        }
    }

    public void flush() {
        if (verticesSize == 0) {
            return;
        }

        glBindVertexArray(vaoId);

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indices);

        shaderProgram.bind();
        glDrawElements(GL_TRIANGLES, indicesSize, GL_UNSIGNED_INT, 0);
        shaderProgram.unbind();

        reset();
    }

    public void cleanup() {
        shaderProgram.cleanup();

        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(indexVboId);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void render(Window window, Camera camera, float partialTicks) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);

            float aspectRatio = (float) window.getWidth() / window.getHeight();
            projectionMatrix.identity().setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        }

        camera.previousPosition.lerp(camera.position, partialTicks, lerpedCameraPos);

        shaderProgram.bind();
        modelViewMatrix.identity().rotateXYZ((float) Math.toRadians(camera.angleXDegrees), (float) Math.toRadians(camera.angleYDegrees), 0)
                .translate(-lerpedCameraPos.x, -lerpedCameraPos.y, -lerpedCameraPos.z);
        shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        shaderProgram.unbind();

        float[] vertices = new float[] {
                //vec3 position, vec2 texPos, vec3 vertexNormal
                -0.5f,  0.5f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f,  0.5f, -1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f
        };
        int[] indices = new int[] {
                0, 1, 3, 3, 1, 2
        };

        addModel(vertices, indices);

        flush();
    }
}
