#version 330

in float[] rot;

in VertexData
{
    vec4 color;
} vertex[];



out vec4 pass_Color;
out vec3 normal;

layout (triangles) in;
layout (triangle_strip) out;
layout (max_vertices = 3) out;

void main(void)
{


	mat3 r = mat3(rot[0], rot[1], rot[2], rot[3], rot[4], rot[5], rot[6], rot[7], rot[8]);
	
    vec3 n = cross(gl_in[1].gl_Position.xyz-gl_in[0].gl_Position.xyz, gl_in[2].gl_Position.xyz-gl_in[0].gl_Position.xyz);

	//n = r * n; Y U NO WURK?

    for (int i = 0; i < gl_in.length(); i++) {

        gl_Position = gl_in[i].gl_Position;
        pass_Color = vertex[i].color;
        normal = n;
        EmitVertex();
    }

    EndPrimitive();
}