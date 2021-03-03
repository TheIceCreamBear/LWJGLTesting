#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 texCoord;
out vec3 surfaceNormal;
out vec3 toLight[4];
out vec3 toCam;
out float visibility;
out vec4 shadowCoords;

uniform mat4 tMatrix;
uniform mat4 projMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPos[4];

uniform mat4 shadowMapSpace;

uniform vec4 clipPlane;

// TODO mess with this
const float density = 0.0035;
const float gradient = 5.0;

// must be same as shadowDistance in shadow box, but TUT dude is being lazy (as usual)
const float shadowDistance = 150.0;
const float transitionDistance = 10.0;

void main(void) {
    vec4 worldPos = tMatrix * vec4(position, 1.0);
    vec4 positionRelToCam = viewMatrix * worldPos;
    shadowCoords = shadowMapSpace * worldPos;
    
    gl_ClipDistance[0] = dot(worldPos, clipPlane);

    gl_Position = projMatrix * viewMatrix * tMatrix * vec4(position, 1.0);
    texCoord = textureCoords;
    
    surfaceNormal = (tMatrix * vec4(normal, 0.0)).xyz;
    for (int i = 0; i < 4; i++) {
        toLight[i] = lightPos[i] - worldPos.xyz;
    }
    toCam = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPos.xyz;
    
    float distance = length(positionRelToCam.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
    
    distance = distance - (shadowDistance - transitionDistance);
    distance = distance / transitionDistance;
    shadowCoords.w = clamp(1.0 - distance, 0.0, 1.0);
}
