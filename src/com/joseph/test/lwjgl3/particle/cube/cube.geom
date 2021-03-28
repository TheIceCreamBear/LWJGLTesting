#version 400 core

layout (points) in;

layout (triangle_strip, max_vertices = 24) out;

in vec3 pass_color[];

out vec3 finalColor;

uniform mat4 projectionViewMatrix;

const float size = 0.1;
const vec3 lightDirection = normalize(vec3(0.4, -1.0, 0.8));

void createVertex(vec3 offset, vec3 faceNormal) {
    vec4 actualOffset = vec4(offset * size, 0.0);
    vec4 worldPos = gl_in[0].gl_Position + actualOffset;
    gl_Position = projectionViewMatrix * worldPos;

    float brightness = max(dot(-lightDirection, faceNormal), 0.3);    
    finalColor = pass_color[0] * brightness;
    
    // tells geom shader to move to next vertex
    EmitVertex();
}

void main(void) {
    vec3 faceNormal = vec3(0.0, 0.0, 1.0);
    createVertex(vec3(-1.0, 1.0, 1.0), faceNormal);
    createVertex(vec3(-1.0, -1.0, 1.0), faceNormal);
    createVertex(vec3(1.0, 1.0, 1.0), faceNormal);
    createVertex(vec3(1.0, -1.0, 1.0), faceNormal);
    
    EndPrimitive();
    
    faceNormal = vec3(1.0, 0.0, 0.0);
    createVertex(vec3(1.0, 1.0, 1.0), faceNormal);
    createVertex(vec3(1.0, -1.0, 1.0), faceNormal);
    createVertex(vec3(1.0, 1.0, -1.0), faceNormal);
    createVertex(vec3(1.0, -1.0, -1.0), faceNormal);
    
    EndPrimitive();
    
    faceNormal = vec3(0.0, 0.0, -1.0);
    createVertex(vec3(1.0, 1.0, -1.0), faceNormal);
    createVertex(vec3(1.0, -1.0, -1.0), faceNormal);
    createVertex(vec3(-1.0, 1.0, -1.0), faceNormal);
    createVertex(vec3(-1.0, -1.0, -1.0), faceNormal);
    
    EndPrimitive();
    
    faceNormal = vec3(-1.0, 0.0, 0.0);
    createVertex(vec3(-1.0, 1.0, -1.0), faceNormal);
    createVertex(vec3(-1.0, -1.0, -1.0), faceNormal);
    createVertex(vec3(-1.0, 1.0, 1.0), faceNormal);
    createVertex(vec3(-1.0, -1.0, 1.0), faceNormal);
    
    EndPrimitive();
    
    faceNormal = vec3(0.0, 1.0, 0.0);
    createVertex(vec3(1.0, 1.0, 1.0), faceNormal);
    createVertex(vec3(1.0, 1.0, -1.0), faceNormal);
    createVertex(vec3(-1.0, 1.0, 1.0), faceNormal);
    createVertex(vec3(-1.0, 1.0, -1.0), faceNormal);
    
    EndPrimitive();
    
    faceNormal = vec3(0.0, -1.0, 0.0);
    createVertex(vec3(-1.0, -1.0, 1.0), faceNormal);
    createVertex(vec3(-1.0, -1.0, -1.0), faceNormal);
    createVertex(vec3(1.0, -1.0, 1.0), faceNormal);
    createVertex(vec3(1.0, -1.0, -1.0), faceNormal);
    
    EndPrimitive();
}