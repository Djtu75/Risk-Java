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
    public ArrayList<placeTroop> draftPhase(int numTroopstoPlace){
        ArrayList<placeTroop> returnArray = new ArrayList<placeTroop>();
        Set<Province> myterritory = myplayer.getTerritory();
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
    
        for(Province  border : borders){
            if(placeXTroops > 0){
                returnArray.add(new placeTroop(1,border));
                placeXTroops--;
                System.out.println(myplayer.getName() + " placed 1 troop in "+ border.getName());
            }else{
                break;
            }
        }
        return(returnArray);
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
     */
    public attack attackPhase(){
        attack ret;
        Province attackingProvince = null;
        Province defendingProvince = null;
        int useXtroops = 1;
        double pct;
        for(Province p: myplayer.getTerritory()){
            if(p.getNumSoldiers() > 3){
                for(Province adj: p.getAdjacent()){
                    if(adj.getOwner() != p.getOwner()){
                        pct = mygame.winPctOfPartialAttack(useXtroops , adj.getNumSoldiers());
                        if(pct == -1.0){
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
            System.out.println(myplayer.getName()+" is attacking from" + attackingProvince.getName() +" Soliders: "+ attackingProvince.getNumSoldiers() +" WinPercentage: " + mygame.winPctOfPartialAttack(useXtroops, defendingProvince.getNumSoldiers()));
            System.out.println("Defending " + defendingProvince);
            //Object[] returnArray = {useXtroops, attackingProvince, defendingProvince};
            ret = new attack(useXtroops, attackingProvince, defendingProvince);
            //return (returnArray);
            return ret;
        }else { return null;}



    }

    /**
     * @return //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]
     */
    public moveTroop movePhase(){
        int moveXTroops = 1;
        Province source = new Province(null, null, 0);
        Province destination = new Province(null, null, 0);
        //Object[] returnArray = {moveXTroops, source, destination};
        moveTroop ret = new moveTroop(moveXTroops, destination, source);
        return ret;
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
        int transferXTroops = 1;
        return(transferXTroops);

    }

    /**
     * @return //Asks player to turn in set of cards. If required, must be [Card X, Card Y, Card Z]. If not required, any invalid input will count as passing on your turn.
     */
    public Set<Card> turnInCards(int currentTroops){
        
            Set<Card> mycards = mygame.getCardData(this);
            //Some Logic to pick which 3 to turn in
            // Iterator<Card> iter = mycards.iterator();
            // Card card1 = iter.next();
            // Card card2 = iter.next();
            // Card card3 = iter.next();
            Set<Card> set = mygame.makeSet(mycards);
            return(set);
    }
    
}

