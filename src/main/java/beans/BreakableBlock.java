package beans;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import enums.blockType;

/**
 * Blocco che si rompe se colpito
 */
public class BreakableBlock extends Block {
	//Sarebbe da inserire per poter effettivamente eliminare i blocchi rotti dal loop del gioco
	//private boolean isRemovable;
	private Image emptyBlock;
	
	public BreakableBlock(int x, int y) {
		super(x,y, blockType.BREAKABLE);
		
		//importo le immagini che mi servono
		try {
			java.net.URL imageUrl = getClass().getResource("/tiles/tile_1.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di blocco caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di blocco non trovata nel classpath: /tiles/tile_1.png");
	            this.image = null;
	        }
	     
	        java.net.URL emptyBlockUrl = getClass().getResource("/tiles/tile_3.png"); 
            if (emptyBlockUrl != null) {
                this.emptyBlock = new ImageIcon(emptyBlockUrl).getImage(); 
            } else {
                System.err.println("ERRORE: Immagine blocco vuoto non trovata: /tiles/tile_3.png");
                this.emptyBlock = null;
            }
	    } catch (Exception e) {
	        System.err.println("Eccezione durante il caricamento dell'immagine di blocco: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	        this.emptyBlock = null;
	    }
	}

	/**
	 * Metodo che definsice il comportamento di un blocco quando viene colpito
	 * @return GameObject, l'oggetto contenuto, generato dal metodo createItem()
	 */
	@Override
	public GameObject hit() {
		if(!this.isHit) {
			this.isHit = true;
			//System.out.println("Blocco rotto!");
			//this.isRemovable = true;
			this.bounceVelY = -BOUNCE_SPEED;
		}
		return null;
	}

	/**
	 * Metodo che disegna gli oggetti usando le immagini importate nel costruttore
	 */
	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		int screenX = this.x - cameraX; // fisso le coordinate dello schermo, adattando le coordinate dell'entità alla camera (scroll)
		int screenY = this.y - cameraY;
		Image currentImage = null; // definisco una nuova variabile che contiene l'immagine istantanea del blocco
		
		if (this.isHit) { //in base al suo stato (colpito o no) varia l'immagine
			currentImage = this.emptyBlock;
		} else {
			currentImage = this.image;
		} 
		
		
		if(currentImage != null) { //se l'immagine esiste
			g.drawImage(currentImage, screenX, screenY, this.width, this.height, null); //la disegno
		}else {  
			g.setColor(new Color (140,140,230)); //fisso un nuovo colore (ciano) sostitutivo a riempimento
			g.fillRect(screenX, screenY, this.width, this.height);
		}
		
	}
	
	//GETTER E SETTER
	public Image getEmptyBlock() {
		return emptyBlock;
	}

	public void setEmptyBlock(Image emptyBlock) {
		this.emptyBlock = emptyBlock;
	}
}
