#version 400 core

out vec4 out_Color;

in vec2 texCoords;
in vec3 fragNormal;

uniform sampler2D modelTexture;

const vec3 lightDir = normalize(vec3(0.2, -1.0, 0.3));
const float ambient = 0.3;

void main(void) {
    float brightness = max(dot(-lightDir, normalize(fragNormal)), ambient);
    out_Color = texture(modelTexture, texCoords) * brightness;
}