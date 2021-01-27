#version 400 core

in vec2 position;

out vec2 texCoord;

uniform mat4 projMatrix;
uniform mat4 viewMatrix;
uniform mat4 tMatrix;

void main(void) {
	gl_Position = projMatrix * viewMatrix * tMatrix * vec4(position.x, 0.0, position.y, 1.0);
	texCoord = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5);
}