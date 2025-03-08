package org.mintype.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;
    private Vector3f position;
    private Vector3f rotation;
    private float aspectRatio, fov, near, far;

    public Camera(float fov, int windowWidth, int windowHeight, float near, float far) {
        this.position = new Vector3f(0, 0, 3); // Start slightly back
        this.rotation = new Vector3f(0, 0, 0);

        this.aspectRatio = (float) windowWidth / windowHeight;
        this.fov = fov;
        this.near = near;
        this.far = far;

        this.viewMatrix = new Matrix4f();
        updateProjectionMatrix(fov, near, far);
    }

    // Method to update camera based on mouse movement
    public void processMouseMovement(double deltaX, double deltaY) {
        float sensitivity = 0.1f; // Adjust sensitivity as needed

        // Update rotation values
        rotation.y += deltaY * sensitivity; // Yaw (left/right)
        rotation.x -= deltaX * sensitivity; // Pitch (up/down)

        // Clamp pitch to prevent flipping
        if (rotation.x > 89.0f) rotation.x = 89.0f;
        if (rotation.x < -89.0f) rotation.x = -89.0f;

        updateViewMatrix(); // Apply changes to view matrix
    }

    private void updateProjectionMatrix(float fov, float near, float far) {
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

    public Vector3f getPosition() {
        return position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation.set(rotation);
        updateViewMatrix();
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setAspectRatio(int windowWidth, int windowHeight) {
        this.aspectRatio = (float) windowWidth / windowHeight;
        updateProjectionMatrix(fov, near, far);
    }
}
