#version 400 core

in vec4 clipSpace;
in vec2 texCoords;
in vec3 toCam;

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;

uniform float moveFactor;

const float waveStrength = 0.02;

void main(void) {
    vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
    vec2 refractTexCoord = vec2(ndc.x, ndc.y);
    vec2 reflectTexCoord = vec2(ndc.x, -ndc.y);
    
    vec2 distortion1 = (texture(dudvMap, vec2(texCoords.x + moveFactor, texCoords.y)).rg * 2.0 - 1.0) * waveStrength;
    vec2 distortion2 = (texture(dudvMap, vec2(-texCoords.x + moveFactor, texCoords.y + moveFactor)).rg * 2.0 - 1.0) * waveStrength;
    vec2 distortion = distortion1 + distortion2;
    
    refractTexCoord += distortion;
    refractTexCoord = clamp(refractTexCoord, 0.001, 0.999);
    
    reflectTexCoord += distortion;
    reflectTexCoord.x = clamp(reflectTexCoord.x, 0.001, 0.999);
    reflectTexCoord.y = clamp(reflectTexCoord.y, -0.999, -0.001);

    vec4 reflectColor = texture(reflectionTexture, reflectTexCoord);
    vec4 refractColor = texture(refractionTexture, refractTexCoord);
    
    vec3 viewVector = normalize(toCam);
    float refractiveFactor = dot(viewVector, vec3(0.0, 1.0, 0.0));
    
	out_Color = mix(reflectColor, refractColor, refractiveFactor);
	out_Color = mix(out_Color, vec4(0.0, 0.3, 0.5, 1.0), 0.2);
}