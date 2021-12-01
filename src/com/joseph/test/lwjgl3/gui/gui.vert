#version 140

in vec2 position;

out vec2 texCoords;

uniform mat4 tMatrix;

void main(void) {
    gl_Position = tMatrix * vec4(position, 0.0, 1.0);
    texCoords = vec2((position.x + 1.0) / 2.0, 1.0 - (position.y + 1.0) / 2.0);
}