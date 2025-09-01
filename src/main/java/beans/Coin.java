package beans;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import core.TileMap;

public class Coin extends GameObject {
	private int value;
	private boolean isCollected;
	private int vel_y;
	private boolean isRising;
	private boolean isRemovable;
	private int initial_y;
	protected boolean isSpawned;
	
	public final static int COIN_SIZE = 16;
	public final static int VALUE = 5;
	public static final int RISE_SPEED = 2;
	public static final int RISE_HEIGHT = 16;
	
	
	public Coin (int x, int y) {
		super(x, y, COIN_SIZE, COIN_SIZE, null);
		this.value = VALUE;
		this.isCollected = false;
		this.vel_y = 0;
		this.isRising = false;
		this.isRemovable = false;
		this.initial_y = y;
	
		
		
		try {
			java.net.URL imageUrl = getClass().getResource("/images/coin.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di moneta caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di moneta non trovata nel classpath: /images/coin.png");
	            this.image = null;
	        }
		}catch (Exception e) {
	        System.err.println("Eccezione durante il caricamento dell'immagine di moneta: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	     }  
	}

	public void collect() {
		this.isCollected = true;
		this.isRemovable = true;
	}

	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
		if (this.isCollected) {
			return;
		}

		if (this.isRising) {
			this.y += this.vel_y; 

			if (this.y <= (this.initial_y - RISE_HEIGHT)) {
				this.y = (this.initial_y - RISE_HEIGHT); 
				this.isRising = false; 
				this.vel_y = 0;
			}
		}
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (!this.isCollected) {
			int screenX = this.x - cameraX;
			int screenY = this.y - cameraY;

			if (this.image != null) {
				g.drawImage(this.image, screenX, screenY, this.width, this.height, null);
			} else {
				g.setColor(Color.MAGENTA);
				g.fillRect(screenX, screenY, this.width, this.height);
			}
		}
	}
	
	public Rectangle getBounds() {
	    return new Rectangle(x, y, width, height);
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the isCollected
	 */
	public boolean isCollected() {
		return isCollected;
	}

	/**
	 * @return the vel_y
	 */
	public int getVel_y() {
		return vel_y;
	}

	/**
	 * @return the isRising
	 */
	public boolean isRising() {
		return isRising;
	}

	/**
	 * @return the initial_y
	 */
	public int getInitial_y() {
		return initial_y;
	}

	/**
	 * @return the coinSize
	 */
	public static int getCoinSize() {
		return COIN_SIZE;
	}


	/**
	 * @return the riseSpeed
	 */
	public static double getRiseSpeed() {
		return RISE_SPEED;
	}

	/**
	 * @return the riseHeight
	 */
	public static int getRiseHeight() {
		return RISE_HEIGHT;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @param isCollected the isCollected to set
	 */
	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}

	/**
	 * @param vel_y the vel_y to set
	 */
	public void setVel_y(int vel_y) {
		this.vel_y = vel_y;
	}

	/**
	 * @param isRising the isRising to set
	 */
	public void setRising(boolean isRising) {
		this.isRising = isRising;
	}

	/**
	 * @param initial_y the initial_y to set
	 */
	public void setInitial_y(int initial_y) {
		this.initial_y = initial_y;
	}
	
	
	
	
}
