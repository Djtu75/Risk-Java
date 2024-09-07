package participant;
import util.*;

import java.util.ArrayList;

import java.util.Set;


public class PlayerLogic implements Logic {

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
        ArrayList<placeTroop> ret = new ArrayList<placeTroop>();
        Set<Province> myterritory = myplayer.getTerritory();
        for(Province p : myterritory){
            ret.add(new placeTroop( 1, p));
        }
        return ret;
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
     */
    public attack attackPhase(){
        int useXtroops = 3;
        Province attackingProvince = new Province(null, null, 0);
        Province defendingProvince = new Province(null, null, 0);
        attack p = new attack(useXtroops, attackingProvince, defendingProvince);
        return p;

    }

    /**
     * @return //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]
     */
    public moveTroop movePhase(){
        int moveXTroops = 1;
        Province source = new Province(null, null, 0);
        Province destination = new Province(null, null, 0);
        moveTroop ret = new moveTroop(moveXTroops, destination, source);
        //Object[] returnArray = {moveXTroops, source, destination};
        return ret;
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
        Set<Card> set = mygame.makeSet(mycards);
            //Some Logic to pick which 3 to turn in
            // Iterator<Card> iter = mycards.iterator();
            // Card card1 = iter.next();
            // Card card2 = iter.next();
            // Card card3 = iter.next();
            // set.add(card1);
            // set.add(card2);
            // set.add(card3);
            // return set;
                return set;
    }
}
