#version 140

in vec2 texCoords;

out vec4 out_Color;

uniform sampler2D guiTex;

void main(void) {
    out_Color = texture(guiTex, texCoords);
}