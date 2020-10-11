#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec2 texCord;

uniform mat4 transformation;

void main(void) {
    gl_Position = transformation * vec4(position, 1.0);
    texCord = textureCoords;
}