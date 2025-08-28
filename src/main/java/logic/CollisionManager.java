package logic;

import java.awt.Rectangle;
import java.util.List;
import beans.Player;
import core.TileMap;
import beans.Enemy;
import beans.Block;

public class CollisionManager {

    /**
     * Controlla tutte le collisioni tra gli oggetti di gioco.
     */
    public void checkAllCollisions(Player mario, List<Enemy> enemies, List<Block> blocks, TileMap tileMap) {
        // Controlla le collisioni tra Mario e le piastrelle solide
        checkPlayerTileCollisions(mario, tileMap);

        // Controlla le collisioni tra Mario e i nemici
        checkPlayerEnemyCollisions(mario, enemies);
    }

    /**
     * Gestisce le collisioni tra il giocatore e le piastrelle solide della mappa.
     */
    private void checkPlayerTileCollisions(Player mario, TileMap tileMap) {
        // Salva le posizioni future per la collisione
        int nextX = (int) (mario.getX() + mario.getVel_x());
        int nextY = (int) (mario.getY() + mario.getVel_y());

        // Collisioni orizzontali
        Rectangle futureBoundsX = new Rectangle(nextX, mario.getY(), mario.getWidth(), mario.getHeight());
        if (checkMapCollision(futureBoundsX, tileMap)) {
            mario.setVel_x(0);
        }

        // Collisioni verticali
        Rectangle futureBoundsY = new Rectangle(mario.getX(), nextY, mario.getWidth(), mario.getHeight());
        if (checkMapCollision(futureBoundsY, tileMap)) {
            /*if (mario.getVel_y() > 0) { // Mario sta scendendo
                mario.setOnGround(true);
            } else if (mario.getVel_y() < 0) { // Mario sta saltando
                mario.setJumping(false);
            }*/
        	// Per un atterraggio corretto
            // Calcola la posizione della riga in cui è avvenuta la collisione
            int collisionRow = (mario.getY() + mario.getHeight()) / TileMap.TILE_SIZE;
            // Sposta il giocatore appena sopra il tile, per evitare che lo attraversi
            mario.setY(collisionRow * TileMap.TILE_SIZE - mario.getHeight());
            
            mario.setOnGround(true);
            mario.setJumping(false);
        } else {
            mario.setOnGround(false);
        	mario.setVel_y(nextY);
        }
    }
    
    /**
     * Controlla se un rettangolo si scontra con una piastrella solida.
     */
    public boolean checkMapCollision(Rectangle bounds, TileMap tileMap) {
        int startCol = bounds.x / TileMap.TILE_SIZE;
        int endCol = (bounds.x + bounds.width) / TileMap.TILE_SIZE;
        int startRow = bounds.y / TileMap.TILE_SIZE;
        int endRow = (bounds.y + bounds.height) / TileMap.TILE_SIZE;

        // Limita i cicli entro i confini della mappa
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
    }

    /**
     * Gestisce le collisioni tra il giocatore e i nemici.
     */
    private void checkPlayerEnemyCollisions(Player mario, List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && mario.getBounds().intersects(enemy.getBounds())) {
                // Collisione tra Mario e un nemico rilevata
                // Se Mario sta scendendo e il suo piede è sopra il nemico
                if (mario.getVel_y() > 0 && mario.getBounds().y + mario.getHeight() < enemy.getBounds().y + enemy.getHeight()) {
                    enemy.die();
                    mario.setVel_y(-10); // Piccolo rimbalzo per feedback
                } else {
                    mario.takeDamage(1); // Mario viene danneggiato
                }
            }
        }
    }
}
