#version 400 core

in vec2 position;
in mat4 modelViewMatrix; // combination of tMatrix (model matrix) and view matrix
in vec4 texOffsets;
in float blendFactor;


out vec2 texCoordsCur;
out vec2 texCoordsNext;
out float blend;

uniform mat4 projMatrix;
uniform float numRows;

void main(void) {
    vec2 texCoords = position + vec2(0.5, 0.5);
    texCoords.y = 1 - texCoords.y;
    texCoords /= numRows;
    
    texCoordsCur = texCoords + texOffsets.xy;
    texCoordsNext = texCoords + texOffsets.zw;
    blend = blendFactor;

    gl_Position = projMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}