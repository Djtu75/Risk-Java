package util;

import java.util.ArrayList;

public class World {
    ArrayList<Continent> continents;
    ArrayList<Province> provinces;

    public World(){
    }

    public ArrayList<Continent> getContinents() {
        return continents;
    }
    public void setContinents(ArrayList<Continent> continents) {
        this.continents = continents;
    }
    public ArrayList<Province> getProvinces() {
        return provinces;
    }
    public void setProvinces(ArrayList<Province> provinces) {
        this.provinces = provinces;
    }

    
    
}
