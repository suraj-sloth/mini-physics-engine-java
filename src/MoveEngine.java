import java.awt.geom.Point2D;

public class MoveEngine extends Thread
{
	private long timePassed = 0;
	private long currrentTime = 0;
	private long lastTime = 0;
	private double timeFraction = 0.0;
	private Acceleration gravity;
	
	public void run()
	{
		initializeGravity();
		while (Main.isRunning) {
			updateTime();
			applyGravity();
			sumForces();
			moveEnts();
			try {
				sleep(1);
			} catch (InterruptedException e) {
			}
		}
	}

	private void updateTime()
	{
		lastTime = currrentTime;
		currrentTime = System.currentTimeMillis();
		timePassed = (currrentTime - lastTime);
		timeFraction = (timePassed / 1500.0);
	}

	private void initializeGravity()
	{
		gravity = new Acceleration(0.0, Main.GRAVITY);
	}

	private synchronized void applyGravity()
	{
		// Apply the gravity to each entity.
		for (int i = 0; i < Main.living.size(); i++) {
			Spawn s = Main.living.get(i);
			s.addAcceleration(gravity);
		}
	}

	private synchronized void sumForces()
	{
		for (int i = 0; i < Main.living.size(); i++) {
			Spawn s = Main.living.get(i);
			// Get the sum of all accelerations acting on object.
			Acceleration theAcceleration = s.sumAcceleration();
			// Apply the resulting change in velocity.
			double vx = s.vx() + (theAcceleration.ax() * timeFraction);
			double vy = s.vy() + (theAcceleration.ay() * timeFraction);
			s.updateVelocity(vx, vy);
			// Apply drag coefficient
			//s.applyDrag(1.0 - (timeFraction * Main.DRAG));
		}
	}

	private synchronized void moveEnts()
	{
		for (int i = 0; i < Main.living.size(); i++) {
			Spawn s = Main.living.get(i);
			double oldX = s.getX(), oldY = s.getY();
			
			// Calculating the new x and y coordinates.
			double newX = oldX + (s.vx() * timeFraction);
			double newY = oldY + (s.vy() * timeFraction);
			
			//Updating the new position.
			s.updatePos(newX, newY);
			checkWallCollisions(s);
		}
		checkCollisions();
	}

	private synchronized void checkCollisions()
	{
		for (int i = 0; i < Main.living.size() - 1; i++) {
			Spawn firstEntity = Main.living.get(i);
			Point2D feCenter = firstEntity.getCenter();
			for (int j = i + 1; j < Main.living.size(); j++) {
				Spawn secondEntity = Main.living.get(j);
				if (secondEntity == null) break;
				Point2D seCenter = secondEntity.getCenter();
				double distBetween = feCenter.distance(seCenter);
				double radiusEntity = firstEntity.getRadius(); //taking any radius since we fixed that
				if (distBetween < (radiusEntity * 2)) collide(firstEntity, secondEntity, distBetween);
			}
		}
	}

	private synchronized void collide(Spawn s, Spawn t, double distBetween)
	{
		// Relative x and y distance between them.
		double relX = s.getX() - t.getX();
		double relY = s.getY() - t.getY();
		
		//Collision angle is calculated by polar coordinates angle - theta
		double collisionAngle = Math.atan2(relY, relX);
		
		Vector2D sVel = s.velVector(), tVel = t.velVector();
		sVel.rotateCoordinates(collisionAngle);
		tVel.rotateCoordinates(collisionAngle);
		
		// In the collision coordinate system, the contact normals lie on the
		// x-axis. Only the velocity values along this axis are affected. We can
		// now apply a simple 1D momentum equation where the new x-velocity of
		// the first object equals a negative times the x-velocity of the
		// second.
		
		double swap = sVel.x;
		sVel.x = tVel.x;
		tVel.x = swap;
		
		// Getting the vectors back into normal coordinate space.
		sVel.restoreCoordinates();
		tVel.restoreCoordinates();
		
		// Updating velocities
		s.updateVelocity(sVel.x * Main.BOUNCE, sVel.y * Main.BOUNCE);
		t.updateVelocity(tVel.x * Main.BOUNCE, tVel.y * Main.BOUNCE);
		
		// Back them up in the opposite angle so they are not overlapping.
		double minDist = s.getRadius() + t.getRadius();
		double overlap = minDist - distBetween;
		double toMove = overlap / 2;
		double newX = s.getX() + (toMove * Math.cos(collisionAngle));
		double newY = s.getY() + (toMove * Math.sin(collisionAngle));
		s.updatePos(newX, newY);
		newX = t.getX() - (toMove * Math.cos(collisionAngle));
		newY = t.getY() - (toMove * Math.sin(collisionAngle));
		t.updatePos(newX, newY);
	}

	private synchronized void checkWallCollisions(Spawn s)
	{
		int maxY = 480 - s.dimY();
		int maxX = 640 - s.dimX();
		if (s.getY() > maxY) {
			s.updatePos(s.getX(), maxY);
			s.updateVelocity(s.vx(), (s.vy() * -Main.BOUNCE));
		}
		if (s.getX() > maxX) {
			s.updatePos(maxX, s.getY());
			s.updateVelocity((s.vx() * -Main.BOUNCE), s.vy());
		}
		if (s.getX() < 1) {
			s.updatePos(1, s.getY());
			s.updateVelocity((s.vx() * -Main.BOUNCE), s.vy());
		}
	}
}
