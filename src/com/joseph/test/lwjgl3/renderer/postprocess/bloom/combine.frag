#version 150

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D colorTexture;
uniform sampler2D highlightTexture;

const float highlightIntensity = 1.0;

void main(void) {
    vec4 scene = texture(colorTexture, textureCoords);
    vec4 highlight = texture(highlightTexture, textureCoords);
    out_Color = scene + highlight * highlightIntensity;
}