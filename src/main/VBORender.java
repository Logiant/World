package main;

import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import util.Matrix4;

public class VBORender {

	int transLoc;

	//shader values
	int vsId; //vertex shader
	int fsId; //fragment shader
	int pId; //shader program id

	public void initialize() {
		shaderSetup();

		GL20.glUseProgram(pId);
	}
	
	public int[] createVBO(float[] vertices, float[] colors, int[] indices) {
		//create the buffers to hold vertex color and index data
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();

		FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
		colorsBuffer.put(colors);
		colorsBuffer.flip();
		// OpenGL expects vertices in counter clockwise order by default
		int indicesCount = indices.length;
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		// Create a new Vertex Array Object in memory and select it (bind)
		int vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		// Create a new Vertex Buffer Object in memory and select it (bind) - VERTICES
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		// Create a new VBO for the indices and select it (bind) - COLORS
		int vbocId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		// Create a new VBO for the indices and select it (bind) - INDICES
		int vboiId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);
		int[] data = {vaoId, vboId, vbocId, vboiId};
		return data;
	}

	public void update(Matrix4 transform) {
		//bind the shaders
		//grab the MVP matrix location in the shaders
		int loc = GL20.glGetUniformLocation(pId, "MVP");
		//load the transform matrix into a float buffer
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4*4);
		transform.store(buffer);
		buffer.flip();
		//load the matrix into the program and then unbind it
		GL20.glUniformMatrix4fv(loc, false, buffer);
	}

	public void render(int vaoId, int indicesCount) {
		// Bind to the VAO that has all the information about the vertices and colors
		GL30.glBindVertexArray(vaoId);
		// Draw the vertices
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
		GL30.glBindVertexArray(0);
	}

	private void shaderSetup() {
		int errorCheckValue = GL11.glGetError();
		// Load the vertex shader
		vsId = this.loadShader("Shaders/vertex.glsl", GL20.GL_VERTEX_SHADER);
		// Load the fragment shader
		fsId = this.loadShader("Shaders/fragment.glsl", GL20.GL_FRAGMENT_SHADER);
		// Create a new shader program that links both shaders
		pId = GL20.glCreateProgram();
		GL20.glAttachShader(pId, vsId);
		GL20.glAttachShader(pId, fsId);
		// Position information will be attribute 0
		GL20.glBindAttribLocation(pId, 0, "in_Position");
		// Color information will be attribute 1
		GL20.glBindAttribLocation(pId, 1, "in_Color");
		
		GL20.glLinkProgram(pId);

		GL20.glValidateProgram(pId);
		errorCheckValue = GL11.glGetError();
		if (errorCheckValue != GL11.GL_NO_ERROR) {
			System.out.println("ERROR - Could not create the shaders:" + glGetProgramInfoLog(errorCheckValue));
			System.exit(-1);
		}
	}

	private int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}

		shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);

		return shaderID;
	}

	//TODO update position in shader using a mat4 transform!
	public void updateVerts(int vaoId, int vboId, float[] verts) {
		System.out.println("Updating Verts...");
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(verts.length);
		verticesBuffer.put(verts);
		verticesBuffer.flip();
		
		GL30.glBindVertexArray(vaoId);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, verts.length, verticesBuffer);
		GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
	}
}