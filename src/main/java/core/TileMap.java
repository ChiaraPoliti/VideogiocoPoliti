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

public class TileMap {
    // Ogni elemento della lista rappresenta un livello (layer) della mappa
    private List<int[][]> layersData;
    private int rows; // Numero di righe della mappa (uguale per tutti i livelli)
    private int cols; // Numero di colonne della mappa (uguale per tutti i livelli)
    private Map<Integer, Tile> tiles; // Mappa per associare ID a oggetti Tile

    // Dimensione fissa di un singolo tile in pixel 
    // Tutte le immagini dei tile verranno scalate a questa dimensione.
    public static final int TILE_SIZE = 16;

    /**
     * Costruttore della mappa tile.
     * @param csvFilePaths Una lista di percorsi ai file CSV, in ordine dal livello più basso al più alto.
     * @param tilesImagesPath Il percorso della cartella contenente le immagini dei tile.
     */
    public TileMap(List<String> csvFilePaths, String tilesImagesPath) {
        layersData = new ArrayList<>();
        tiles = new HashMap<>();
        
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
     * Carica le immagini dei tile dalla cartella specificata.
     * Si assume che le immagini siano nominate come "tile_ID.png" (es. tile_0.png, tile_1.png).
     */
    private void loadTiles(String tilesImagesPath) {
        try {
            for (int i = 0; i < 28; i++) { 
                String imagePath = tilesImagesPath + "tile_" + i + ".png";
                InputStream is = getClass().getResourceAsStream(imagePath);
                if (is == null) {
                    System.err.println("Impossibile trovare l'immagine del tile: " + imagePath + ". Verrà usata un'immagine nulla.");
                    tiles.put(i, new Tile(i, null)); // Aggiunge un tile con immagine nulla se non trovata
                    continue;
                }
                BufferedImage img = ImageIO.read(is);
                tiles.put(i, new Tile(i, img));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore durante il caricamento delle immagini dei tile.");
        }
    }

    /**
     * Carica un singolo livello della mappa da un file CSV.
     * @param csvFilePath Il percorso del file CSV per questo livello.
     */
    private void loadLayer(String csvFilePath) {
        List<List<Integer>> tempLayer = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream(csvFilePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                throw new IOException("File CSV per il livello non trovato: " + csvFilePath);
            }

            String line;
            int currentRow = 0;
            int currentCols = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                List<Integer> row = new ArrayList<>();
                for (String val : values) {
                    try {
                        row.add(Integer.parseInt(val.trim()));
                    } catch (NumberFormatException e) {
                        System.err.println("Errore di formato numero nel file " + csvFilePath + " riga " + currentRow + ", valore: '" + val + "'. Impostato a 0.");
                        row.add(0); // Valore di default in caso di errore
                    }
                }
                tempLayer.add(row);
                
                if (currentRow == 0) {
                    currentCols = row.size();
                } else if (row.size() != currentCols) {
                    System.err.println("Attenzione: La riga " + currentRow + " nel file " + csvFilePath + " ha un numero di colonne inconsistente. Potrebbe causare problemi.");
                }
                currentRow++;
            }

            if (!tempLayer.isEmpty()) {
                int layerRows = tempLayer.size();
                int layerCols = tempLayer.get(0).size();
                int[][] layerData = new int[layerRows][layerCols];
                
                for (int r = 0; r < layerRows; r++) {
                    for (int c = 0; c < layerCols; c++) {
                        layerData[r][c] = tempLayer.get(r).get(c);
                    }
                }
                layersData.add(layerData);
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
     * @param layerIndex L'indice del livello (0 per il più basso).
     * @param row La riga del tile.
     * @param col La colonna del tile.
     * @return L'ID del tile, o -1 se fuori dai limiti.
     */
    public int getTileId(int layerIndex, int row, int col) {
    	if (layerIndex < 0 || layerIndex >= layersData.size()) {
            // Puoi stampare un errore qui se vuoi, ma è probabile che paintComponent chiami molti getTileId
            // System.err.println("Errore in getTileId: Indice livello fuori dai limiti: " + layerIndex);
            return -1; // Restituisce -1 per indicare un tile non valido/fuori mappa
        }

        int[][] currentLayer = layersData.get(layerIndex);

        // Controlla se la riga è valida per il *layer corrente*
        if (row < 0 || row >= currentLayer.length) {
            return -1;
        }

        // Controlla se la colonna è valida per la *riga corrente del layer corrente*
        if (col < 0 || col >= currentLayer[row].length) {
            return -1;
        }

        // Se tutto è valido, restituisce l'ID del tile
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
        return tiles.get(tileId); // Restituisce null se l'ID non è presente nella mappa 'tiles'
    }
    
    /**
     * Restituisce true se la tile alle coordinate (col, row) è considerata solida.
     *
     * @param col La colonna della tile.
     * @param row La riga della tile.
     * @return True se la tile è solida, altrimenti false.
     */
    public boolean isTileSolid(int col, int row) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) {
            return false;
        }
        
        int[][] collisionLayer = layersData.get(2);
        int tileId = collisionLayer[row][col];
        Tile tile = tiles.get(tileId);
        
        return tile != null && tile.isSolid();
    }
    
    
    /**
     * Restituisce il numero totale di livelli nella mappa.
     */
    public int getNumLayers() {
        return layersData.size();
    }

    /**
     * Restituisce il numero di righe della mappa.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Restituisce il numero di colonne della mappa.
     */
    public int getCols() {
        return cols;
    }
}
