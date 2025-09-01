package core;

import java.awt.image.BufferedImage;

public class Tile {
    private int id;
    private boolean isSolid;
    private BufferedImage image;

    public Tile(int id, BufferedImage image) {
        this.id = id;
        this.image = image;
        this.isSolid = determinateSolidState(id);
    }
    
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
                return true; // Questi ID sono solidi
            
            default:
                System.out.println("Avviso: Tile ID " + tileId + " non ha una definizione di solidità esplicita. Considerata non solida.");
                return false; 
        }
    }

    
    
    /**
	 * @return the isSolid
	 */
	public boolean isSolid() {
		return isSolid;
	}

	public int getId() {
        return id;
    }

    public BufferedImage getImage() {
        return image;
    }
}
