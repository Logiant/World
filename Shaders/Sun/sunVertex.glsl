#version 330

uniform mat4 MVP;
uniform vec3 SUNPOS;

in vec4 in_Position;
in vec4 in_Color;


out VertexData
{
    vec4 color;
} vertex;


void main(void) {

	
	vec4 sunPos = vec4(SUNPOS*100, 1);
	
	//output variables
	gl_Position = (MVP * sunPos);
	
    vertex.color = in_Color;
}