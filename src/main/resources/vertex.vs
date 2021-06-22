#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 inColour;

out vec3 exColour;

uniform mat4 cameraMatrix;

void main() {
    gl_Position = cameraMatrix * vec4(position, 1.0);
    exColour = inColour;
}