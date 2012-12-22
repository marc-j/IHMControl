package net.alienx.ihm_stab.utils;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.Vector;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PolygonEx extends Polygon {
	private Vector<Point2D.Double> points;
	private double centerX = 0;
	private double centerY = 0;
	
	public PolygonEx(Point2D.Double... points){
		super();
		this.points = new Vector<Point2D.Double>();
		for(int i=0;i<points.length;i++){
			this.points.add(points[i]);
			addPoint((int) points[i].getX(), (int) points[i].getY());
			//System.out.println((int) points[i].getX()+", "+ (int) points[i].getY());
		}
	}
	
	public void rotate(double angleRad, Point2D.Double origin){
		double x=0;
		double y=0;
		for(Point2D.Double point : points){
			x = (point.getX()-origin.getX())*cos(angleRad)
				- (point.getY()-origin.getY())*sin(angleRad) 
				+ origin.getX();
			
			y = (point.getX()-origin.getX())*sin(angleRad)
				+ (point.getY()-origin.getY())*cos(angleRad)
				+ origin.getY();
			
			point.setLocation(Math.floor(x),Math.floor(y));
		}
		reset();
	}
	
	public void updateCenter(){
		process:
		{
			if(points.size() <= 0){
				centerX = 0;
				centerY = 0;
				break process;
			}
			
			double ySum=0;
			double xSum=0;
			for(Point2D.Double point : points){
				xSum += point.getX();
				ySum += point.getY();
			}
			centerX = xSum / points.size();
			centerY = ySum / points.size();
		}
	}
	
	public void reset ()
	{
		super.reset ();
		for (Point2D.Double point : points)
		{
			addPoint ((int) point.getX(), (int) point.getY());
		}
	}
}
