package participant;

import util.*;

public interface Logic {

    public void initialize(Game mygame);
    public void beginTurn(); //Method to tell player their turn is about to start. Intended to let player initialize variables and logic.
    public void endTurn(); //Method to tell player their turn is about to end. Intended to let player clean up variables and logic.

    public Object[] draftPhase(int numTroopstoPlace); //Asks player to place troops. Should return [int numTroopstoPlace, Province destination]
    public Object[] attackPhase(); //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
    public Object[] movePhase(); //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]

    public int moveAfterConquer(Province attackingProvince, Province defendingProvince); //Asks player how many troops to move into conquered territory. Should return [int numTroops]
    public Object[] turnInCards(boolean required, int currentTroops); //Asks player to turn in set of cards. If required, must be [Card X, Card Y, Card Z]. If optional, any invalid input will count as passing on your turn.

    
}
