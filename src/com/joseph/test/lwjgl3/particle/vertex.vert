#version 400 core

in vec2 position;

out vec2 texCoordsCur;
out vec2 texCoordsNext;
out float blend;

uniform mat4 projMatrix;
uniform mat4 modelViewMatrix; // combination of tMatrix (model matrix) and view matrix

uniform vec2 texOffsetCur;
uniform vec2 texOffsetNext;
uniform vec2 texCoordInfo; // x == numRows, y == blendFactor

void main(void) {
    vec2 texCoords = position + vec2(0.5, 0.5);
    texCoords.y = 1 - texCoords.y;
    texCoords /= texCoordInfo.x;
    
    texCoordsCur = texCoords + texOffsetCur;
    texCoordsNext = texCoords + texOffsetNext;
    blend = texCoordInfo.y;

    gl_Position = projMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}