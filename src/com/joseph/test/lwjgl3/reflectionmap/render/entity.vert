#version 400 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec3 fragNormal;
out vec2 texCoords;
out vec3 reflectedVector;

uniform mat4 transMatrix;
uniform mat4 projMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPosition;

void main(void) {
    vec4 worldPosition = transMatrix * vec4(position, 1.0);
    gl_Position = projMatrix * viewMatrix * worldPosition;
    
    texCoords = textureCoordinates;
    fragNormal = normal;
    vec3 unitNormal = normalize(normal);
    
    vec3 viewVector = normalize(worldPosition.xyz - cameraPosition);
    reflectedVector = reflect(viewVector, unitNormal);
}