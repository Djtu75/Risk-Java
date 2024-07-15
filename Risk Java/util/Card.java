package util;
import java.util.Random;

public class Card {
    
    private int troop; //0 = Infantry, 1 = Cavalry, 2 = Artillery

    public Card(){
        Random rand = new Random();
        troop = rand.nextInt(3);
    }

    public int getValue(){
        return troop;
    }


}
