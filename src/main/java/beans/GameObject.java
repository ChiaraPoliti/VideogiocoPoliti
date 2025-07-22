package beans;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import core.TileMap;


/**
 * Classe che definisce in maniera astratta tutti gli oggetti dinamici che saranno
 * presenti nella mia mappa del gioco.
 * @author Chiara
 */

public abstract class GameObject {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected Image image;
	
	public GameObject (int x, int y, int width, int height, Image image) {	
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = image;
	}
	
	
	public abstract void update (int mapWidthPixels, int mapHeightPixels, TileMap tileMap);
	public abstract void draw(Graphics2D g, int cameraX, int cameraY);
	
	public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	
	
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}


	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	
	

}
