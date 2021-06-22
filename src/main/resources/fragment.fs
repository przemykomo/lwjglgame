#version 330

in vec3 exColour;

out vec4 fragColor;

uniform sampler2D aTexture;

void main() {
    fragColor = texture(aTexture, exColour.xy);
}