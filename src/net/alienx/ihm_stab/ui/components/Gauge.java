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
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import net.alienx.ihm_stab.Utils;
import net.alienx.ihm_stab.utils.PolygonEx;

public class Gauge extends Canvas {
	private BufferedImage img;
	private Graphics2D buffer;
	private int _height = 100;
	private int _width = 100;
	private Font font = new Font("Verdana",0,20);
	private Font fontTitle = new Font("Verdana",0,12);
	private int fleche[][];
	private float _min,_max;
	private float _value;
	private String _title = "-";
	private Color _bgColor = Color.black;
	
	public Gauge(String title){
		_min = 0.0f;
		_max = 100.0f;
		_value = 0.0f;
		_title = title;
	}
	
	public Gauge(String title,float min, float max){
		_min = min;
		_max = max;
		_value = 0.0f;
		_title = title;
	}
	
	public Gauge(String title, float min, float max, Color bgcolor){
		_min = min;
		_max = max;
		_value = 0.0f;
		_title = title;
		_bgColor = bgcolor;
	}
	
	public Gauge(String title, float min, float max, Dimension size){
		_min = min;
		_max = max;
		_value = 0.0f;
		_title = title;
		_width = (int) size.getWidth();
		_height = (int) size.getHeight();
	}
	
	public Gauge(String title, float min, float max, Dimension size, Color bgcolor){
		_min = min;
		_max = max;
		_value = 0.0f;
		_title = title;
		_width = (int) size.getWidth();
		_height = (int) size.getHeight();
		_bgColor = bgcolor;
	}
	
	private void createBackground(){
		buffer.setColor(_bgColor);
		buffer.fillRect(0,0,_width,_height);
		
		buffer.setColor(Color.white);
		//buffer.setPaint(new GradientPaint(0,0,Color.green,_width,_height,Color.red,true));
		buffer.setStroke(new BasicStroke(4,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		buffer.drawOval(2, 2, _width-5, _height-5);

		Graphics2D boxTitle = (Graphics2D) buffer.create(0, 0, _width, 50);
		boxTitle.setFont(fontTitle);
		FontMetrics fm = boxTitle.getFontMetrics();
		boxTitle.setColor(Color.gray);
		boxTitle.drawString(_title,(_width/2)-(fm.stringWidth(_title)/2),30);
		
		Graphics2D box = (Graphics2D) buffer.create(_width-70, _height-30, 70, 30);
		box.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		box.setColor(_bgColor);
		box.fillRect(0, 0, 70, 30);
		
		box.setFont(font);
		 fm = box.getFontMetrics();
		box.setColor(Color.white);
		String value = Float.toString(_value);
		box.drawString(value,(70/2)-(fm.stringWidth(value)/2),(fm.getAscent() + (30 - (fm.getAscent() + fm.getDescent())) / 2));
		

	}
	
	private void drawArrow(){
		Graphics2D arrow = (Graphics2D) buffer.create(0, 0, _height, _width);
		arrow.translate(_width/2, _height/2);
		arrow.setColor(Color.white);
		
		PolygonEx gaugeArrow = new PolygonEx(
				new Point2D.Double(0,0),
				new Point2D.Double(-1,1),
				new Point2D.Double(-2,2),
				new Point2D.Double(-3,3),
				new Point2D.Double(-4,4),
				new Point2D.Double(0,(_width-11)/2),
				new Point2D.Double(4,4),
				new Point2D.Double(3,3),
				new Point2D.Double(2,2),
				new Point2D.Double(1,1),
				new Point2D.Double(0,0)
		);		
		gaugeArrow.rotate(getAngleRad(), new Point2D.Double(0,0));
		arrow.fillPolygon(gaugeArrow);
	}
	
	private double getAngleRad(){
		return Math.toRadians(Utils.map(_value, _min, _max, 25, 295));
	}
	
	public void paint(Graphics g){
		if(buffer != null)
			buffer.dispose();
		
		img = new BufferedImage(_width,_height,BufferedImage.TYPE_INT_RGB);
		buffer = (Graphics2D) img.getGraphics();
		
		buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		buffer.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		
		createBackground();
		drawArrow();
		
		g.drawImage(img, 0, 0, this);
	}
	
	public void update(Graphics g){
		paint(g);
	}
	
	public void setValue(float value){
		if(_value == value)
			return;
		
		if(value > _max)
			_value = _max;
		else if(_value < _min)
			_value = _min;
		else
			_value = value;
		
		repaint();
	}
	
	public void setValueRad(float rad){
		setValue((float)Math.toDegrees(rad));
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(_width,_height);
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}

}
