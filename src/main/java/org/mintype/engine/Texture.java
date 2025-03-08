package org.mintype.engine;

import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final int id;
    private final int width;
    private final int height;

    public Texture(String filePath) {
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Load image
        ByteBuffer image;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            image = stbi_load(filePath, w, h, channels, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load texture file: " + filePath + "\n" + stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        }

        // Upload texture to GPU
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        glGenerateMipmap(GL_TEXTURE_2D);

        // Free image memory
        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void cleanup() {
        glDeleteTextures(id);
    }

    // New method for extracting texture coordinates from a grid
    public float[] getTextureCoordinates(int row, int col, int gridRows, int gridCols) {
        // Each tile is 16x16 pixels
        float tileWidth = 16.0f / 64.0f; // 16 pixels out of 64 pixels (width of each tile)
        float tileHeight = 16.0f / 48.0f; // 16 pixels out of 48 pixels (height of each tile)

        // Calculate the start and end points of the tile
        float xStart = col * tileWidth;
        float yStart = row * tileHeight;
        float xEnd = xStart + tileWidth;
        float yEnd = yStart + tileHeight;

        if(row == 0 && col == 1) {
            // Print the calculated texture coordinates for debugging
            System.out.printf("Texture Coordinates for Row %d, Col %d:\n", row, col);
            System.out.printf("Start (x, y): (%.3f, %.3f)\n", xStart, yStart);
            System.out.printf("End (x, y): (%.3f, %.3f)\n", xEnd, yEnd);
            System.out.println("----------------------------");
        }

        // Return the texture coordinates for the specified tile
        return new float[]{xStart, yStart, xEnd, yStart, xEnd, yEnd, xStart, yEnd};
    }


}
