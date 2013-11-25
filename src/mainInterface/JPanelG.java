package mainInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;


/*! \brief a EFFACER
 * 	
 * 
 * */
public class JPanelG extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4741716392408703199L;
	private Color ss3Color = Color.ORANGE;
	private Color powerSIMColor = Color.ORANGE;
	private Color blinkColor = Color.GRAY;

	private int sep = 10;
	private int recul = 100;

	private int height =20;
	private int pageHeight;
	private int pageWidth;
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		pageWidth = this.getWidth();
		pageHeight=this.getHeight();
		
		/*
		g.setColor(ss3Color);
		g.fill3DRect(0, pageHeight-recul, pageWidth, height,true);
		g.setColor(Color.black);
		Font font = new Font("Time New Roman",Font.BOLD,12);
		g.setFont(font);
		g.drawString("        SS3", 2, pageHeight-recul+14);
		

		g.setColor(powerSIMColor);
		g.fill3DRect(0, pageHeight-(recul+ sep+ height), pageWidth, 20,true);
		g.setColor(Color.black);
		g.setFont(font);
		g.drawString("        AHTP", 2, pageHeight-(recul+ sep+ height)+14);
		*/
	}
	
	public void setSS3Color(Color c){
		ss3Color = c;
	}
	public void setPowerSIMColor(Color c){
		powerSIMColor = c;
	}
	
	public void setBlinkColor(Color c){
		blinkColor = c;
	}

}
