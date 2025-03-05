package org.mintype.engine;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ShaderProgram {
    private final int programId;

    public ShaderProgram(String vertexPath, String fragmentPath) throws Exception {
        String vertexSource = new String(Files.readAllBytes(Paths.get(vertexPath)));
        String fragmentSource = new String(Files.readAllBytes(Paths.get(fragmentPath)));

        int vertexShader = compileShader(GL20.GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = compileShader(GL20.GL_FRAGMENT_SHADER, fragmentSource);

        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShader);
        GL20.glAttachShader(programId, fragmentShader);
        GL20.glLinkProgram(programId);

        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Failed to link shader program: " + GL20.glGetProgramInfoLog(programId));
        }

        GL20.glValidateProgram(programId);
    }

    private int compileShader(int type, String source) {
        int shaderId = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderId, source);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Failed to compile shader: " + GL20.glGetShaderInfoLog(shaderId));
        }

        return shaderId;
    }

    public void use() {
        GL20.glUseProgram(programId);
    }

    public void setUniform(String name, float[] matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            int location = GL20.glGetUniformLocation(programId, name);
            if (location != -1) {
                GL20.glUniformMatrix4fv(location, false, matrix);
            }
        }
    }

    public void cleanup() {
        GL20.glDeleteProgram(programId);
    }
}
