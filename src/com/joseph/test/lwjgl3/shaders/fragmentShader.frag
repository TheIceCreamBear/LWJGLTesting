#version 400 core

in vec2 texCoord;
in vec3 surfaceNormal;
in vec3 toLight[4];
in vec3 toCam;
in float visibility;

out vec4 out_Color;

uniform sampler2D texSampler;

uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform vec3 skyColor;
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLight = 0.2;
uniform float isLightSource = 0.0;

void main(void) {
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
	    
	    vec3 reflected = reflect(lightDir, normal);
	    float specularFactor = dot(reflected, cam);
	    specularFactor = max(specularFactor, 0.0);
	    float dampedFactor = pow(specularFactor, shineDamper);
	    totalDiffuse += (brightness * lightColor[i]) / attFactor;
        totalSpecular += (dampedFactor * reflectivity * lightColor[i]) / attFactor;
    }
    
    totalDiffuse = max(totalDiffuse, ambientLight);
    
    vec4 texColor = texture(texSampler, texCoord);
    if (texColor.a < 0.5) {
        discard;
    }
    
    out_Color = vec4(totalDiffuse, 1.0) * texColor + vec4(totalSpecular, 1.0);
    out_Color = mix(vec4(skyColor,1.0), out_Color, visibility);
}
