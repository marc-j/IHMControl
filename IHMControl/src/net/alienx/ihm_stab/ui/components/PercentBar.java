package net.alienx.ihm_stab.ui.components;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import net.alienx.ihm_stab.Utils;

public class PercentBar extends Canvas {

	public static final short HORIZONTAL = 0;
	public static final short VERTICAL = 0;
	
	private int orientation;
	private int percent = 0;
	private Image img;
	private Graphics2D buffer;
	private Color bgColor;
	private Color barColor;
	private Color barColor2;
	private Color borderColor;
	private boolean gradientFont = false;
	private Font font;
	private Font defaultFont = new Font("Helvetica",1,11);
	
	private int _height=100;
	private int _width=20;
	
	public PercentBar(int orientation){
		this.orientation = orientation;
		this.bgColor = Color.white;
		this.barColor = Color.red;
		this.borderColor = Color.gray;
		this.font = this.defaultFont;
	}
	
	public PercentBar(int orientation, Color bgColor, Color borderColor){
		this.orientation = orientation;
		this.bgColor = bgColor;
		this.barColor = Color.red;
		this.borderColor = borderColor;
		this.font = this.defaultFont;		
	}

	public PercentBar(int orientation, Color barColor){
		this.orientation = orientation;
		this.bgColor = Color.white;
		this.barColor = barColor;
		this.borderColor = Color.gray;
		this.font = this.defaultFont;		
	}

	public PercentBar(int orientation, Color bgColor, Color borderColor, Color barColor){
		this.orientation = orientation;
		this.bgColor = bgColor;
		this.barColor = barColor;
		this.borderColor = borderColor;
		this.font = this.defaultFont;		
	}
	
	public PercentBar(int orientation, Color bgColor, Color borderColor, Color barColor1, Color barColor2){
		this.orientation = orientation;
		this.bgColor = bgColor;
		this.barColor = barColor1;
		this.barColor2 = barColor2;
		this.borderColor = borderColor;
		this.font = this.defaultFont;	
		this.gradientFont = true;
	}
	
	public void paint(Graphics g){
		if(buffer == null || _height != this.getHeight() || _width != this.getWidth()){
			img = new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_INT_RGB);
			buffer = (Graphics2D)img.getGraphics();
			_height = this.getHeight();
			_width = this.getWidth();
		}
		buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		buffer.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		
		buffer.setColor(bgColor);
		buffer.fillRect(0, 0, _width, _height);
		
		buffer.setColor(borderColor);
		buffer.drawRect(0, 0, _width-1, _height-1);
		
		
		int barWidth = (int)(percent*(_width-2)*0.01);
		if(gradientFont){
			buffer.setPaint(new GradientPaint(1,1,barColor,_width-2,_height-2,barColor2,true));
		}else
			buffer.setColor(barColor);
		
		buffer.fillRect(1,1, barWidth, _height-2);
		
		buffer.setColor(Color.black);
		buffer.setFont(font);
		FontMetrics fm = buffer.getFontMetrics(font);
		
		buffer.drawString(percent+"%", (_width/2)-(fm.stringWidth(percent+"%")/2) , (fm.getAscent() + (_height - (fm.getAscent() + fm.getDescent())) / 2));
		
		g.drawImage(img,0,0,this);
	}
	
	public void update(Graphics g){
		paint(g);
	}
	
	public void setPercent(int percent){
		this.percent = Utils.constrain(percent, 0, 100);
		repaint();
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(100,20);
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
}
