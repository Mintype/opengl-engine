package org.mintype.engine;

import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int vaoId;
    private int vertexCount;

    public Mesh(Vertex[] vertices, int[] indices) {
        this.vertexCount = indices.length;

        // Create the VAO (Vertex Array Object)
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create VBO (Vertex Buffer Object)
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        // Load vertex data into buffer
        FloatBuffer vertexBuffer = MemoryStack.stackMallocFloat(vertices.length * Vertex.SIZE);
        for (Vertex vertex : vertices) {
            vertexBuffer.put(vertex.getPosition());
        }
        vertexBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Set the vertex attributes (assuming positions are 3 floats)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Create EBO (Element Buffer Object) for indices
        int eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glBindVertexArray(0); // Unbind VAO
    }

    public void render() {
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDeleteVertexArrays(vaoId);
    }
}
