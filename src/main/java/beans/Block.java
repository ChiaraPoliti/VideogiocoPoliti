package beans;

import enums.blockType;

public abstract class Block extends GameObject {
	protected blockType type;
	protected boolean isHit;
	
	public static final int BLOCK_SIZE = 16;
	
	
	public Block (int x, int y, blockType type) {
		super (x, y, BLOCK_SIZE, BLOCK_SIZE, null);
		this.type = type;
		this.isHit = false;	
	}
	
	public abstract GameObject hit();

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
