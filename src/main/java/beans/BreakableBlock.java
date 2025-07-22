package beans;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import core.TileMap;
import enums.blockType;

public class BreakableBlock extends Block {
	protected boolean isRemovable;
	
	public BreakableBlock(int x, int y) {
		super(x,y, blockType.BREAKABLE );
		
		try {
			java.net.URL imageUrl = getClass().getResource("/images/block.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di blocco caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di blocco non trovata nel classpath: /images/block.png");
	            this.image = null;
	        }
	    } catch (Exception e) {
	        System.err.println("Eccezione durante il caricamento dell'immagine di blocco: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	    }
	}

	@Override
	public GameObject hit() {
		if(!this.isHit) {
			this.isHit = true;
			System.out.println("Blocco rotto!");
			this.isRemovable = true;
		}
		return null;
	}

	
	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
    }
	
	
	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		int screenX = this.x - cameraX;
		int screenY = this.y - cameraY;
		
		if (!this.isHit) {
			if(this.image != null) {
				g.drawImage(image, screenX, screenY, this.width, this.height, null);
			}else {
				g.setColor(java.awt.Color.BLACK);
				g.fillRect(this.x, this.y, this.width, this.height);
			}
		}
	}

}
