#version 400 core

in vec2 texCord;
in vec3 surfaceNormal;
in vec3 toLight;

out vec4 out_Color;

uniform sampler2D texSampler;

uniform vec3 lightColor;

void main(void) {
    vec3 normal = normalize(surfaceNormal);
    vec3 light = normalize(toLight);
    
    float dotProd = dot(normal, light);
    float brightness = max(dotProd, 0.0);
    vec3 diffuse = brightness * lightColor;
    
    out_Color = vec4(diffuse, 1.0) * texture(texSampler, texCord);
}
