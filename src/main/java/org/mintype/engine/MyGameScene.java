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

    private static Mesh createCubeMesh(ShaderProgram shaderProgram, Texture texture) {
        int gridRows = 3;  // 3 rows (top, sides, bottom)
        int gridCols = 4;  // 4 columns (side1, side2, side3, side4)

        float[] frontTextureCoords = texture.getTextureCoordinates(1, 0, gridRows, gridCols);
        float[] backTextureCoords = texture.getTextureCoordinates(1, 1, gridRows, gridCols);
        float[] leftTextureCoords = texture.getTextureCoordinates(1, 2, gridRows, gridCols);
        float[] rightTextureCoords = texture.getTextureCoordinates(1, 3, gridRows, gridCols);

        float[] topTextureCoords = texture.getTextureCoordinates(0, 1, gridRows, gridCols);
        float[] bottomTextureCoords = texture.getTextureCoordinates(2, 1, gridRows, gridCols);

        // Define vertices for a cube with texture coordinates
        Vertex[] vertices = new Vertex[]{
                // Front face (using side 1 from the texture)
                new Vertex(new float[]{-0.5f, -0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{frontTextureCoords[0], frontTextureCoords[7]}),
                new Vertex(new float[]{ 0.5f, -0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{frontTextureCoords[2], frontTextureCoords[5]}),
                new Vertex(new float[]{ 0.5f,  0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{frontTextureCoords[4], frontTextureCoords[3]}),
                new Vertex(new float[]{-0.5f,  0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{frontTextureCoords[6], frontTextureCoords[1]}),

                // Back face (using side 2 from the texture)
                new Vertex(new float[]{-0.5f, -0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{backTextureCoords[0], backTextureCoords[7]}),
                new Vertex(new float[]{ 0.5f, -0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{backTextureCoords[2], backTextureCoords[5]}),
                new Vertex(new float[]{ 0.5f,  0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{backTextureCoords[4], backTextureCoords[3]}),
                new Vertex(new float[]{-0.5f,  0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{backTextureCoords[6], backTextureCoords[1]}),

                // Top face (using top row of the texture)
                new Vertex(new float[]{-0.5f,  0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{topTextureCoords[0], topTextureCoords[1]}),
                new Vertex(new float[]{ 0.5f,  0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{topTextureCoords[2], topTextureCoords[3]}),
                new Vertex(new float[]{ 0.5f,  0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{topTextureCoords[4], topTextureCoords[5]}),
                new Vertex(new float[]{-0.5f,  0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{topTextureCoords[6], topTextureCoords[7]}),

                // Bottom face (using bottom row of the texture)
                new Vertex(new float[]{-0.5f, -0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{bottomTextureCoords[0], bottomTextureCoords[1]}),
                new Vertex(new float[]{ 0.5f, -0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{bottomTextureCoords[2], bottomTextureCoords[3]}),
                new Vertex(new float[]{ 0.5f, -0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{bottomTextureCoords[4], bottomTextureCoords[5]}),
                new Vertex(new float[]{-0.5f, -0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{bottomTextureCoords[6], bottomTextureCoords[7]}),

                // Left face (using side 3 from the texture)
                new Vertex(new float[]{-0.5f, -0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{leftTextureCoords[0], leftTextureCoords[7]}),
                new Vertex(new float[]{-0.5f, -0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{leftTextureCoords[2], leftTextureCoords[5]}),
                new Vertex(new float[]{-0.5f,  0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{leftTextureCoords[4], leftTextureCoords[3]}),
                new Vertex(new float[]{-0.5f,  0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{leftTextureCoords[6], leftTextureCoords[1]}),

                // Right face (using side 4 from the texture)
                new Vertex(new float[]{ 0.5f, -0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{rightTextureCoords[0], rightTextureCoords[7]}),
                new Vertex(new float[]{ 0.5f, -0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{rightTextureCoords[2], rightTextureCoords[5]}),
                new Vertex(new float[]{ 0.5f,  0.5f,  0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{rightTextureCoords[4], rightTextureCoords[3]}),
                new Vertex(new float[]{ 0.5f,  0.5f, -0.5f}, new float[]{1.0f, 1.0f, 1.0f}, new float[]{rightTextureCoords[6], rightTextureCoords[1]})
        };

        // Define indices for drawing the cube
        int[] indices = new int[]{
                // Front face
                0, 1, 2, 2, 3, 0,
                // Back face
                4, 5, 6, 6, 7, 4,
                // Top face
                8, 9, 10, 10, 11, 8,
                // Bottom face
                12, 13, 14, 14, 15, 12,
                // Left face
                16, 17, 18, 18, 19, 16,
                // Right face
                20, 21, 22, 22, 23, 20
        };


        // Create the mesh and pass the shader program to it
        return new Mesh(vertices, indices, shaderProgram, texture);
    }

    public static void main(String[] args) {
        Scene scene = new MyGameScene(800, 600);

        Engine window = new Engine(800, 600, "LWJGL Engine with Scene", scene);
        window.init();  // Initializes OpenGL

        // Load the shaders
        ShaderProgram shaderProgram = new ShaderProgram("src/main/resources/shaders/vertex_shader.glsl", "src/main/resources/shaders/fragment_shader.glsl");

        // Load the texture
        Texture texture = new Texture("src/main/resources/textures/grass_block.png");

        Mesh cubeMesh = createCubeMesh(shaderProgram, texture); // Create one cube mesh

        int cubeSize = 20;
        for (int x = 0; x < cubeSize; x++) {
            for (int y = 0; y < cubeSize; y++) {
                for (int z = 0; z < cubeSize; z++) {
                    Entity entity = new Entity(cubeMesh, new Vector3f((float)x, (float)y, (float)z));
                    scene.addEntity(entity);
                }
            }
        }


        window.loop();
        window.cleanup();
    }
}
