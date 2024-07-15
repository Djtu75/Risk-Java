package util;
import Players.*;
import java.util.ArrayList;

public class Province extends Continent {
    
    private static Player owner;
    private static int count;
    private static ArrayList<Province> adjacent = new ArrayList<Province>();

    public Province(Player ownership, int numsoldiers, ArrayList<Province> adjacents){
        super();
        owner = ownership;
        count = numsoldiers;
        adjacent = adjacents;
    }

    public Player getOwner(){
        return owner;
    }

    public void setOwner(Player ownership){
        owner = ownership;
    }

    public int getCount(){
        return count;
    }

    public void setCount(int numsoldiers){
        count = numsoldiers;
    }

    public ArrayList<Province> getAdjacent(){
        return adjacent;
    }

    public void setAdjacent(ArrayList<Province> adjacents){
        adjacent = adjacents;
    }

    public void addAdjacent(Province newProvince){
        adjacent.add(newProvince);
    }

    public void removeAdjacent(Province remProvince){
        adjacent.remove(remProvince);
    }
}
