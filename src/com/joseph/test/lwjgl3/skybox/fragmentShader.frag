#version 400

in vec3 texCord;

out vec4 out_Color;

uniform samplerCube cubeMap;

void main(void) {
    out_Color = texture(cubeMap, texCord);
}