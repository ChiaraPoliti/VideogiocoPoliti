package beans;

import enums.blockType;
import enums.itemType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

import core.TileMap;


public class QuestionBlock extends Block {
	//private boolean isEmpty;
	private int originalY;
	private Image emptyBlock;
	private itemType contentItemType;
	private boolean isContentReleased;
	
	private static final int BOUNCE_HEIGHT = 10; 
    private static final int BOUNCE_SPEED = 2; 
    private double bounceVelY = 0; 
	
	public QuestionBlock(int x, int y, itemType contentItemType) {
		super(x,y, blockType.QUESTION);
		this.contentItemType = contentItemType;
		this.isContentReleased = false;
		this.originalY = y;
		
		try {
			java.net.URL imageUrl = getClass().getResource("/tiles/tile_2.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di blocco ? caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di blocco ? non trovata nel classpath: /tiles/tile_2.png");
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
	        System.err.println("Eccezione durante il caricamento dell'immagine di blocco vuoto: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	        this.emptyBlock = null;
	    }
    }

	@Override
	public GameObject hit() {
		if(!this.isContentReleased) {
			super.setHit(true);
			this.isContentReleased = true;
			this.image = this.emptyBlock;
			System.out.println("Blocco vuoto.");
			//this.releasePowerUp();
			
			this.bounceVelY = -BOUNCE_SPEED;
			return createItem();
		} else {
			if (this.image == this.emptyBlock) {
				this.bounceVelY = -BOUNCE_SPEED / 2;
			System.out.println("Blocco già vuoto.");
			}
			return null;
		}
	}
	
	private GameObject createItem() {
		int itemX = this.x;
        int itemY = this.y - Block.BLOCK_SIZE;
        
        switch (contentItemType) {
        case COIN:
            Coin coin = new Coin(itemX, itemY);
            coin.setVel_y(-BOUNCE_SPEED); // La moneta "salta" verso l'alto
      
            return coin;
            
        case MUSHROOM:
            Mushroom mushroom = new Mushroom(itemX, itemY);
            // Il fungo potrebbe salire e poi iniziare a muoversi orizzontalmente
            mushroom.setVel_y(-BOUNCE_SPEED); 
            return mushroom;
            
        /*case STAR:
            // return star;
        
        case FIREFLOWER:
            // return flower;*/
            
        default:
            System.err.println("Tipo di item non supportato: " + contentItemType);
            return null;
        }
	}

	@Override
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
    }

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		int screenX = this.x - cameraX;
		int screenY = this.y - cameraY;
		Image currentImage = null;
		
		if (this.isContentReleased) {
			currentImage = this.emptyBlock;
		} else {
			currentImage = this.image;
		} 
		
		if (currentImage != null) {
			g.drawImage(currentImage, screenX, screenY, this.width, this.height, null);	
		} else {
			if (this.isContentReleased) {
				g.setColor(Color.YELLOW.darker());
			} else {
				g.setColor(Color.YELLOW);
			}
			g.fillRect (screenX, screenY, this.width, this.height);
		}
		
		
	
		
	}

	/**
	 * @return the originalY
	 */
	public int getOriginalY() {
		return originalY;
	}

	/**
	 * @return the emptyBlock
	 */
	public Image getEmptyBlock() {
		return emptyBlock;
	}

	/**
	 * @return the contentItemType
	 */
	public itemType getContentItemType() {
		return contentItemType;
	}

	/**
	 * @return the isContentReleased
	 */
	public boolean isContentReleased() {
		return isContentReleased;
	}

	/**
	 * @return the bounceHeight
	 */
	public static int getBounceHeight() {
		return BOUNCE_HEIGHT;
	}

	/**
	 * @return the bounceSpeed
	 */
	public static double getBounceSpeed() {
		return BOUNCE_SPEED;
	}

	/**
	 * @return the bounceVelY
	 */
	public double getBounceVelY() {
		return bounceVelY;
	}

	/**
	 * @param originalY the originalY to set
	 */
	public void setOriginalY(int originalY) {
		this.originalY = originalY;
	}

	/**
	 * @param emptyBlock the emptyBlock to set
	 */
	public void setEmptyBlock(Image emptyBlock) {
		this.emptyBlock = emptyBlock;
	}

	/**
	 * @param contentItemType the contentItemType to set
	 */
	public void setContentItemType(itemType contentItemType) {
		this.contentItemType = contentItemType;
	}

	/**
	 * @param isContentReleased the isContentReleased to set
	 */
	public void setContentReleased(boolean isContentReleased) {
		this.isContentReleased = isContentReleased;
	}

	/**
	 * @param bounceVelY the bounceVelY to set
	 */
	public void setBounceVelY(double bounceVelY) {
		this.bounceVelY = bounceVelY;
	}

	
	
	
	
	
	
	
	

}
