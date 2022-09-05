public class Vector2D {
	// non-static variables
	public double x;
	public double y;
	public double restoreAngle;

	// parameterized constructor
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
		this.restoreAngle = 0.0;
	}

	public double angle() {
		return Math.atan2(y, x);
		// arc tangent -> to get angle value, theta
	}

	public double magnitude() {
		return Math.sqrt((x * x) + (y * y));
		// to find magnitude between two positions
	}

	public void rotateCoordinates(double tiltAngle) {
		// tiltAngle -> angle made with axis initially
		this.restoreAngle += tiltAngle;
		double angle = angle();
		double magnitude = magnitude();
		angle -= tiltAngle;
		x = magnitude * Math.cos(angle);
		y = magnitude * Math.sin(angle);
	}

	public void restoreCoordinates() {
		double angle = angle();
		double magnitude = magnitude();
		angle += restoreAngle;
		x = magnitude * Math.cos(angle);
		y = magnitude * Math.sin(angle);
		restoreAngle = 0.0;
	}
}
