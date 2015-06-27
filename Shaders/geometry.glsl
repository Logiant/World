#version 330

precision highp float;

in VertexData
{
    vec4 color;
    mat3 rot;
    vec3 lightDir;
    
} vertex[];



out vec4 pass_Color;
out vec3 normal;

layout (triangles) in;
layout (triangle_strip) out;
layout (max_vertices = 3) out;

void main(void)
{

	vec3 lightDir = vertex[0].lightDir;


	
	highp vec3 l1 = gl_in[1].gl_Position.xyz - gl_in[0].gl_Position.xyz;
	highp vec3 l2 = gl_in[1].gl_Position.xyz - gl_in[2].gl_Position.xyz;
	
	
   	vec3 n = normalize(cross(l1, l2));
    


	n = -vertex[0].rot * n;
	

	float ambient = 0.4f;

	float diffuse = clamp(dot(lightDir, n), ambient, 1);

	normal = n;

    for (int i = 0; i < gl_in.length(); i++) {


        gl_Position = gl_in[i].gl_Position;
        pass_Color = vec4(vertex[i].color.xyz * diffuse, vertex[i].color.w);

        EmitVertex();
    }

    EndPrimitive();
}