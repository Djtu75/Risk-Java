package util;

import java.util.ArrayList;

public class Continent extends World{
    ArrayList<Province> provinces;
    String name;
    int bonus;

    public Continent(String initName, int initBonus){
        super();
        provinces = new ArrayList<Province>();
        this.name = initName;
        this.bonus = initBonus;
    }

    public Continent(String initName, int initBonus, ArrayList initProvinces){
        super();
        provinces = initProvinces;
        this.name = initName;
        this.bonus = initBonus;
    }

    public String getName() {
        return name;
    }

    public int getBonus(){
        return bonus;
    }

    public ArrayList<Province> getProvinces() {
        ArrayList<Province> returnArray = new ArrayList<Province>();
        for (Province province : provinces) {
            
        }
        return provinces;
        
    }

    public void setProvinces(ArrayList<Province> provinces) {
        this.provinces = provinces;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    

}
