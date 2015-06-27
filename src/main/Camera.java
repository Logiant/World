package main;



import entities.Transform;
import util.Matrix4;
import util.Time;
import util.Vector3;

public class Camera {

	public static float viewAngle = 45;
	public static float nearClip = 0.01f;
	public static float farClip = 500;

	private Vector3 position;
	private Vector3 rotation;
	
	private Vector3 desiredPosition;
	private Vector3 desiredRotation;
	
	private float bias = 0.01f;

	Vector3 linVel;
	Vector3 rotVel;

	private Matrix4 viewMatrix;
	
	Transform target;

	
	Vector3 follow = new Vector3(0, 10, 30);

	public Camera(Transform target) {
		position = new Vector3();
		rotation = new Vector3();
		this.target = target;
	}
	
	public void update() {

		desiredPosition = target.getPosition();
		desiredRotation = target.getRotation();
		
		Vector3 forward = Vector3.rotate(follow, desiredRotation);
				
		desiredPosition.add(forward);
		desiredPosition.mul(-1); desiredRotation.mul(-1);
				
		
		position.x = lerp(position.x, desiredPosition.x, bias*Time.dt*20);
		position.y = lerp(position.y, desiredPosition.y, bias*Time.dt);
		position.z = lerp(position.z, desiredPosition.z, bias*Time.dt);
		rotation.x = desiredRotation.x; rotation.y = desiredRotation.y; rotation.z = desiredRotation.z;

		updateView();
	}
	
	
	private float lerp(float a, float b, float t) {
		t = Math.min(t, 1);
		t = Math.max(t, 0);
		
		return a + ((b-a)*t);
	}

	private void updateView() {
		viewMatrix = Matrix4.Identity();
		viewMatrix.translate(position);

		Matrix4 rotationMat = Matrix4.Identity();
		rotationMat.m00 = (float)Math.cos(-rotation.y*Math.PI/180);
		rotationMat.m02 = (float)Math.sin(-rotation.y*Math.PI/180);
		rotationMat.m20 = (float)-Math.sin(-rotation.y*Math.PI/180);
		rotationMat.m22 = (float)Math.cos(-rotation.y*Math.PI/180);

		Matrix4.mul(rotationMat, viewMatrix, viewMatrix);

		rotationMat = Matrix4.Identity();
		rotationMat.m00 = 1;
		rotationMat.m11 = (float)Math.cos(-rotation.x*Math.PI/180);
		rotationMat.m12 = (float)-Math.sin(-rotation.x*Math.PI/180);
		rotationMat.m21 = (float)Math.sin(-rotation.x*Math.PI/180);
		rotationMat.m22 = (float)Math.cos(-rotation.x*Math.PI/180);


		Matrix4.mul(rotationMat, viewMatrix, viewMatrix);
	}

	public Matrix4 getView() {
		return Matrix4.mul(Matrix4.perspective(viewAngle, 4f/3, nearClip, farClip), viewMatrix, new Matrix4());
	}
}
