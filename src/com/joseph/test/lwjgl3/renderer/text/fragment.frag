#version 400 core

in vec2 texCoords;

out vec4 out_Color;

uniform vec3 color;
uniform sampler2D atlas;

// TODO make uniform, allows for better values per font size, and allows for control, def make this uniform
const float width = 0.5;
const float edge = 0.1;

// outlined
const float borderWidth = 0.7;
const float borderEdge = 0.1;

// glowy
// const float borderWidth = 0.4;
// const float borderEdge = 0.5;

const vec2 shadowOffset = vec2(0.006, 0.006);

const vec3 outlineColor = vec3(0.0, 0.0, 0.0);

void main(void) {
    float dist = 1.0 - texture(atlas, texCoords).a;
    float alpha = 1.0 - smoothstep(width, width + edge, dist);
    
    float distBor = 1.0 - texture(atlas, texCoords + shadowOffset).a;
    float alphaBor = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distBor);
    
    // uncomment this to use the effects
    // float totalAlpha = alpha + (1.0 - alpha) * alphaBor;
    // vec3 totalColor = mix(outlineColor, color, alpha / totalAlpha);
    
    float totalAlpha = alpha;
    vec3 totalColor = color;
    
    out_Color = vec4(totalColor, totalAlpha);
}