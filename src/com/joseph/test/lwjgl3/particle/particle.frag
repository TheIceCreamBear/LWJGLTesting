#version 400 core

in vec2 texCoordsCur;
in vec2 texCoordsNext;
in float blend;

out vec4 out_Color;

uniform sampler2D atlas;

void main(void) {
    vec4 colorCur = texture(atlas, texCoordsCur);
    vec4 colorNext = texture(atlas, texCoordsNext);

    out_Color = mix(colorCur, colorNext, blend);
}