#version 400 core

out vec4 out_Color;

in vec2 texCoords;
in vec3 fragNormal;
in vec3 reflectedVector;

uniform sampler2D modelTexture;
uniform samplerCube envMap;

const vec3 lightDir = normalize(vec3(0.2, -1.0, 0.3));
const float ambient = 0.3;

void main(void) {
    float brightness = max(dot(-lightDir, normalize(fragNormal)), ambient);
    out_Color = texture(modelTexture, texCoords) * brightness;
    
    vec4 reflectedColor = texture(envMap, reflectedVector);
    out_Color = mix(out_Color, reflectedColor, 0.6);
}