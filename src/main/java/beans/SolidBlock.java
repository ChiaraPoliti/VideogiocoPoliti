package beans;

import java.awt.Graphics2D;
import javax.swing.ImageIcon;

import core.TileMap;
import enums.blockType;

public class SolidBlock extends Block {

	public SolidBlock(int x, int y) {
		super(x,y, blockType.SOLID);
			
		try {
			java.net.URL imageUrl = getClass().getResource("/images/emptyBlock.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di blocco solido caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di blocco solido non trovata nel classpath: /images/emptyBlock.png");
	            this.image = null;
	        }
	    } catch (Exception e) {
	        System.err.println("Eccezione durante il caricamento dell'immagine di blocco solido: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	    }
	}
	
	@Override
	public GameObject hit() {
		if(!this.isHit) {
			System.out.println("Blocco colpito!");
		}
		return null;
	}
	
	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
    }

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		int screenX = this.x - cameraX;
		int screenY = this.y -cameraY;
		
		if(this.image != null) {
			g.drawImage(image, screenX, screenY, this.width, this.height, null);
		}else {
			g.setColor(java.awt.Color.BLACK);
			g.fillRect(this.x, this.y, this.width, this.height);
		}
	}


}
