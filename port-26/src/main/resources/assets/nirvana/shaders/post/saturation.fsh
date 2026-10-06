#version 330
uniform sampler2D InSampler;
in vec2 texCoord;
out vec4 fragColor;
void main() { vec4 c=texture(InSampler,texCoord); float l=dot(c.rgb,vec3(0.3,0.59,0.11)); fragColor=vec4(mix(vec3(l),c.rgb,1.4),c.a); }
