package util;

public class moveTroop{
    private int movingTroops;
    private Province targetProvince;
    private Province sourceProvince;

    public moveTroop(int movingTroops, Province targetProvince, Province sourceProvince){
        this.movingTroops = movingTroops;
        this.targetProvince = targetProvince;
        this.sourceProvince = sourceProvince;
    }
    public Province getTargetProvince(){
        return this.targetProvince;
    }
    public Province getSourceProvince(){
        return this.sourceProvince;
    }
    public int getMovingTroops(){
        return this.movingTroops;
    }
}
