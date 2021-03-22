#version 400 core

in vec4 clipSpace;
in vec2 texCoords;
in vec3 toCam;
in vec3 fromLight;

layout (location = 0) out vec4 out_Color;
layout (location = 1) out vec4 out_BrightColor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;

uniform vec3 lightColor;

uniform float moveFactor;

const float waveStrength = 0.04;
const float shineDamper = 20.0;
const float reflectivity = 0.5;

void main(void) {
    vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
    vec2 refractTexCoord = vec2(ndc.x, ndc.y);
    vec2 reflectTexCoord = vec2(ndc.x, -ndc.y);
    
    float near = 0.1;
    float far = 1000.0; // should be uniforms but the tut dude didnt care
    float depth = texture(depthMap, refractTexCoord).r;
    // also tut dude said here, have this conversion, you can learn from the links in the description
    // https://stackoverflow.com/questions/6652253/getting-the-true-z-value-from-the-depth-buffer
    float floorDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
    
    depth = gl_FragCoord.z;
    float waterDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
    float waterDepth = floorDistance - waterDistance;

    // distort
    vec2 distortedTexCoords = texture(dudvMap, vec2(texCoords.x + moveFactor, texCoords.y)).rg * 0.1;
    distortedTexCoords = texCoords + vec2(distortedTexCoords.x, distortedTexCoords.y + moveFactor);
    vec2 distortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength * clamp(waterDepth / 20, 0.0, 1.0);    
    
    refractTexCoord += distortion;
    refractTexCoord = clamp(refractTexCoord, 0.001, 0.999);
    
    reflectTexCoord += distortion;
    reflectTexCoord.x = clamp(reflectTexCoord.x, 0.001, 0.999);
    reflectTexCoord.y = clamp(reflectTexCoord.y, -0.999, -0.001);

    vec4 reflectColor = texture(reflectionTexture, reflectTexCoord);
    vec4 refractColor = texture(refractionTexture, refractTexCoord);
    
    // normal
    vec4 normalMapColor = texture(normalMap, distortedTexCoords);
    vec3 normal = vec3(normalMapColor.r * 2.0 - 1.0, normalMapColor.b * 2.0, normalMapColor.g * 2.0 - 1.0); // .b * 2.0 makes normal more vertical
    normal = normalize(normal);
    
    // fresnel
    vec3 viewVector = normalize(toCam);
    float refractiveFactor = dot(viewVector, normal);
    refractiveFactor = clamp(pow(refractiveFactor, 0.5), 0.0, 1.0);
    
    // specular
    vec3 reflectedLight = reflect(normalize(fromLight), normal);
    float specular = max(dot(reflectedLight, viewVector), 0.0);
    specular = pow(specular, shineDamper);
    vec3 specularHighlights = lightColor * specular * reflectivity * clamp(waterDepth / 5, 0.0, 1.0);
    
    // output
	out_Color = mix(reflectColor, refractColor, refractiveFactor);
	out_Color = mix(out_Color, vec4(0.0, 0.3, 0.5, 1.0), 0.2) + vec4(specularHighlights, 0.0);
	out_Color.a = clamp(waterDepth / 5, 0.0, 1.0);
    out_BrightColor = vec4(0.0);
}