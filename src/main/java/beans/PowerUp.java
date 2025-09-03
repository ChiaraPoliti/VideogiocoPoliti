package beans;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import core.TileMap;
import enums.itemType;

public abstract class PowerUp extends GameObject {
	/**
	 * Classe astratta per la creazione dei potenziamenti.
	 * Nel mio progetto inserirò solo il fungo classico, ma il codice
	 * è facilmente estendibile con specializzazioni della classe
	 *  
	 */
	
	protected double vel_y;
	protected boolean isRising;
	protected int initial_y;
	protected boolean isCollected;
	protected itemType type;
	
	
	public static final int POWER_UP_SIZE = 16;
	public static final int RISE_SPEED = 2;
	public static final int RISE_HEIGHT = 16;
	
	public PowerUp(int x, int y) {
		super(x, y, POWER_UP_SIZE, POWER_UP_SIZE, null);
		this.isCollected = false;
		this.isRising = false;
		this.vel_y = 0;
		this.initial_y = y;
	}
	
	
	public abstract void applyEffect(Player mario);
	/**
	 * Updates the state of the PowerUp.
	 * This method handles the initial rising movement.
	 * @param mapWidthPixels The width of the game map in pixels.
	 * @param mapHeightPixels The height of the game map in pixels.
	 * @param tileMap The TileMap of the game.
	 */
	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
		// If the power-up is collected or not alive, stop updating it.
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
        return new Rectangle(x, y, getWidth(), getHeight());
    }


	/**
	 * @return the vel_y
	 */
	public double getVel_y() {
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
	 * @return the isCollected
	 */
	public boolean isCollected() {
		return isCollected;
	}


	/**
	 * @return the powerUpSize
	 */
	public static int getPowerUpSize() {
		return POWER_UP_SIZE;
	}


	/**
	 * @param vel_y the vel_y to set
	 */
	public void setVel_y(double vel_y) {
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


	/**
	 * @param isCollected the isCollected to set
	 */
	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}

	
	/**
	 * @return the type
	 */
	public itemType getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(itemType type) {
		this.type = type;
	}
	
	
	
	
}
