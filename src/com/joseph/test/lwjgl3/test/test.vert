#version 400 core

in vec3 position;

uniform mat4 projectionView;

void main(void) {
    gl_Position = projectionView * vec4(position, 1.0);
}