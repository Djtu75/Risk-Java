package participant;

import java.util.Iterator;
import java.util.Set;

import util.*;

public class StupidLogic extends PlayerLogic {

    public StupidLogic(){
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
    public Object[] draftPhase(int numTroopstoPlace){
        Province destination = myplayer.getTerritory().iterator().next();
        int placeXTroops = numTroopstoPlace;
        Object[] returnArray = {placeXTroops, destination};
        System.out.println("Placing " + destination);
        System.out.println("# " + placeXTroops);
        return(returnArray);
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
     */
    public Object[] attackPhase(){
        Province attackingProvince = null;
        Province defendingProvince = null;
        for(Province p: myplayer.getTerritory()){
            if(p.getNumSoldiers() > 3){
                for(Province adj: p.getAdjacent()){
                    if(adj.getOwner() != p.getOwner()){
                        attackingProvince = p;
                        defendingProvince = adj;
                    }
                }
            }
        }
        int useXtroops = 3;
        if((attackingProvince != null) && (defendingProvince != null)){
            System.out.println("Attacking " + attackingProvince);
            System.out.println("Defending " + defendingProvince);
            Object[] returnArray = {useXtroops, attackingProvince, defendingProvince};
            return (returnArray);
        }else {
            System.out.println("Not Attacking");
            return null;
        }

    }

    /**
     * @return //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]
     */
    public Object[] movePhase(){
        int moveXTroops = 1;
        Province source = new Province(null, null, 0);
        Province destination = new Province(null, null, 0);
        Object[] returnArray = {moveXTroops, source, destination};
        return (returnArray);
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
    public Object[] turnInCards(boolean required, int currentTroops){
        if(required){
            Set<Card> mycards = mygame.getCardData(this);
            //Some Logic to pick which 3 to turn in
            Iterator<Card> iter = mycards.iterator();
            Card card1 = iter.next();
            Card card2 = iter.next();
            Card card3 = iter.next();
            return(new Object[]{card1, card2, card3});
        }
        else{
            return (new Object[]{"optional"});
        }

    }
    
}
