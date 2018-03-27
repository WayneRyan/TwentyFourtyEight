import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class Tile {
	int x,y,value;
	public static final int SIZE = 175;
	
	public Tile(int r, int c){
		update(r,c);
		if(Math.random()<0.5){
			value = 2;
		}else{
			value = 4;
		}
	}
	
	
	public void draw(Graphics g){
		Font f = g.getFont();
		
		if(value<10)f = f.deriveFont(120f);
		else if(value<100)f = f.deriveFont(80f);
		else f = f.deriveFont(50f);
		g.setFont(f);
		g.setColor(Color.white);
		g.fillRect(x+3, y+3, SIZE-6, SIZE-6);
		g.setColor(Color.black);
		g.drawString(""+value, x+40, y+130);
	}


	public void update(int r, int c) {
		x = c*(780/4)+20;
		y = r*(780/4)+50;
	}
}
