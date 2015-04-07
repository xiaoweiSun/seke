package com.graph;

public class Point {
	static final int R = 600;
	private static final double PUSH = 100;
	private static final double PULL = 1000;
	private static final double RES = 0.94;
	public int x = 0, y = 0;
	public double vx = 0, vy = 0, ax = 0, ay = 0;

	public void push(Point p) {
		int dx = x - p.x;
		int dy = y - p.y;
		double r_r = dx * dx + dy * dy;
		if (r_r == 0)
			r_r = 1e-5;
		double r = Math.sqrt(r_r);
		double ux = dx / r;
		double uy = dy / r;

		double dax = PUSH * ux / (r_r);
		ax += dax;
		p.ax -= dax;
		double day = PUSH * uy / (r_r);
		ay += day;
		p.ay -= day;
	}

	public void pull(Point p) {
		int dx = x - p.x;
		int dy = y - p.y;
		double r_r = dx * dx + dy * dy;
		if (r_r == 0)
			r_r = 1e-5;
		double r = Math.sqrt(r_r);
		double ux = dx / r;
		double uy = dy / r;

		double dax = PULL * ux / (r_r);
		ax -= dax;
		p.ax += dax;
		double day = PULL * uy / (r_r);
		ay -= day;
		p.ay += day;
	}

	public void resistance() {
		vx = RES * vx;
		vy = RES * vy;
	}

	public void checkEdge() {
		int dx = x - R;
		int dy = y - R;
		double r = Math.sqrt(dx * dx + dy * dy);
		if (r == 0)
			r = 1e-5;
		if (r > R) {
			double ux = dx / r;
			double uy = dy / r;
			x = (int) (ux * R) + R;
			y = (int) (uy * R) + R;

			ax *= -ux;
			ay *= -uy;

			vx *= -ux;
			vy *= -uy;
		}
	}

	public void update() {
		x += vx;
		y += vy;
		vx += ax;
		vy += ay;
		ax = 0;
		ay = 0;
	}

	public boolean checkStop() {
		if (vx < 0.1 && vy < 0.1 && ax < 0.1 && ay < 0.1 && vx > -0.1 && vy > -0.1
				&& ax > -0.1 && ay > -0.1)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "x=" + x + ",y=" + y + ";ax=" + ax + ",ay=" + ay + ";vx=" + vx
				+ ",vy=" + vy;
	}
}
