package org.mintype.engine;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int vaoId;
    private int vboId;
    private int eboId;
    private int vertexCount;
    private ShaderProgram shaderProgram;
    private Texture texture;

    public Mesh(Vertex[] vertices, int[] indices, ShaderProgram shaderProgram, Texture texture) {
        this.shaderProgram = shaderProgram;
        this.texture = texture;
        this.vertexCount = indices.length;

        // Create the VAO (Vertex Array Object)
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create VBO (Vertex Buffer Object) for vertex data (position, color, texCoords)
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        // Load vertex data (position, color, texCoords) into buffer
        FloatBuffer vertexBuffer = MemoryStack.stackMallocFloat(vertices.length * Vertex.SIZE);
        for (Vertex vertex : vertices) {
            vertexBuffer.put(vertex.getElements());
        }
        vertexBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Set the vertex attributes (Position attribute)
        int stride = Vertex.SIZE * Float.BYTES;
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        // Set the color attribute
        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // Set the texture coordinates attribute
        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        // Create EBO (Element Buffer Object) for indices:
        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        IntBuffer indexBuffer = MemoryStack.stackMallocInt(indices.length);
        indexBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        glBindVertexArray(0); // Unbind VAO
    }

    public void render(Matrix4f modelMatrix, Matrix4f viewMatrix, Matrix4f projectionMatrix) {
        shaderProgram.use();
        shaderProgram.setUniform("modelMatrix", modelMatrix);
        shaderProgram.setUniform("viewMatrix", viewMatrix);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        texture.bind();
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDeleteBuffers(vboId);
        glDeleteBuffers(eboId);
        glDeleteVertexArrays(vaoId);
        texture.cleanup();
        shaderProgram.cleanup();
    }
}