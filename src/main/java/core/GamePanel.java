package core;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import beans.Block;
import beans.Coin;
import beans.Enemy;
import beans.Player;
import beans.PowerUp;
import logic.CollisionManager;
import logic.Level1;
import enums.GameState;

public class GamePanel extends JPanel implements Runnable{
	private Thread gameThread;
	private boolean running;
	private Player mario;
	private List<beans.Enemy> enemies;
	private List<beans.Coin> coins;
	private List<beans.PowerUp> powerUps; 
	private List<beans.Block> blocks;
	private final Object gameLoopLock = new Object();
    private TileMap tileMap;
    private int cameraX;
    private int cameraY;
    private CollisionManager collisionManager;
    private GameState gameState;
    
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 200;
    
	
	public GamePanel() {
		this.mario = new Player (100,112); //UNICO
	    this.powerUps = new ArrayList<>();
	    running = false;
	    
	    //creo livello con percorso definito
	    Level1 level1 = new Level1();
	    
	    //definisco gli attributi
        this.enemies = level1.getEnemies();
        this.blocks = level1.getBlocks();
        this.coins = level1.getCoins();
		
        //Lista per i path dei file csv della TileMap
		List<String> mapLayersPaths = new ArrayList<>();
		mapLayersPaths.add("/maps/marioTileset_background.csv"); 
        mapLayersPaths.add("/maps/marioTileset_sfondo.csv"); 
        mapLayersPaths.add("/maps/marioTileset_terreno.csv");
        
        
        String tilesImagesPath = "/tiles/";
        this.tileMap = new TileMap(mapLayersPaths, tilesImagesPath); //definisco la mappa
        
		//Definisco le variabili che individuano la camera (finestra visibile < mappa livello ==> si sposta ma centrata su Mario)
        this.cameraX = mario.getX() - GamePanel.WINDOW_WIDTH/2;
        this.cameraY = mario.getY() - GamePanel.WINDOW_HEIGHT/2;
        clampCamera(); //gestione camera
        
        //creo un collisionManager per rilevare e gestire le collisioni
        this.collisionManager = new CollisionManager();
        this.gameState = GameState.PLAYING; //aggiorno lo stato di gioco perché si deve attivare

	}
	
	/**
	 * Metodo che avvia il 'gioco' --> apre il Thread impostandolo sullo stato di gioco
	 */
	public void startGame() {
		synchronized (gameLoopLock) { //garantisce l'apertura di un singolo thread
			running = true; 
		}
		gameThread = new Thread(this);
		gameThread.start(); //esegue il metodo run()
        
		//Tentativo di inserimento di musica, ma servono altre librerie.
		//Possibile espansione
        //avvio musica
        //this.frame.startBackgroundMusic();
    }

    /**
     * Metodo per fermare il gioco (utile per Game Over o Pausa)
     */
    public void stopGame() {
        synchronized (gameLoopLock) {
            running = false; //ferma il thread
        }
        
        //Tentativo inserimento musica
        //stop musica
        //this.frame.stopBackgroundMusic();
    }

    
    /**
     * Metodo di aggiornamento del thread
     */
    @Override
    public void run() {
        // Il loop continua finché 'running' è true.
        while (true) {
            boolean currentRunningState;
            synchronized (gameLoopLock) {
                currentRunningState = running;
            }

            if (!currentRunningState) {
                break; // Esce dal loop se 'running' è diventato false
            }

            update(); //Aggiorna il gioco
            repaint(); //ridisegna la schermata (swing) by paintcomponent

            try {
                Thread.sleep(1000 / 60); //frequenza degli aggiornamenti
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Game loop interrupted.");
                stopGame(); // ferma il gioco in caso di interruzione
                break;
            }
        }
    }

    
    /**
     * aggiorna la logica di gioco, gestendo i componenti e le collisioni
     */
    private void update() {
    	//se il gioco non è in PLAYING esce
    	if (gameState == GameState.GAME_OVER || gameState == GameState.WIN) {
    		return;
    	}
    	
    	//Recupero le misure della mappa in pixel da passare ai vari update()
        int mapWidthPixels = tileMap.getCols() * TileMap.TILE_SIZE;
        int mapHeightPixels = tileMap.getRows() * TileMap.TILE_SIZE;

        //COMINCIANO GLI AGGIORNAMENTI DI TUTTI
        //Player
        mario.update(mapWidthPixels, mapHeightPixels, tileMap);
        
        //verifica tile vittoria
        int marioCol = (mario.getX() + mario.getWidth() / 2) / TileMap.TILE_SIZE;
        int marioRow = (mario.getY() + mario.getHeight() / 2) / TileMap.TILE_SIZE;
        Tile currentTile = tileMap.getTile(1, marioRow, marioCol); //livello 1 perché si trova lì nella mappa

        //id tile dell'asta è 22
        if (currentTile != null && currentTile.getId() == 22) {
            gameState = GameState.WIN;
        }

        
        //Nemici e rimozione morti
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update(mapWidthPixels, mapHeightPixels, this.tileMap);
            if (!enemy.isAlive() && enemy.isRemovable()) {
                enemyIterator.remove();
            }
        }

        
        //Blocchi
        for (Block block : blocks) {
            block.update(mapWidthPixels, mapHeightPixels, tileMap);
        }
      
        
        //Monete
        Iterator<Coin> coinIterator = coins.iterator();
        while(coinIterator.hasNext()) {
        	Coin coin = coinIterator.next();
            coin.update(mapWidthPixels, mapHeightPixels, tileMap);
            // Rimuove la moneta se raccolta
            if (coin.isCollected()) {
                coinIterator.remove();
            }
        }
     
