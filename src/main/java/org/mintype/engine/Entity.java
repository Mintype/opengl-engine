package org.mintype.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Entity {
    private Mesh mesh;
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;
    private String name;

    public Entity(Mesh mesh) {
        this(mesh, "unnamed entity");
    }

    public Entity(Mesh mesh, String name) {
        this.mesh = mesh;
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = new Vector3f(1, 1, 1);
        this.name = name;
    }

    public Matrix4f getModelMatrix() {
        return new Matrix4f()
                .identity()
                .translate(position)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .scale(scale);
    }

    public void render(Matrix4f viewMatrix, Matrix4f projectionMatrix) {
        mesh.render(getModelMatrix(), viewMatrix, projectionMatrix);
    }

    // Rotate on Y-axis over time or user input
    public void rotate(float deltaY) {
        this.rotation.y += deltaY; // Increase rotation around the Y-axis
        if (this.rotation.y > 360) this.rotation.y -= 360; // Keep it within 0-360 degrees
    }

    public Mesh getMesh() {
        return mesh;
    }

    // Getters & Setters for position, rotation, scale
    public Vector3f getPosition() { return position; }
    public void setPosition(Vector3f position) { this.position.set(position); }

    public Vector3f getRotation() { return rotation; }
    public void setRotation(Vector3f rotation) { this.rotation.set(rotation); }

    public Vector3f getScale() { return scale; }
    public void setScale(Vector3f scale) { this.scale.set(scale); }
}
