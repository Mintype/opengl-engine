package org.mintype.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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
            if (isInFrustum(entity)) { // Only render if inside the frustum
                entity.render(camera.getViewMatrix(), camera.getProjectionMatrix());
            }
        }
    }

    private boolean isInFrustum(Entity entity) {
        // Get View-Projection Matrix
        Matrix4f vpMatrix = new Matrix4f();
        camera.getProjectionMatrix().mul(camera.getViewMatrix(), vpMatrix);

        // Get AABB Corners
        Vector3f min = entity.getMinBounds(); // Bottom-left-back corner
        Vector3f max = entity.getMaxBounds(); // Top-right-front corner

        // Create 8 corners of the AABB
        Vector3f[] corners = {
                new Vector3f(min.x, min.y, min.z), new Vector3f(min.x, min.y, max.z),
                new Vector3f(min.x, max.y, min.z), new Vector3f(min.x, max.y, max.z),
                new Vector3f(max.x, min.y, min.z), new Vector3f(max.x, min.y, max.z),
                new Vector3f(max.x, max.y, min.z), new Vector3f(max.x, max.y, max.z)
        };

        // Check if ANY corner is inside the frustum
        for (Vector3f corner : corners) {
            Vector4f clipPos = new Vector4f(corner, 1.0f).mul(vpMatrix);
            if (!(clipPos.x < -clipPos.w || clipPos.x > clipPos.w ||
                    clipPos.y < -clipPos.w || clipPos.y > clipPos.w ||
                    clipPos.z < -clipPos.w || clipPos.z > clipPos.w)) {
                return true; // If any corner is inside, render the entity
            }
        }

        return false; // If all corners are outside, skip rendering
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
