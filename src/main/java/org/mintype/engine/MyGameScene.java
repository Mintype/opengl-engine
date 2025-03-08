package org.mintype.engine;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class MyGameScene extends Scene {

    private float speed = 0.05f, sensitivity = 1f;

    public MyGameScene(int windowWidth, int windowHeight) {
        super(windowWidth, windowHeight);
    }

    @Override
    protected void init() {
        // Initialize entities, camera position, etc.
    }

    @Override
    protected void update() {
        camera.updateViewMatrix();

        cameraMovement();

//        for (Entity entity : entities) {
//            entity.rotate(1, 1, 0);
//        }
    }

    private void cameraMovement() {
        if (isKeyPressed(GLFW.GLFW_KEY_W)) {
            camera.setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z - speed));
        }
        if (isKeyPressed(GLFW.GLFW_KEY_S)) {
            camera.setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z + speed));
        }
        if (isKeyPressed(GLFW.GLFW_KEY_A)) {
            camera.setPosition(new Vector3f(camera.getPosition().x - speed, camera.getPosition().y, camera.getPosition().z));
        }
        if (isKeyPressed(GLFW.GLFW_KEY_D)) {
            camera.setPosition(new Vector3f(camera.getPosition().x + speed, camera.getPosition().y, camera.getPosition().z));
        }
        if (isKeyPressed(GLFW.GLFW_KEY_E)) {
            camera.setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y + speed, camera.getPosition().z));
        }
        if (isKeyPressed(GLFW.GLFW_KEY_Q)) {
            camera.setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y - speed, camera.getPosition().z));
        }

        if (isKeyPressed(GLFW.GLFW_KEY_G)) {
            camera.setRotation(new Vector3f(camera.getRotation().x + sensitivity, camera.getRotation().y, camera.getRotation().z));
        }
        if (isKeyPressed(GLFW.GLFW_KEY_F)) {
            camera.setRotation(new Vector3f(camera.getRotation().x - sensitivity, camera.getRotation().y, camera.getRotation().z));
        }
    }

    @Override
    protected void onKeyPress(int key) {
        if (key == GLFW.GLFW_KEY_SPACE) {
            System.out.println("Jump!");
        }
    }

    @Override
    protected void onKeyRelease(int key) {
        if (key == GLFW.GLFW_KEY_SPACE) {
            System.out.println("Jump key released.");
        }
    }

    public static void main(String[] args) {

    }
}
