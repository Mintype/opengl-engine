package org.mintype.engine;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShaderManager {
    private Map<String, Integer> shaders;  // Store compiled shader programs (key = shader name, value = program ID)

    public ShaderManager() {
        shaders = new HashMap<>();
    }

    // Loads and compiles shaders, then links them into a program
    public void loadShader(String shaderName, String vertexShaderFilePath, String fragmentShaderFilePath) {
        // Compile vertex and fragment shaders
        int vertexShader = compileShader(vertexShaderFilePath, GL20.GL_VERTEX_SHADER);
        int fragmentShader = compileShader(fragmentShaderFilePath, GL20.GL_FRAGMENT_SHADER);

        // Link shaders into a program
        int programID = linkProgram(vertexShader, fragmentShader);

        // Store program in map with the given shader name
        shaders.put(shaderName, programID);

        // Clean up individual shaders as they're now part of the program
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
    }

    // Compiles a shader from a file and returns the shader ID
    private int compileShader(String filePath, int shaderType) {
        String shaderSource = readFile(filePath);

        // Create a new shader
        int shaderID = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        // Check for compilation errors
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            String errorMessage = GL20.glGetShaderInfoLog(shaderID);
            System.err.println("Shader Compilation Error: " + errorMessage);
            throw new RuntimeException("Shader compilation failed");
        }

        return shaderID;
    }

    // Links vertex and fragment shaders into a single shader program
    private int linkProgram(int vertexShader, int fragmentShader) {
        // Create a new program
        int programID = GL20.glCreateProgram();

        // Attach shaders
        GL20.glAttachShader(programID, vertexShader);
        GL20.glAttachShader(programID, fragmentShader);

        // Link the program
        GL20.glLinkProgram(programID);

        // Check for linking errors
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
            String errorMessage = GL20.glGetProgramInfoLog(programID);
            System.err.println("Program Linking Error: " + errorMessage);
            throw new RuntimeException("Program linking failed");
        }

        return programID;
    }

    // Reads the content of a file and returns it as a string
    private String readFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading shader file");
        }

        return stringBuilder.toString();
    }

    // Switches to the specified shader program
    public void useShader(String shaderName) {
        Integer programID = shaders.get(shaderName);
        if (programID != null) {
            GL20.glUseProgram(programID);
        } else {
            System.err.println("Shader not found: " + shaderName);
        }
    }

    // Cleans up the shader programs by deleting them from OpenGL
    public void cleanup() {
        for (Integer programID : shaders.values()) {
            GL20.glDeleteProgram(programID);
        }
        shaders.clear();
    }
}
