#version 400 core

out vec4 out_Color;

uniform vec3 color = vec3(0.6);

void main(void) {
    out_Color = vec4(color, 1.0);
}