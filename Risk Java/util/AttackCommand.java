package util;


public class AttackCommand {
    private int numAttackingTroops;
    private Province defendingProvince;
    private Province attackingProvince;

    public AttackCommand(int numAttackingTroops, Province attackingProvince, Province defendingProvince){
        this.numAttackingTroops = numAttackingTroops;
        this.defendingProvince = defendingProvince;
        this.attackingProvince = attackingProvince;
    }
    public Province getAttackingProvince(){
        return this.attackingProvince;
    }
    public Province getDefendingProvince(){
        return this.defendingProvince;
    }
    public int getnumAttackingTroops(){
        return this.numAttackingTroops;
    }
    public void endAttackPhase(){
        this.numAttackingTroops = -1;
    }
}