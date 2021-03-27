#version 400 core

in vec3 position;

out vec3 pass_color;

uniform mat4 projectionViewMatrix;

void main(void) {
    gl_PointSize = 2;
    gl_Position = projectionViewMatrix * vec4(position, 1.0);
    
    pass_color = vec3(1.0);
}