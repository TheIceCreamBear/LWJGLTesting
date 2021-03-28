#version 400 core

in vec3 position;

out vec3 pass_color;

void main(void) {
    gl_Position = vec4(position, 1.0);
    pass_color = vec3(1.0);
}