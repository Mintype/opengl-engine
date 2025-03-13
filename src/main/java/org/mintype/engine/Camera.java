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

        // Normalize yaw (rotation.y) to be within the 0 to 360 degrees range
        if (rotation.y > 360.0f) rotation.y -= 360.0f;
        if (rotation.y < 0.0f) rotation.y += 360.0f;

        // Clamp pitch to prevent flipping
        if (rotation.x > 89.0f) rotation.x = 89.0f;
        if (rotation.x < -89.0f) rotation.x = -89.0f;

        updateViewMatrix(); // Apply changes to view matrix

        printRotation();
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

    // Method to move the camera forward in the direction it's looking
    public void moveForward(float distance) {
//        Vector3f forward = new Vector3f();
//        forward.x = (float) Math.sin(Math.toRadians(rotation.y)) * (float) Math.cos(Math.toRadians(rotation.x));
//        forward.y = (float) Math.sin(Math.toRadians(rotation.x));
//        forward.z = (float) Math.cos(Math.toRadians(rotation.y)) * (float) Math.cos(Math.toRadians(rotation.x));

        // Calculate the forward direction vector based on the current yaw, pitch, and roll
        Vector3f forward = new Vector3f(0, 0, -1);  // Default forward direction (looking along negative Z-axis)

        // Apply rotation (yaw, pitch, roll) to the forward direction vector
        forward = applyRotation(forward, rotation.y, rotation.x, rotation.z);

        // Normalize the forward vector to ensure consistent movement
        forward.normalize().mul(distance);

        // Move the camera position by the forward vector
        position.add(forward);

        // Invert the forward vector to move the camera in the direction it is facing
        forward.negate();

        forward.normalize().mul(distance);
        position.add(forward);
        updateViewMatrix();
    }

    // Method to apply yaw, pitch, and roll rotations to a vector
    public Vector3f applyRotation(Vector3f vector, float yaw, float pitch, float roll) {
        // Apply yaw (around the Y-axis)
        float x = vector.x * (float) Math.cos(yaw) - vector.z * (float) Math.sin(yaw);
        float z = vector.z * (float) Math.cos(yaw) + vector.x * (float) Math.sin(yaw);

        // Apply pitch (around the X-axis)
        float y = vector.y * (float) Math.cos(pitch) - z * (float) Math.sin(pitch);
        z = z * (float) Math.cos(pitch) + vector.y * (float) Math.sin(pitch);

        // Apply roll (around the Z-axis)
        x = x * (float) Math.cos(roll) - y * (float) Math.sin(roll);
        y = y * (float) Math.cos(roll) + x * (float) Math.sin(roll);

        return new Vector3f(x, y, z);
    }


    public void printRotation() {
        // Format the rotation values to 2 decimal places for better readability
        System.out.println("Rotation: [Pitch: " + String.format("%.2f", rotation.x)
                + ", Yaw: " + String.format("%.2f", rotation.y)
                + ", Roll: " + String.format("%.2f", rotation.z) + "]");
    }
}
