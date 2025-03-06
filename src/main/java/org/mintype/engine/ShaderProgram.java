package org.mintype.engine;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private final int programId;

    public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Failed to create OpenGL shader program.");
        }

        // Load, compile, and attach shaders
        int vertexShaderId = createShader(vertexShaderPath, GL_VERTEX_SHADER);
        int fragmentShaderId = createShader(fragmentShaderPath, GL_FRAGMENT_SHADER);

        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        glLinkProgram(programId);

        // Check for linking errors
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking shader program: " + glGetProgramInfoLog(programId));
        }

        // Shaders are linked, can be deleted
        glDetachShader(programId, vertexShaderId);
        glDetachShader(programId, fragmentShaderId);
        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
    }

    private int createShader(String filePath, int shaderType) {
        String shaderSource = loadShaderSource(filePath);
        int shaderId = glCreateShader(shaderType);

        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);

        // Check for compilation errors
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling shader at " + filePath + ":\n" + glGetShaderInfoLog(shaderId));
        }
        return shaderId;
    }

    private String loadShaderSource(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader file: " + filePath, e);
        }
    }

    public void use() {
        glUseProgram(programId);
    }

    public void setUniform(String name, Matrix4f matrix) {
        int location = glGetUniformLocation(programId, name);
        if (location != -1) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer buffer = stack.mallocFloat(16);
                matrix.get(buffer);
                glUniformMatrix4fv(location, false, buffer);
            }
        } else {
            System.err.println("Warning: Uniform '" + name + "' not found in shader!");
        }
    }

    public void cleanup() {
        glDeleteProgram(programId);
    }
}
