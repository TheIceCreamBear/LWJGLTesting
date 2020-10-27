#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 texCord;
out vec3 surfaceNormal;
out vec3 toLight;
out vec3 toCam;

uniform mat4 tMatrix;
uniform mat4 projMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPos;

void main(void) {
    vec4 worldPos = tMatrix * vec4(position, 1.0);

    gl_Position = projMatrix * viewMatrix * tMatrix * vec4(position, 1.0);
    texCord = textureCoords * 40;
    
    surfaceNormal = (tMatrix * vec4(normal, 0.0)).xyz;
    toLight = lightPos - worldPos.xyz;
    toCam = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPos.xyz;
}
