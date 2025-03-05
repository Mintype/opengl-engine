#version 330 core

in vec3 fragColor; // Color from the vertex shader
out vec4 outColor; // Final color of the fragment

void main() {
    outColor = vec4(fragColor, 1.0f); // Set the output color
}
