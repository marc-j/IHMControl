package net.alienx.ihm_stab.ui.components;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Led extends Canvas {

	private Color onColor;
	private Color offColor;
	private Image img;
	private Graphics2D buffer;
	private boolean _on=false;
	private int _height=100;
	private int _width=20;
	private boolean update = true;
	
	public Led(){
		this.onColor = Color.green;
		this.offColor = Color.red;
	}
	
	public Led(Color onColor, Color offColor){
		this.onColor = onColor;
		this.offColor = offColor;
	}
	
	public void paint(Graphics g){
		if(buffer == null || _height != this.getHeight() || _width != this.getWidth()){
			img = new BufferedImage(this.getWidth(),this.getHeight(),  BufferedImage.TYPE_INT_RGB);
			if(buffer != null)
				buffer.dispose();
			buffer = (Graphics2D)img.getGraphics();

			_height = this.getHeight();
			_width = this.getWidth();
			
		}
		
		buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		buffer.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		
		buffer.setColor(Color.white);
		buffer.fillRect(0, 0, _width, _height);
		
		buffer.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		buffer.setColor(Color.black);
		buffer.fillOval(0, 0, _width, _height);
		
		buffer.setColor(_on ? onColor : offColor);
		buffer.fillOval(1, 1, _width-2, _height-2);
		
		g.drawImage(img,0,0,this);
		update = false;
		
		
	}
	
	public void setStatus(boolean on){
		boolean repaint = false;
		if(_on != on)
			repaint = true;
		
		_on = on;
		
		if(repaint)
			repaint();
	}
	
	public void update(Graphics g){
		paint(g);
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(15,15);
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
}
