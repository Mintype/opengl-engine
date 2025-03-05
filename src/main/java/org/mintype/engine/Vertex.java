package org.mintype.engine;

public class Vertex {
    public static final int SIZE = 3; // Assuming 3 floats for position (x, y, z)
    private float[] position;
    private float[] color;

    public Vertex(float[] position, float[] color) {
        if (position.length != 3) {
            throw new IllegalArgumentException("Position must be a 3-element array");
        }
        if (color.length != 3) {
            throw new IllegalArgumentException("Color must be a 3-element array");
        }
        this.position = position;
        this.color = color;
    }

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        if (position.length != 3) {
            throw new IllegalArgumentException("Position must be a 3-element array");
        }
        this.position = position;
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        if (color.length != 3) {
            throw new IllegalArgumentException("Color must be a 3-element array");
        }
        this.color = color;
    }

    public float[] getElements() {
        float[] elements = new float[6];
        System.arraycopy(position, 0, elements, 0, 3);
        System.arraycopy(color, 0, elements, 3, 3);
        return elements;
    }
}