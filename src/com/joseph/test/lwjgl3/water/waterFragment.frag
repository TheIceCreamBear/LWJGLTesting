#version 400 core

in vec4 clipSpace;
in vec2 texCoords;
in vec3 toCam;
in vec3 fromLight;

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;

uniform vec3 lightColor;

uniform float moveFactor;

const float waveStrength = 0.02;
const float shineDamper = 20.0;
const float reflectivity = 0.6;

void main(void) {
    vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
    vec2 refractTexCoord = vec2(ndc.x, ndc.y);
    vec2 reflectTexCoord = vec2(ndc.x, -ndc.y);

    vec2 distortedTexCoords = texture(dudvMap, vec2(texCoords.x + moveFactor, texCoords.y)).rg * 0.1;
    distortedTexCoords = texCoords + vec2(distortedTexCoords.x, distortedTexCoords.y + moveFactor);
    vec2 distortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength;    
    
    refractTexCoord += distortion;
    refractTexCoord = clamp(refractTexCoord, 0.001, 0.999);
    
    reflectTexCoord += distortion;
    reflectTexCoord.x = clamp(reflectTexCoord.x, 0.001, 0.999);
    reflectTexCoord.y = clamp(reflectTexCoord.y, -0.999, -0.001);

    vec4 reflectColor = texture(reflectionTexture, reflectTexCoord);
    vec4 refractColor = texture(refractionTexture, refractTexCoord);
    
    vec3 viewVector = normalize(toCam);
    float refractiveFactor = dot(viewVector, vec3(0.0, 1.0, 0.0));
    refractiveFactor = pow(refractiveFactor, 0.5);
    
    vec4 normalMapColor = texture(normalMap, distortedTexCoords);
    vec3 normal = vec3(normalMapColor.r * 2.0 - 1.0, normalMapColor.b, normalMapColor.g * 2.0 - 1.0);
    normal = normalize(normal);
    
    vec3 reflectedLight = reflect(normalize(fromLight), normal);
    float specular = max(dot(reflectedLight, viewVector), 0.0);
    specular = pow(specular, shineDamper);
    vec3 specularHighlights = lightColor * specular * reflectivity;
    
	out_Color = mix(reflectColor, refractColor, refractiveFactor);
	out_Color = mix(out_Color, vec4(0.0, 0.3, 0.5, 1.0), 0.2) + vec4(specularHighlights, 0.0);
}