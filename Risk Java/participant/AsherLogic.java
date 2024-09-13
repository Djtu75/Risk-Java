package participant;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

import util.*;

public class AsherLogic extends PlayerLogic {

    public AsherLogic(){
        super();
    }

    Game mygame = null;
    Player myplayer = null;

    public void initialize(Game mygame, Player myplayer){
        this.mygame = mygame;
        this.myplayer = myplayer;
    }

    /**
     * @return Method to tell player their turn is about to start. Intended to let player initialize variables and logic.
     */
    public void beginTurn(){
        //Do setup logic
    }

    /**
     * @return //Method to tell player their turn is about to end. Intended to let player clean up variables and logic.
     */
    public void endTurn(){
        //Do ending logic
    }

    /**
     * @return //Asks player to place troops. Should return [int numTroopstoPlace, Province destination]
     */
    public Set<DeployCommand> draftPhase(int numTroopstoPlace){
        Set<DeployCommand> returnArray = new HashSet<DeployCommand>();
        Set<Province> myterritory = myplayer.getTerritory();
        if(myterritory.size() <= 0){
            return returnArray;
        }
        Set<Province> neighbors = new HashSet<Province>();
        Set<Province> borders = new HashSet<Province>();
        int placeXTroops = numTroopstoPlace;

        //find all neighbors
        for(Province owned : myterritory){
            Set<Province> adjacent =  owned.getAdjacent();
            for(Province target : adjacent){
                if(target.getOwner() != this.myplayer){
                    neighbors.add(target);
                    borders.add(owned);

                }
            }
        }
        System.out.println("Has "+ borders.size()+" border territories and "+neighbors.size()+" neighboring territories");
        while(placeXTroops > 0){
            for(Province  border : borders){
                if(placeXTroops > 0){
                    returnArray.add(new DeployCommand(1,border));
                    placeXTroops--;
                    System.out.println(myplayer.getName() + " placed 1 troop in "+ border.getName());
                }else{
                    break;
                }
            }
        }
        return(returnArray);
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
     */
    public AttackCommand attackPhase(){
        if(myplayer.getTerritory().size() <= 0){
            return new AttackCommand(0, null,null);
        }
        AttackCommand ret;
        Province attackingProvince = null;
        Province defendingProvince = null;
        int useXtroops = 1;
        double pct;
        for(Province p: myplayer.getTerritory()){
            if(p.getNumSoldiers() > 3){
                for(Province adj: p.getAdjacent()){
                    if(adj.getOwner() != p.getOwner()){
                        pct = mygame.winPctOfPartialAttack(useXtroops , adj.getNumSoldiers());
                        if(pct == -1.0 || p.getNumSoldiers() >= 100){
                            if((p.getNumSoldiers() - 1) >= (3*adj.getNumSoldiers())){
                                useXtroops = 3*adj.getNumSoldiers();
                                attackingProvince = p;
                                defendingProvince = adj;
                            }else if(p.getNumSoldiers() >= 2000){
                                useXtroops = p.getNumSoldiers() -1;
                                attackingProvince = p;
                                defendingProvince = adj;
                            }
                            //win percentage is not exact, just go with who has more at this point
                        }else{
                            while(pct < 70.0){
                                useXtroops++;
                                if(useXtroops >= p.getNumSoldiers()){
                                    break;
                                }
                                pct = mygame.winPctOfPartialAttack(useXtroops , adj.getNumSoldiers());
                            }
                            if(useXtroops <= p.getNumSoldiers() - 1 && pct > 70.0){
                                attackingProvince = p;
                                defendingProvince = adj;
                            }
                        }
                    }
                }
            }
        }
        if((attackingProvince != null) && (defendingProvince != null)){
            System.out.println(myplayer.getName()+" is attacking from " + attackingProvince.getName() +" Soliders: "+ attackingProvince.getNumSoldiers() +" WinPercentage: " + mygame.winPctOfPartialAttack(useXtroops, defendingProvince.getNumSoldiers()));
            System.out.println("Defending " + defendingProvince.getName() + " with "+ defendingProvince.getNumSoldiers()+ " Soldiers");
            ret = new AttackCommand(useXtroops, attackingProvince, defendingProvince);
            //return (returnArray);
            return ret;
        }else { return null;}



    }

    /**
     * @return //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]
     */
    public MoveCommand movePhase(){
        if(myplayer.getTerritory().size() <= 0){
            return new MoveCommand(0, null,null);
        }
        Set<Province> owned = this.myplayer.getTerritory();
        int maxLazyTroops = 0;
        int maxEnemies = 0;
        Province underSuppliedProvince = null;
        Province lazyProvince = null;
        for(Province mine: owned){
            Set<Province> adjacent =  mine.getAdjacent();
            int lazyTroops = 0;
            int enemies = 0;
            for(Province target : adjacent){
                if(target.getOwner() != this.myplayer){
                    lazyTroops = -1;
                    enemies += target.getNumSoldiers();
                }else{
                    if(lazyTroops == -1){
                    lazyTroops = mine.getNumSoldiers() - 1;
                    }
                }
            }
            if(enemies > maxEnemies && enemies > 0){
                maxEnemies = enemies;
                underSuppliedProvince = mine;
            }
            if(lazyTroops > maxLazyTroops && lazyTroops > 0){
                maxLazyTroops = lazyTroops;
                lazyProvince = mine;
            }
        }
        return new MoveCommand(maxLazyTroops , underSuppliedProvince, lazyProvince);
        //return (returnArray);
    }

    public void attackPhaseResults(int[] battleResults){
        //Check if attack went well
        //Change logic before attackPhase gets called again

        return;
    }


    /**
     * @return //Asks player how many troops to move into conquered territory. Should return [int numTroops]
     */
    public int moveAfterConquer(Province attackingProvince, Province defendingProvince){
    //find all neighbors
    if(myplayer.getTerritory().size() <= 0){
        return 0;
    }
        int TransferXTroops = 0;
        Set<Province> adjacent =  attackingProvince.getAdjacent();
            for(Province target : adjacent){
                if(target.getOwner() != this.myplayer){
                    TransferXTroops = (attackingProvince.getNumSoldiers()-1) / 2;
                    break;
                }else{
                    TransferXTroops = (attackingProvince.getNumSoldiers()-1);
                }
            }

        return TransferXTroops;

    }

    /**
     * @return //Asks player to turn in set of cards. If required, must be [Card X, Card Y, Card Z]. If not required, any invalid input will count as passing on your turn.
     */
    public Set<Card> turnInCards(boolean required, int currentTroops){
        
            Set<Card> mycards = mygame.getCardData(this);
            Set<Card> set = mygame.makeSet(mycards);
            return(set);
    }
    
}

