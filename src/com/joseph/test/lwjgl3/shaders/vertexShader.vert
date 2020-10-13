#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec2 texCord;

uniform mat4 tMatrix;
uniform mat4 projMatrix;
uniform mat4 viewMatrix;

void main(void) {
    gl_Position = projMatrix * viewMatrix * tMatrix * vec4(position, 1.0);
    texCord = textureCoords;
}
