#version 400 core

in vec2 texCord;
in vec3 surfaceNormal;
in vec3 toLight;
in vec3 toCam;

out vec4 out_Color;

uniform sampler2D texSampler;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

void main(void) {
    vec3 normal = normalize(surfaceNormal);
    vec3 light = normalize(toLight);
    vec3 lightDir = -light;
    vec3 cam = normalize(toCam);
    
    float dotProd = dot(normal, light);
    float brightness = max(dotProd, 0.0);
    vec3 diffuse = brightness * lightColor;
    
    vec3 reflected = reflect(lightDir, normal);
    float specularFactor = dot(reflected, cam);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 specular = dampedFactor * reflectivity * lightColor;
    
    out_Color = vec4(diffuse, 1.0) * texture(texSampler, texCord) + vec4(specular, 1.0);
}
