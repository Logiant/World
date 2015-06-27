#version 330

in vec4 pass_Color;
in vec3 normal;

out vec4 out_Color;

void main(void) {

	out_Color = pass_Color;
	
}