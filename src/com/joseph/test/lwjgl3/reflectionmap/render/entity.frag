#version 400 core

out vec4 out_Color;

in vec2 texCoords;
in vec3 fragNormal;
in vec3 reflectedVector;
in vec3 refractedVector;

uniform sampler2D modelTexture;
uniform samplerCube envMap;

const vec3 lightDir = normalize(vec3(0.2, -1.0, 0.3));
const float ambient = 0.3;

void main(void) {
    float brightness = max(dot(-lightDir, normalize(fragNormal)), ambient);
    out_Color = texture(modelTexture, texCoords) * brightness;
    
    vec4 reflectedColor = texture(envMap, reflectedVector);
    vec4 refractedColor = texture(envMap, refractedVector);
    
    vec4 envrioColor = mix(reflectedColor, refractedColor, 0.5);
    
    out_Color = mix(out_Color, envrioColor, 0.6);
}