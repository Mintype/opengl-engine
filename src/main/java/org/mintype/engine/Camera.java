package org.mintype.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;
    private Vector3f position;
    private Vector3f rotation;

    public Camera(float fov, float aspectRatio, float near, float far) {
        position = new Vector3f(0, 0, 3); // Start slightly back
        rotation = new Vector3f(0, 0, 0);

        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(fov), aspectRatio, near, far);
    }

    public void updateViewMatrix() {
        viewMatrix.identity()
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
        updateViewMatrix();
    }

    public void setRotation(Vector3f rotation) {
        this.rotation.set(rotation);
        updateViewMatrix();
    }
}
