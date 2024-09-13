package participant;

import util.*;
import java.util.Set;

public interface Logic {

    public void initialize(Game mygame, Player myPlayer);
    public void beginTurn(); //Method to tell player their turn is about to start. Intended to let player initialize variables and logic.
    public void endTurn(); //Method to tell player their turn is about to end. Intended to let player clean up variables and logic.

    public Set<DeployCommand> draftPhase(int numTroopstoPlace); //Asks player to place troops. Should return [int numTroopstoPlace, Province destination]
    public AttackCommand attackPhase(); //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
    public MoveCommand movePhase(); //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]

    public void attackPhaseResults(int[] battleResults); //Returns the result of a player's attack. Intended to allow the player to edit their logic based on the result of a battle.
    public int moveAfterConquer(Province attackingProvince, Province defendingProvince); //Asks player how many troops to move into conquered territory. Should return [int numTroops]
    public Set<Card> turnInCards(boolean required, int currentTroops); //Asks player to turn in set of cards. If required, must be [Card X, Card Y, Card Z]. If optional, any invalid input will count as passing on your turn.

    
}
