#version 400 core

in vec2 position;
in vec2 textureCoords;

out vec2 texCoords;

uniform vec2 translation;

void main(void) {
    texCoords = textureCoords;
    gl_Position = vec4(position + (translation * vec2(2.0, -2.0)), 0.0, 1.0);
}