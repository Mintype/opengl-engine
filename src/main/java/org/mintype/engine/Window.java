package org.mintype.engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private long window;
    private int width, height;
    private String title;
    private Scene scene;

    public Window(int width, int height, String title, Scene scene) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.scene = scene;
    }

    public void init() {
        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the window
        window = glfwCreateWindow(width, height, title, 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Center the window
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                window,
                (vidMode.width() - width) / 2,
                (vidMode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable v-sync
        glfwShowWindow(window);

        // Initialize OpenGL bindings
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
    }

    public void loop() {
        // Main game loop
        while (!glfwWindowShouldClose(window)) {
            // Poll events
            glfwPollEvents();

            // Update the scene
            scene.update();

            // Clear the screen
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Render the scene
            scene.render();

            // Swap buffers
            glfwSwapBuffers(window);
        }
    }

    public void cleanup() {
        scene.cleanup();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }

    public long getWindowHandle() {
        return window;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
