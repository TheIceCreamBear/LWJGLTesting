#version 400 core

in vec3 finalColor;

out vec4 out_Color;

void main(void) {
    out_Color = vec4(finalColor, 1.0);
}