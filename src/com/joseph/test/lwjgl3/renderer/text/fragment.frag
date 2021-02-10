#version 400 core

in vec2 texCoords;

out vec4 out_Color;

uniform vec3 color;
uniform sampler2D atlas;

void main(void) {
    out_Color = vec4(color, texture(atlas, texCoords).a);
}