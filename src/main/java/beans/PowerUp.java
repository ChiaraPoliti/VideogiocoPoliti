package beans;

import java.awt.Color;
import java.awt.Graphics2D;
import core.TileMap;
import enums.itemType;

/**
 * Classe astratta per la creazione dei potenziamenti.
 * Nel mio progetto inserirò solo il fungo classico, ma il codice
 * è facilmente estendibile con specializzazioni della classe
 *  
 */

public abstract class PowerUp extends GameObject {
	protected int vel_y;
	protected int initial_y;
	protected boolean isRising;
	protected boolean isCollected;
	protected itemType type;
	
	
	protected static final int POWER_UP_SIZE = 16;
	protected static final int RISE_SPEED = 2;
	protected static final int RISE_HEIGHT = 16;
	
	public PowerUp(int x, int y) {
		super(x, y, POWER_UP_SIZE, POWER_UP_SIZE, null);
		this.isCollected = false;
		this.isRising = false;
		this.vel_y = RISE_SPEED;
		this.initial_y = y;
	}
	
	
	
	/**
	 * Metodo che applica l'effetto del potenziamento su Mario
	 * È astratta: viene definito dalle sottoclassi concrete
	 * @param mario: il giocatore su cui si applica l'effetto
	 */
	public abstract void applyEffect(Player mario);
	
	/**
	 * Metodo che aggorna il movimento: per i potenziamenti l'unico movimento concesso è la fuoriuscita dal blocco
	 * @param mapWidthPixels lunghezza della mappa in pixel
	 * @param mapHeightPixels altezza della mappa in pixel
	 * @param tileMap Mappa del gioco
	 */
	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
		if (this.isCollected) {
			return; //se il potenziamento è già stato raccolto, uscire, non si fa più nulla
		}

		if (this.isRising) { // se sta 'nascendo'
			this.y += this.vel_y; //aggiorno posizione con la velocità di uscita, MRU

			if (this.y <= (this.initial_y - RISE_HEIGHT)) { // se la posizione non è quella finale 
				this.y = (this.initial_y - RISE_HEIGHT); //fisso la posizione a quella finale (scatto diretto a quella nuova)
				this.isRising = false; //spette di 'nascere'
				this.vel_y = 0; // ferma la velocità di salita
			}
		}
	}
	
	
	/**
	 * Disegna il PowewrUp con le immagini importate nel costruttore
	 */
	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (!this.isCollected) { //se non è già stato raccolto
			int screenX = this.x - cameraX; // fisso le coordinate dello schermo, adattando le coordinate dell'entità alla camera (scroll)
			int screenY = this.y - cameraY;

			if (this.image != null) {
				g.drawImage(this.image, screenX, screenY, this.width, this.height, null); //disegno l'entità
			} else {
				g.setColor(Color.MAGENTA); // alternativa se l'immagine manca
				g.fillRect(screenX, screenY, this.width, this.height);
			}
		}
	}

	//GETTER E SETTER
	public int getVel_y() {
		return vel_y;
	}

	public int getInitial_y() {
		return initial_y;
	}

	public boolean isRising() {
		return isRising;
	}

	public boolean isCollected() {
		return isCollected;
	}

	public itemType getType() {
		return type;
	}

	public static int getPowerUpSize() {
		return POWER_UP_SIZE;
	}

	public static int getRiseSpeed() {
		return RISE_SPEED;
	}

	public static int getRiseHeight() {
		return RISE_HEIGHT;
	}

	public void setVel_y(int vel_y) {
		this.vel_y = vel_y;
	}

	public void setInitial_y(int initial_y) {
		this.initial_y = initial_y;
	}

	public void setRising(boolean isRising) {
		this.isRising = isRising;
	}

	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}

	public void setType(itemType type) {
		this.type = type;
	}
}
