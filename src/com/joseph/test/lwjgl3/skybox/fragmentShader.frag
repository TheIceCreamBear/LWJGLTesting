#version 400

in vec3 texCoord;

out vec4 out_Color;

uniform samplerCube cubeMap;
uniform samplerCube cubeMap2;
uniform float blendFactor;
uniform vec3 fogColor;

const float lowerLimit = 0.0;
const float upperLimit = 30.0;

void main(void) {
    vec4 tex1 = texture(cubeMap, texCoord);
    vec4 tex2 = texture(cubeMap2, texCoord);
    vec4 finalColor = mix(tex1, tex2, blendFactor);
    
    float factor = (texCoord.y - lowerLimit) / (upperLimit - lowerLimit);
    factor = clamp(factor, 0.0, 1.0);
    out_Color = mix(vec4(fogColor, 1.0), finalColor, factor);
}