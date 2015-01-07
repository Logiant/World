package util;

public class Matrix3 {

	public float m00, m01, m02;
	public float m10, m11, m12;
	public float m20, m21, m22;
	
	public Matrix3(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
		this.m00 = m00; this.m01 = m01; this.m02 = m02;
		this.m10 = m10; this.m11 = m11; this.m12 = m12;
		this.m20 = m20; this.m21 = m21; this.m22 = m22;
	}
	
	public static Matrix3 Identity() {
		return new Matrix3(1, 0, 0,
				0, 1, 0,
				0, 0, 1);
	}
	
	@Override
	public String toString() {
		return "Matrix\n"+m00+", "+m01+", "+m02+";\n"+
				m10+", "+m11+", "+m12+";\n"+
				m20+", "+m21+", "+m22;
	}
}
