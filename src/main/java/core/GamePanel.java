package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import core.TileMap;
//import core.Tile;
import beans.Block;
import beans.BreakableBlock;
import beans.Coin;
import beans.Enemy;
import beans.GameObject;
import beans.Goomba;
import beans.Koopa;
import beans.Mushroom;
import beans.Player;
import beans.PowerUp;
import beans.QuestionBlock;
import logic.CollisionManager;
import enums.GameState;
//import beans.QuestionBlock;
//import beans.Mushroom;
//import enums.itemType;

public class GamePanel extends JPanel implements Runnable,KeyListener {
	private Thread gameThread;
	private boolean running = false;
	private Player mario;
	private List<beans.Enemy> enemies;
	private List<beans.Coin> coins;
	private List<beans.PowerUp> powerUps; 
	private List<beans.Block> blocks;
	private final Object gameLoopLock = new Object();
	public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 200;
    private TileMap tileMap;
    private int cameraX;
    private int cameraY;
    private CollisionManager collisionManager;
    private GameState gameState;
	
	public GamePanel() {
		Player mario = new Player (100,112);
		this.mario = mario;
		this.enemies = new ArrayList<>();
	    this.coins = new ArrayList<>();
	    this.powerUps = new ArrayList<>();
	    this.blocks = new ArrayList<>(); 
	    this.gameState = GameState.PLAYING;
		
		List<String> mapLayersPaths = new ArrayList<>();
		mapLayersPaths.add("/maps/marioTileset_background.csv"); 
        mapLayersPaths.add("/maps/marioTileset_sfondo.csv"); 
        mapLayersPaths.add("/maps/marioTileset_terreno.csv");
        
        String tilesImagesPath = "/tiles/";
        this.tileMap = new TileMap(mapLayersPaths, tilesImagesPath);
        System.out.println("--- DEBUG MAP DIMENSIONS ---");
        System.out.println("Mappa Caricata - Colonne: " + tileMap.getCols());
        System.out.println("Mappa Caricata - Righe: " + tileMap.getRows());
        System.out.println("Mappa Caricata - Larghezza in Pixels: " + (tileMap.getCols() * TileMap.TILE_SIZE));
        System.out.println("--- FINE DEBUG ---");
		
        cameraX = mario.getX()-this.WINDOW_WIDTH/2;
        cameraY = mario.getY() - this.WINDOW_HEIGHT/2;
        clampCamera();
        
        this.collisionManager = new CollisionManager();
        
        // Aggiungi nemici
        enemies.add(new Goomba(250, 112));
        enemies.add(new Goomba(550, 112));
        enemies.add(new Goomba(1018, 112));
        //enemies.add(new Koopa(1018, 112));
        
        
        // Aggiungi una moneta
        coins.add(new Coin(400, 112));
        //coins.add(new Coin ());
        //coins.add(new Coin ());
        //coins.add(new Coin ());
        
        //Aggiungo Power Up
        Mushroom pu1 = new Mushroom (193,80);
        powerUps.add(pu1);
        
        //Aggiungo blocchi
        blocks.add(new BreakableBlock (192,80));
        blocks.add(new QuestionBlock (193,80, pu1.getType()));
        
        
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setLayout(new BorderLayout()); // Usa BorderLayout per il bottone iniziale
        
		JButton bottoneAvvio = new JButton("Start Game");
	    bottoneAvvio.setFont(new Font("Arial", Font.BOLD, 24));
	    bottoneAvvio.setForeground(Color.BLACK);
	    bottoneAvvio.setBackground(Color.WHITE);
	    bottoneAvvio.setOpaque(true);
	    bottoneAvvio.setBorderPainted(false);
		this.add(bottoneAvvio, BorderLayout.SOUTH);
		
		bottoneAvvio.addActionListener(new ActionListener(){
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            //ORA MESSAGGIO PER CAPIRE, POI DA MODIFICARE
	            JOptionPane.showMessageDialog(GamePanel.this, "Inizia il gioco!", "Avviso", JOptionPane.INFORMATION_MESSAGE);
	            //System.out.println("Inizia  il gioco!");
	            startGame();
	            remove(bottoneAvvio);
	            revalidate();
	            repaint();
	            
	            addKeyListener(GamePanel.this);
	            setFocusable(true);
	            requestFocusInWindow();
	            System.out.println("focus su gamepanel");
	        }
	    }
		);
	}
	
	public void startGame() {
        synchronized (gameLoopLock) { // Sincronizza la scrittura di 'running'
            running = true;
        }
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Metodo per fermare il gioco (utile per Game Over o Pausa)
    public void stopGame() {
        synchronized (gameLoopLock) { // Sincronizza la scrittura di 'running'
            running = false;
        }
    }

    @Override
    public void run() {
        // Il loop continuerà finché 'running' è true.
        // La lettura di 'running' deve essere sincronizzata.
        while (true) {
            boolean currentRunningState;
            synchronized (gameLoopLock) { // Sincronizza la lettura di 'running'
                currentRunningState = running;
            }

            if (!currentRunningState) {
                break; // Esci dal loop se 'running' è diventato false
            }

            update();
            repaint();

            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Game loop interrupted.");
                stopGame(); // Assicurati di fermare il gioco in caso di interruzione
                break;
            }
        }
    }

    /*private void update() {
    	int mapWidthPixels = tileMap.getCols() * TileMap.TILE_SIZE;
    	int mapHeightPixels = tileMap.getRows() * TileMap.TILE_SIZE;

    	mario.update(mapWidthPixels, mapHeightPixels, tileMap);
        //mario.update(WINDOW_WIDTH, WINDOW_HEIGHT, this.tileMap);
        
        // Rimuovi i nemici morti usando un Iterator per evitare problemi di concorrenza
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update(WINDOW_WIDTH, WINDOW_HEIGHT, this.tileMap);
            if (!enemy.isAlive() && enemy.isRemovable()) {
                enemyIterator.remove();
            }
        }
        
        collisionManager.checkAllCollisions(mario, enemies, blocks, tileMap);
        
        for (int j = 0; j < coins.size(); j++) {
            Coin coin = coins.get(j);
            coin.update(WINDOW_WIDTH, WINDOW_HEIGHT, this.tileMap);
            // Gestisci la rimozione delle monete raccolte
        }
        
        for (Block block : blocks) {
            block.update(0,0, tileMap);// Passa i parametri necessari
            //block.update(WINDOW_WIDTH, WINDOW_HEIGHT, this.tileMap);
        }
            
        
     // --- LOGICA DI AGGIORNAMENTO DELLA CAMERA ---
        // La camera segue Mario, ma non va oltre i bordi della mappa
        
        // Calcola la posizione desiderata della camera basata su Mario
        // Centra Mario nella finestra se possibile
        cameraX = mario.getX() - WINDOW_WIDTH / 2;
        cameraY = mario.getY() - WINDOW_HEIGHT / 2;

        // Limita la camera ai bordi della mappa (orizzontale)
        clampCamera();
        // devo mettere aggiornamenti di tutti
        //devo mettere le collisioni (rimandi ai metodi della classe giusta)
        collisionManager.checkAllCollisions(mario, enemies, blocks, tileMap);
    }*/
    
    private void update() {
    	if (gameState == GameState.GAME_OVER || gameState == GameState.WIN) {
    		return;
    	}
    	
        int mapWidthPixels = tileMap.getCols() * TileMap.TILE_SIZE;
        int mapHeightPixels = tileMap.getRows() * TileMap.TILE_SIZE;

        //Update Player
        mario.update(mapWidthPixels, mapHeightPixels, tileMap);
        int marioCol = (mario.getX() + mario.getWidth() / 2) / TileMap.TILE_SIZE;
        int marioRow = (mario.getY() + mario.getHeight() / 2) / TileMap.TILE_SIZE;

        Tile currentTile = tileMap.getTile(1, marioRow, marioCol); // layer 0 o quello giusto

        if (currentTile != null && currentTile.getId() == 22) {
            gameState = GameState.WIN;
        }

        // Update Nemici e rimozione morti
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update(WINDOW_WIDTH, WINDOW_HEIGHT, this.tileMap);
            if (!enemy.isAlive() && enemy.isRemovable()) {
                enemyIterator.remove();
            }
        }

        // collisioni nemici e mappa
        collisionManager.checkPlayerTileCollisions(mario, tileMap);
        collisionManager.checkPlayerEnemyCollisions(mario, enemies);
        //collisionManager.checkEnemyTileCollisions(enemies, tileMap);
        
        
        //update blocchi
        for (Block block : blocks) {
            block.update(mapWidthPixels, mapHeightPixels, tileMap);
        }
        collisionManager.checkPlayerBlockCollisions(mario, blocks, coins, powerUps);
        
        //update monete
        Iterator<Coin> coinIterator = coins.iterator();
        while(coinIterator.hasNext()) {
        	Coin coin = coinIterator.next();
            coin.update(WINDOW_WIDTH, WINDOW_HEIGHT, tileMap);
            // Rimuovi la moneta se raccolta
            if (coin.isCollected()) {
                coinIterator.remove();
            }
        }
        
        //update powerup
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp pu = powerUpIterator.next();
            pu.update(mapWidthPixels, mapHeightPixels, tileMap);

            // Controlla collisione con Mario
            if (!pu.isCollected() && mario.getBounds().intersects(pu.getBounds())) {
                pu.applyEffect(mario);
                pu.setCollected(true);
            }

            // Rimuovi power-up raccolto
            if (pu.isCollected()) {
                powerUpIterator.remove();
            }
        }
        
        if (mario.getHp() == 0) {
        	stopGame();
        	gameState = GameState.GAME_OVER;
        	repaint();
        }
        
        /*
        // controlla blocchi e recupera eventuali oggetti generati
        List<GameObject> spawned = collisionManager.checkPlayerBlockCollisions(mario, blocks);
        for (GameObject obj : spawned) {
            if (obj instanceof Coin) {
                coins.add((Coin) obj);
            } else {
                items.add(obj); // es. Mushroom, FireFlower...
            }
        }

        // --- Update monete esistenti ---
        Iterator<Coin> coinIterator = coins.iterator();
        while (coinIterator.hasNext()) {
            Coin coin = coinIterator.next();
            coin.update(WINDOW_WIDTH, WINDOW_HEIGHT, this.tileMap);

            // opzionale: se la moneta è "scomparsa" (animazione finita), rimuovila
            if (coin.isCollected() || coin.shouldDespawn()) {
                coinIterator.remove();
            }
        }

        // --- Update oggetti spawnati (funghi, fiori, ecc.) ---
        Iterator<GameObject> itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
            GameObject item = itemIterator.next();
            item.update(WINDOW_WIDTH, WINDOW_HEIGHT, this.tileMap);

            // opzionale: se raccolto, rimuovi
            if (item instanceof Collectible && ((Collectible) item).isCollected()) {
                itemIterator.remove();
            }
        }

        // --- Update blocchi (per il bounce ecc.) ---
        for (Block block : blocks) {
            block.update(mapWidthPixels, mapHeightPixels, tileMap);
        }*/
        
    

        // --- LOGICA DI CAMERA ---
        cameraX = mario.getX() - WINDOW_WIDTH / 2;
        cameraY = mario.getY() - WINDOW_HEIGHT / 2;
        clampCamera();
    }
    
    private void restartGame() {
        mario = new Player(100,112);
        enemies.clear();
        coins.clear();
        powerUps.clear();
        blocks.clear();

        // ricrei la mappa e riaggiungi i nemici/oggetti come in costruttore
        enemies.add(new Goomba(250, 112));
        enemies.add(new Goomba(550, 112));
        enemies.add(new Koopa(1018, 112));

        coins.add(new Coin(400, 112));
        Mushroom pu1 = new Mushroom(193,80);
        powerUps.add(pu1);
        blocks.add(new BreakableBlock(192,80));
        blocks.add(new QuestionBlock(193,80, pu1.getType()));

        gameState = GameState.PLAYING;
        startGame(); // riavvia il thread
    }


    
	
    private void clampCamera() {
        int mapWidthPixels = tileMap.getCols() * TileMap.TILE_SIZE;
        int mapHeightPixels = tileMap.getRows() * TileMap.TILE_SIZE;
        
        //Centro camera su Mario
        int desiredCameraX = mario.getX() - WINDOW_WIDTH/2; 
        int minCameraX = 0;
        int maxCameraX = mapWidthPixels - WINDOW_WIDTH;
        
        // Limita orizzontalmente
        // Se la mappa è più piccola della finestra, centrala
        if (mapWidthPixels < GameFrame.WINDOW_WIDTH) {
            cameraX = maxCameraX / 2;
        } else { // Altrimenti, blocca ai bordi della mappa
            cameraX = Math.max(minCameraX, Math.min(desiredCameraX, Math.max(0, maxCameraX)));
        }
        
        
        int desiredCameraY = mario.getY() - WINDOW_HEIGHT / 2;
        int minCameraY = 0;
        int maxCameraY = mapHeightPixels - WINDOW_HEIGHT;
        
        // Limita verticalmente
        // Se la mappa è più piccola della finestra, centrala
        if (mapHeightPixels < WINDOW_HEIGHT) {
            //cameraY = maxCameraY / 2;
        	cameraY = 0;
        } else { // Altrimenti, blocca ai bordi della mappa
            cameraY = Math.max(minCameraY, Math.min(desiredCameraY, maxCameraY));
        }
    }
    
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
        
        //System.out.println("DEBUG: CameraX finale: " + cameraX + ", MaxCameraX calcolato: " + (tileMap.getCols() * TileMap.TILE_SIZE - WINDOW_WIDTH + (2 * TileMap.TILE_SIZE))); // Assumendo 2 tile extra

        // Itera su ogni livello visibile
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
	    if(this.mario!= null) {
	    	mario.draw(g2d, cameraX, cameraY);
	    }
	    
	 // Disegna i nemici
	    for (Enemy enemy : enemies) {
	        enemy.draw(g2d, cameraX, cameraY);
	    }

	    // Disegna le monete
	    for (Coin coin : coins) {
	        coin.draw(g2d, cameraX, cameraY);
	    }

	    // Disegna i power-up
	    for (PowerUp powerUp : powerUps) {
	        powerUp.draw(g2d, cameraX, cameraY);
	    }
   }
		
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if(gameState == GameState.GAME_OVER) {
			//drawWorld(g2d);
			
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
		} else if (gameState == GameState.WIN) {
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
	

    
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_RIGHT) {
        	mario.setMovingRight(true);
            System.out.println("Destra premuto.");
        } else if (keyCode == KeyEvent.VK_LEFT) {
            mario.setMovingLeft(true);
            System.out.println("Sinistra premuto.");
        } else if (keyCode == KeyEvent.VK_SPACE) {
        	mario.jump();
        	System.out.println("Salto.");
        	
        }
        
        if(gameState == GameState.GAME_OVER && e.getKeyCode() == KeyEvent.VK_ENTER) {
        	restartGame();
        }
        
        if(gameState == GameState.WIN && e.getKeyCode() == KeyEvent.VK_ENTER) {
        	restartGame();
        }
        
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not typically used for direct game controls like movement
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_RIGHT) {
        	mario.setMovingRight(false); // Mario smette di muoversi a destra
            System.out.println("Destra rilasciato.");
        } else if (keyCode == KeyEvent.VK_LEFT) {
            mario.setMovingLeft(false); // Mario smette di muoversi a sinistra
            System.out.println("Sinistra rilasciato.");
        }
            // Il rilascio di SPACE non ha un effetto diretto sulla velocità di salto in Mario,
            // la gravità e lo stato di isJumping/isOnGround gestiscono la caduta.
            System.out.println("Tasto rilasciato: " + keyCode);
     }

}
	
