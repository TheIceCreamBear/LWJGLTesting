#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 texCord;
out vec3 surfaceNormal;
out vec3 toLight[4];
out vec3 toCam;
out float visibility;

uniform mat4 tMatrix;
uniform mat4 projMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPos[4];

uniform float useFakeLight;

uniform float numRows;
uniform vec2 offset;

// TODO mess with this
const float density = 0.0035;
const float gradient = 5.0;

void main(void) {
    vec4 worldPos = tMatrix * vec4(position, 1.0);
    vec4 positionRelToCam = viewMatrix * worldPos;

    gl_Position = projMatrix * viewMatrix * tMatrix * vec4(position, 1.0);
    texCord = (textureCoords / numRows) + offset;
    
    vec3 displayNormal = normal;
    if (useFakeLight > 0.5) {
        displayNormal = vec3(0.0, 1.0, 0.0);
    }
    
    surfaceNormal = (tMatrix * vec4(displayNormal, 0.0)).xyz;
    for (int i = 0; i < 4; i++) {
        toLight[i] = lightPos[i] - worldPos.xyz;
    }
    toCam = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPos.xyz;
    
    float distance = length(positionRelToCam.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}
