#version 330 core

in vec3 vertexColor;
in vec2 fragTexCoords;

out vec4 fragColor;

uniform sampler2D textureSampler;

void main() {
    fragColor = texture(textureSampler, fragTexCoords) * vec4(vertexColor, 1.0);
}