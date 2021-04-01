#version 400 core

in vec3 position;

out vec3 texCoords;

uniform mat4 projViewMat;

void main(void) {
    gl_Position = projViewMat * vec4(position, 1.0);
    texCoords = position;
}