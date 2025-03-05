#version 330 core

layout(location = 0) in vec3 inPosition; // Vertex position
layout(location = 1) in vec3 inColor;    // Vertex color

out vec3 fragColor; // Output color to the fragment shader

uniform mat4 modelViewProjection; // Transformation matrix (model, view, projection)

void main() {
    gl_Position = modelViewProjection * vec4(inPosition, 1.0f); // Apply the transformation
    fragColor = inColor; // Pass color to fragment shader
}
