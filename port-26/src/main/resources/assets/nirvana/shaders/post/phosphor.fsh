#version 330
uniform sampler2D InSampler;
uniform sampler2D PrevSampler;
in vec2 texCoord;
out vec4 fragColor;
void main() { fragColor = vec4(max(texture(PrevSampler,texCoord).rgb * vec3(0.8,0.85,0.8), texture(InSampler,texCoord).rgb),1.0); }
