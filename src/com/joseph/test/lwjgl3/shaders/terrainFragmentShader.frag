#version 400 core

in vec2 texCoord;
in vec3 surfaceNormal;
in vec3 toLight[4];
in vec3 toCam;
in float visibility;

out vec4 out_Color;

uniform sampler2D blendMap;
uniform sampler2D baseTex;
uniform sampler2D rTex;
uniform sampler2D gTex;
uniform sampler2D bTex;

uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform vec3 skyColor;
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLight = 0.2;

void main(void) {
    vec4 blendColor = texture(blendMap, texCoord);
    
    float baseAmount = 1 - (blendColor.r + blendColor.g + blendColor.b);
    vec2 tiledCoords = texCoord * 40;
    vec4 baseColor = texture(baseTex, tiledCoords) * baseAmount;
    vec4 rColor = texture(rTex, tiledCoords) * blendColor.r;
    vec4 gColor = texture(gTex, tiledCoords) * blendColor.g;
    vec4 bColor = texture(bTex, tiledCoords) * blendColor.b;
    
    vec4 totalColor = baseColor + rColor + gColor + bColor;

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
	    float brightness = max(dotProd, ambientLight);
	    
	    vec3 reflected = reflect(lightDir, normal);
	    float specularFactor = dot(reflected, cam);
	    specularFactor = max(specularFactor, 0.0);
	    float dampedFactor = pow(specularFactor, shineDamper);
        totalDiffuse += (brightness * lightColor[i]) / attFactor;
        totalSpecular += (dampedFactor * reflectivity * lightColor[i]) / attFactor;
    }
    
    totalDiffuse = max(totalDiffuse, ambientLight);
    
    out_Color = vec4(totalDiffuse, 1.0) * totalColor + vec4(totalSpecular, 1.0);
    out_Color = mix(vec4(skyColor,1.0), out_Color, visibility);
}
