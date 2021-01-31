#version 400 core

in vec2 position;

out vec4 clipSpace;
out vec2 texCoords;

uniform mat4 projMatrix;
uniform mat4 viewMatrix;
uniform mat4 tMatrix;

const float tiling = 6.0;

void main(void) {
	clipSpace = projMatrix * viewMatrix * tMatrix * vec4(position.x, 0.0, position.y, 1.0);
	gl_Position = clipSpace;
	texCoords = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * tiling;
}