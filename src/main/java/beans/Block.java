package beans;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import core.TileMap;
import enums.blockType;

public abstract class Block extends GameObject {
	protected blockType type;
	protected boolean isHit;
	//protected int bounceVelY;
	//protected int originalY;
	
	public static final int BLOCK_SIZE = 16;
	protected static final int BOUNCE_HEIGHT = 10; 
    protected static final int BOUNCE_SPEED = 2;
	
	
	public Block (int x, int y, blockType type) {
		super (x, y, BLOCK_SIZE, BLOCK_SIZE, null);
		this.type = type;
		this.isHit = false;	
		//this.bounceVelY = 0;
		//this.originalY = y;
	}
	
	/*public Rectangle getHitBox() {
	    int extraWidth = 14;  // allarga 2 pixel per lato
	    int extraHeight = 10; // allunga un po' verso l'alto
	    return new Rectangle(
	        this.x - extraWidth / 2,
	        this.y - extraHeight,
	        this.width + extraWidth,
	        this.height + extraHeight
	    );
	}*/
	
	public Rectangle getTriggerBox() {
	    return new Rectangle(this.x-1, this.y + this.height - 13, this.width, 32);
		//return new Rectangle(this.x, this.y + this.height, this.width, 5);
	}

	
	public abstract GameObject hit();
	public abstract void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap);
	public abstract void draw (Graphics2D g, int cameraX, int cameraY);

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
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the type
	 */
	public blockType getType() {
		return type;
	}

	/**
	 * @return the isHit
	 */
	public boolean isHit() {
		return isHit;
	}

	
	
	
	/**
	 * @return the blockSize
	 */
	public static int getBlockSize() {
		return BLOCK_SIZE;
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
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param isHit the isHit to set
	 */
	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}

	
	
}
