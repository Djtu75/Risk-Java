package util;


public class placeTroop{
    private int placedTroops;
    private Province targetProvince;
    
    public placeTroop(int placedTroops, Province targetProvince){
        this.placedTroops = placedTroops;
        this.targetProvince = targetProvince;
    }
    public Province getProvince(){
        return this.targetProvince;
    }
    public int getTroopPlacement(){
        return this.placedTroops;
    }
}
