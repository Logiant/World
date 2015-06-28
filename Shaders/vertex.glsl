#version 330

uniform mat4 MVP;
uniform vec3 TRAN;
uniform vec4 QUAT;
uniform vec3 SUNPOS;

in vec4 in_Position;
in vec4 in_Color;


out VertexData
{
    vec4 color;
    mat3 rot;
    vec3 lightDir;
} vertex;


void main(void) {

	//default light direction
	vertex.lightDir = -normalize(SUNPOS);
    
    //quaternion rotation
	vec3 temp = 2*cross(QUAT.xyz, in_Position.xyz);
    vec3 rotated = in_Position.xyz + (QUAT.w*temp) + cross(QUAT.xyz, temp);


	//output variables
	gl_Position = (MVP * (vec4(rotated,1) + vec4(TRAN, 0)));
	
    vertex.color = in_Color;
  	vertex.rot = mat3(MVP);
}