#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texPos;
layout (location = 2) in vec3 vertexNormal;

out vec2 exTexPos;
out vec3 exVertexNormal;
out vec3 exVertexPos;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main() {
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
    exTexPos = texPos;
    exVertexNormal = vertexNormal;
    exVertexPos = position;
}