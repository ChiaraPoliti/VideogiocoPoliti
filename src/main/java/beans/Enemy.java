package beans;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import core.TileMap;
import logic.CollisionManager;

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


	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
		if(isAlive) {
			if (isMovingLeft) {
			this.vel_x = -VEL_X;
			} else if (isMovingRight){
				this.vel_x = VEL_X;
			} else {
				this.vel_x = 0;
			}
			
			// Calcola la prossima posizione orizzontale
			int nextX = this.x + this.vel_x;
			CollisionManager cm = new CollisionManager();
			Rectangle futureBoundsX = new Rectangle((int) nextX, (int) this.y, this.width, this.height);
						
			// Controlla se sta per colpire un muro e inverte la direzione
			if (cm.checkMapCollision(futureBoundsX, tileMap)) {
				this.isMovingLeft = !isMovingLeft;
				this.isMovingRight = !isMovingRight;
			} else {
				this.x = nextX;
			}


			// Applica la gravità per farli cadere
			this.vel_y += g;
			int nextY = this.y + this.vel_y;
			
			// Controlla le collisioni verticali solo per la caduta
			Rectangle futureBoundsY = new Rectangle((int) this.x, (int) nextY, this.width, this.height);
			if (cm.checkMapCollision(futureBoundsY, tileMap)) {
				if (this.vel_y > 0) {
					this.vel_y = 0;
					//this.y = (futureBoundsY.y / TileMap.TILE_SIZE) * TileMap.TILE_SIZE - this.height;
					this.isOnGround = true;
					int collisionRow = (this.y + this.height) / TileMap.TILE_SIZE;
	                this.y = collisionRow * TileMap.TILE_SIZE - this.height;
				} else {
					this.vel_y = 0;
					int collisionRow = (this.y / TileMap.TILE_SIZE);
	                this.y = (collisionRow + 1) * TileMap.TILE_SIZE;
				}
			} else {
				this.y = nextY;
				this.isOnGround = false;
			}
			
			if (this.x < 0) {
				this.x = 0;
			}
			
			
			if (this.y > mapHeightPixels) { // Se è caduto troppo in basso
	            this.die();
			}
		}
	}
	
	
	public void die() {
		this.isAlive = false;
		this.isRemovable = true;
	}
	
	/**
	 * @return the isAlive
	 */
	public boolean isAlive() {
		return isAlive;
	}



	/**
	 * @return the isRemovable
	 */
	public boolean isRemovable() {
		return isRemovable;
	}



	/**
	 * @return the velX
	 */
	public static int getVelX() {
		return VEL_X;
	}



	/**
	 * @param isAlive the isAlive to set
	 */
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}



	/**
	 * @param isRemovable the isRemovable to set
	 */
	public void setRemovable(boolean isRemovable) {
		this.isRemovable = isRemovable;
	}



	public abstract void dealDamage(Player mario);
	public abstract void draw(Graphics2D g, int cameraX, int cameraY);
	
	//le collisioni sono gestite nel CollisionManager

}
