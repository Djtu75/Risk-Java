package util;

public class MoveCommand{
    private int numMovingTroops;
    private Province targetProvince;
    private Province sourceProvince;

    public MoveCommand(int numMovingTroops, Province targetProvince, Province sourceProvince){
        this.numMovingTroops = numMovingTroops;
        this.targetProvince = targetProvince;
        this.sourceProvince = sourceProvince;
    }
    public Province getTargetProvince(){
        return this.targetProvince;
    }
    public Province getSourceProvince(){
        return this.sourceProvince;
    }
    public int getnumMovingTroops(){
        return this.numMovingTroops;
    }
}
