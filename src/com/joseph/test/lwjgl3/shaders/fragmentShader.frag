#version 400 core

in vec2 texCord;

out vec4 out_Color;

uniform sampler2D texSampler;

void main(void) {
    out_Color = texture(texSampler, texCord);
}
