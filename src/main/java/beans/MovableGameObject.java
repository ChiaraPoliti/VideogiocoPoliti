package beans;

//import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import core.TileMap;

public abstract class MovableGameObject extends GameObject {
	protected int vel_x;
	protected int vel_y;
	protected boolean isOnGround;
	protected boolean isMovingLeft;
	protected boolean isMovingRight;
	
	public static final double g = 1.0;

	public MovableGameObject(int x, int y, int width, int height, Image image, int vel_x, int vel_y) {
		super(x, y, width, height, image);
		this.vel_x = vel_x;
		this.vel_y = vel_y;
		this.isOnGround = true;
		this.isMovingLeft = false;
		this.isMovingRight = false;
	}

	//@Override
	/*public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
        this.vel_y += g;

     // Gestione delle collisioni orizzontali
        int nextX = this.x + this.vel_x;
        Rectangle futureBoundsX = new Rectangle((int)nextX, this.y, this.width, this.height);
        if (checkMapCollision(futureBoundsX, tileMap)) {
            this.vel_x = 0; // Ferma il movimento orizzontale se si scontra
            // Riposiziona l'oggetto per non farlo incastrare nel muro
            // (logica più precisa gestita nel CollisionManager)
        }
        
        // Gestione delle collisioni verticali
        int nextY = this.y + this.vel_y;
        Rectangle futureBoundsY = new Rectangle(this.x, (int)nextY, this.width, this.height);
        if (checkMapCollision(futureBoundsY, tileMap)) {
            if (this.vel_y > 0) { // Collisione dal basso ( sta cadendo )
            	this.y = (futureBoundsY.y / TileMap.TILE_SIZE) * TileMap.TILE_SIZE - this.height;
                this.isOnGround = true;
            } else if (this.vel_y <0) { // collisione alto, salta
            	this.y = (futureBoundsY.y / TileMap.TILE_SIZE) * TileMap.TILE_SIZE + TileMap.TILE_SIZE;
            }
            this.vel_y = 0; // Ferma il movimento verticale
        } else {
        	this.isOnGround = false;
        }
        
     // Aggiorna la posizione finale dopo le collisioni
        this.x += this.vel_x;
        this.y += this.vel_y;
        
        // Limiti dello schermo
        if (this.x < 0) {
            this.x = 0;
        }
        if (this.x + this.width > mapWidthPixels) {
            this.x = mapWidthPixels - this.width;
        }
    }*/
    
    /*// Metodo per il controllo delle collisioni con le tile solide
	protected boolean checkMapCollision(Rectangle bounds, TileMap tileMap) {
        int startCol = bounds.x / TileMap.TILE_SIZE;
        int endCol = (bounds.x + bounds.width - 1) / TileMap.TILE_SIZE;
        int startRow = bounds.y / TileMap.TILE_SIZE;
        int endRow = (bounds.y + bounds.height - 1) / TileMap.TILE_SIZE;

        // Assicurati di non andare fuori dai limiti della mappa
        startCol = Math.max(0, startCol);
        endCol = Math.min(tileMap.getCols() - 1, endCol);
        startRow = Math.max(0, startRow);
        endRow = Math.min(tileMap.getRows() - 1, endRow);

        for (int r = startRow; r <= endRow; r++) {
            for (int c = startCol; c <= endCol; c++) {
                if (tileMap.isTileSolid(c, r)) {
                    Rectangle tileBounds = new Rectangle(c * TileMap.TILE_SIZE, r * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
                    if (bounds.intersects(tileBounds)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }*/
	
