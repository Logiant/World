#version 330

uniform mat4 MVP;
uniform vec4 TRAN;
uniform vec4 QUAT;

in vec4 in_Position;
in vec4 in_Color;


out VertexData
{
    vec4 color;
    mat3 rot;
    vec3 lightDir;
} vertex;


void main(void) {

	vertex.lightDir = new vec3(-1, -1, 0);
    
	vec4 translation = in_Position;

	vec3 temp = cross(QUAT.xyz, translation.xyz) + QUAT.w * translation.xyz;
    vec4 rotated = new vec4((cross(temp, -QUAT.xyz) + dot(QUAT.xyz,translation.xyz) * QUAT.xyz + QUAT.w * temp), 1) + TRAN;



	gl_Position = MVP * (rotated);
    vertex.color = in_Color;
  
  
  	vertex.rot = mat3(MVP);
}