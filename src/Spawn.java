import java.awt.geom.Point2D;//The Point2D class defines a point representing a location in (x,y) coordinate space
import java.util.ArrayList;

public class Spawn
{
  private double x, y, vx, vy, radius;
  private ArrayList<Acceleration> accelerations = new ArrayList<Acceleration>();// to store the changing accelerations of all balls

  public Spawn(int x, int y, double vx, double vy)
  {
    this.x = x;
    this.y = y;
    this.vx = vx;
    this.vy = vy;
    this.radius = 15.0;
  }

  public Spawn(int x, int y)
  {
    this(x, y, 0.0, 0.0);
  }

  public Vector2D velVector()//returns vector2D object with restore angle for x and y axis velocity
  {
    return new Vector2D(this.vx(), this.vy());
  }

  public Acceleration sumAcceleration()//to sum the accelerations of spawn on x and y axis
  {
    double xAccel = 0, yAccel = 0;
    for (int i = 0; i < this.accelerations.size(); i++) {
      xAccel += this.accelerations.get(i).ax();
      yAccel += this.accelerations.get(i).ay();
    }
    this.accelerations.clear();
    return new Acceleration(xAccel, yAccel);//it should restore it to 0 bcoz to find the new acceleration after the collision
  }

  public void addAcceleration(Acceleration a)
  {
    this.accelerations.add(a);
  }

  public void updateVelocity(double vx, double vy)//to update the changing velocity in every momentum
  {
    this.vx = vx;
    this.vy = vy;
  }

  public void updatePos(double newX, double newY)//to update the position after every collision
  {
    this.x = newX;
    this.y = newY;
  }

  public double vx()
  {
    return this.vx;
  }

  public double vy()
  {
    return this.vy;
  }

  public int dimX()
  {
    return (int) (this.radius * 2);
  }

  public int dimY()
  {
    return (int) (this.radius * 2);
  }

  public Point2D getCenter()//to find Minimum Distance,Distance Between,Overlap Distance,Distance to Move.
  {
    return new Point2D.Double(this.x + (this.dimX() / 2), this.y
                              + (this.dimY() / 2));
  }

  public double getRadius()
  {
    return this.radius;
  }

  public double getX()
  {
    return this.x;
  }

  public double getY()
  {
    return this.y;
  }

  public double getX2()
  {
    return (this.x + this.dimX());
  }

  public double getY2()
  {
    return (this.y + this.dimY());
  }

  public void setX(int newX)
  {
    this.x = newX;
  }

  public void setY(int newY)
  {
    this.y = newY;
  }
}
