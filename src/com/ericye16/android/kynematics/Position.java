package com.ericye16.android.kynematics;

//TODO: finish this on-phone implementation
public class Position {
	private double[] pos = new double[] {0,0,0};
	private double[] vel = new double[] {0,0,0};
	
	public double[] getPos() {
		return pos;
	}
	
	public void update(double[] accel, long deltaT) {
	}
}
