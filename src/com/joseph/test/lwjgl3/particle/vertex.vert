#version 400 core

in vec2 position;

uniform mat4 projMatrix;
uniform mat4 viewMatrix;

void main(void) {
    gl_Position = projMatix * viewMatrix * vec4(position, 0.0, 1.0);
}