package beans;

import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import enums.blockType;

/**
 * Blocco che è solido, non si rompe e non cambia 
 */
public class SolidBlock extends Block {

	public SolidBlock(int x, int y) {
		super(x,y, blockType.SOLID);
		
		//estraggo sia l'immagine standard che l'immagine da colpito
		try {
			java.net.URL imageUrl = getClass().getResource("/tiles/tile_3.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di blocco solido caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di blocco solido non trovata nel classpath: /tiles/tile_3.png");
	            this.image = null;
	        }
	    } catch (Exception e) {
	        System.err.println("Eccezione durante il caricamento dell'immagine di blocco solido: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	    }
	}
	
	/**
	 * Metodo che definsice il comportamento di un blocco quando viene colpito
	 * @return GameObject, l'oggetto contenuto, generato dal metodo createItem()
	 */
	@Override
	public GameObject hit() {
		if(!this.isHit) {
			//System.out.println("Blocco colpito!");
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
		int screenY = this.y -cameraY;
		
		if(this.image != null) {
			g.drawImage(image, screenX, screenY, this.width, this.height, null); //disegno
		}else {
			g.setColor(java.awt.Color.BLACK); //colore alternativo
			g.fillRect(this.x, this.y, this.width, this.height);
		}
	}


}
