package net.alienx.ihm_stab.ui.components;

import java.awt.BasicStroke;
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

public class MotorSpeed extends Canvas {
	private Image img;
	private Graphics2D buffer;
	
	private Color bgColor = Color.black;
	
	private int _height=150;
	private int _width=50;
	private int percent = 50;
	
	private String title = "MLF";
	
	private Font defaultFont = new Font("Helvetica",1,12);
	
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
		
		buffer.setColor(Color.WHITE);
		buffer.setFont(defaultFont);
		FontMetrics fm = buffer.getFontMetrics(defaultFont);
		
		int xStr = (_width/2)-(fm.stringWidth(title)/2);
		int yStr = fm.getAscent();
		buffer.drawString(title,xStr,yStr);
		
		int paddingTop = fm.getHeight();// + fm.getAscent();
		int barHeight = _height-paddingTop-1;

		
		buffer.setPaint(new GradientPaint(1,barHeight-2,Color.green,_width-2,2,Color.red,true));
		int h = percent*barHeight/100;
		buffer.fillRect(_width/4, (barHeight-h)+paddingTop, _width-(_width/4*2), h);
		
		buffer.setColor(Color.WHITE);
		buffer.drawRect(1,paddingTop, _width-2, barHeight);
		
		int i = barHeight/10;
		for(int j=paddingTop;j<barHeight+paddingTop;j+=i){
			buffer.drawLine(1, j, _width/4-2, j);
			//buffer.drawLine(_width-(_width/4-2), j+paddingTop+2, _width-2, j+paddingTop+2);
		}

		
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
		return new Dimension(_width,_height);
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
}
