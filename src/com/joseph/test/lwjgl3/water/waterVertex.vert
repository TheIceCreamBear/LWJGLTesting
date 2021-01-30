#version 400 core

in vec2 position;

out vec4 clipSpace;

uniform mat4 projMatrix;
uniform mat4 viewMatrix;
uniform mat4 tMatrix;

void main(void) {
	clipSpace = projMatrix * viewMatrix * tMatrix * vec4(position.x, 0.0, position.y, 1.0);
	gl_Position = clipSpace;
}