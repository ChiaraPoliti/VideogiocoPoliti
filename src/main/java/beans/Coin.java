package beans;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import core.TileMap;

public class Coin extends GameObject {
	private int value;
	private int vel_y;
	private int initial_y;
	private boolean isCollected;
	private boolean isSpawned;
	private boolean isRising;
	private boolean isRemovable;
	
	private final static int COIN_SIZE = 16;
	private final static int VALUE = 5;
	private static final int RISE_SPEED = 2;
	private static final int RISE_HEIGHT = 16;
	
	
	public Coin (int x, int y) {
		super(x, y, COIN_SIZE, COIN_SIZE, null);
		this.value = VALUE;
		this.isCollected = false;
		this.vel_y = 0;
		this.isRising = false;
		this.isRemovable = false;
		this.initial_y = y;
	
		
		//estraggo l'immagine 
		try {
			java.net.URL imageUrl = getClass().getResource("/tiles/tile_14.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di moneta caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di moneta non trovata nel classpath: /tiles/tile_14.png");
	            this.image = null;
	        }
		}catch (Exception e) {
	        System.err.println("Eccezione durante il caricamento dell'immagine di moneta: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	     }  
	}
	
	/**
	 * Metodo ereditato da GameObject: aggiorna la logica di 'moto' del blocco:
	 * effettua un piccolo rimbalzo se colpito da Mario
	 */
	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
		if (this.isCollected) { // se è già stata raccolta, esce, non si deve fare nulla
			return; 
		}
		if (this.isRising) { //se sta uscendo
			this.y += this.vel_y; //aggiorno la posizine con la velocità MRU

			if (this.y <= (this.initial_y - RISE_HEIGHT)) { // se la posizione nuova è più bassa di quella fissata per l'apparizione
				this.y = (this.initial_y - RISE_HEIGHT); // allora fisso la  posizione a quella in cui arrivo
				this.isRising = false; //cambio il booleano di salita
				this.vel_y = 0; // fisso la velocità di salita a 0
			}
		}
	}

	/**
	 * Metodo che disegna gli oggetti usando le immagini importate nel costruttore
	 */
	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (!this.isCollected) { // se non è già stata raccolta 
			int screenX = this.x - cameraX; // fisso le coordinate dello schermo, adattando le coordinate dell'entità alla camera (scroll)
			int screenY = this.y - cameraY;

			if (this.image != null) {
				g.drawImage(this.image, screenX, screenY, this.width, this.height, null); // disegno l'entità, con l'immagine caricata, centrata nel punto di schermo definito prima
			} else {
				g.setColor(Color.MAGENTA); // definisco un colore alternativo se manca l'immagine
				g.fillRect(screenX, screenY, this.width, this.height); //riempio lo spazio dedicato all'entità
			}
		}
	}
	
	/**
	 * Metodo che cambia i booleani in base allo stato di raccolta
	 */
	public void collect() {
		this.isCollected = true;
		this.isRemovable = true;
	}

	//GETTER E SETTER
	public int getValue() {
		return value;
	}

	public int getVel_y() {
		return vel_y;
	}

	public int getInitial_y() {
		return initial_y;
	}

	public boolean isCollected() {
		return isCollected;
	}

	public boolean isSpawned() {
		return isSpawned;
	}

	public boolean isRising() {
		return isRising;
	}

	public boolean isRemovable() {
		return isRemovable;
	}

	public static int getCoinSize() {
		return COIN_SIZE;
	}

	public static int getRiseSpeed() {
		return RISE_SPEED;
	}

	public static int getRiseHeight() {
		return RISE_HEIGHT;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setVel_y(int vel_y) {
		this.vel_y = vel_y;
	}

	public void setInitial_y(int initial_y) {
		this.initial_y = initial_y;
	}

	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}

	public void setSpawned(boolean isSpawned) {
		this.isSpawned = isSpawned;
	}

	public void setRising(boolean isRising) {
		this.isRising = isRising;
	}

	public void setRemovable(boolean isRemovable) {
		this.isRemovable = isRemovable;
	}	
}
