package net.alienx.ihm_stab.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageUtils {

	public static BufferedImage getImagesAlpha(String file, Color alpha){
		BufferedImage img = loadImage(file);
		BufferedImage aimg = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) aimg.getGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(img,null,0,0);
		g.dispose();
		for(int i=0;i< aimg.getHeight();i++){
			for(int j=0;j<aimg.getWidth();j++){
				if(aimg.getRGB(j, i) == alpha.getRGB()){
					aimg.setRGB(j, i, 0x8F1C1C);
				}
			}
		}
		return aimg;
	}
	
	
	public static BufferedImage loadImage(String ref) {  
         BufferedImage bimg = null;  
         try {  
   
             bimg = ImageIO.read(new File(ref));  
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
         return bimg;  
     }
}
