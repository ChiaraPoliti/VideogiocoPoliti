package logic;


import beans.Player;
import core.Tile;
import core.TileMap;

import java.awt.Rectangle; 

public class CollisionManager {

    private TileMap tileMap;
    private Player mario; // Riferimento al giocatore

    // Costruttore
    public CollisionManager(TileMap tileMap, Player mario) {
        this.tileMap = tileMap;
        this.mario = mario;
    }

    /**
     * Controlla le collisioni di Mario con le tile della mappa (terreno, blocchi).
     * Questo metodo dovrebbe essere chiamato in GamePanel.update() dopo aver aggiornato la posizione di Mario.
     */
    public void checkMarioTileCollisions() {
        // Ottieni la bounding box di Mario per i calcoli di collisione
        Rectangle marioBounds = mario.getBounds();

        // Determina le colonne e righe di tile che Mario interseca
        int startTileCol = marioBounds.x / TileMap.TILE_SIZE;
        int endTileCol = (marioBounds.x + marioBounds.width) / TileMap.TILE_SIZE;
        int startTileRow = marioBounds.y / TileMap.TILE_SIZE;
        int endTileRow = (marioBounds.y + marioBounds.height) / TileMap.TILE_SIZE;

        // Assicurati che i limiti non vadano fuori dalla mappa
        startTileCol = Math.max(0, startTileCol);
        endTileCol = Math.min(tileMap.getCols() - 1, endTileCol);
        startTileRow = Math.max(0, startTileRow);
        endTileRow = Math.min(tileMap.getRows() - 1, endTileRow);

        boolean onGround = false; // Flag per sapere se Mario è a terra

        // Cicla attraverso le tile che Mario sta potenzialmente intersecando
        for (int row = startTileRow; row <= endTileRow; row++) {
            for (int col = startTileCol; col <= endTileCol; col++) {
                // Ottieni la tile dal layer del terreno
                Tile tile = tileMap.getTile(TileMap.TERRENO_LAYER, row, col);

                if (tile != null && tile.isSolid()) {
                    // Calcola la bounding box della tile
                    Rectangle tileBounds = new Rectangle(
                            col * TileMap.TILE_SIZE,
                            row * TileMap.TILE_SIZE,
                            TileMap.TILE_SIZE,
                            TileMap.TILE_SIZE
                    );

                    // Se Mario e la tile si intersecano
                    if (marioBounds.intersects(tileBounds)) {
                        // Gestisci la collisione
                        handleSolidTileCollision(mario, marioBounds, tileBounds, tile);

                        // Se Mario è sopra una tile solida, è a terra
                        if (marioBounds.y < tileBounds.y && Player.getVel_y() >= 0) {
                            onGround = true;
                        }
                    }
                }
            }
        }

        // Imposta lo stato a terra di Mario
        mario.setOnGround(onGround);

        // Controllo per caduta fuori mappa (solo se non ci sono collisioni con il terreno)
        if (!onGround && mario.getBounds().y + mario.getBounds().height > tileMap.getRows() * TileMap.TILE_SIZE) {
            // Mario è caduto sotto la mappa
            mario.setY((tileMap.getRows() * TileMap.TILE_SIZE) - mario.getBounds().height);
            mario.setY(0);
            mario.setOnGround(true);
        }
    }

    /**
     * Gestisce la collisione di Mario con una tile solida.
     * Determina da quale lato Mario ha colpito la tile e reagisce di conseguenza.
     */
    private void handleSolidTileCollision(Player mario, Rectangle marioBounds, Rectangle tileBounds, Tile tile) {
        // Calcola la sovrapposizione-
        Rectangle intersection = marioBounds.intersection(tileBounds);

        // Collisione dal basso (Mario salta contro un blocco)
        if (intersection.height > intersection.width && mario.getyVel() < 0) { // Mario si sta muovendo verso l'alto
            mario.setY(tileBounds.y + tileBounds.height); // Riposiziona Mario sotto il blocco
            mario.setyVel(0); // Ferma il movimento verticale
            if (tile.isQuestionBlock()) {
                // Logica per blocchi domanda (es. rilasciare moneta/power-up)
                // Questo dovrebbe notificare GamePanel o la TileMap che il blocco è stato colpito
                System.out.println("Mario ha colpito un Blocco Domanda!");
                // tile.setQuestionBlock(false); // Potresti voler cambiare lo stato del blocco domanda
                // GamePanel dovrà gestire la creazione della moneta/power-up e la modifica del tile
            } else if (tile.isBreakable()) {
                // Logica per blocchi rompibili (es. distruggere il blocco)
                System.out.println("Mario ha colpito un Blocco Rompibile!");
                // tile.setBreakable(false); // Il blocco deve essere rimosso o cambiato nel GamePanel
            }
        }
        // Collisione dall'alto (Mario atterra su un blocco)
        else if (intersection.height > intersection.width && mario.getyVel() >= 0) { // Mario si sta muovendo verso il basso o fermo
            mario.setY(tileBounds.y - marioBounds.height); // Riposiziona Mario sopra il blocco
            mario.setyVel(0); // Ferma la caduta
            mario.setOnGround(true);
        }
        // Collisione laterale (Mario si muove orizzontalmente contro un blocco)
        else if (intersection.width > intersection.height) { // Collisione orizzontale
            if (mario.getX() < tileBounds.x) { // Collisione da sinistra
                mario.setX(tileBounds.x - marioBounds.width);
            } else { // Collisione da destra
                mario.setX(tileBounds.x + tileBounds.width);
            }
            mario.setxVel(0); // Ferma il movimento orizzontale
        }
    }
}
