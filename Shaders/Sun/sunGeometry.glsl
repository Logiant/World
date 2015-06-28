#version 330

precision highp float;

in VertexData
{
    vec4 color;    
} vertex[];



out vec4 pass_Color;

layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

void main(void)
{


    gl_Position = gl_in[0].gl_Position + vec4(-10, 10, 0, 0);
    pass_Color = vertex[0].color;
    EmitVertex();
   
    gl_Position = gl_in[0].gl_Position + vec4(-10, -10, 0, 0);
    pass_Color = vertex[0].color;
    EmitVertex();    
    
    gl_Position = gl_in[0].gl_Position + vec4(10, 10, 0, 0);
    pass_Color = vertex[0].color;
    EmitVertex();    
    
    gl_Position = gl_in[0].gl_Position + vec4(10, -10, 0, 0);
    pass_Color = vertex[0].color;
    EmitVertex();
    
    EndPrimitive();
}