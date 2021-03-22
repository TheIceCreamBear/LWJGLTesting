#version 400 core

in vec2 texCoord;
in vec3 surfaceNormal;
in vec3 toLight[4];
in vec3 toCam;
in vec4 shadowCoords;
in float visibility;

out vec4 out_Color;

uniform sampler2D texSampler;
uniform sampler2D entityShadowMap;
uniform sampler2D terrainShadowMap;
uniform sampler2D specularMap;

uniform float usesSpecularMap;
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLight = 0.2;
uniform float isLightSource = 0.0;
uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform vec3 skyColor;

const int pcfCount = 2;
const float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);
// I realy reccomment that you load this up as a uniform variable so that if you ever change
// it in the java code it will change in the shaders as well, but im being lazy here - TUT dude
const float mapSize = 4096.0;
const float shadowBias = 0.002;

const float levels = 4;
const bool celShading = false;

void main(void) {
    float texelSize = 1.0 / mapSize;
    float totalE = 0.0;
    float totalT = 0.0;
    
    for (int x = -pcfCount; x <= pcfCount; x++) {
        for (int y = -pcfCount; y <= pcfCount; y++) {
            float objectNearestLight = texture(entityShadowMap, shadowCoords.xy + vec2(x, y) * texelSize).r;
            if (shadowCoords.z > objectNearestLight + shadowBias) {
                totalE += 1.0;
            }
            objectNearestLight = texture(terrainShadowMap, shadowCoords.xy + vec2(x, y) * texelSize).r;
            if (shadowCoords.z > objectNearestLight) {
                totalT += 1.0;
            }
        }
    }
    float total = clamp((totalE + totalT) / (totalTexels), 0.0, 1.0);
    float lightFactor = 1.0 - (total * shadowCoords.w);
    
    vec3 normal = normalize(surfaceNormal);
    vec3 cam = normalize(toCam);
    
    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);
    
    for (int i = 0; i < 4; i++) {
        float distance = length(toLight[i]);
        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
	    vec3 light = normalize(toLight[i]);
	    vec3 lightDir = -light;
	    
	    float dotProd = dot(normal, light);
	    float brightness = max(dotProd, 0.0);
	    
	    if (isLightSource > 0.0) {
	       brightness = max(abs(dotProd), 0.0);
	    }
	    
	    if (celShading) {
	       float level = floor(brightness * levels);
	       brightness = level / levels;
	    }
	    
	    vec3 reflected = reflect(lightDir, normal);
	    float specularFactor = dot(reflected, cam);
	    specularFactor = max(specularFactor, 0.0);
	    float dampedFactor = pow(specularFactor, shineDamper);
	    
	    if (celShading) {
           float level = floor(dampedFactor * levels);
           dampedFactor = level / levels;
	    }
	    
	    totalDiffuse += (brightness * lightColor[i]) / attFactor;
        totalSpecular += (dampedFactor * reflectivity * lightColor[i]) / attFactor;
    }
    
    totalDiffuse = max(totalDiffuse * lightFactor, ambientLight);
    
    vec4 texColor = texture(texSampler, texCoord);
    if (texColor.a < 0.5) {
        discard;
    }
    
    if (usesSpecularMap > 0.5) {
        vec4 mapInfo = texture(specularMap, texCoord);
        totalSpecular *= mapInfo.r;
        if (mapInfo.g > 0.5) {
            totalDiffuse = vec3(1.0);
        }
    }
    
    out_Color = vec4(totalDiffuse, 1.0) * texColor + vec4(totalSpecular * lightFactor, 1.0);
    out_Color = mix(vec4(skyColor,1.0), out_Color, visibility);
}
