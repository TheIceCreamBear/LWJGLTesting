#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec3 tangent;

out vec2 texCoord;
out vec3 toLight[4];
out vec3 toCam;
out float visibility;

uniform mat4 tMatrix;
uniform mat4 projMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPositionEyeSpace[4];

uniform float numberOfRows;
uniform vec2 offset;

uniform vec4 plane;

const float density = 0.0035;
const float gradient = 5.0;

void main(void) {
	vec4 worldPosition = tMatrix * vec4(position,1.0);
	mat4 modelViewMatrix = viewMatrix * tMatrix; // i dont like you
	vec4 positionRelativeToCam = modelViewMatrix * vec4(position, 1.0);
	
	gl_ClipDistance[0] = dot(worldPosition, plane);
	
	gl_Position = projMatrix * positionRelativeToCam;
	texCoord = (textureCoords / numberOfRows) + offset;
	
	vec3 surfaceNormal = (modelViewMatrix * vec4(normal, 0.0)).xyz;
	
	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize((modelViewMatrix * vec4(tangent, 0.0)).xyz);
	vec3 bitang = cross(norm, tang);
	
	mat3 toTangentSpace = mat3(
	   tang.x, bitang.x, norm.x,
       tang.y, bitang.y, norm.y,
       tang.z, bitang.z, norm.z
	);
	
	for (int i = 0; i < 4; i++) {
		toLight[i] = toTangentSpace * (lightPositionEyeSpace[i] - positionRelativeToCam.xyz);
	}
	toCam = toTangentSpace * (-positionRelativeToCam.xyz);
	
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}
