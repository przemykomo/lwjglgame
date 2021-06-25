package xyz.przemyk.lwjglgame.game;

import org.joml.Matrix4fStack;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Model {

    public static final Model CUBE = new Model(new Model.Vertex[] {
            //front +z
            new Model.Vertex(new Vector3f(-0.5f,  0.5f, 0.5f), new Vector2f(0.0f, 1.0f), new Vector3f(0.0f, 0.0f, 1.0f)),
            new Model.Vertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector2f(0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f)),
            new Model.Vertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector2f(1.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f)),
            new Model.Vertex(new Vector3f(0.5f,  0.5f, 0.5f), new Vector2f(1.0f, 1.0f), new Vector3f(0.0f, 0.0f, 1.0f)),

            //right +x
            new Model.Vertex(new Vector3f(0.5f,  0.5f, -0.5f), new Vector2f(0.0f, 1.0f), new Vector3f(1.0f, 0.0f, 0.0f)),
            new Model.Vertex(new Vector3f(0.5f, -0.5f, -0.5f), new Vector2f(0.0f, 0.0f), new Vector3f(1.0f, 0.0f, 0.0f)),
            new Model.Vertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector2f(1.0f, 0.0f), new Vector3f(1.0f, 0.0f, 0.0f)),
            new Model.Vertex(new Vector3f(0.5f,  0.5f, 0.5f), new Vector2f(1.0f, 1.0f), new Vector3f(1.0f, 0.0f, 0.0f)),

            //back -z
            new Model.Vertex(new Vector3f(0.5f,  0.5f, -0.5f), new Vector2f(0.0f, 1.0f), new Vector3f(0.0f, 0.0f, -1.0f)),
            new Model.Vertex(new Vector3f(0.5f, -0.5f, -0.5f), new Vector2f(0.0f, 0.0f), new Vector3f(0.0f, 0.0f, -1.0f)),
            new Model.Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector2f(1.0f, 0.0f), new Vector3f(0.0f, 0.0f, -1.0f)),
            new Model.Vertex(new Vector3f(-0.5f,  0.5f, -0.5f), new Vector2f(1.0f, 1.0f), new Vector3f(0.0f, 0.0f, -1.0f)),

            //left -x
            new Model.Vertex(new Vector3f(-0.5f,  0.5f, -0.5f), new Vector2f(0.0f, 1.0f), new Vector3f(-1.0f, 0.0f, 0.0f)),
            new Model.Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector2f(0.0f, 0.0f), new Vector3f(-1.0f, 0.0f, 0.0f)),
            new Model.Vertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector2f(1.0f, 0.0f), new Vector3f(-1.0f, 0.0f, 0.0f)),
            new Model.Vertex(new Vector3f(-0.5f,  0.5f, 0.5f), new Vector2f(1.0f, 1.0f), new Vector3f(-1.0f, 0.0f, 0.0f)),

            //up +y
            new Model.Vertex(new Vector3f(-0.5f,  0.5f, 0.5f), new Vector2f(0.0f, 1.0f), new Vector3f(0.0f, 1.0f, 0.0f)),
            new Model.Vertex(new Vector3f(-0.5f, 0.5f, -0.5f), new Vector2f(0.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f)),
            new Model.Vertex(new Vector3f(0.5f, 0.5f, -0.5f), new Vector2f(1.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f)),
            new Model.Vertex(new Vector3f(0.5f,  0.5f, 0.5f), new Vector2f(1.0f, 1.0f), new Vector3f(0.0f, 1.0f, 0.0f)),

            //down -y
            new Model.Vertex(new Vector3f(-0.5f,  -0.5f, -0.5f), new Vector2f(0.0f, 1.0f), new Vector3f(0.0f, -1.0f, 0.0f)),
            new Model.Vertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector2f(0.0f, 0.0f), new Vector3f(0.0f, -1.0f, 0.0f)),
            new Model.Vertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector2f(1.0f, 0.0f), new Vector3f(0.0f, -1.0f, 0.0f)),
            new Model.Vertex(new Vector3f(0.5f,  -0.5f, -0.5f), new Vector2f(1.0f, 1.0f), new Vector3f(0.0f, -1.0f, 0.0f))

    }, new int[] {
        0, 1, 3, 3, 1, 2,
        4, 5, 7, 7, 5, 6,
        8, 9, 11, 11, 9, 10,
        12, 13, 15, 15, 13, 14,
        16, 17, 19, 19, 17, 18,
        20, 21, 23, 23, 21, 22
    });

    public Vertex[] vertices;
    public int[] indices;

    public Model(Vertex[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }

    public static class Vertex {
        public Vector3f pos;
        public Vector2f texturePos;
        public Vector3f normal;

        public Vertex(Vector3f pos, Vector2f texturePos, Vector3f normal) {
            this.pos = pos;
            this.texturePos = texturePos;
            this.normal = normal;
        }
    }

    public void render(BatchRenderer renderer, Matrix4fStack matrix4fStack) {
        float[] verticesArray = new float[vertices.length * 8];

        Vector4f temp = new Vector4f();
        for (int i = 0; i < vertices.length; i++) {
            int j = i * 8;
            Vertex vertex = vertices[i];
            temp.set(vertex.pos, 1.0f).mul(matrix4fStack);
            verticesArray[j] = temp.x;
            verticesArray[j + 1] = temp.y;
            verticesArray[j + 2] = temp.z;

            verticesArray[j + 3] = vertex.texturePos.x;
            verticesArray[j + 4] = vertex.texturePos.y;

            temp.set(vertex.normal, 0.0f).mul(matrix4fStack);

            verticesArray[j + 5] = temp.x;
            verticesArray[j + 6] = temp.y;
            verticesArray[j + 7] = temp.z;
        }

        renderer.addModel(verticesArray, indices);
    }
}
