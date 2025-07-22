package beans;


import java.awt.Graphics2D;
import java.awt.Image;

import core.TileMap;

/** 
 * @author Chiara
 */

public abstract class Enemy extends MovableGameObject {
	/** Classe astratta che definisce il nemico,
	 * si dovrà estendere con le tipologie specifiche.
	 */
	protected boolean isAlive;
	protected boolean isRemovable;
	
	public static final int VEL_X = 3;
	
	
	public Enemy (int x, int y, int width, int height, Image image) {
		super (x,y,width, height, image, 0, 0);
		this.isMovingLeft = true;
		this.isMovingRight = !this.isMovingLeft;
		this.isOnGround = true;
		this.isAlive = true;
		this.isRemovable = false;
	}



	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
		if(isAlive) {
			if (isMovingLeft) {
			this.vel_x = -VEL_X;
			} else if (isMovingRight){
				this.vel_x = VEL_X;
			} else {
				this.vel_x = 0;
			}
			
			super.update(mapWidthPixels, mapHeightPixels, tileMap);
			if (this.y > mapHeightPixels + 100) { // Se è caduto troppo in basso
	            this.die();
			}
		}else {
			return;
		}
		
	}
	
	
	public void die() {
		this.isAlive = false;
		this.isRemovable = true;
	}
	
	
	
	public abstract void dealDamage(Player mario);
	public abstract void draw(Graphics2D g, int cameraX, int cameraY);
	
	//le collisioni sono gestite nel CollisionManager

}
