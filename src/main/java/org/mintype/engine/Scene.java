package org.mintype.engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public abstract class Scene {
    protected List<Entity> entities;
    protected Camera camera;
    protected Set<Integer> pressedKeys = new HashSet<>();


    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private boolean firstMouse = true;

    public Scene(int windowWidth, int windowHeight) {
        entities = new ArrayList<>();
        camera = new Camera(70, windowWidth, windowHeight, 0.1f, 100.0f);
        init();
    }

    protected abstract void init();

    protected abstract void update();

    public void handleKeyEvent(int key, int action) {
        if (action == GLFW.GLFW_PRESS) {
            pressedKeys.add(key);
            onKeyPress(key);
        } else if (action == GLFW.GLFW_RELEASE) {
            pressedKeys.remove(key);
            onKeyRelease(key);
        }
    }

    // Mouse movement handling
    public void handleMouseMovement(long window) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            // Get the window size
            GLFW.glfwGetWindowSize(window, width, height);

            // Get the current mouse position
            double mouseX = getCursorPosX(window), mouseY = getCursorPosY(window);

            if (firstMouse) {
                lastMouseX = mouseX;
                lastMouseY = mouseY;
                firstMouse = false;
            }

            // Calculate the difference (delta) from the last position
            double deltaX = mouseX - lastMouseX;
            double deltaY = lastMouseY - mouseY; // Reverse the Y-axis since higher mouse Y is lower on screen

            lastMouseX = mouseX;
            lastMouseY = mouseY;

            // Adjust camera rotation based on mouse movement
            camera.processMouseMovement(deltaX, deltaY);
        }
    }

    private static double getCursorPosX(long windowID) {
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(windowID, null, posY);
        return posY.get(0);
    }

    private static double getCursorPosY(long windowID) {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(windowID, posX, null);
        return posX.get(0);
    }

    protected void onKeyPress(int key) {
        // Override in subclasses to define behavior
    }

    protected void onKeyRelease(int key) {
        // Override in subclasses to define behavior
    }

    public boolean isKeyPressed(int key) {
        return pressedKeys.contains(key);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void render() {
        for (Entity entity : entities) {
            entity.render(camera.getViewMatrix(), camera.getProjectionMatrix());
        }
    }

    public void cleanup() {
        for (Entity entity : entities) {
            entity.getMesh().cleanup();
        }
    }

    public Camera getCamera() {
        return camera;
    }
}
