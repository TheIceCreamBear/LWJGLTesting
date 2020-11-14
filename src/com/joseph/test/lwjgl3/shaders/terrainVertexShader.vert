#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 texCord;
out vec3 surfaceNormal;
out vec3 toLight;
out vec3 toCam;
out float visibility;

uniform mat4 tMatrix;
uniform mat4 projMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPos;

// TODO mess with this
const float density = 0.007;
const float gradient = 1.5;

void main(void) {
    vec4 worldPos = tMatrix * vec4(position, 1.0);
    vec4 positionRelToCam = viewMatrix * worldPos;

    gl_Position = projMatrix * viewMatrix * tMatrix * vec4(position, 1.0);
    texCord = textureCoords * 40;
    
    surfaceNormal = (tMatrix * vec4(normal, 0.0)).xyz;
    toLight = lightPos - worldPos.xyz;
    toCam = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPos.xyz;
    
    float distance = length(positionRelToCam.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}
