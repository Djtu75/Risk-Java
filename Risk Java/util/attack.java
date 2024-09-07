package util;


public class attack {
    private int attackingTroops;
    private Province defendingProvince;
    private Province attackingProvince;

    public attack(int attackingTroops, Province attackingProvince, Province defendingProvince){
        this.attackingTroops = attackingTroops;
        this.defendingProvince = defendingProvince;
        this.attackingProvince = attackingProvince;
    }
    public Province getAttackingProvince(){
        return this.attackingProvince;
    }
    public Province getDefendingProvince(){
        return this.defendingProvince;
    }
    public int getAttackingTroops(){
        return this.attackingTroops;
    }
    public void endAttackPhase(){
        this.attackingTroops = -1;
    }
}