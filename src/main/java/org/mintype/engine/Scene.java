package org.mintype.engine;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Mesh> meshes;

    public Scene() {
        meshes = new ArrayList<>();
    }

    public void addMesh(Mesh mesh) {
        meshes.add(mesh);
    }

    public void update() {
        // Update logic (e.g., game objects, transformations, etc.)
    }

    public void render() {
        // Render all meshes
        for (Mesh mesh : meshes) {
            mesh.render();
        }
    }

    public void cleanup() {
        for (Mesh mesh : meshes) {
            mesh.cleanup();
        }
    }
}
