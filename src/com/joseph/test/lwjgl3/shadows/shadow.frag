#version 400 core

in vec2 textureCoords;

out vec4 color;

uniform sampler2D modelTexture;

void main(void) {
    float alpha = texture(modelTexture, textureCoords).a;
    if (alpha < 0.5) {
        discard;
    }

	color = vec4(1.0);
}