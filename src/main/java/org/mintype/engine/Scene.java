package org.mintype.engine;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Entity> entities;
    private Camera camera;

    public Scene(int windowWidth, int windowHeight) {
        entities = new ArrayList<>();
        camera = new Camera(70, windowWidth, windowHeight, 0.1f, 100.0f);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void update() {
        camera.updateViewMatrix(); // Update camera before rendering
        // Future logic (e.g., animation, physics)

        for (Entity entity : entities) {
            entity.rotate(1, 1, 0);
        }
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
