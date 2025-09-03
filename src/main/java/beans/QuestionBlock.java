package beans;

import enums.blockType;
import enums.itemType;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Blocco che contiene un'entità del gioco, potenziamento o moneta
 */
public class QuestionBlock extends Block {
	private Image emptyBlock;
	private itemType contentItemType;
	private boolean isContentReleased; 
	
	public QuestionBlock(int x, int y, itemType contentItemType) {
		super(x,y, blockType.QUESTION);
		this.contentItemType = contentItemType;
		this.isContentReleased = false;
		
		//estraggo sia l'immagine standard che l'immagine da colpito
		try {
			java.net.URL imageUrl = getClass().getResource("/tiles/tile_2.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di blocco ? caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di blocco ? non trovata nel classpath: /tiles/tile_2.png");
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
	        System.err.println("Eccezione durante il caricamento dell'immagine di blocco vuoto: " + e.getMessage());
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
		if(!this.isContentReleased) {
			super.setHit(true);
			this.isContentReleased = true;
			//System.out.println("Blocco vuoto.");
			this.bounceVelY = -BOUNCE_SPEED; // fisso la velocità di rimbalzo a valore negativo (così sale e si attiva l'if in Block.update()
			return createItem(); // restituisce l'oggetto contenuto nel blocco
		} else {
			if (this.image == this.emptyBlock) {
				this.bounceVelY = 0; // se il contenuto è già stato rilasciato, il blocco rimane fisso
				//System.out.println("Blocco già vuoto.");
			}
			return null;
		}
	}

	/**
	 * Metodo per la generazione degli oggetti contenuti nei blocchi ?
	 * @return GameObject, che può essere al momento solo Coin o Mushroom
	 */
	private GameObject createItem() {
		int itemX = this.x; //fisso le coordinate dell'oggetto: uguale a quella del blocco
        int itemY = this.y - Block.BLOCK_SIZE; // y più alta perché l'oggetto esce sopra il blocco
        
        switch (contentItemType) {
        case COIN:
            Coin coin = new Coin(itemX, itemY);
            coin.setVel_y(-BOUNCE_SPEED); // La moneta "salta" verso l'alto
            return coin;
            
        case MUSHROOM:
            Mushroom mushroom = new Mushroom(itemX, itemY);
            mushroom.setVel_y(-BOUNCE_SPEED); 
            return mushroom;
            
        //possibili espansioni del gioco, non implementate ad ora
        /*case STAR:
         	Star star = new Star (itemX, itemY);
         	star.setVel_y(-BOUNCE_SPEED)
            return star;
        
        case FIREFLOWER:
        	FireFlower ff = new FireFlower (itemX, itemY)
        	ff.setVel_y(-BOUNCE_SPEED);
            return flower;*/
            
        default:
            System.err.println("Tipo di item non supportato: " + contentItemType);
            return null;
        }
	}

	/**
	 * Metodo che disegna gli oggetti usando le immagini importate nel costruttore
	 */
	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		int screenX = this.x - cameraX; // fisso le coordinate dello schermo, adattando le coordinate dell'entità alla camera (scroll)
		int screenY = this.y - cameraY;
		Image currentImage = null; // definisco una nuova variabile che contiene l'immagine istantanea del blocco
		
		if (this.isContentReleased) { //se il contenuto è già stato rilasciato
			currentImage = this.emptyBlock;
		} else {
			currentImage = this.image;
		} 
		
		if (currentImage != null) {
			g.drawImage(currentImage, screenX, screenY, this.width, this.height, null);	//disegno
		} else {
			if (this.isContentReleased) {
				g.setColor(Color.YELLOW.darker()); //colori alternativi in caso di assenza delle immagini
			} else {
				g.setColor(Color.YELLOW);
			}
			g.fillRect (screenX, screenY, this.width, this.height);
		}
	}

	
	//GETTER E SETTER
	public Image getEmptyBlock() {
		return emptyBlock;
	}

	public itemType getContentItemType() {
		return contentItemType;
	}

	public boolean isContentReleased() {
		return isContentReleased;
	}
	public void setEmptyBlock(Image emptyBlock) {
		this.emptyBlock = emptyBlock;
	}

	public void setContentItemType(itemType contentItemType) {
		this.contentItemType = contentItemType;
	}

	public void setContentReleased(boolean isContentReleased) {
		this.isContentReleased = isContentReleased;
	}
}
