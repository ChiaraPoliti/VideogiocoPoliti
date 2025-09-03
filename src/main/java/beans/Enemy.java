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
	protected boolean isDangerous;
	
	public static final int VEL_X = 1;
	public static final int MAX_FALL_SPEED = 1;
	
	
	public Enemy (int x, int y, int width, int height, Image image) {
		super (x,y,width, height, image, 0, 0);
		this.isMovingLeft = true;
		this.isMovingRight = !this.isMovingLeft;
		this.isOnGround = true;
		this.isAlive = true;
		this.isRemovable = false;
		this.isDangerous = true;
	}
 
	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
	    if (isAlive) {
	        // La logica di collisione è stata spostata nel CollisionManager.
	        // Questa classe si occupa solo del movimento.

	        // 1. Gestione del movimento orizzontale
	        int nextX = this.x;
	        if (isMovingLeft) {
	            nextX -= VEL_X;
	        } else if (isMovingRight) {
	            nextX += VEL_X;
	        }
	        this.setX(nextX);
	        
	        // 2. Applicazione della gravità
	        this.vel_y += g;
	        if (this.vel_y > MAX_FALL_SPEED) {
	            this.vel_y = MAX_FALL_SPEED;
	        }
	        this.setY((int) (this.y + this.vel_y));
	        
	        // 3. Controlli per i limiti della mappa
	        if (this.x < 0) {
	        	this.x = 0;
	        	this.isMovingLeft = !this.isMovingLeft;
	        	this.isMovingRight = !this.isMovingRight;
	        }
	        if (this.x + this.width > mapWidthPixels) {
	        	this.x = mapWidthPixels - this.width;
	        	this.isMovingLeft = !this.isMovingLeft;
	        	this.isMovingRight = !this.isMovingRight;
	        }
	        if (this.y > mapHeightPixels) { 
	            this.die();
	        }
	    }
	}
	
	//MIO FUNZIONA
	/*
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
		    } /////FINO A QUI

	        // 4. Calcola la prossima posizione verticale
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
	        }
		    
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
	
	}


