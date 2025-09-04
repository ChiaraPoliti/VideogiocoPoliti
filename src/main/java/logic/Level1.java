package logic;

import java.util.ArrayList;
import java.util.List;
import beans.Block;
import beans.BreakableBlock;
import beans.Coin;
import beans.Enemy;
import beans.Goomba;
import beans.Koopa;
import beans.QuestionBlock;

/**
 * Classe che definisce gli elemeti beans che formano il livello
 * In questo caso è l'unico implementato
 */
public class Level1 {
    private List<Enemy> enemies;
    private List<Block> blocks;
    private List<Coin> coins;

    public Level1() {
        this.enemies = new ArrayList<>();
        this.blocks = new ArrayList<>();
        this.coins = new ArrayList<>();

        buildLevel(enemies, coins, blocks);
    }

    /**
     * Metodo che crea il livello: aggiunge i beans dove devono andare
     * @param enemies
     * @param coins
     * @param blocks
     */
    private void buildLevel(List <Enemy> enemies, List <Coin> coins, List <Block> blocks) {
    	// Aggiungi nemici
        enemies.add(new Goomba(250, 128));
        enemies.add(new Goomba(550, 128));
        enemies.add(new Goomba(1050, 128));
        enemies.add(new Koopa(1018, 112));
        
        
        // Aggiungi una moneta
        coins.add(new Coin(400, 122));
        coins.add(new Coin (656,92));
        coins.add(new Coin (672,92));
        coins.add(new Coin (896, 64));
        coins.add(new Coin (912,64));
        coins.add(new Coin (928,64));
        
        //Aggiungo blocchi
        blocks.add(new BreakableBlock (176,80));
        blocks.add(new BreakableBlock (544,80));
        blocks.add(new BreakableBlock (224,80));
        blocks.add(new BreakableBlock (896,80));
        blocks.add(new BreakableBlock (912,80));
        blocks.add(new BreakableBlock (928,80));
        
        
        blocks.add(new QuestionBlock (192,80, enums.itemType.MUSHROOM));
        blocks.add(new QuestionBlock (912,32, enums.itemType.MUSHROOM));
        
        blocks.add(new QuestionBlock (528, 80, enums.itemType.COIN));
        blocks.add(new QuestionBlock (560, 80, enums.itemType.COIN));
    }
   
    // Getter per il GamePanel
    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Coin> getCoins() {
        return coins;
    }

}
