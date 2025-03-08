package org.mintype.engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine {
    private long window;
    private int width, height;
    private String title;
    private Scene currentScene;
    private double lastTime;
    private int fps;
    private double deltaTime;

    public Engine(int width, int height, String title, Scene scene) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.currentScene = scene;
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

        // Enable Depth Testing
        glEnable(GL_DEPTH_TEST);

        GLFW.glfwSetFramebufferSizeCallback(window, (win, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
            glViewport(0, 0, newWidth, newHeight);
        });

        GLFW.glfwSetKeyCallback(window, (windowHandle, key, scancode, action, mods) -> {
            if (currentScene != null) {
                currentScene.handleKeyEvent(key, action);
            }
        });

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        lastTime = glfwGetTime();
        fps = 0;
    }

    public void loop() {
        double frameCounter = 0;
        int frames = 0;

        while (!glfwWindowShouldClose(window)) {
            double currentTime = glfwGetTime();
            deltaTime = currentTime - lastTime;
            lastTime = currentTime;
            frameCounter += deltaTime;
            frames++;

            if (frameCounter >= 1.0) {
                fps = frames;
                glfwSetWindowTitle(window, title + " | FPS: " + fps);
                frames = 0;
                frameCounter = 0;
            }

            glfwPollEvents();
            currentScene.handleMouseMovement(window);
//            currentScene.update(deltaTime);
            currentScene.update();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            currentScene.render();

            glfwSwapBuffers(window);
        }
    }

    public void cleanup() {
        currentScene.cleanup();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }

    public long getWindowHandle() {
        return window;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public double getDeltaTime() {
        return deltaTime;
    }
}
