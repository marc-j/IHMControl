package net.alienx.ihm_stab.ui.components;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import net.alienx.ihm_stab.Utils;
import net.alienx.ihm_stab.utils.ImageUtils;

public class TurnCoordinator extends Canvas {
	private BufferedImage bg;
	private BufferedImage plane;
	private double angle = 0.0;
	
	public TurnCoordinator(){
		bg = ImageUtils.getImagesAlpha("./images/TurnCoordinator.png", Color.red);
		plane = ImageUtils.getImagesAlpha("./images/TurnCoordinatorPlane.png", Color.red);
	}
	
	public void paint(Graphics g){
		g.drawImage(bg, 0,0,this);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.translate(bg.getWidth()/2, bg.getHeight()/2);
		g2.drawImage(getRotatedPlane(), -plane.getWidth()/2, (-plane.getHeight()/2)-7, this);
	}
	
	public BufferedImage getRotatedPlane(){
		BufferedImage img = new BufferedImage(plane.getWidth(),plane.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D buffer = (Graphics2D)img.getGraphics();
		buffer.drawImage(plane,0,0,this);
		
		AffineTransform at = new AffineTransform();
		at.rotate(angle, plane.getWidth()/2, plane.getHeight()/2);
		
	    BufferedImageOp bio;
	    bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

	    img = bio.filter(plane, null);
		
		return img;
	}
	
	public void update(Graphics g){
		paint(g);
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(bg.getWidth(),bg.getHeight());
	}
	
	public void setAngleRad(double rads){
		setAngleDeg(Math.toDegrees(rads));
	}
	
	public void setAngleDeg(double deg){
		if(deg == Math.toDegrees(angle))
			return;
		angle = Math.toRadians(Utils.constrain(deg,-15.0d,15.0d));
		repaint();
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
}
