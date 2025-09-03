package beans;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import core.TileMap;


/**
 * Classe che definisce in maniera astratta tutti gli oggetti dinamici che saranno
 * presenti nella mia mappa del gioco.
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
	
	/**
	 * I due metodi astratti che dovranno essere implementati da ogni sottoclasse
	 * 
	 * @param mapWidthPixels è la larghezza della mappa;
	 * @param mapHeightPixels è l'altezza della mappa;
	 * @param tileMap è la mappa vera e propria.
	 */
	 
	public abstract void update (int mapWidthPixels, int mapHeightPixels, TileMap tileMap); //Aggiornamemto logico
	public abstract void draw(Graphics2D g, int cameraX, int cameraY); //Disegna gli oggetti con le giuste immagini
	
	/**
	 * Metodo che definisce un rettangolo che circonda l'oggetto, utile per le collisioni
	 * @return Rectangle.
	 */
	public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
	
	
	//GETTER E SETTER
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Image getImage() {
		return image;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
