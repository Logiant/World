#version 330

uniform mat4 MVP;
uniform vec4 TRAN;
uniform vec4 QUAT;

in vec4 in_Position;
in vec4 in_Color;

out float[] rot;


out VertexData
{
    vec4 color;
} vertex;


void main(void) {

	vec4 translation = in_Position;

	vec3 temp = cross(QUAT.xyz, translation.xyz) + QUAT.w * translation.xyz;
    vec4 rotated = new vec4((cross(temp, -QUAT.xyz) + dot(QUAT.xyz,translation.xyz) * QUAT.xyz + QUAT.w * temp), 1) + TRAN;


	gl_Position = MVP * (rotated);
    vertex.color = in_Color;
    
	float[9] rot = float[9](MVP[0][0], MVP[0][1], MVP[0][2],
							MVP[1][0], MVP[1][1], MVP[1][2],
							MVP[2][0], MVP[2][1], MVP[2][2]);

}