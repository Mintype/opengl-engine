package org.mintype.engine;

public class Vertex {
    public static final int SIZE = 3 + 3 + 2; // 3 for position, 3 for color, 2 for texture coordinates
    private float[] position;
    private float[] color;
    private float[] texCoords;  // Texture coordinates will now be a 4-element array for each face

    public Vertex(float[] position, float[] color, float[] texCoords) {
        if (position.length != 3) {
            throw new IllegalArgumentException("Position must be a 3-element array");
        }
        if (color.length != 3) {
            throw new IllegalArgumentException("Color must be a 3-element array");
        }
//        if (texCoords.length != 2) {  // Ensure only 2 texture coordinates are passed
//            throw new IllegalArgumentException("Texture coordinates must be a 2-element array");
//        }
        this.position = position;
        this.color = color;
        this.texCoords = texCoords;
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

    public float[] getTexCoords() {
        return texCoords;
    }

    public void setTexCoords(float[] texCoords) {
        if (texCoords.length != 2) {
            throw new IllegalArgumentException("Texture coordinates must be a 2-element array");
        }
        this.texCoords = texCoords;
    }

    public float[] getElements() {
        float[] elements = new float[SIZE];
        int i = 0;

        // Position coordinates
        elements[i++] = position[0];
        elements[i++] = position[1];
        elements[i++] = position[2];

        // Color coordinates
        elements[i++] = color[0];
        elements[i++] = color[1];
        elements[i++] = color[2];

        // Texture coordinates
        elements[i++] = texCoords[0];
        elements[i++] = texCoords[1];

        return elements;
    }
}