	public abstract void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap);
	//public abstract void draw(Graphics2D g, int cameraX, int cameraY);

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    // Aggiungi i getters e setters mancanti o modificati per usare i float
    public int getVel_x() { return vel_x; }
    public int getVel_y() { return vel_y; }
    public void setVel_x(int vel_x) { this.vel_x = vel_x; }
    public void setVel_y(int vel_y) { this.vel_y = vel_y; }
    public boolean isOnGround() { return isOnGround; }
    
    /**
	 * @return the isMovingLeft
	 */
	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	/**
	 * @return the isMovingRight
	 */
	public boolean isMovingRight() {
		return isMovingRight;
	}

	/**
	 * @return the g
	 */
	public static double getG() {
		return g;
	}

	public void setOnGround(boolean onGround) { this.isOnGround = onGround; }
    public void setMovingLeft(boolean isMovingLeft) { this.isMovingLeft = isMovingLeft; }
    public void setMovingRight(boolean isMovingRight) { this.isMovingRight = isMovingRight; }
}

        
        
        /*
        boolean collidedVertically = false;
        if (this.vel_y > 0 || (this.vel_y == 0 && !isOnGround)) { 
            int tileBelowY = (nextY + height) / TileMap.TILE_SIZE;
            int tileLeftX = x / TileMap.TILE_SIZE;
            int tileRightX = (x + width - 1) / TileMap.TILE_SIZE;

            
            tileBelowY = Math.min(tileBelowY, tileMap.getRows() - 1);
            tileLeftX = Math.max(0, tileLeftX);
            tileRightX = Math.min(tileRightX, tileMap.getCols() - 1);

            for (int col = tileLeftX; col <= tileRightX; col++) {
                if (tileMap.isTileSolid(col, tileBelowY)) {
                    if (this.y + height <= tileBelowY * TileMap.TILE_SIZE) { // Collisione da sopra
                        this.y = tileBelowY * TileMap.TILE_SIZE - height;
                        this.vel_y = 0;
                        this.isOnGround = true;
                        collidedVertically = true;
                        break;
                    } else if (this.y >= (tileBelowY + 1) * TileMap.TILE_SIZE) {
                        // Collisione dal basso 
                        
                        this.y = (tileBelowY + 1) * TileMap.TILE_SIZE; 
                        this.vel_y = 0; // Ferma il movimento verso l'alto
                        
                        // Questa logica sarà più raffinata nel CollisionManager
                    }
                }
            }
        }

        if (!collidedVertically) {
            this.y = nextY;
            // Importante: se vel_y è 0 ma non c'è collisione, potrebbe essere ancora in aria se si muoveva orizzontalmente
            // L'impostazione a false è corretta se non c'è stata una collisione che l'ha messo a terra
            if(this.vel_y != 0) { // Se c'è movimento verticale, non è a terra
               this.isOnGround = false;
            } else { // Se vel_y è 0, ricontrolla se è davvero sul terreno (per i casi di spigolo)
               // Questa è una verifica più "costosa" ma più precisa
               this.isOnGround = checkGroundBelow(this.x, this.y + 1, this.width, this.height, tileMap);
            }
        }

        
        if (this.x < 0) {
            this.x = 0;
        }
        if (this.x + this.width > mapWidthPixels) {
            this.x = mapWidthPixels - this.width;
        }

        
        if (this.y > mapHeightPixels + 200) { 
        }
    }

    // Metodo ausiliario per controllare se c'è terreno sotto
    protected boolean checkGroundBelow(int currentX, int currentY, int objWidth, int objHeight, TileMap tileMap) {
        int tileBelowY = (currentY + objHeight) / TileMap.TILE_SIZE;
        int tileLeftX = currentX / TileMap.TILE_SIZE;
        int tileRightX = (currentX + objWidth - 1) / TileMap.TILE_SIZE;

        tileBelowY = Math.min(tileBelowY, tileMap.getRows() - 1);
        tileLeftX = Math.max(0, tileLeftX);
        tileRightX = Math.min(tileRightX, tileMap.getCols() - 1);

        for (int col = tileLeftX; col <= tileRightX; col++) {
            if (tileMap.isTileSolid(col, tileBelowY)) {
                // Controlla se la parte inferiore dell'oggetto è al di sopra o esattamente sulla parte superiore della tile
                if (currentY + objHeight <= tileBelowY * TileMap.TILE_SIZE + 1) { // +1 per tolleranza
                    return true;
                }
            }
        }
        return false;
    }
    
    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle(x, y, width, height);
    }

	@Override
	public abstract void draw(Graphics2D g, int cameraX, int cameraY);

}*/