        //Powerup esistenti
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp pu = powerUpIterator.next();
            pu.update(mapWidthPixels, mapHeightPixels, tileMap);

      
            // Rimuove power-up raccolto
            if (pu.isCollected()) {
                powerUpIterator.remove();
            }
        }
        
        //CONTROLLO COLLISIONI TOTALE
        collisionManager.checkAllCollisions(mario, enemies, blocks,coins,powerUps,tileMap);
        
        
        //Aggiorno stato di gioco sulla base di vite di Mario e ridisegna
        if (mario.getHp() == 0) {
        	stopGame();
        	gameState = GameState.GAME_OVER;
        	repaint();
        }
        
        // GESTIONE CAMERA 
        cameraX = mario.getX() - WINDOW_WIDTH / 2;
        cameraY = mario.getY() - WINDOW_HEIGHT / 2;
        clampCamera();
        
        
        repaint(); //ridisegno tutto
    }
    
    public void restartGame() {
    	mario.resetState(); //riporto mario all'inizio
    	enemies.clear(); //elimino il resto
        coins.clear();
        powerUps.clear();
        blocks.clear();
    	
        this.gameState = GameState.PLAYING; //aggiorno stato di gioco
        Level1 level1 = new Level1(); //ricarico il livello e le relative liste di elementi
        enemies = level1.getEnemies();
        blocks = level1.getBlocks();
        coins = level1.getCoins();
       
        //Interrompe il thread
        if (gameThread != null && gameThread.isAlive()) {
        	running = false;
        	try {
        		gameThread.join();
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        }
        
        //per poi riavviare tutto
        startGame(); // riavvia il thread
       
    }
    
	/**
	 * Metodo che sposta la camera/focus 
	 */
    private void clampCamera() {
        int mapWidthPixels = tileMap.getCols() * TileMap.TILE_SIZE; //dimensioni della mappa in pixel
        int mapHeightPixels = tileMap.getRows() * TileMap.TILE_SIZE;
        
        //Centro camera su Mario
        int desiredCameraX = mario.getX() - WINDOW_WIDTH/2; // posizione ideale del focus 
        //range valido della camera
        int minCameraX = 0; // non può andare più a sinistra del limite
        int maxCameraX = mapWidthPixels - WINDOW_WIDTH; // oltre a questo ci sarebbe il vuoto
        
        // Limita orizzontalmente: mappa è più piccola della finestra --> da centrare
        if (mapWidthPixels < GameFrame.WINDOW_WIDTH) { //se la mappa è più piccola della finestra
            cameraX = maxCameraX / 2; //camera non si muove --> centrata
        } else { // Altrimenti
        	cameraX = Math.max(minCameraX, Math.min(desiredCameraX, maxCameraX));
            //min: valore più piccolo tra desiderata e il limite max (non oltre bordo destro)
            //max: valore più grande tra il limite min e il risultato precedente
        }
        
        //asse verticale
        //centro su mario sempre
        int desiredCameraY = mario.getY() - WINDOW_HEIGHT / 2;
        int minCameraY = 0;
        int maxCameraY = mapHeightPixels - WINDOW_HEIGHT;
        
        // Limita verticalmente
        if (mapHeightPixels < WINDOW_HEIGHT) { //mappa è più piccola della finestra, centrala
        	cameraY = 0;
        } else { // Altrimenti, blocca ai bordi della mappa
            cameraY = Math.max(minCameraY, Math.min(desiredCameraY, maxCameraY));
            //uguale a limite orizzontale
        }
    }
    
    /**
     * Metodo che disegna un box per il punteggio del giocatore
     * @param g2d
     */
    private void drawScore(Graphics2D g2d) {
        int padding = 10; // distanza dai bordi
        int boxWidth = 95;
        int boxHeight = 40;

        // Sfondo del box
        g2d.setColor(new Color(0, 0, 0, 0)); // semi-trasparente
        g2d.fillRoundRect(padding, padding, boxWidth, boxHeight, 10, 10);


        // Testo del punteggio
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        g2d.drawString("Score: " + mario.getScore(), padding + 10, padding + 25);
    }
    
    /**
     * Metodo che disegna il logo del gioco
     * @param g2d
     */
    private void drawLogo(Graphics2D g2d) {
    	Image imageIcon = null;
        java.net.URL logoUrl = getClass().getResource("/images/logo.png");
        if (logoUrl != null) {
            imageIcon = new ImageIcon(logoUrl).getImage();
        }
        
        if (imageIcon != null) {
            g2d.drawImage(imageIcon, getWidth() - 180, 10, 100, 50, null);
        } else {
        	System.out.println("logo non caricato");        }
    }

    
    /**
     * Metodo che disegna tutte le componenti
     * @param g2d
     */
    public void drawWorld (Graphics2D g2d) {
    	// Calcola quali tile sono visibili
        int startCol = cameraX / TileMap.TILE_SIZE;
        int endCol = (cameraX + WINDOW_WIDTH) / TileMap.TILE_SIZE + 1;
        int startRow = cameraY / TileMap.TILE_SIZE;
        int endRow = (cameraY + WINDOW_HEIGHT) / TileMap.TILE_SIZE + 1;

        // Limita i cicli per non andare oltre i bordi effettivi della mappa
        if (startCol < 0) { 
        	startCol = 0;
        }
        if (endCol > tileMap.getCols()) {
        	endCol = tileMap.getCols();
        }
        if (startRow < 0) {
        	startRow = 0;
        }
        if (endRow > tileMap.getRows()) {
        	endRow = tileMap.getRows();
        }
        
        //System.out.println("DEBUG: CameraX finale: " + cameraX + ", MaxCameraX calcolato: " + (tileMap.getCols() * TileMap.TILE_SIZE - WINDOW_WIDTH + (2 * TileMap.TILE_SIZE))); 
        
        // Itera su ogni livello visibile della tileMap
        for (int layerIndex = 0; layerIndex < tileMap.getNumLayers(); layerIndex++) {
            for (int row = startRow; row < endRow; row++) {
                for (int col = startCol; col < endCol; col++) {
                    Tile tile = tileMap.getTile(layerIndex, row, col);

                    // Disegna il tile solo se esiste e ha un'immagine
                    if (tile != null && tile.getImage() != null) {
                        BufferedImage img = tile.getImage();

                        // Calcola la posizione di disegno, sottraendo la posizione della camera
                        int drawX = (col * TileMap.TILE_SIZE) - cameraX;
                        int drawY = (row * TileMap.TILE_SIZE) - cameraY;

                        g2d.drawImage(img,
                                      drawX,
                                      drawY,
                                      TileMap.TILE_SIZE,
                                      TileMap.TILE_SIZE,
                                      null);
                    }
                }
            }
        }
        
        //DISEGNA LE COMPONENTI
        //Blocchi
        for (Block block : blocks) {
        	block.draw(g2d, cameraX, cameraY);
        }
        
        //Mario
	    if(this.mario!= null) {
	    	mario.draw(g2d, cameraX, cameraY);
	    }
	    
	 // Nemici
	    for (Enemy enemy : enemies) {
	        enemy.draw(g2d, cameraX, cameraY);
	    }

	    // Monete
	    for (Coin coin : coins) {
	        coin.draw(g2d, cameraX, cameraY);
	    }

	    // Power-up
	    for (PowerUp powerUp : powerUps) {
	        powerUp.draw(g2d, cameraX, cameraY);
	    }
	    
	    //Logo e punteggio
	    drawScore(g2d);
	    drawLogo(g2d);

   }
		
    /**
     * disegno e aggiornamento pannello
     */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		//se game over
		if(gameState == GameState.GAME_OVER) {
			//Overlay nero semitrasparente
			g2d.setColor(new Color (0, 0, 0, 180));
			g2d.fillRect(0, 0, getWidth(), getHeight());
			
			// Scritta Game Over
	        g2d.setColor(Color.RED);
	        g2d.setFont(new Font("Arial", Font.BOLD, 48));
	        g2d.drawString("GAME OVER", getWidth()/2 - 150, getHeight()/2);

	        // Messaggio per restart
	        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
	        g2d.setColor(Color.WHITE);
	        g2d.drawString("Premi Invio per ricominciare", getWidth()/2 - 140, getHeight()/2 + 50);

	        return; // non disegnare altro
		} else if (gameState == GameState.WIN) { //se vittoria
		    drawWorld(g2d);

		    // Overlay verde semi-trasparente
		    g2d.setColor(new Color(0, 150, 0, 150));
		    g2d.fillRect(0, 0, getWidth(), getHeight());

		    // Scritta di vittoria
		    g2d.setColor(Color.YELLOW);
		    g2d.setFont(new Font("Arial", Font.BOLD, 48));
		    g2d.drawString("HAI VINTO!", getWidth()/2 - 120, getHeight()/2);

		    g2d.setFont(new Font("Arial", Font.PLAIN, 24));
		    g2d.setColor(Color.WHITE);
		    g2d.drawString("Premi Invio per ricominciare", getWidth()/2 - 140, getHeight()/2 + 50);

		    return;
		} else {
			drawWorld(g2d);
		}
		
    }

	//GETTER E SETTER
	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Player getMario() {
		return mario;
	}

	public void setMario(Player mario) {
		this.mario = mario;
	}
}
	
