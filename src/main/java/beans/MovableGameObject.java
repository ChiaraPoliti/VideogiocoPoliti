package beans;

import java.awt.Image;

/**
 * Classe che definisce le generalità degli oggetti che si muovono anche orizzontalmente
 */

public abstract class MovableGameObject extends GameObject {
	protected int vel_x;
	protected int vel_y;
	protected boolean isOnGround;
	protected boolean isMovingLeft;
	protected boolean isMovingRight;
	
	protected static final double g = 1.0;

	public MovableGameObject(int x, int y, int width, int height, Image image, int vel_x, int vel_y) {
		super(x, y, width, height, image);
		this.vel_x = vel_x;
		this.vel_y = vel_y;
		this.isOnGround = true;
		this.isMovingLeft = false;
		this.isMovingRight = false;
	}
    
    // GETTER E SETTER
    public int getVel_x() { 
    	return vel_x; 
    }
    
    public int getVel_y() { 
    	return vel_y; 
    }
    
    public boolean isOnGround() { 
    	return isOnGround; 
    }
    
	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	public boolean isMovingRight() {
		return isMovingRight;
	}

	public static double getG() {
		return g;
	}
    
    public void setVel_x(int vel_x) { 
    	this.vel_x = vel_x; 
    }
    
    public void setVel_y(int vel_y) {
    	this.vel_y = vel_y;
    }
  
	public void setOnGround(boolean onGround) { 
		this.isOnGround = onGround; 
	}
	
    public void setMovingLeft(boolean isMovingLeft) {
    	this.isMovingLeft = isMovingLeft; 
    
    }
    public void setMovingRight(boolean isMovingRight) { 
    	this.isMovingRight = isMovingRight;
    }
}
