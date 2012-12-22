package net.alienx.ihm_stab.ui.components;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import net.alienx.ihm_stab.Utils;
import net.alienx.ihm_stab.utils.ImageUtils;

public class Horizon extends Canvas {
	private BufferedImage bg;
	private BufferedImage horizon;
	private BufferedImage image;
	private Graphics2D buffer1;
	private Graphics2D buffer;
	private Font font = new Font("Verdana",0,10);
	private double pitch = 0;
	private double roll = 0;
	
	public Horizon(){
		bg = ImageUtils.getImagesAlpha("./images/HorizonBG.png", Color.red);
	}
	
	public void paint(Graphics g){
		if(buffer1 != null)
			buffer1.dispose();
		
		image = new BufferedImage(bg.getWidth(),(90*4)*2,BufferedImage.TYPE_INT_ARGB);
		buffer1 = (Graphics2D) image.getGraphics();
		
		horizon = new BufferedImage(bg.getWidth(),(90*4)*2,BufferedImage.TYPE_INT_RGB);
		buffer = (Graphics2D) horizon.getGraphics();
		
		buffer.translate(bg.getWidth()/2, bg.getHeight()/2);

		buffer.setColor(new Color(67,166,202));
		buffer.fillRect(-(bg.getWidth()/2),-(bg.getHeight()/2),bg.getWidth(),(bg.getHeight()/2)+(int)getScaledPitch());
		
		buffer.setColor(new Color(124,81,39));
		buffer.fillRect(-(bg.getWidth()/2),(int)getScaledPitch(),bg.getWidth(),(bg.getHeight()/2)-getScaledPitch());
		
		drawIndicator(buffer);
		
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		at.rotate(getRollRads(), bg.getWidth()/2, (bg.getHeight()/2));
		
		buffer1.drawImage(horizon,at,this);
		
		buffer1.drawImage(bg,0,0,this);
		
		g.drawImage(image,0,0,this);
	}
	
	public void update(Graphics g){
		paint(g);
	}
	
	private void drawIndicator(Graphics2D g){
		g.setColor(Color.white);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		g.setStroke(new BasicStroke(4,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		g.drawLine(-(bg.getWidth()/2),getScaledPitch(),bg.getWidth()/2,getScaledPitch());
		
		for(int i=0;i<8;i++){
			for(int j=1;j<10;j++){
				if(j != 5){
					g.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
					g.drawLine(-5,(i*40)+(4*j)+getScaledPitch(),5,(i*40)+(4*j)+getScaledPitch());
					g.drawLine(-5,-((i*40)+(4*j)-getScaledPitch()),5,-((i*40)+(4*j)-getScaledPitch()));
				}else{
					g.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
					g.drawLine(-10,(i*40)+(4*j)+getScaledPitch(),10,(i*40)+(4*j)+getScaledPitch());
					g.drawLine(-10,-((i*40)+(4*j)-getScaledPitch()),10,-((i*40)+(4*j)-getScaledPitch()));
				}
			}
			g.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
			g.drawLine(-30,(i+1)*(40)+getScaledPitch(),30,(i+1)*(40)+getScaledPitch());
			g.drawLine(-30,-((i+1)*(40)-getScaledPitch()),30,-((i+1)*(40)-getScaledPitch()));
			
			g.drawString("-"+(i+1)*10, 30+fm.getAscent(), ((i+1)*40)+fm.getDescent()+getScaledPitch());
			g.drawString(""+(i+1)*10, 30+fm.getAscent(), -((i+1)*40-getScaledPitch())+fm.getDescent());
		}
	}
	
	
	private double getRollRads(){
		return Math.toRadians(roll);
	}
	
	private int getScaledPitch(){
		return (int) (pitch*4);
	}
	
	
	public void setPitch(double angle){
		if(pitch == angle)
			return;
		pitch = angle;
		repaint();
	}
	
	public void setRoll(double angle){
		if(roll == angle)
			return;
		roll = angle;
		repaint();
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(bg.getWidth(),bg.getHeight());
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
}
