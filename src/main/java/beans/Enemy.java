package beans;


import java.awt.Graphics2D;
import java.awt.Image;
import core.TileMap;


/** 
 * Classe astratta che definisce il nemico
 */

public abstract class Enemy extends MovableGameObject {
	protected boolean isAlive;
	protected boolean isRemovable;
	protected boolean isDangerous;
	
	protected static final int VEL_X = 1;
	protected static final int MAX_FALL_SPEED = 1;
	
	public Enemy (int x, int y, int width, int height, Image image) {
		super (x,y,width, height, image, 0, 0);
		this.isMovingLeft = true;
		this.isMovingRight = !this.isMovingLeft;
		this.isOnGround = true;
		this.isAlive = true;
		this.isRemovable = false;
		this.isDangerous = true;
	}
 
	/**
	 * Metodo che si occupa della gestione del moto dei nemici:
	 * la logica di collisione è gestita dalla classe CollisionManager nel package logic
	 */
	
	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
	    if (isAlive) { //se è vivo
	    	
	        // 1. Gestione del movimento orizzontale
	        int nextX = this.x; //fisso la posizione X iniziale
	        this.vel_x = VEL_X; // fisso la velocità orizzontale
	        if (isMovingLeft) { // se si muove a sx 
	            nextX -= this.vel_x; //la posizione X viene decrementata della velocità orizzontale MRU
	        } else if (isMovingRight) { //se si muove a dx
	            nextX += this.vel_x; // la posizione X viene incrementata della velocità orizzontale MRU
	        }
	        this.setX(nextX); // setto la posizione X al nuovo valore aggiornato
	        
	        // 2. Applicazione della gravità
	        this.vel_y += g; //applico la gravità
	        if (this.vel_y > MAX_FALL_SPEED) { //se la velocità verticale supera la soglia
	            this.vel_y = MAX_FALL_SPEED; // fisso la velocità verticale alla soglia massima
	        }
	        
	        this.setY(this.y + this.vel_y); //setto la posizione Y al nuovo valore aggiornato
	        
	        // 3. Controlli per i limiti della mappa
	        if (this.x < 0) { //se la X del nemico è troppo a sinistra
	        	this.x = 0; //sposto il nemico nell'origine
	        	this.isMovingLeft = !this.isMovingLeft; //setto il moto con i comandi invertiti
	        	this.isMovingRight = !this.isMovingRight;
	        }
	        if (this.x + this.width > mapWidthPixels) { //se la posizione X sommata alla larghezza del neico supera la larghezza della mappa
	        	this.x = mapWidthPixels - this.width; //fisso la posizione X più indietro (spostato verso sinistra della larghezza del nemico)
	        	this.isMovingLeft = !this.isMovingLeft; //controllo i comandi invertiti
	        	this.isMovingRight = !this.isMovingRight;
	        }
	        if (this.y > mapHeightPixels) { //se la posizione Y è troppo in basso (supera l'altezza della mappa)
	            this.die(); // il nemico muore
	        }
	    }
	}
	
	/**
	 * Metodo che definisce la morte del nemico, settando i booleani relativi
	 */
	public void die() {
		this.isAlive = false; 
		this.isRemovable = true;
	}
	
	/**
	 * Metodi astratti che devono essere implementati nelle specializzazioni
	 */
	public abstract void dealDamage(Player mario); //gestione del danno a mario
	public abstract void draw(Graphics2D g, int cameraX, int cameraY); //disegna
	
	//GETTER E SETTER
	public boolean isAlive() {
		return isAlive;
	}

	public boolean isRemovable() {
		return isRemovable;
	}

	public boolean isDangerous() {
		return isDangerous;
	}

	public static int getVelX() {
		return VEL_X;
	}

	public static int getMaxFallSpeed() {
		return MAX_FALL_SPEED;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void setRemovable(boolean isRemovable) {
		this.isRemovable = isRemovable;
	}

	public void setDangerous(boolean isDangerous) {
		this.isDangerous = isDangerous;
	}
}


