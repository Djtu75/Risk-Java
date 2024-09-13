package util;
import java.util.Random;

public class Card {
    
    private int troop; //0 = Infantry, 1 = Cavalry, 2 = Artillery, 3 = Wild
    private Province province; //The Province the card is attached to

    protected Card(){ //Creates wild card
        province = null;
        troop = 3;
    }

    protected Card(Province prov){ //Creates standard card
        Random rand = new Random();
        troop = rand.nextInt(3);
        province = prov;
    }

    protected int getValue(){
        return troop;
    }

    protected int getTroop() {
        return troop;
    }

    protected void setTroop(int troop) {
        this.troop = troop;
    }

    protected Province getProvince() {
        return province;
    }

    protected void setProvince(Province province) {
        this.province = province;
    }
    


}
