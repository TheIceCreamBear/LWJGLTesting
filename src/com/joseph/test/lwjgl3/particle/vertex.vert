#version 400 core

in vec2 position;

uniform mat4 projMatrix;
uniform mat4 modelViewMatrix; // combination of tMatrix (model matrix) and view matrix

void main(void) {
    gl_Position = projMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}