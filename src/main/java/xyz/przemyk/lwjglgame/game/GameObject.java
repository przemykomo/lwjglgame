package xyz.przemyk.lwjglgame.game;

import org.joml.Matrix4fStack;
import org.joml.Vector3f;

public class GameObject {
    public Vector3f position = new Vector3f();
    public Vector3f previousPosition = new Vector3f();
    public float angleXDegrees;
    public float angleYDegrees;
    public float prevAngleXDegrees;
    public float prevAngleYDegrees;

    public void update() {
        previousPosition.set(position);
        prevAngleXDegrees = angleXDegrees;
        prevAngleYDegrees = angleYDegrees;
    }

    public void moveRelative(float right, float forward, float up) {
        position.x += Math.sin(Math.toRadians(angleYDegrees)) * forward;
        position.z += Math.cos(Math.toRadians(angleYDegrees)) * -forward;

        position.x += Math.cos(Math.toRadians(angleYDegrees)) * right;
        position.z += Math.sin(Math.toRadians(angleYDegrees)) * right;

        position.y += up;
    }

    public void render(BatchRenderer renderer, Matrix4fStack matrix4fStack, float partialTicks) {
        matrix4fStack.pushMatrix();
        Vector3f lerpedPos = new Vector3f();
        previousPosition.lerp(position, partialTicks, lerpedPos);
        matrix4fStack.translate(lerpedPos)
                .rotateXYZ(
                (float) Math.toRadians(Math.fma(angleXDegrees - prevAngleXDegrees, partialTicks, prevAngleXDegrees)),
                (float) Math.toRadians(Math.fma(angleYDegrees - prevAngleYDegrees, partialTicks, prevAngleYDegrees)),
                0.0f);
        Model.SQUARE.render(renderer, matrix4fStack);
        matrix4fStack.popMatrix();
    }
}
