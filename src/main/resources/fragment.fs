#version 330

in vec2 exTexPos;
in vec3 exVertexNormal;
in vec3 exVertexPos;

out vec4 fragColor;

uniform sampler2D aTexture;

void main() {
    vec3 lightPos = vec3(0.0, 0.0, 1.0);
    float ambientStrength = 0.1;
    vec3 lightColor = vec3(1.0, 1.0, 1.0);
    vec3 ambient = ambientStrength * lightColor;

    vec3 objectColor = texture(aTexture, exTexPos).xyz;

    vec3 norm = normalize(exVertexNormal);
    vec3 lightDirection = normalize(lightPos - exVertexPos);

    float diff = max(dot(norm, lightDirection), 0.0);
    vec3 diffuse = diff * lightColor;

    vec3 result = (ambient + diffuse) * objectColor;
    fragColor = vec4(result, 1.0);
}