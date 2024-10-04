package util;
import java.util.Random;

public class Card {
    
    private int type; //0 = Infantry, 1 = Cavalry, 2 = Artillery, 3 = Wild
    private Province province; //The Province the card is attached to

    protected Card(){ //Creates wild card
        province = null;
        type = 3;
    }

    protected Card(Province prov){ //Creates standard card
        Random rand = new Random();
        type = rand.nextInt(3);
        province = prov;
    }

    public int getType() {
        return type;
    }

    protected void setType(int type) {
        this.type = type;
    }

    public Province getProvince() {
        return province;
    }

    protected void setProvince(Province province) {
        this.province = province;
    }
    


}
