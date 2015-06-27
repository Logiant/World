#version 330

in VertexData
{
    vec4 color;
    mat3 rot;
    
    vec3 lightDir;
    
} vertex[];



out vec4 pass_Color;

layout (triangles) in;
layout (triangle_strip) out;
layout (max_vertices = 3) out;

void main(void)
{

	vec3 lightDir = vertex[0].lightDir;

    vec3 n = cross(gl_in[1].gl_Position.xyz - gl_in[0].gl_Position.xyz, gl_in[2].gl_Position.xyz - gl_in[0].gl_Position.xyz);
	n = vertex[0].rot * n;

	float ambient = 0.4f;

	float diffuse = clamp(dot(lightDir, n), ambient, 1);


    for (int i = 0; i < gl_in.length(); i++) {


        gl_Position = gl_in[i].gl_Position;
        pass_Color = vec4(vertex[i].color.xyz * diffuse, vertex[i].color.w);

        EmitVertex();
    }

    EndPrimitive();
}