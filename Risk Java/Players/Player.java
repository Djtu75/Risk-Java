package Players;

import java.util.ArrayList;
import util.*;

public class Player {

    private ArrayList<Province> territory = new ArrayList<Province>();
    private int numsoldiers;
    private ArrayList<Card> cards = new ArrayList<Card>();

    public ArrayList<Province> getPlayerTerritory(){
        return territory;
    }

    public void setTerritory(ArrayList<Province> newTerritory){
        territory = newTerritory;
    }

    public void addTerritory(Province newProvince){
        territory.add(newProvince);
    }

    public void removeTerritory(Province remProvince){
        territory.remove(remProvince);
    }

    public int getNumSoldiers(){
        return numsoldiers;
    }

    public int getCards(){
        return cards.size();
    }

}
