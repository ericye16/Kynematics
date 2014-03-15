package com.ericye16.android.kynematics;

public class Position {
	private float[] pos = new float[] {0,0,0};
	private float[] posn_1 = new float[] {0,0,0};
	float[] R;
	float[] I;
	float[] orientation;
	
	public Position(float[] R, float[] I, float[] orientation) {
		this.R = R;
		this.I = I;
		this.orientation = orientation;
	}
	
	public float[] getPos() {
		return pos;
	}
	
	/**
	 * Using: https://en.wikipedia.org/wiki/Verlet_integration
	 * @param accel
	 * @param deltaT
	 */
	public void update(float[] accel, long deltaT) {
		double deltaTdouble = deltaT * 1e-9;
		float[] tmp = pos.clone();
		for (int i = 0; i < 3; i++) {
			pos[i] = (float) (2 * pos[i] - posn_1[i] + accel[i] * deltaTdouble * deltaTdouble);
			posn_1[i] = tmp[i];
		}
	}
}
