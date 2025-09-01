package beans;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import core.TileMap;
import enums.blockType;

public class BreakableBlock extends Block {
	protected boolean isRemovable;
	private Image emptyBlock;
	
	public BreakableBlock(int x, int y) {
		super(x,y, blockType.BREAKABLE );
		
		try {
			java.net.URL imageUrl = getClass().getResource("/tiles/tile_1.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di blocco caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di blocco non trovata nel classpath: /tiles/tile_1.png");
	            this.image = null;
	        }
	     // Caricamento dell'immagine vuota (se esiste)
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
	/*@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
	    if (bounceVelY != 0) {
	        this.y += bounceVelY;
	        if (bounceVelY < 0) {
	            if (this.y <= originalY - BOUNCE_HEIGHT) {
	                this.y = originalY - BOUNCE_HEIGHT;
	                bounceVelY = BOUNCE_SPEED;
	            }
	        } else {
	            if (this.y >= originalY) {
	                this.y = originalY;
	                bounceVelY = 0;
	            }
	        }
	    }
	}*/

	
	
	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		int screenX = this.x - cameraX;
		int screenY = this.y - cameraY;
		Image currentImage = null;
		
		if (this.isHit) {
			currentImage = this.emptyBlock;
		} else {
			currentImage = this.image;
		} 
		
		//if (!this.isHit) {
			if(currentImage != null) {
				g.drawImage(currentImage, screenX, screenY, this.width, this.height, null);
			}else {
				//g.setColor(java.awt.Color.CYAN);
				g.setColor(new Color (140,140,230));
				g.fillRect(screenX, screenY, this.width, this.height);
			}
		//}
	}

}
