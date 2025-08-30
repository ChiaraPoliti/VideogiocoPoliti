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
	
	public static final int VEL_X = 1;
	
	
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
	    if (isAlive) {
	        CollisionManager cm = new CollisionManager();

	        // 1. Gestione del movimento orizzontale
	        int nextX = this.x;
	        if (isMovingLeft) {
	            nextX -= VEL_X;
	        } else if (isMovingRight) {
	            nextX += VEL_X;
	        }
	        
	        // 2. Controllo e aggiornamento della posizione orizzontale
	        Rectangle futureBoundsX = new Rectangle(nextX, this.y, this.width, this.height);
	        if (cm.checkMapCollision(futureBoundsX, tileMap)) {
	            // Cambia direzione se c'è una collisione
	            this.isMovingLeft = !isMovingLeft;
	            this.isMovingRight = !isMovingRight;
	        } else {
	            this.x = nextX;
	        }

	        // 3. Applicazione della gravità
	        this.vel_y += g;
	        
	        // 4. Calcola la prossima posizione verticale
		    int nextY = this.y + (int) this.vel_y;
		    
		    // 5. Controllo e aggiornamento della posizione verticale
		    Rectangle futureBoundsY = new Rectangle(this.x, nextY, this.width, this.height);
		    if (cm.checkMapCollision(futureBoundsY, tileMap)) {
		    	if (vel_y == 0) {
		    		int tileRow = (nextY + this.height)/TileMap.TILE_SIZE;
		    		this.y = tileRow * TileMap.TILE_SIZE - this.height;
		    		this.isOnGround = true;
		    	} else if (vel_y < 0) {
		    		int tileRow = nextY / TileMap.TILE_SIZE;
		    		this.y = (tileRow +1) * TileMap.TILE_SIZE;
		    	}
		    	vel_y = 0;
		    } else {
		    	this.y = nextY;
		    	this.isOnGround= false;
		    }

	        /*// 4. Calcola la prossima posizione verticale
	        int nextY = this.y + (int) this.vel_y;
	        
	        // 5. Controllo e aggiornamento della posizione verticale
	        Rectangle futureBoundsY = new Rectangle(this.x, nextY, this.width, this.height);
	        if (cm.checkMapCollision(futureBoundsY, tileMap)) {
	            this.vel_y = 0;
	            this.isOnGround = true;
	            
	            // Posiziona l'oggetto perfettamente sulla superficie
	            int tileY = (this.y / TileMap.TILE_SIZE);
	            if (this.vel_y > 0) { // Caduta
	                 tileY = (this.y + this.height + (int)this.vel_y) / TileMap.TILE_SIZE;
	                 this.y = tileY * TileMap.TILE_SIZE - this.height;
	            } else { // Salto (non applicabile ai nemici, ma utile per robustezza)
	                 tileY = (this.y + (int)this.vel_y) / TileMap.TILE_SIZE;
	                 this.y = (tileY + 1) * TileMap.TILE_SIZE;
	            }
	        } else {
	            this.y = nextY;
	            this.isOnGround = false;
	        }*/
		    
		    int footX = isMovingRight ? x + width + VEL_X : x - VEL_X;
		    int footY = y + height + 1; // un pixel sotto i piedi
		    Rectangle footCheck = new Rectangle(footX, footY, 1, 1);

		    if (!cm.checkMapCollision(footCheck, tileMap)) {
		        // manca terreno → inverti direzione
		        isMovingLeft = !isMovingLeft;
		        isMovingRight = !isMovingRight;
		    }

	        // 6. Limiti della mappa
	        if (this.x < 0) this.x = 0;
	        if (this.x + this.width > mapWidthPixels) this.x = mapWidthPixels - this.width;
	        if (this.y > mapHeightPixels) { 
	            this.die();
	        }
	    }
	}
	
	/*@Override
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
	}*/
	
	
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
