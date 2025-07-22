package beans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Pipe {
	private int x;
	private int y;
	private int width;
	private int height;
	private Image image;
	
	public static final int PIPE_WIDTH = 10;
	public static final int PIPE_HEIGHT = 5;
	
	public Pipe (int x, int y) {
		this.x = x;
		this.y = y;
		this.width = PIPE_WIDTH;
		this.height = PIPE_HEIGHT;
		ImageIcon pipe = new ImageIcon (getClass().getResource("/images/pipe.png"));
		this.image = pipe.getImage();
	}
	
	public void draw(Graphics g) {
		if (this.image != null) {
			g.drawImage(this.image, this.x, this.y, this.width, this.height, null);
		} else {
			g.setColor(Color.RED);
			g.fillRect (this.x, this.y, this.width, this.height);
			}
	}
}
