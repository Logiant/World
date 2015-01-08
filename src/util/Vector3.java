package util;

public class Vector3 {

	public float x;
	public float y;
	public float z;

	public Vector3(float x, float y, float z) {
		this.x = x; this.y = y; this.z = z;
	}

	public Vector3() {
		this(0,0,0);
	}

	public Vector3(Vector3 clone) {
		this(clone.x, clone.y, clone.z);
	}

	public void add(Vector3 other) {
		x += other.x; y += other.y; z += other.z;
	}

	public static Vector3 rotate(Vector3 v, Vector3 eulerAngles) {
		return Vector3.rotate(v, eulerAngles.x, eulerAngles.y, eulerAngles.z);
	}
	
	public static Vector3 rotate(Vector3 v, float x, float y, float z) {
		Vector3 result = new Vector3();
		Matrix3 rot = Matrix3.Identity();
/*		//z rot
		rot.m11 = (float)Math.cos(x*Math.PI/180);
		rot.m12 = (float)-Math.sin(x*Math.PI/180);
		rot.m21 = (float)Math.sin(x*Math.PI/180);
		rot.m22 = (float)Math.cos(x*Math.PI/180);
		result = Vector3.Transform(rot, v);
		//y rot
	*/	rot = Matrix3.Identity();
		rot.m00 = (float)Math.cos(y*Math.PI/180);
		rot.m02 = (float)Math.sin(y*Math.PI/180);
		rot.m20 = (float)-Math.sin(y*Math.PI/180);
		rot.m22 = (float)Math.cos(y*Math.PI/180);
		result = Vector3.Transform(rot, v);
		//z rot
/*		rot = Matrix3.Identity();
		rot.m00 = (float)Math.cos(z*Math.PI/180);
		rot.m01 = (float)-Math.sin(z*Math.PI/180);
		rot.m10 = (float)Math.sin(z*Math.PI/180);
		rot.m11 = (float)Math.cos(z*Math.PI/180);
		result = Vector3.Transform(rot, v);
	*/	return result;
	}

	public static Vector3 Transform(Matrix3 m, Vector3 v) {
		Vector3 result = new Vector3();
		result.x = m.m00 * v.x + m.m01 * v.y + m.m02 * v.z;
		result.y = m.m10 * v.x + m.m11 * v.y + m.m12 * v.z;
		result.z = m.m20 * v.x + m.m21 * v.y + m.m22 * v.z;
		return result;
	}
	
	@Override
	public String toString() {
		return "Vector3 " + x + ", " + y + ", " + z;
	}

	public void mul(float amt) {
		x *= amt;
		y *= amt;
		z *= amt;
	}
}
