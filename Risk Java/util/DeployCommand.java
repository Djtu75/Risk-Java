package util;


public class DeployCommand{
    private int numTroopsToPlace;
    private Province targetProvince;
    
    public DeployCommand(int numTroopsToPlace, Province targetProvince){
        this.numTroopsToPlace = numTroopsToPlace;
        this.targetProvince = targetProvince;
    }
    public Province getProvince(){
        return this.targetProvince;
    }
    public int getNumTroops(){
        return this.numTroopsToPlace;
    }
}
