package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Classe che crea una mappa di tile
 * è lo sfondo del livello
 */
public class TileMap {
    private List<int[][]> layersData; // livelli della mappa
    private int rows; // Numero di righe della mappa (uguale per tutti i livelli)
    private int cols; // Numero di colonne della mappa (uguale per tutti i livelli)
    private Map<Integer, Tile> tiles; // Mappa per associare ID a Tile

    public static final int TILE_SIZE = 16;

    public TileMap(List<String> csvFilePaths, String tilesImagesPath) {
        this.layersData = new ArrayList<>();
        this.tiles = new HashMap<>();
        
        // Carica tutte le immagini dei tile una sola volta all'inizio
        loadTiles(tilesImagesPath); 
        
        // Carica ogni livello dal suo file CSV
        for (String path : csvFilePaths) {
            loadLayer(path);
        }
        
        // Imposta le dimensioni della mappa basandosi sul primo livello caricato
        if (layersData.isEmpty()) {
            System.err.println("Nessun livello caricato! La mappa sarà vuota.");
            rows = 0;
            cols = 0;
        } else {
            rows = layersData.get(0).length;
            cols = layersData.get(0)[0].length;
            // Verifica che tutti i livelli abbiano le stesse dimensioni
            for (int i = 1; i < layersData.size(); i++) {
                if (layersData.get(i).length != rows || layersData.get(i)[0].length != cols) {
                    System.err.println("Attenzione: Il livello " + i + " ha dimensioni diverse dagli altri livelli.");
                }
            }
        }
    }

    /**
     * Carica le immagini dei tile dalla cartella specificata
     * le immagini devono essere nominate come "tile_ID.png"
     */
    private void loadTiles(String tilesImagesPath) {
        try {
            for (int i = 0; i < 28; i++) { //per ogni id
                String imagePath = tilesImagesPath + "tile_" + i + ".png"; //path
                InputStream is = getClass().getResourceAsStream(imagePath); //recupero risorsa
                if (is == null) { //se nullo, errore
                    System.err.println("Impossibile trovare l'immagine del tile: " + imagePath + ". Verrà usata un'immagine nulla.");
                    tiles.put(i, new Tile(i, null)); // tile con immagine nulla se non trovata
                    continue;
                }
                BufferedImage img = ImageIO.read(is); //altrimenti leggo l'immagine
                tiles.put(i, new Tile(i, img)); // e la inserisco nella mappa con id e nuova tile
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore durante il caricamento delle immagini dei tile.");
        }
    }

    /**
     * Carica un singolo livello (strato) della mappa da un file CSV
     * @param csvFilePath percorso del file CSV per questo livello
     */
    private void loadLayer(String csvFilePath) {
        List<List<Integer>> tempLayer = new ArrayList<>(); // lista di una lista di integer == matrice
        try (InputStream is = getClass().getResourceAsStream(csvFilePath); //tento di recuperare la risorsa del csv
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) { //legge i dati dall'input

            if (is == null) { //se nullo
                throw new IOException("File CSV per il livello non trovato: " + csvFilePath);
            }

            String line; //stringa nuova che conterrà le righe
            int currentRow = 0;
            int currentCols = 0;
            while ((line = br.readLine()) != null) { //finché la stringa non è nulla
                String[] values = line.split(";"); //separa i valori quando incontra un ;, creando un array di stringhe
                List<Integer> row = new ArrayList<>(); //crea lista di righe
                for (String val : values) { //cicla sui valori
                    try {
                        row.add(Integer.parseInt(val.trim())); //aggiunge alla riga il valore in int senza spazi
                    } catch (NumberFormatException e) {
                        System.err.println("Errore di formato numero nel file " + csvFilePath + " riga " + currentRow + ", valore: '" + val + "'. Impostato a 0.");
                        row.add(0); // Valore di default in caso di errore
                    }
                }
                tempLayer.add(row); //aggiungo la riga alla matrice
                
                //controllo numero di colonne delle righe
                if (currentRow == 0) { //all'inizio parto da zero (eseguo solo una volta)
                    currentCols = row.size(); // e fisso il numero di colonne dalla prima riga
                } else if (row.size() != currentCols) { //se si trova una riga troppo lunga/corta
                    System.err.println("Attenzione: La riga " + currentRow + " nel file " + csvFilePath + " ha un numero di colonne inconsistente. Potrebbe causare problemi.");
                }
                currentRow++;
            }

            if (!tempLayer.isEmpty()) { //se matrice non è vuota
                int layerRows = tempLayer.size(); //recupero il numero di righe
                int layerCols = tempLayer.get(0).size(); //recupero il numero di colonne
                int[][] layerData = new int[layerRows][layerCols]; //creo matrice con queste misure
                
                for (int r = 0; r < layerRows; r++) { //ciclo sul numero di righe
                    for (int c = 0; c < layerCols; c++) { // sul numero di colonne
                        layerData[r][c] = tempLayer.get(r).get(c); //recupeero ogni strato
                    }
                }
                layersData.add(layerData); //lo aggiungo alla matrice
                System.out.println("Livello caricato con successo da: " + csvFilePath);
            } else {
                System.err.println("Il file CSV " + csvFilePath + " è vuoto o non contiene dati validi. Livello non aggiunto.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore I/O durante il caricamento del livello dal file CSV: " + csvFilePath);
        }
    }

    /**
     * Restituisce l'ID del tile in una specifica posizione e livello.
     * @param layerIndex l'indice del livello (0 per il più basso).
     * @param row la riga del tile.
     * @param col la colonna del tile.
     * @return L'ID del tile, o -1
     */
    public int getTileId(int layerIndex, int row, int col) {
    	if (layerIndex < 0 || layerIndex >= layersData.size()) { //se il layer è fuori margini
            // System.err.println("Errore in getTileId: Indice livello fuori dai limiti: " + layerIndex);
            return -1;
        }

        int[][] currentLayer = layersData.get(layerIndex); //recupero le tile dello strato in esame

        // Controlla se la riga è valida per il layer corrente
        if (row < 0 || row >= currentLayer.length) {
            return -1;
        }

        // Controlla se la colonna è valida per la riga corrente del layer corrente
        if (col < 0 || col >= currentLayer[row].length) {
            return -1;
        }

     // Se tutto ok, restituisco l'ID del tile
        return currentLayer[row][col];
    }

    /**
     * Restituisce l'oggetto Tile in una specifica posizione e livello.
     * @param layerIndex L'indice del livello.
     * @param row La riga del tile.
     * @param col La colonna del tile.
     * @return L'oggetto Tile, o null se fuori dai limiti o l'ID del tile non è mappato.
     */
    public Tile getTile(int layerIndex, int row, int col) {
        int tileId = getTileId(layerIndex, row, col);
        return tiles.get(tileId); // o null se l'ID non è presente nella mappa tiles
    }
    
    /**
     * Restituisce true se la tile alle coordinate (col, row) è considerata solida.
     *
     * @param col La colonna della tile.
     * @param row La riga della tile.
     * @return True se la tile è solida, altrimenti false.
     */
    public boolean isTileSolid(int col, int row) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) { //se riga/colonna fuori margine
            return false;
        }
        
        int[][] collisionLayer = layersData.get(2); //reupero lo strato che contiene le tile solide
        int tileId = collisionLayer[row][col]; //recupero l'id in posizione ricercata
        Tile tile = tiles.get(tileId); //definisco tile con quell'id
        
        return tile != null && tile.isSolid(); //recupero il suo stato
    }
    
    
    //GETTER E SETTER
    public int getNumLayers() {
        return layersData.size();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}