#version 400 core

layout (points) in;

layout (line_strip, max_vertices = 2) out;

uniform mat4 projectionViewMatrix;
uniform vec3 offsetDir;
uniform int radius;

void main(void) {
    vec4 offset = vec4(offsetDir * 2 * radius, 0.0);
    vec4 worldPos = gl_in[0].gl_Position;
    
    // emit first vertex
    gl_Position = projectionViewMatrix * worldPos;
    EmitVertex();
    
    // emit second vertex
    gl_Position = projectionViewMatrix * (worldPos + offset);
    EmitVertex();
}