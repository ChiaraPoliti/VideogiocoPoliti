package core;

import java.awt.image.BufferedImage;

/**
 * Classe che definisce il concetto di componenente (16*16 px) di una mappa 
 */
public class Tile {
    private int id;
    private boolean isSolid;
    private BufferedImage image;

    public Tile(int id, BufferedImage image) {
        this.id = id;
        this.image = image;
        this.isSolid = determinateSolidState(id);
    }
    
    /**
     * definisce lo stato solido di alcune tile, per la creazione della mappa di base
     * @param tileId
     * @return true/false 
     */
    private boolean determinateSolidState(int tileId) {
        switch (tileId) {
            //case 1: 
            //case 2: 
            case 3:
            case 21: 
            case 22:
            case 24:
            case 25:
            case 26:
            case 27:
                return true; // Queste tile sono solide
            
            default:
                System.out.println("Avviso: Tile ID " + tileId + " non ha una definizione di solidità esplicita. Considerata non solida.");
                return false; 
        }
    }

    //GETTER E SETTER
	public int getId() {
		return id;
	}

	public boolean isSolid() {
		return isSolid;
	}

	public BufferedImage getImage() {
		return image;
	}


	public void setImage(BufferedImage image) {
		this.image = image;
	}

    
}
