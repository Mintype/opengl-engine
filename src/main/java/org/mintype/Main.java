package org.mintype;

import org.mintype.engine.Mesh;
import org.mintype.engine.Scene;
import org.mintype.engine.Vertex;
import org.mintype.engine.Window;
import org.mintype.engine.ShaderProgram;

public class Main {
    public static void main(String[] args) throws Exception {
        Scene scene = new Scene();

        Window window = new Window(800, 600, "LWJGL Window with Scene", scene);
        window.init();  // Initializes OpenGL

        // Load the shaders
        ShaderProgram shaderProgram = new ShaderProgram("src/main/resources/shaders/vertex_shader.glsl", "src/main/resources/shaders/fragment_shader.glsl");

        // Define some vertices with position and color
        Vertex[] vertices = new Vertex[]{
                new Vertex(new float[]{-0.5f, -0.5f, 0.0f}, new float[]{1.0f, 0.0f, 0.0f}),  // Red
                new Vertex(new float[]{ 0.5f, -0.5f, 0.0f}, new float[]{0.0f, 1.0f, 0.0f}),  // Green
                new Vertex(new float[]{ 0.0f,  0.5f, 0.0f}, new float[]{0.0f, 0.0f, 1.0f})   // Blue
        };

        // Define indices for drawing the triangle
        int[] indices = new int[]{0, 1, 2};

        // Create the mesh and pass the shader program to it
        Mesh mesh = new Mesh(vertices, indices, shaderProgram);

        // Add the mesh to the scene
        scene.addMesh(mesh);

        window.loop();
        window.cleanup();
    }
}
