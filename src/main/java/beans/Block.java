package beans;

import java.awt.Rectangle;
import core.TileMap;
import enums.blockType;

/**
 * Classe astratta che estende GameObject che definisce il concetto di blocco.
 * Non è un oggetto mobile (non si sposta orizzontalmente), ma ha un minimo spostamento se colpito
 */
public abstract class Block extends GameObject {
	protected blockType type;
	protected boolean isHit;
	protected int bounceVelY;
	protected int originalY;
	
	protected static final int BLOCK_SIZE = 16;
	protected static final int BOUNCE_HEIGHT = 10; 
    protected static final int BOUNCE_SPEED = 2;
	
	
	public Block (int x, int y, blockType type) {
		super (x, y, BLOCK_SIZE, BLOCK_SIZE, null);
		this.type = type;
		this.isHit = false;	
		this.bounceVelY = 0;
		this.originalY = y;
	}
	
	/**
	 * Metodo che crea un'area rettangolare intorno al blocco centrata nel punto in basso a sinistra
	 * Utile per le collisioni
	 */
	public Rectangle getTriggerBox() {
		return new Rectangle(this.x, this.y + this.height, this.width, 10);
	}

	/**
	 * Metodo ereditato da GameObject: aggiorna la logica di 'moto' del blocco:
	 * effettua un piccolo rimbalzo se colpito da Mario
	 */
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
		if (bounceVelY != 0) { // se il blocco si muove in qualche modo
            this.y += bounceVelY; // a priori aggiorno la posizione per MRU
            if (bounceVelY < 0) { //se il blocco sale
                if (this.y <= originalY - BOUNCE_HEIGHT) { // se la posizione nuova nel blocco non ha superato la soglia fissata di rimbalzo
                    this.y = originalY - BOUNCE_HEIGHT; // aggiorno la posizione del blocco fissandola a quella in cui arrivo
                    bounceVelY = BOUNCE_SPEED; //e inverto il senso della velocità per farlo scendere
                } //
            } else { //se il blocco scende
                if (this.y >= originalY) { // se la posizione nuova è almeno uguale a quella originale
                    this.y = originalY; //fisso la posizione aquella originale
                    bounceVelY = 0; // azzero la velocità
                }
            }
        }
    }
	
	//Metodo astratto che definisce il comportamento del blocco.
	//Dipende dal tipo di blocco considerato.
	public abstract GameObject hit(); // qui verrà impostata la velocità dei blocchi negativa, in modo da far scattare l'if presente nell'update()

	
	//GETTER E SETTER
	public blockType getType() {
		return type;
	}

	public boolean isHit() {
		return isHit;
	}

	
	public int getBounceVelY() {
		return bounceVelY;
	}

	public int getOriginalY() {
		return originalY;
	}
	
	public static int getBlockSize() {
		return BLOCK_SIZE;
	}

	public static int getBounceHeight() {
		return BOUNCE_HEIGHT;
	}

	public static int getBounceSpeed() {
		return BOUNCE_SPEED;
	}

	public void setType(blockType type) {
		this.type = type;
	}

	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}

	public void setBounceVelY(int bounceVelY) {
		this.bounceVelY = bounceVelY;
	}

	public void setOriginalY(int originalY) {
		this.originalY = originalY;
	}	
	
}
