#version 400 core

in vec2 position;

out vec4 clipSpace;
out vec2 texCoords;
out vec3 toCam;
out vec3 fromLight;

uniform mat4 projMatrix;
uniform mat4 viewMatrix;
uniform mat4 tMatrix;
uniform vec3 lightPos;

uniform vec3 camPos;

const float tiling = 4.0;

void main(void) {
    vec4 worldPosition = tMatrix * vec4(position.x, 0.0, position.y, 1.0);

	clipSpace = projMatrix * viewMatrix * worldPosition;
	gl_Position = clipSpace;
	texCoords = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * tiling;
	
	toCam = camPos - worldPosition.xyz;
	
	fromLight = worldPosition.syx - lightPos;
}